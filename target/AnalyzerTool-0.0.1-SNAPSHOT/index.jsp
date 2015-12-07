<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="css/demo.min.css">
<link rel="stylesheet" type="text/css" href="css/dropdownmenu.css">
<link href="css/bootstrap.css" rel="stylesheet">
<link href="css/nv.d3.css" rel="stylesheet">

<script src="js/d3.v3.js"></script>
<script src="js/nv.d3.js"></script>
<script src="js/stream_layers.js"></script>
<script src="js/index.js"></script>

<script type="text/javascript" language="javascript"
	src="js/jquery.min.js"></script>
	
		<script>
	
			function getData() {
				var categoryFavorites = [];
				var categoryRetweets = [];

				$.ajax({
					async : false,
					url : "DataGetter",
					type : "post",
					success : function(data) {
						var parsedData = JSON.parse(data);
						for (var i = 1; i <= parsedData[0]; ++i) {
							categoryFavorites.push({
								"label" : parsedData[i][0],
								"value" : parsedData[i][1]
							});
							categoryRetweets.push({
								"label" : parsedData[i][0],
								"value" : parsedData[i][2]
							});
						}
					},
					error : function(data) {
						window.location.href = "error.jsp";
					}
				}); //end of ajax

				return [ categoryFavorites, categoryRetweets ];
			}

			var dataTweets = getData();

			function addGraph(data_, name) {
				//Donut chart example
				nv.addGraph(function() 
				{
					var chart = nv.models.pieChart().x(function(d) 
					{
						return d.label
					}).y(function(d) {
						return d.value
					}).showLabels(true) //Display pie labels
					.labelThreshold(.05) //Configure the minimum slice size for labels to show up
					.labelType("percent") //Configure what type of data to show in the label. Can be "key", "value" or "percent"
					.donut(true) //Turn on Donut mode. Makes pie chart look tasty!
					.donutRatio(0.35) //Configure how big you want the donut hole size to be.
					;

					d3.select(name).datum(data_).transition()
							.duration(350).call(chart);

					return chart;
				});
			}
		
	</script>
</head>
<body>
	<div id='cssmenu'
		style="position: fixed; width: 100%; font-weight: 300">
		<ul>
			<li id="home"><a href='#'><span>Home</span></a></li>
			<li class='has-sub'><a href='#'><span>Tools</span></a>
				<ul>
					<li id="log" class='has-sub'><a href='#'><span>Log</span></a>
					</li>
				</ul></li>
		</ul>
	</div>

	<img id="logo" src="img/logo.png" style="margin: 7% 0 4% 5%">
	<br>

	<div id='chart2' class='span4' style="float: left; margin:0 15% 0 0">
		<h3 style="color:#1c1f3e;margin:0 0 5% 50%">Favorites Count</h3>
		<svg style='height:900px;width:800px'></svg>
	</div>
	<div id='chart3' class='span4' style="margin:0 0 0 10%">
		<h3 style="color:#1c1f3e;margin:0 0 5% 50%">Retweet Count</h3>
		<svg style='height:900px;width:800px'></svg>
	</div>
	<script>
		addGraph(dataTweets[0], "#chart2 svg");  
		addGraph(dataTweets[1], "#chart3 svg");  
	</script>

</body>
</html>





