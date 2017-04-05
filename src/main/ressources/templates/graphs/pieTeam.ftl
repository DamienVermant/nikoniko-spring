<!DOCTYPE html>
<html>

<head>
    <!-- Encodage -->
    <meta charset="utf-8">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">



    <!-- Redirection ???? -->
    <link rel="stylesheet" href="menu/">

    <!-- Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Gloria+Hallelujah" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=VT323" rel="stylesheet">


    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {

        var data = google.visualization.arrayToDataTable([
          ['NikoNiko', 'Number of Smile'],
          ['Good', ${good}],
          ['Medium', ${medium}],
          ['Bad', ${bad}]
        ]);

        var options = {
          backgroundColor:'transparent',
          legend: 'none',
          slices: {
            0: { color: '#00CC00' },
            1: { color: 'orange' },
            2: { color: '#EE0000' }
          },
          pieSliceText: 'value'
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart'));

        function selectHandler() {
          var selectedItem = chart.getSelection()[0];
          if (selectedItem) {
            var topping = data.getValue(selectedItem.row, 0);
            alert('The user selected ' + topping);
          }
        }

        google.visualization.events.addListener(chart, 'select', selectHandler);

        chart.draw(data, options);
      }
    </script>
  </head>

<style>
    <#include "static/employee.css">
</style>

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
                    <div class="align">
                    <button onclick="location.href='/logout' " class="logout"> Déconnexion </button>
                    <button onclick="location.href='${back}'" class="vote"> Retour </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<hr>
<div class="col_lg_2">
        <div class="row-fluid">
            <div class="col-lg-12">
                <div class="alignleft">
                    <#list nameteam>
                        <table>
                            <div class="dropdown">
                                <span>Team</span>
                                <div>
                                    <#items as nameteam>
                                        <p><button onclick="location.href='./${nameteam?counter-1}'" class="dropdown-content"> ${nameteam} </button></p>
                                    </#items>
                            </div></div>
                        </table>
                    </#list>
                </div>
            </div>
        </div>
    </div>
<#if mood != 0>
<div class="welcome"> ${title}
    <div class="piechart" id="piechart" style="width: 700px; height: 400px;"></div>
</div>
<#else>
<div class="welcome"> Pas de résultats disponibles...
    <div style="margin-top:20px"><img src="http://www.tagtele.com/img/videos/thumbs640x360/b/f/b/55318_default.jpg" alt="canard"/></div>
</div>
</#if>
<div class="container-fluid">
    <div class="col-lg-2">
        <div class="row-fluid">
            <div class="col-lg-12">
                <div class="alignleft">
                <button onclick="location.href='../showGraph'" class="myresults"> My Results </button>
                <#if role != "employee">
                    <button onclick="location.href='../showGraphAll'" class="allresults"> ALL Results </button>
                </#if>
                <button onclick="location.href='../showGraphVerticale'" class="resultsverticale"> Results Verticale </button>
                </div>
            </div>
        </div>
    </div>

</div>
<!-- FOOTER -->
<div class="container-fluid">
	<div class="row-fluid">
		<div class="col-lg-12">
			<div class="copyright">&copy; Niko-Niko CGI 2017</div>
		</div>
	</div>
</div>
</html>

