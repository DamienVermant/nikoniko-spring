<!DOCTYPE html>
<html>

<head>
	<!-- Encodage -->
	<meta charset="utf-8">

	<!-- Bootstrap -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

	<!-- Fonts -->
	<link href="https://fonts.googleapis.com/css?family=Gloria+Hallelujah" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css?family=VT323" rel="stylesheet">

    <!-- css -->
	<#if roles == "chefProjet">
		<link href="/css/chefprojet.css"  rel="stylesheet">
	<#else>
		<link href="/css/employee.css"  rel="stylesheet">
	</#if>

	<!-- Title -->
	<title>Se connecter</title>

</head>
<body>

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
					<#if roles == "chefProjet" >
						<button onclick="location.href=''" class="password"> Préférences </button>
					<#else>
					</#if>
					<#if mood != 0 && status == true>
						<button onclick="location.href='${add_nikoniko}'" class="vote"> Modifier vote </button>
					<#else>
					</#if>
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

			<#if roles == "vp">
				<h2> Gestion </h2>
				- <a href=""> Menu graphes </a> <br>
			<#else>
			</#if>

			<#if roles == "gestionTeam">
				<h2> Gestion </h2>
				- <a href=""> Gérer équipe </a> <br>
			<#else>
			</#if>

			<h2> Niko Niko </h2>
			- <a href="${calendar}"> Voir calendrier <a> <br>
			- <a href="${pie_chart}"> Voir vos résultats <a> <br>
			<#if mood == 0 || status == false>
				- <a href="${add_nikoniko}"> Pas de vote enregistré... On vote ? </a>

			<#else>
			</#if>
			<#if lastNiko == true>
			 	- <a href="${add_last}"> Voter pour la veille ? </a>

			<#else>
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

</body>
</html>
