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
		
		if (sizeof($structure) == 4)
		{
			if (trim($structure[0]) != "Date" || trim($structure[1]) != "Net_Asset_Value" || trim($structure[2]) != "Turnover_Rate" || trim($structure[3])
			!= "Total_Position")
			{
				echo "Sheet " . ($i + 1) . " is not 'Portfolio Statistic'. Please, <a href='index.php'> try again </a> </br>";
				return;
			}
		}
		else
		{
			echo "Sheet " . ($i + 1) . " is not 'Portfolio Statistic'. Please, <a href='index.php'> try again </a> </br>";
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
			$net_asset_value = $row[1];
			if(!is_numeric ($net_asset_value))
			{
				echo "Net Asset Value of row " . $line . " in sheet " . ($i + 1) . " is not number. </br>";
				continue;
			}
			$turnover_rate = $row[2];
			if(!is_numeric ($turnover_rate))
			{
				echo "Turnover Rate of row " . $line . " in sheet " . ($i + 1) . " is not number. </br>";
				continue;
			}
			$total_position = $row[3];	
			if(!is_numeric ($total_position))
			{
				echo "Total Position of row " . $line . " in sheet " . ($i + 1) . " is not number. </br>";
				continue;
			}			
			
			// retrieve all records from database
			// compare to update or insert
			$update = false;
			$r_sql = "select * from vcbf_portfolio_statistic_bcf";
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
				$sql = "UPDATE vcbf_portfolio_statistic_bcf SET net_asset_value = " . $net_asset_value . ", turnover_rate = " . $turnover_rate . ", 
					total_position = " . $total_position . " WHERE date = '" . $date . "'";
				
				$count = execution($pdo, $sql);
				$count_update += $count;
			}
			else 
			{
				$sql = "INSERT INTO vcbf_portfolio_statistic_bcf VALUES ('" . $date . "'," . $net_asset_value . "," . $turnover_rate . ",
					" . $total_position . ")";

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



