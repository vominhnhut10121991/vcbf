<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
</head>

<body>
<?php
	session_start();
		
	require("vcbf_database_connection.php");
	$pdo=cn_db();
	
	$username = trim($_POST['username']);
	$password = trim($_POST['password']);
	$sql="select * from vcbf_admin where username='" . $username . "' and password='" . $password . "'";
	$result=select($pdo, $sql);
	$total = $result->rowCount();

	echo $total . "username: " . $username . " - " . $password ;
	if($total > 0)
	{
        $_SESSION['login'] = true;
		header("location:index.php");
	}
	else
		//header("location:vcbf_login.php?fail=wrong username/password");
		echo "Wrong username/password. Please, <a href='vcbf_login.php'>try again</a>";
?>
</body>
</html>
