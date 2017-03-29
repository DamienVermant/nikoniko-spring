<html>
<head>
</head>
	<body>
	<#if roles == "admin">
		<#include "adminMenu.ftl">
	<#elseif roles == "employee">
		<#if status == true>
			<#include "employeeMenu.ftl">
		<#else>
			<meta http-equiv="refresh" content="0; URL=/user/${id}/add">
		</#if>
	<#else>
		<#include "employeeMenu.ftl">
	</#if>
	</body>
</html>