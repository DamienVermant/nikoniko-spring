<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>

<body>
	<h1>${page}</h1>
	<form action = "" method = "POST">
	<#include "../includable/security/securityToken.ftl">
	<table class="table table-bordered table-hover">
		<#list sortedFields as field>
			<#if field != "id">
				<#list items?keys as key>
					<#if key == field>
					<#if key== "id">
						<#else>
						<tr>
							<th>${key}</th>
							<#if items[key]?is_boolean>
								<td>
									<input type="text" name = "${key}" value ="${items[key]?c}">
								</td>
							<#elseif items[key]?is_date_like>
								<td>
									<input type="text" name = "${key}" value ="${items[key]?string("yyyy/MM/dd")!}">
								</td>
							<#else>
								<td>
									<input type="text" name = "${key}" value ="${items[key]}">
								</td>
							</#if>
						</tr>
					</#if>
					</#if>
				</#list>
			</#if>
		</#list>
	</table>
		<input type="submit" value="Update">
	</form>
	<#if items["id"]??>
		<a href="../">Back</a>
	<#else>
		<a href="../..">Back</a>
	</#if>
</body>
</html>