<html>
<head>
	<!-- Encodage -->
	<meta charset="utf-8">

	<!-- Bootstrap -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

	<!-- CSS -->
	<link rel="stylesheet" href="vote.css">

	<!-- Fonts -->
	<link href="https://fonts.googleapis.com/css?family=Gloria+Hallelujah" rel="stylesheet"> 
	<link href="https://fonts.googleapis.com/css?family=VT323" rel="stylesheet"> 

	<!-- Title -->
	<title>Satisfaction</title>
</head>
<body>
<style>
<#include "static/vote.css">
</style>

<!-- HEAD -->
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
					<button onclick="location.href='changer_mdp.html'" class="password"> Préférences (KO) </button> 
					<button onclick="location.href='menu_employe.html'" class="vote"> Voter plus tard (KO)</button>
					<button onclick="location.href='/logout' " class="logout"> Déconnexion </button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<hr>

<!-- MAKE YOUR CHOICE -->
<form action = "" method = "POST">
<div class="container-fluid">
	<div class="row-fluid">
		<div class="col-lg-12">
			<div class="col-md-4 col-sl-4 col-xs-12"">
        		<center>
					<div id="crouge"></div>
					<input type="radio" name="mood" value="1" > 
					<div class="bad_day"> Vous avez passé une très très mauvaise journée...</div>
			</center>
				</center>
			</div>
			<div class="col-md-4 col-sl-4 col-xs-12">
				<center>
					<div id="corange"></div>
					<input type="radio" name="mood" value="2" >
					<div class="correct_day"> Vous avez passé une journée assez moyenne. </div>
				</center>
			</div>
			<div class="col-md-4 col-sl-4 col-xs-12">
				<center>
					<div id="cvert"></div>
					<input type="radio" name="mood" value="3">
					<div class="good_day"> Vous avez passé une bonne journée. </div>
				</center>
			</div>
		</div>
	</div>
</div>

<hr>

<!-- COMMENTARY -->
<div class="container-fluid">
	<div class="row-fluid">
		<div class="col-lg-12">
			<center>
				<#include "../includable/security/securityToken.ftl">
				<TEXTAREA style="margin-top: 30px" name="comment" rows=5 cols=60>Ecrire votre commentaire...</TEXTAREA> <br>
				<button class="buttons" onclick="location.href='/menu'"> Valider </button>
				</form>
			</center>
		</div>
	</div>
</div>

<!-- FOOTER -->
<div class="container-fluid">
	<div class="row-fluid">
		<div class="col-lg-12">
			<div class="copyright">&copy; Niko-Niko CGI 2017</div>
		</div>
	</div>
</div>
</body>
</html>
 
<!-- COMMENT TO TEST
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>
	<h1>${page}</h1>
	<form action = "${create_item}" method = "POST">
        <table class="table table-bordered table-hover">
            <#list sortedFields as field>
                <#if field != "id">
                    <#list item?keys as key>
                        <#if key == field>
                            <tr>
                                <th>${key}</th>
                                <td>
                                    <input type="text" name = "${key}">
                                </td>
                            </tr>
                        </#if>
                    </#list>
                </#if>
            </#list>
        </table>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="submit" value="create"><br>
    </form>
    <#if item["id"]??>
        <a href="${go_index}">Back</a><br>
    <#else>
        <a href="${go_index}">Back</a><br>
    </#if>
</body>
-->