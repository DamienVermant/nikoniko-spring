<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>

<body>
	<h1>${page}</h1>
	<a href="${go_create}">Create new</a>
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
					<td>
						<#if item["id"]??>
							<a href="${item["id"]}/${go_show}">Select</a>

						<#else>
							<a href="${item["idLeft"]}/${item["idRight"]}/${go_show}">Select</a>
						</#if>
					</td>
					<td>
						<#if item["id"]??>
							<form action = "${item["id"]}/${go_delete}" method = "POST">
								<input type="hidden" name = "id" value = "${item["id"]}">
								<input type="submit" value="Delete">
							</form>

						<#else>
							<form action = "${item["idLeft"]}/${item["idRight"]}/${go_delete}" method = "POST">
								<input type="hidden" name = "idl" value = "${item["idLeft"]}">
								<input type="hidden" name = "idr" value = "${item["idRight"]}">
								<input type="submit" value="Delete">
							</form>
						</#if>

					</td>
				</tr>
			</#list>
		</table>
</body>