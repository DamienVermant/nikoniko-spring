<html>

<head>
	<!-- Encodage -->
	<meta charset="utf-8">

	<!-- Bootstrap -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

	<!-- Fonts -->
	<link href="https://fonts.googleapis.com/css?family=Gloria+Hallelujah" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css?family=VT323" rel="stylesheet">

	<!-- Title -->
	<title>Se connecter</title>

</head>
<body>
<style>
	<#include "static/employee.css">
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
					<#if true>
						<button onclick="location.href='${add_nikoniko}'" class="vote"> Modifier vote </button>
					<#else>
					</#if>
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

<div class="container-fluid">
	<div class="row-fluid">
		<div class="col-lg-12">
			- <a href="${pie_chart}"> Voir vos résultats <a> <br>
			<#if false>
				- <a href="${add_nikoniko}"> Donner votre satisfaction ! </a>
			<#else>
				- <a href="${add_nikoniko}"> Pas encore voté... Go ? </a>
			</#if>
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

