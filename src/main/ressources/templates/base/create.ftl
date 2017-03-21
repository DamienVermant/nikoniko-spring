<body>
	<h1>${page}</h1>

	<form action = "${create_item}" method = "POST">
		<table class="table table-bordered table-hover">
			<#list sortedFields as field>
				<#if field != "id">
					<#list item?keys as key>
						<#if key == field>
							<tr>
								<th>${key}</th>
								<td>
									<input type="text" name = "${key}">
								</td>
							</tr>
						</#if>
					</#list>
				</#if>
			</#list>
		</table>
		<input type="submit" value="create"><br>
	</form>
	
		<a href="../">Back</a><br>					
			

</body>