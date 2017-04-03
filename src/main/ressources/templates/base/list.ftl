<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>

<body>
	<h1>${page}</h1>
	<form action = "" method = "POST">
		<#include "../includable/security/securityToken.ftl">
		<#if model = "user" || model = "">
			<input type="text" class="search" onkeyup="myFunction()" name="name" placeholder="Search for registration" title="Type in a name">
		<#elseif model = "team" || model = "">
			<input type="text" class="search" onkeyup="myFunction()" name="name" placeholder="Search for team names" title="Type in a name">
		<#elseif model = "verticale" || model = "">
			<input type="text" class="search" onkeyup="myFunction()" name="name" placeholder="Search for verticales names" title="Type in a name">
		<#elseif model = "nikoniko">
			<input type="text" class="search" onkeyup="myFunction()" name="name" placeholder="Search for id user" title="Type in a name">
		<#else>
		</#if>
		
		<#if model == "role">
		<#else>
			<input type="submit" value="Search">
		</#if>
	</form>
	<#if model == "nikoniko" || model == "role">
	<#else>
		<a href="${go_create}">Create new</a>
	</#if>
	<table class="table table-bordered table-hover">
			<tr>
				<#list items as item>
					<#list sortedFields as field>
						<#list item?keys as key>
							<#if key == field>
								<th>${key}</th>
							</#if>
						</#list>
					</#list>
					<#break>
				</#list>
			</tr>
			<#list items as item>
				<tr>
					<#list sortedFields as field>
						<#list item?keys as key>
							<#if key == field>
								<#if item[key]?is_boolean>
									<td>${item[key]?c}</td>
								<#elseif item[key]?is_date_like>
									<td>${item[key]?string("yyyy/MM/dd")}</td>
								<#else>
									<td>${item[key]}</td>
								</#if>
							</#if>
						</#list>
					</#list>
					<td>
						<#if item["id"]??>
							<a href="${item["id"]}/${go_show}">Select</a>

						<#else>
							<a href="${item["idLeft"]}/${item["idRight"]}/${go_show}">Select</a>
						</#if>
					</td>
					<td>
						<#if item["id"]??>
							<form action = "${item["id"]}/${go_delete}" method = "POST">
							<#include "../includable/security/securityToken.ftl">
								<input type="hidden" name = "id" value = "${item["id"]}">
								<input type="submit" value="Delete">
							</form>

						<#else>
							<form action = "${item["idLeft"]}/${item["idRight"]}/${go_delete}" method = "POST">
							<#include "../includable/security/securityToken.ftl">
								<input type="hidden" name = "idl" value = "${item["idLeft"]}">
								<input type="hidden" name = "idr" value = "${item["idRight"]}">
								<input type="submit" value="Delete">
							</form>
						</#if>
					</td>
				</tr>
			</#list>
		</table>
		<a href="/menu/">Back</a>
</body>