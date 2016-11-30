//vcbf_login.php
function checkLogin()
{
	username = document.getElementById("username").value;
	password = document.getElementById("password").value;
	
	if (username == "")
	{ 
		hideAllErrors();
		document.getElementById("username_null_error"). style.display= "inline";
		document.getElementById("username").select();
		document.getElementById("username").focus();
		return false;
	} 

	if (password == "")
	{ 
		hideAllErrors();
		document.getElementById("password_null_error"). style.display= "inline";
		document.getElementById("password").select();
		document.getElementById("password").focus();
		return false;
	} 


	return true;
}
	
function hideAllErrors()
{ 
	document.getElementById("username_null_error"). style.display= "none";
	document.getElementById("password_null_error"). style.display= "none";
}