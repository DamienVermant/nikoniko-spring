<!DOCTYPE html>
<html>
<head>
<!-- Encodage -->
    <meta charset="utf-8">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- css -->
	<link href="/css/nikoniko.css"  rel="stylesheet">
	
	<!-- Fonts -->
	<link href="https://fonts.googleapis.com/css?family=Gloria+Hallelujah" rel="stylesheet"> 

</head>
<body>
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
	                    <button onclick="location.href='${back}'" class="vote"> Retour </button>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<br>

	<div class = "date_buttons">
		<span class = "previous_button">
			<a href="?month=${monthToUse}&year=${yearToUse?c}&action=previous">Mois précédent</a>
		</span>
		<span class = "month_year">
			Vos résultats pour ${monthName} ${yearToUse}
		</span>
		<span class = "next_button">
			<a href="?month=${monthToUse}&year=${yearToUse?c}&action=next">Mois suivant</a>
		</span>
	</div>
	<table class="table table-bordered table-hover">
		<tr>
			<#list jourSemaine as jour>
				<#if jour != "Samedi"&&jour != "Dimanche">
					<th class = "daysNames">${jour}</th>
				</#if>
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
									<#if jour == key && week == map["endOfWeek"]&&key != "Samedi"&&key != "Dimanche">
											<td class = "fillableDay">
												<div class = "dayHeader">
														${map[key]}
												</div>
												<div class = "daynikos" onclick = "location.href='/graph/showgraph/${yearToUse?c}/${monthToUse}/${map[key]}'">
														<center>
															<#if map["nikoOfDay"]== 1>
																<img src = "http://i.ebayimg.com/images/i/151172319961-0-1/s-l1000.jpg">
															<#elseif map["nikoOfDay"]== 2>
																<img src = "http://cdn.olshop.ag/a/store_files/11/images/product_images/info_images/171900_0_e3mwjfux8n.jpg">
															<#elseif map["nikoOfDay"]== 3>
																<img src = "http://i.ebayimg.com/images/i/151172320059-0-1/s-l1000.jpg">
															<#else>
															</#if>
														</center>
												</div>
											</td>
									</#if>
								<#else>
									<#if jour == key && week == map["endOfWeek"]&&key != "Samedi"&&key != "Dimanche">
											<td class = "fillableDay">
												<div class = "dayHeader" align="right">
														${map[key]}
												</div>
												<div class = "daynikos" onclick = "location.href='/graph/showgraph/${yearToUse?c}/${monthToUse}/${map[key]}'">
												<div class = "daynikos" onclick = "location.href='nikoniko/day/${yearToUse?c}/${monthToUse}/${map[key]}'">
														<center>
															<#if map["nikoOfDay"]== 1>
																<img src = "http://i.ebayimg.com/images/i/151172319961-0-1/s-l1000.jpg">
															<#elseif map["nikoOfDay"]== 2>
																<img src = "http://cdn.olshop.ag/a/store_files/11/images/product_images/info_images/171900_0_e3mwjfux8n.jpg">
															<#elseif map["nikoOfDay"]== 3>
																<img src = "http://i.ebayimg.com/images/i/151172320059-0-1/s-l1000.jpg">
															<#else>
															</#if>
														</center>
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
</html>