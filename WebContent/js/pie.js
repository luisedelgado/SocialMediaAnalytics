var dataTweets = getData();

//Donut chart example
nv.addGraph(function() {
	var chart = nv.models.pieChart()
	.x(function(d) { return d.label })
	.y(function(d) { return d.value })
	.showLabels(true)     //Display pie labels
	.labelThreshold(.05)  //Configure the minimum slice size for labels to show up
	.labelType("percent") //Configure what type of data to show in the label. Can be "key", "value" or "percent"
	.donut(true)          //Turn on Donut mode. Makes pie chart look tasty!
	.donutRatio(0.35)     //Configure how big you want the donut hole size to be.
	;

	d3.select("#chart2 svg")
	.datum(dataTweets[0])
	.transition().duration(350)
	.call(chart);

	return chart;
});

//Donut chart example
nv.addGraph(function() {
	var chart = nv.models.pieChart()
	.x(function(d) { return d.label })
	.y(function(d) { return d.value })
	.showLabels(true)     //Display pie labels
	.labelThreshold(.05)  //Configure the minimum slice size for labels to show up
	.labelType("percent") //Configure what type of data to show in the label. Can be "key", "value" or "percent"
	.donut(true)          //Turn on Donut mode. Makes pie chart look tasty!
	.donutRatio(0.35)     //Configure how big you want the donut hole size to be.
	;

	d3.select("#chart3 svg")
	.datum(dataTweets[1])
	.transition().duration(350)
	.call(chart);

	return chart;
});

//Pie chart example data. Note how there is only a single array of key-value pairs.
function getData() 
{
	var categoryFavorites = [];
	var categoryRetweets = [];
	
	$.ajax({
		async : false,
		url : "DataGetter",
		type : "post",
		success : function(data)
		{
			var parsedData = JSON.parse(data);
			for(var i = 1; i <= parsedData[0]; ++i)
			{
				categoryFavorites.push({
					"label" : parsedData[i][0],
					"value" : parsedData[i][1]
				});
			}	
		},
		error : function(data)
		{
            window.location.href = "error.jsp";
		}
	}); //end of ajax

	return [categoryFavorites,categoryRetweets];
}

