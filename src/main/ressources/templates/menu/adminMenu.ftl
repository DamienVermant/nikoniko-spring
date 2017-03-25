<html>
	<body>
		<h1> Bienvenue : ${auth} </h1>
		<h1> ADMINISTRATION </h1>
			<h2> ADMIN SIMPLE TABLES </h2>
				<a href="${go_users}"> Users </a> <br>
				<a href="${go_nikonikos}"> Niko Niko </a> <br>
				<a href="${go_teams}"> Teams </a> <br>
				<a href="${go_verticales}"> Verticales </a> <br>
				<a href="${go_roles}"> Roles </a> <br>
				<a href="${go_functions}"> Functions </a> 
			<h2> ADMIN ASSOCIATIONS TABLES </h2>
				<a href="${go_user_has_team}"> User Has Team </a> <br>
				<a href="${go_user_has_role}"> User Has Role </a> <br>
				<a href="${go_role_has_function}"> Role Has Function </a>
	</body>
</html>