<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<?php

require("vcbf_database_connection.php");
require("vcbf_function.php");

session_start();
if(!isset($_SESSION['login']))
	header("location:vcbf_login.php");

$allowedExts = array("xlsx","xls");
$temp = explode(".", $_FILES["file"]["name"]);
$extension = end($temp);

if (($_FILES["file"]["type"] == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
&& ($_FILES["file"]["size"] < 2000000)
&& in_array($extension, $allowedExts)) 
{
  if ($_FILES["file"]["error"] > 0) 
  {
    echo "Return Code: " . $_FILES["file"]["error"] . "</br>";
  } 
  else 
  { 
  
	include 'Classes/PHPExcel/IOFactory.php';

	$inputFileName = $_FILES["file"]["tmp_name"];

	//  Read your Excel workbook
	try {
		$inputFileType = PHPExcel_IOFactory::identify($inputFileName);
		$objReader = PHPExcel_IOFactory::createReader($inputFileType);
		$objPHPExcel = $objReader->load($inputFileName);
	} catch (Exception $e) {
		die('Error loading file "' . pathinfo($inputFileName, PATHINFO_BASENAME) 
		. '": ' . $e->getMessage());
	}	
	
	// the funcion cn_db() is in vcbf_database_connection.php
	$pdo=cn_db();
	$count_update = 0;
	$count_insert = 0;

	// loop through all sheets
	$total_sheets=$objPHPExcel->getSheetCount(); 
	for ($i = 0; $i < $total_sheets; $i++)
	{
		$objPHPExcel->setActiveSheetIndex($i);
		$objWorksheet = $objPHPExcel->getActiveSheet();
		$highestRow = $objWorksheet->getHighestRow(); 
		$highestColumn = $objWorksheet->getHighestColumn();  
		$highestColumnIndex = PHPExcel_Cell::columnIndexFromString($highestColumn);	
		
		// check structure
		for ($col = 0; $col <$highestColumnIndex;++$col)
		{  
			$value=$objWorksheet->getCellByColumnAndRow($col, 2)->getValue();  
			$structure[$col] = $value; 
		}
		
		if (sizeof($structure) == 6)
		{
			if (trim($structure[0]) != "Date" || trim($structure[1]) != "Nav_Unit" || trim($structure[2]) != "Nav_Change" || trim($structure[3])
			!= "POP" || trim($structure[4]) != "Highest_Nav_Unit" || trim($structure[5]) != "Lowest_Nav_Unit")
			{
				echo "Sheet " . ($i + 1) . " is not 'Weekly Fund Price'. Please, <a href='index.php'> try again </a> </br>";
				return;
			}
		}
		else
		{
			echo "Sheet " . ($i + 1) . " is not 'Weekly Fund Price'. Please, <a href='index.php'> try again </a> </br>";
			return;
		}
			
		// iterate over spreadsheet rows and columns
		// assign values of the spreadsheet to array
		for ($row = 3; $row <= $highestRow;++$row) 
		{  
			for ($col = 0; $col <$highestColumnIndex;++$col)
			{  
				$value=$objWorksheet->getCellByColumnAndRow($col, $row)->getValue();  
				$arraydata[$row-3][$col]=$value; 
			}
		}
		
		// compare the array elements with the result records from the database
		// if the elements have already existed, data in database will be updated based on the array elements.
		// Otherwise, elements will be inserted.
		$line = 3;
		foreach ($arraydata as $row)
		{
			$date = date('Y-m-d',unixstamp($row[0]));
			if(!preg_match("/^(?:20|19)[0-9]{2}([-.\\/])(?:0?[1-9]|1[012])\\1(?:0?[1-9]|[12][0-9]|3[01])$/", $date)) 
			{ 
				echo "Date format of row " . $line . " in sheet " . ($i + 1) . " is invalid. </br>";
				continue;
			}	
			$nav_unit = $row[1];
			if(!is_numeric ($nav_unit))
			{
				echo "NAV Unit of row " . $line . " in sheet " . ($i + 1) . " is not number. </br>";
				continue;
			}
			$nav_change = $row[2];
			if(!is_numeric ($nav_change))
			{
				echo "NAV Change of row " . $line . " in sheet " . ($i + 1) . " is not number. </br>";
				continue;
			}
			$pop = $row[3];
			if(!is_numeric ($pop))
			{
				echo "POP of row " . $line . " in sheet " . ($i + 1) . " is not number. </br>";
				continue;
			}
			$highest_nav_unit = $row[4];
			if(!is_numeric ($highest_nav_unit))
			{
				echo "Highest NAV Unit of row " . $line . " in sheet " . ($i + 1) . " is not number. </br>";
				continue;
			}
			$lowest_nav_unit = $row[5];
			if(!is_numeric ($lowest_nav_unit))
			{
				echo "Lowest NAV Unit of row " . $line . " in sheet " . ($i + 1) . " is not number. </br>";
				continue;
			}
			
			// retrieve all records from database
			// compare to update or insert
			$update = false;
			$r_sql = "select * from vcbf_weekly_fund_price_bcf";
			foreach (select($pdo, $r_sql) as $db_row) 
			{ 
				if ($db_row[0] == $date)
				{
					$update = true;
					break;
				}
			}
			
			if ($update ==  true)
			{
				$sql = "UPDATE vcbf_weekly_fund_price_bcf SET nav_unit = " . $nav_unit . ", nav_change = " . $nav_change . ", pop = " . $pop . ", 
					highest_nav_unit = " . $highest_nav_unit . ", lowest_nav_unit = " . $lowest_nav_unit . " WHERE date = '" . $date . "'";
				
				$count = execution($pdo, $sql);
				$count_update += $count;
			}
			else
			{
				$sql = "INSERT INTO vcbf_weekly_fund_price_bcf VALUES ('" . $date . "'," . $nav_unit . " ," . $nav_change . "," . $pop . " ,
					" . $highest_nav_unit . "," . $lowest_nav_unit . " )";
				
				$count = execution($pdo, $sql);
				$count_insert += $count;
			}
			
			$line++;
		}		
	}
	
	echo "Update: " . $count_update . " row(s). </br>" .
		 "Insert: " . $count_insert . " row(s). </br>" .
	 	 "Go to the <a href='index.php'> index page </a> </br>";
	
	// close connection
	$pdo = null;
  }
} 
else 
{
  echo "Invalid file or no file chosen. Please, <a href='index.php'> try again </a> </br>";
}
?>



