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

	
	$sql = "TRUNCATE TABLE vcbf_information_message";				
	execution($pdo, $sql);
	
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
		
		if (sizeof($structure) == 5)
		{
			if (trim($structure[0]) != "Title" || trim($structure[1]) != "Content" || trim($structure[2]) != "Title_VN" || trim($structure[3])
			!= "Content_VN" || trim($structure[4]) != "Date")
			{
				echo "Sheet " . ($i + 1) . " is not 'Information Message'. Please, <a href='index.php'> try again </a> </br>";
				return;
			}
		}
		else
		{
			echo "Sheet " . ($i + 1) . " is not 'Information Message'. Please, <a href='index.php'> try again </a> </br>";
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
			$title = $row[0];
			$content = $row[1];
			$title_vn = $row[2];
			$content_vn = $row[3];
			$date = date('Y-m-d',unixstamp($row[4]));
			if(!preg_match("/^(?:20|19)[0-9]{2}([-.\\/])(?:0?[1-9]|1[012])\\1(?:0?[1-9]|[12][0-9]|3[01])$/", $date)) 
			{ 
				echo "Date format of row " . $line . " in sheet " . ($i + 1) . " is invalid. </br>";
				continue;
			}			
			
			// retrieve all records from database
			// compare to update or insert
			$update = false;
			$r_sql = "select * from vcbf_information_message";
			foreach (select($pdo, $r_sql) as $db_row) 
			{ 
				if ($db_row[1] == $title || $db_row[3] == $title_vn)
				{
					$update = true;
					break;
				}
			}
			
			if ($update ==  true)
			{
				$sql = "UPDATE vcbf_information_message SET title = '" . $title . "', content = '" . $content . "', title_vn = '" . $title_vn . "', 
					content_vn = '" . $content_vn . "', date = '" . $date . "' WHERE title = '" . $title . "' or title_vn = '" . $title_vn . "'";
				
				$count = execution($pdo, $sql);
				$count_update += $count;
			}
			else
			{
				$sql = "INSERT INTO vcbf_information_message(title, content, title_vn, content_vn, date) VALUES ('" . $title . "','" . $content . "'
					, '" . $title_vn . "','" . $content_vn . "', '" . $date . "' )";
				
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



