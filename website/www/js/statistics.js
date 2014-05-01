$(document).ready(function() {
	$('#statistics').highcharts({
		title: {
			text: 'Брой изпратени SMS-и на цена 1 лв.'
		},
		xAxis: {
			categories: categories
		},
		yAxis: {
			title: {
				text: 'Брой'
			},
			plotLines: [{
				value: 0,
				width: 1
			}],
			min: 0,
			max: max
		},
		legend: {
			align: 'bottom',
			borderWidth: 0
		},
		series: series
	});
});
