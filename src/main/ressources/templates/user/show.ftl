<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>

<body>
<<<<<<< HEAD
	<h1>${page}</h1>
	<table class="table table-bordered table-hover">
		<#list sortedFields as field>
			<#if field != "id">
				<#list item?keys as key>
					<#if key == field>
						<tr>
							<th>${key}</th>
							<#if item[key]?is_boolean>
								<td>
									${item[key]?c}
								</td>
							<#else>
								<td>
									${item[key]}
								</td>
							</#if>
						</tr>
					</#if>
				</#list>
			</#if>
		</#list>
	</table>
    <a href="${show_nikonikos}"> Show nikonikos </a> <br>
    <a href="${show_verticale}"> Show verticale </a> <br>
	<a href="${show_roles}"> Show roles </a> <br>
	<a href="${show_teams}"> Show teams </a> <br>
	<a href="${go_update}">Update</a> <br>
	<a href="${go_delete}">Delete</a> <br>
=======
	<#if myRole??>
		<h1>${page} ROLE ADMIN </h1>

		<table class="table table-bordered table-hover">
			<#list sortedFields as field>
				<#if field != "id">
					<#list item?keys as key>
						<#if key == field>
							<tr>
								<th>${key}</th>
								<#if item[key]?is_boolean>
									<td>
										${item[key]?c}
									</td>
								<#else>
									<td>
										${item[key]}
									</td>
								</#if>
							</tr>
						</#if>
					</#list>
				</#if>
			</#list>
		</table>
	    <a href="${show_nikonikos}"> Show nikonikos </a> </br>
		<a href="${show_roles}"> Show roles </a> <br>
		<a href="${show_teams}"> Show teams </a> <br>
		<a href="${go_update}">Update</a> <br>
		<a href="${go_delete}">Delete</a> <br>
	<#else>
		<h1>${page} ROLE NON ADMIN</h1>
		<table class="table table-bordered table-hover">
			<#list sortedFields as field>
				<#if field != "id">
					<#list item?keys as key>
						<#if key == field>
							<tr>
								<th>${key}</th>
								<#if item[key]?is_boolean>
									<td>
										${item[key]?c}
									</td>
								<#else>
									<td>
										${item[key]}
									</td>
								</#if>
							</tr>
						</#if>
					</#list>
				</#if>
			</#list>
		</table>
	    <a href="${show_nikonikos}"> Show nikonikos </a> </br>
		<a href="${show_roles}"> Show roles(A virer dans l'affichage non admin)</a> <br>
		<a href="${show_teams}"> Show teams </a> <br>
		<a href="${go_update}">Update(A virer dans l'affichage non admin)</a> <br>
		<a href="${go_delete}">Delete(A virer dans l'affichage non admin)</a> <br>
	</#if>

>>>>>>> 04a082e45b29ae7793640eb72b47f7a7feaba518
	<#if item["id"]??>
		<a href="../">Back</a>
	<#else>
		<a href="../..">Back</a>
	</#if>
</body>