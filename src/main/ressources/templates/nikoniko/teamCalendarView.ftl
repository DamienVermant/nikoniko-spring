<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>
<body>
<body>
	<h1>Calendrier des nikonikos du mois : ${monthName} ${yearToUse}</h1>
	<a href="?month=${monthToUse}&year=${yearToUse?c}&action=previous">Previous Month</a> Mois en cours
	<a href="?month=${monthToUse}&year=${yearToUse?c}&action=next">Next Month</a>

	<table class="table table-bordered table-hover">
		<tr>
			<#list jourSemaine as jour>
				<th class = "daysNames">${jour}</th>
			</#list>
		</tr>
		<#list nbweeks as week>
			<tr>
				<#if week == 1 && firstWeekUncomplete == 1>
					<#list 1..nbJoursSemaineAIgnorer as i>
					<td class = "emptyDay"></td>
					</#list>
				</#if>
				<#list jourSemaine as jour>
					<#list days as map>
				    	<#assign keys = map?keys>
				    		<#list map?keys as key>
								<#if map["uncompleteWeek"] == 1>
									<#if jour == key && week == map["endOfWeek"]>
											<td class = "fillableDay">
												<div class = "dayHeader" align="right">
														${map[key]}

												</div>
												<div class = "daynikos" onclick = "location.href='?REDIRECTION_DYN_A_FAIRE'">
														Good :  ${map["nikoGood"]} <br>
														Neutral :  ${map["nikoNeutral"]} <br>
														Bad :  ${map["nikoBad"]} <br>
												</div>
											</td>
									</#if>
								<#else>
									<#if jour == key && week == map["endOfWeek"]>
											<td class = "fillableDay">
												<div class = "dayHeader" align="right">
														${map[key]}

												</div>
												<div class = "daynikos" onclick = "location.href='?REDIRECTION_DYN_A_FAIRE'">
														Good :  ${map["nikoGood"]} <br>
														Neutral :  ${map["nikoNeutral"]} <br>
														Bad :  ${map["nikoBad"]} <br>
												</div>
											</td>
									</#if>
								</#if>
							</#list>
						</#list>
					</td>
				</#list>
				<#if week == numberOfWeekInMonth && lastWeekUncomplete == 1>
					<#list 1..nbJoursSemaineAAjouter as i>
					<td class = "emptyDay"></td>
					</#list>
				</#if>
			</tr>
		</#list>


	</table>

	<a href="/menu/">Back</a>

</body>