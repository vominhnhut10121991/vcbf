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
		
		if (sizeof($structure) == 8)
		{
			if (trim($structure[0]) != "Username" || trim($structure[1]) != "Password" || trim($structure[2]) != "Investor_Name" || 
			trim($structure[3]) != "Investor_Folio_Number" || trim($structure[4]) != "Investor_Holding" || trim($structure[5])
			!= "Investor_Nav" || trim($structure[6]) != "Investor_Value" || trim($structure[7]) != "Investor_Type")
			{
				echo "Sheet " . ($i + 1) . " is not 'User'. Please, <a href='index.php'> try again </a> </br>";
				return;
			}
		}
		else
		{
			echo "Sheet " . ($i + 1) . " is not 'User'. Please, <a href='index.php'> try again </a> </br>";
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
			$username = $row[0];
			$password = $row[1];
			$investor_name = $row[2];
			$investor_folio_number = $row[3];
			$investor_holding = $row[4];
			if(!is_numeric ($investor_holding))
			{
				echo "Investor Holding of row " . $line . " in sheet " . ($i + 1) . " is not number. </br>";
				continue;
			}
			$investor_nav = $row[5];
			if(!is_numeric ($investor_nav))
			{
				echo "Investor NAV of row " . $line . " in sheet " . ($i + 1) . " is not number. </br>";
				continue;
			}
			$investor_value = $row[6];
			if(!is_numeric ($investor_value))
			{
				echo "Investor Value of row " . $line . " in sheet " . ($i + 1) . " is not number. </br>";
				continue;
			}
			$investor_type = $row[7];
			
			
			// retrieve all records from database
			// compare to update or insert
			$update = false;
			$r_sql = "select * from vcbf_user";
			foreach (select($pdo, $r_sql) as $db_row) 
			{ 
				if ($db_row[0] == $username)
				{
					$update = true;
					break;
				}
			}
			
			if ($update ==  true)
			{
				$sql = "UPDATE vcbf_user SET password = '" . $password . "', investor_name = '" . $investor_name . "', investor_folio_number = '" . $investor_folio_number . "', 
					investor_holding = " . $investor_holding . ", investor_nav = " . $investor_nav . ", investor_value = " . $investor_value . ",
					investor_type = '" . $investor_type . "' WHERE username = '" . $username . "'";
				
				$count = execution($pdo, $sql);
				$count_update += $count;
			}
			else
			{
				$sql = "INSERT INTO vcbf_user VALUES ('" . $username . "','" . $password . "' ,'" . $investor_name . "',
					'" . $investor_folio_number . "', " . $investor_holding . ", " . $investor_nav . ", " . $investor_value . ", 
					'" . $investor_type . "')";

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



