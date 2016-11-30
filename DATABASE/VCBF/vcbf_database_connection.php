<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
</head>

<body>
<?php
function cn_db()
{
	try 
	{
	
		define('DB_HOST', 'localhost');
		define('DB_SCHEMA', 'vcbf');
		define('DB_USER', 'root');
		define('DB_PASSWORD', '');
		define('DB_ENCODING', 'utf8');


		$dsn = 'mysql:host=' . DB_HOST . ';dbname=' . DB_SCHEMA;
		$options = array(
			PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
		);

		if( version_compare(PHP_VERSION, '5.3.6', '<') ){
			if( defined('PDO::MYSQL_ATTR_INIT_COMMAND') ){
				$options[PDO::MYSQL_ATTR_INIT_COMMAND] = 'SET NAMES ' . DB_ENCODING;
			}
		}else{
			$dsn .= ';charset=' . DB_ENCODING;
		}

		$pdo = new PDO($dsn, DB_USER, DB_PASSWORD, $options);

		if( version_compare(PHP_VERSION, '5.3.6', '<') && !defined('PDO::MYSQL_ATTR_INIT_COMMAND') ){
			$sql = 'SET NAMES ' . DB_ENCODING;
			$pdo->exec($sql);
		}
	
	   //$pdo = new PDO('mysql:dbname=vcbf;host=localhost', 'root', '123456');
	} 
	catch (PDOException $e) 
	{
	   die("ERROR: Could not connect: " . $e->getMessage());
	} 

    return $pdo;

}

function select($pdo, $sql)
{
  try 
  { 
  	//$pdo=cn_db();
  	$result=$pdo->query($sql);
	$pdo=null;
	
	return $result;
	
  } 
  catch(PDOException $e) 
  {
  	echo $e->getMessage() . "</br>"; 
  } 	
}

function execution($pdo, $sql)
{
  try 
  { 
  	//$pdo=cn_db();
  	$result=$pdo->exec($sql);
	$pdo=null;
	
	return $result;
	
  } 
  catch(PDOException $e) 
  {
  	echo $e->getMessage() . "</br>"; 
  } 	
}
?>
</body>
</html>
