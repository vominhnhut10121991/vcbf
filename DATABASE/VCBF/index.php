<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="style.css" media="screen" />
<title>Index</title>
</head>

<body>
	<div id="wrapper">


		<?php include('vcbf_header.php'); ?>
		
		<?php
			session_start();
			if(!isset($_SESSION['login']))
				header("location:vcbf_login.php");
		?>

		<form id="form" method="post" enctype="multipart/form-data">
            <div id="content"> 
                <div id='log'>
					    <h1>Upload file </h1>
                        <hr />
                        <div id='log_username'>
                            <div id='log_username_text'>Choose type of file:</div> 
                            <div id='log_username_input'>
								<select name="select" onchange="document.getElementById('form').action = this.value;">
								  <option value="">type of file</option>
								  <option value="vcbf_information_message.php">vcbf_information_message</option>
								  <option value="vcbf_nav_bcf.php">vcbf_nav_bcf</option>
								  <option value="vcbf_nav_tbf.php">vcbf_nav_tbf</option>
								  <option value="vcbf_news.php">vcbf_news</option>
								  <option value="vcbf_portfolio_detail_bcf.php">vcbf_portfolio_detail_bcf</option>
								  <option value="vcbf_portfolio_detail_tbf.php">vcbf_portfolio_detail_tbf</option>
								  <option value="vcbf_portfolio_industry_breakdown_bcf.php">vcbf_portfolio_industry_breakdown_bcf</option>
								  <option value="vcbf_portfolio_industry_breakdown_tbf.php">vcbf_portfolio_industry_breakdown_tbf</option>
								  <option value="vcbf_portfolio_statistic_bcf.php">vcbf_portfolio_statistic_bcf</option>
								  <option value="vcbf_portfolio_statistic_tbf.php">vcbf_portfolio_statistic_tbf</option>
								  <option value="vcbf_portfolio_top_holding_bcf.php">vcbf_portfolio_top_holding_bcf</option>
								  <option value="vcbf_portfolio_top_holding_tbf.php">vcbf_portfolio_top_holding_tbf</option>
								  <option value="vcbf_reminding_message.php">vcbf_reminding_message</option>
								  <option value="vcbf_user.php">vcbf_user</option>
								  <option value="vcbf_version.php">vcbf_version</option>
								  <option value="vcbf_weekly_fund_price_bcf.php">vcbf_weekly_fund_price_bcf</option>
								  <option value="vcbf_weekly_fund_price_tbf.php">vcbf_weekly_fund_price_tbf</option>
								</select>
							</div>
                        </div>
                        <div id='log_password'>
                            <div id='log_password_text'>Filename:</div>
                            <div id='log_password_input'><input type="file" name="file" id="file"></div>
                        </div>
                        <div id='log_submit'>
                            <div id='log_submit_text'><input type="submit" name="submit" value="Submit"></div>
                            <div id='log_submit_input'><a href="vcbf_logout.php">Logout</a></div>
                        </div>
                </div>                
            </div>
    	</form>

		<?php include('vcbf_footer.php'); ?>


	</div>
</body>
</html>
