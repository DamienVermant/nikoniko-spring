<html>
	<body>
		<form action = "/login" method = "POST">
			<input type = "text" name = "login"/>
			<input type = "password" name = "password"/>
			<input type = "submit" name = "validate"/>
			<#include "../includable/security/securityToken.ftl">
		</form>
	</body>
</html>