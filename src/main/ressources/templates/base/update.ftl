<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>

<body>
	<h1>${page}</h1>
	<table class="table table-bordered table-hover">
	<form action = "" method = "POST">
		<#list sortedFields as field>
			<#if field != "id">
				<#list items?keys as key>
					<#if key == field>
						<tr>
							<th>${key}</th>
							<#if items[key]?is_boolean>
								<td>
									<input type="text" name = "${key}">
								</td>
							<#else>
								<td>
									<input type="text" name = "${key}">
								</td>
							</#if>
						</tr>
					</#if>
				</#list>
			</#if>
		</#list>
	</table>
		<input type="submit" value="Update">
	</form><br>
	<a href="../">Back</a>
</body>