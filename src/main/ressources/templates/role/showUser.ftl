<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- css -->
    <link href="/css/design.css"  rel="stylesheet">

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
	                        <button onclick="location.href='/menu'" class="logout"> Menu </button>
	                        <button onclick="location.href='/logout' " class="logout"> Déconnexion </button>
	                        <button onclick="location.href='${back}'" class="logout"> Retour </button>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<h1> Utilisateurs ayant le rôle : ${page}  </h1>
		<table class="table table-bordered table-hover">
			<tr>
				<#list items as item>
					<#list sortedFields as field>
						<#list item?keys as key>
							<#if key == field && key != "id" && key!= "password">
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
							<#if key == field && key != "id" && key!= "password">
								<#if item[key]?is_boolean>
									<td>${item[key]?c}</td>
								<#elseif item[key]?is_date_like>
									<td>${item[key]?string("yyyy:MM:dd HH:mm:ss")}</td>
								<#else>
									<td>${item[key]}</td>

								</#if>
							</#if>
						</#list>
					</#list>
					<td>
						<form action = "" method = "POST">
							<#include "../includable/security/securityToken.ftl">
							<input type="hidden" name = "idUser" value = "${item["id"]}">
							<input type="submit" value="Enlever"><br>
						</form>
					</td>
				</tr>
			</#list>
		</table>
	<a class ="btn btn-default" href = "${add}"> Ajouter un utilisateur </a> <br>
</body>
</html>