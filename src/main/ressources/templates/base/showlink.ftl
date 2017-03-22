<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>

<body>
	<h1>USERS IN TEAM : ${page}  </h1>
		
	<table class="table table-bordered table-hover">
		
		<#list fullName as name>
			<tr>
				<td>
			 ${name} 
			 	</td>
			 	<!-- ADD SELECT BUTTON REF TO USER PAGE -->
			</tr>
		</#list>		
		
	</table>		
	
</body>