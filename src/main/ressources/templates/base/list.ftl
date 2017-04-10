<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- css -->
<link href="/css/design.css"  rel="stylesheet">

<!-- Fonts -->
<link href="https://fonts.googleapis.com/css?family=Gloria+Hallelujah" rel="stylesheet"> 

</head>

<body>
	<div class="container-fluid">
	    <div class="row-fluid">
	        <div class="col-lg-2">
	            <img class="logo" src="https://upload.wikimedia.org/wikipedia/fr/5/51/LOGO-CGI-1993-1998.svg">
	        </div>
	        <div class="col-lg-8">
	            <div class="title">Niko-Niko</div>
	        </div>
	        <div class="col-lg-2">
	            <div class="row-fluid">
	                <div class="col-lg-12">
	                    <div class="align">
	                        <button onclick="location.href='/logout' " class="logout"> Déconnexion </button>
	                        <button onclick="location.href='/menu/'" class="logout"> Retour </button>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>

	<h1>Liste des ${page}</h1>
	<form action = "" method = "POST">
		<#include "../includable/security/securityToken.ftl">
		<#if model = "user">
			<input type="text" class="search" onkeyup="myFunction()" name="name" placeholder="Search for registration" title="Type in a name">
		<#elseif model = "team">
			<input type="text" class="search" onkeyup="myFunction()" name="name" placeholder="Search for team names" title="Type in a name">
		<#elseif model = "verticale" >
			<input type="text" class="search" onkeyup="myFunction()" name="name" placeholder="Search for verticales names" title="Type in a name">
		<#elseif model = "nikoniko">
			<input type="text" class="search" onkeyup="myFunction()" name="name" placeholder="Search for registration_cgi" title="Type in a name">
		<#else>
		</#if>
		<#if model == "role">
		<#else>
			<input type="submit" value="Rechercher">
		</#if>
	</form>
	<#if model == "nikoniko" || model == "role">
	<#else>
		 <br><a class ="btn btn-default" href="${go_create}">Créer un nouveau</a>
	</#if>
	<table class="table table-bordered table-hover">
		<tr>
			<#list items as item>
				<#list sortedFields as field>
					<#list item?keys as key>
						<#if key == field>
							<#if key == "login" || key == "password" || key== "id">
							<#elseif item[key]??>
								<th>${key}</th>
							<#else>
							</#if>
						</#if>
					</#list>
				</#list>
				<#break>
			</#list>
			<#if model == "role">
				<th>Actions</th>
			<#else>
				<th colspan = 2>Actions</th>
			</#if>
		</tr>
		<#list items as item>
			<tr>
				<#list sortedFields as field>
					<#list item?keys as key>
						<#if key == field>
							<#if key == "login" || key == "password" || key== "id">
							<#else>
								<#if item[key]??>
									<#if item[key]?is_boolean>
									<td>${item[key]?c}</td>
								<#elseif item[key]?is_date_like>
									<td>${item[key]?string("yyyy/MM/dd")}</td>
								<#else>
									<td>${item[key]}</td>
								</#if>
								<#else>
								</#if>
							</#if>
						</#if>
					</#list>
				</#list>
				<#if model == "role">
					<td>
						<#if item["id"]??>
							<a class = "btn btn-default" href="${item["id"]}/showUser">Afficher les utilisateurs</a>
						<#else>
							<a class = "btn btn-default" href="${item["idLeft"]}/${item["idRight"]}/${go_show}">Select</a>
						</#if>
					</td>
				<#else>
					<td>
						<#if item["id"]??>
							<a class = "btn btn-default" href="${item["id"]}/${go_show}">Détails</a>

						<#else>
							<a class = "btn btn-default" href="${item["idLeft"]}/${item["idRight"]}/${go_show}">Détails</a>
						</#if>
					</td>
					<td>
							<#if item["id"]??>
								<form action = "${item["id"]}/${go_delete}" method = "POST">
								<#include "../includable/security/securityToken.ftl">
									<input type="hidden" name = "id" value = "${item["id"]}">
									<input type="submit" value="Supprimer">
								</form>

							<#else>
								<form action = "${item["idLeft"]}/${item["idRight"]}/${go_delete}" method = "POST">
								<#include "../includable/security/securityToken.ftl">
									<input type="hidden" name = "idl" value = "${item["idLeft"]}">
									<input type="hidden" name = "idr" value = "${item["idRight"]}">
									<input type="submit" value="Supprimer">
								</form>
							</#if>
						</td>
					</#if>
				</tr>
			</#list>
		</table>
</body>
</html>