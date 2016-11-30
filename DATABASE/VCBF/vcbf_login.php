<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="style.css" media="screen" />
<script type="text/javascript" src="vcbf_validation.js"></script>
<title>Login</title>
</head>

<body>
	<div id="wrapper">


		<?php include('vcbf_header.php'); ?>

		<?php //include('vcbf_navigation.php'); ?>
        
        <?php
			session_start();
			if(isset($_SESSION['login']))
				header("location:index.php");
		?>
        
        <?php //include('vcbf_nav_category.php'); ?>

		<form onsubmit="return checkLogin();" action="vcbf_check_login.php" method="post">
            <div id="content"> 
                <div id='log'>
                        <h1>Sign in </h1>
                        <hr />
                        <div id='log_username'>
                            <div id='log_username_text'>User Name</div> 
                            <div id='log_username_input'><input type="text" id="username" name="username" maxlength="20"/></div><br />
							<div class='error' id='username_null_error'>Required: Username cannot be null<br /></div><br />
                        </div>
                        <div id='log_password'>
                            <div id='log_password_text'>Password</div>
                            <div id='log_password_input'><input type="password" id="password" name="password" maxlength="20"/></div><br />
							<div class='error' id='password_null_error'>Required: Password cannot be null<br /></div><br />
                        </div>
                        <div id='log_submit'>
                            <div id='log_submit_text'><input type='submit' value='Sign in' /></div>
                            <div id='log_submit_input'><input type='reset' value='Reset' /></div>
                        </div>
                </div>                
            </div>
    	</form>

		<?php include('vcbf_footer.php'); ?>


	</div>
</body>
</html>
