<html>
	<body>
	<#if roles == "admin">
		<#include "adminMenu.ftl">
	<#elseif roles == "employee">
		<#if status == true>
			<#include "employeeMenu.ftl">
		<#else>
			<#include "../nikoniko/addNikoNiko.ftl">
		</#if>
	<#elseif roles == "vp">
		<#include "vpMenu.ftl">
	<#elseif roles == "chefProjet">
		<#include "chefProjetMenu.ftl">
	<#else>
		<#include "gestionTeam.ftl">
	</#if>
	</body>
</html>