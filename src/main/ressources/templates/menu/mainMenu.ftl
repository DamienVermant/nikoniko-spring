<html>
	<body>
	<#if roles == "admin">
		<#include "adminMenu.ftl">
	<#elseif roles == "employee">
		<#include "employeeMenu.ftl">
	<#elseif roles == "vp">
		<#include "vpMenu.ftl">
	<#elseif roles == "chefProjet">
		<#include "chefProjetMenu.ftl">
	<#else>
		<#include "gestionTeam.ftl">
	</#if>
	</body>
</html>