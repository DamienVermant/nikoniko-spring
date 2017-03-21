<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>

<body>
	<h1>Login Page</h1>
	form
	<table class="table table-bordered table-hover">
		<#list sortedFields as field>
			<tr>
				<th>${field}</th>
				<td>
					<input type="text" name = "${field}">
				</td>
			</tr>
		</#list>
	</table>
	<input type="submit" value="Connexion"><br>
	/form
</body>