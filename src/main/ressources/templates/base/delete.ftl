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
	                        <button onclick="location.href='${go_index}'" class="logout"> Retour </button>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<h1>Elément à supprimer :</h1>
	<table class="table table-bordered table-hover">
		<#list sortedFields as field>
			<#if field != "id">
				<#list item?keys as key>
					<#if key == field>
						<tr>
							<th>${key}</th>
							<#if item[key]?is_boolean>
								<td>
									${item[key]?c}
								</td>
							<#else>
								<td>
									${item[key]}
								</td>
							</#if>
						</tr>
					</#if>
				</#list>
			</#if>
		</#list>
	</table>
	<form action = "${go_delete}" method = "POST">
		<#include "../includable/security/securityToken.ftl">
		<#if item["id"]??>
			<input type="hidden" name = "id" value = "${item["id"]}">
			<span class = "confirmation_message">Êtes-vous vraiment sûr de vouloir supprimer cet élément? </span>
			<input type="submit" value="Supprimer" class="logout">
		<#else>
			<input type="hidden" name = "idl" value = "${item["idLeft"]}">
			<input type="hidden" name = "idr" value = "${item["idRight"]}">
			<span class = "confirmation_message">Êtes-vous vraiment sûr de vouloir supprimer cet élément? </span>
			<input type="submit" value="Supprimer" class="logout"><br>
		</#if>
	</form>
</body>
</html>