<html>

<head>
	<!-- Encodage -->
	<meta charset="utf-8">

	<!-- Bootstrap -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">


	<link rel="stylesheet" type="text/css" href="login.css">
	
	
	<!-- Redirection ???? -->
	<link rel="stylesheet" href="menu/">
	
	<!-- Fonts -->
	<link href="https://fonts.googleapis.com/css?family=Gloria+Hallelujah" rel="stylesheet"> 
	<link href="https://fonts.googleapis.com/css?family=VT323" rel="stylesheet"> 

	<!-- Title -->
	<title>Se connecter</title>
	
</head>
<body>
<style>
<#include "static/admin.css">
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
						<button onclick="location.href='/logout' " class="logout"> Déconnexion </button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<hr>

<div class="welcome"> Bienvenue : ${auth} </div>

<hr>

<!-- MENU -->
<div class="container-fluid">
	<div class="row-fluid">
		<div class="col-lg-12">
			<div class="row-fluid">
				<a class="text" href=""> Visualisation des graphes (NOT ACTIVE)</a>
			</div>
			<div class="row-fluid">
				<div class="text"> Administration des tables simples </div>
				<ul>
					<li> <a href="${go_users}"> Users </a> </li> 
					<li> <a href="${go_nikonikos}"> Niko Niko </a> </li>
					<li> <a href="${go_teams}"> Teams </a> </li>
					<li> <a href="${go_verticales}"> Verticales </a> </li>
					<li> <a href="${go_roles}"> Roles </a> </li>
					<li> <a href="${go_functions}"> Functions </a> </li>
				</ul>
			</div>
			<div class="row-fluid">
				<div class="text"> Administration des tables d'association </div>
				<ul>
					<li> <a href="${go_user_has_team}"> User Has Team </a> </li>
					<li> <a href="${go_user_has_role}"> User Has Role </a> </li>
					<li> <a href="${go_role_has_function}"> Role Has Function </li>
				</ul>
			</div>
		</div>
	</div>
</div>

<hr>
				
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