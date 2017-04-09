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
	                        <button onclick="location.href='/logout' " class="logout"> Déconnexion </button>
	                        <#if item["id"]??>
	                            <button onclick="location.href='../'" class="logout"> Retour </button>
	                        <#else>
	                            <button onclick="location.href='../..'" class="logout"> Retour </button>
	                        </#if>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<h1>${page}</h1>
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
	<a href="${show_users}"> Show users </a> <br>
	<a href="${go_update}">Update</a> <br>
	<a href="${go_delete}">Delete</a> <br>
</body>
</html>