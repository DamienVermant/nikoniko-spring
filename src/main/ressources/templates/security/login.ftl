<html>
	<body>
		<form action = "/login" method = "POST">
			<input type = "text" name = "login"/>
			<input type = "password" name = "password"/>
			<#include "../includable/security/securityToken.ftl">
			<input type = "submit" name = "validate"/>
		</form>
	</body>
</html>