<!-- Je renvoie sur la page "items" qui contient tous les utilisateurs -->
<head>
	<!-- Encodage -->
	<meta charset="utf-8">

	<!-- Bootstrap -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

	<!-- CSS -->
	<link rel="stylesheet" href="/login.css">

	<!-- Fonts -->
	<link href="https://fonts.googleapis.com/css?family=Gloria+Hallelujah" rel="stylesheet"> 
	<link href="https://fonts.googleapis.com/css?family=VT323" rel="stylesheet"> 

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
				</div>
			</div>
		</div>
	</div>
</div>

<hr>

<!-- FIELDS -->
<div class="container-fluid">
	<div class="row-fluid">
		<div class="col-lg-4">
			<div class="text">Login :</div>
			
			<!-- TABLE A SUPPRIMER PAR LA SUITE -->
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
									<td>${item[key]?string("yyyy:MM:dd HH:mm:ss")}</td>
								<#else>
									<td>${item[key]}</td>
								</#if>
							</#if>
						</#list>
					</#list>
				</tr>
			</#list>
		</table>
		
		
			<form action = "" method = "POST">
				<input type="texte" name="login" placeholder="Votre login"> <br>
				<div class="text"> Mot de passe :</div> 
					<input type="texte" name="password" placeholder="Votre mot de passe"> <br>
					<input type="submit" value="Se connecter"><br>
					<button class="buttons" onclick="affichage()" class="password"> Mot de passe oublié ?</button>
				</div>
			</form>
		<div class="col-lg-4">
		<div class="comment">
			Bienvenue sur Niko Niko !
			Merci de vous connecter et de sélectionner votre satisfaction du jour.
		</div>
		<div class="comment">
			Ca ne prendra pas plus d'une minute ! Promis ! 
		</div>
		</div>
		<div class="col-lg-4">
			<div class="img_graph">
				<img src="http://www.cgi-recrute.fr/sites/cgi/files/uploads/images/Top_Employer_France_2016.png">
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

<script>
function affichage() {
    alert("Veuillez contacter l'administrateur système.");
}
</script>

</body>