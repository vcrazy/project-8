<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<script type="text/javascript" src="jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="jquery.slidertron-1.3.js"></script>
<script src="highcharts.js"></script>
<script src="jquery.highchartTable-min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('table.highchart').highchartTable();

		$('.menuheader').click(function(){
			$('.menucontent').hide();
			$('#' + $(this).data('show')).show();
		});
	});
</script>
<link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600,700,900" rel="stylesheet" />
<link href="reset.css" rel="stylesheet" type="text/css" media="all" />
<link href="default.css" rel="stylesheet" type="text/css" media="all" />
<link href="fonts.css" rel="stylesheet" type="text/css" media="all" />

<!--[if IE 6]><link href="default_ie6.css" rel="stylesheet" type="text/css" /><![endif]-->

</head>
<body>
<div id="header-wrapper">
	<div id="topline">&nbsp;</div>
	<div id="header" class="container">
		<div id="logo">
			<a href="#">
				<img src="images/Logo.png" />
			</a>
		</div>
		<div id="menu">
			<ul>
				<li class="current_page_item"><a href="#" accesskey="1" title="" class="menuheader" data-show="people">Хора</a></li>
				<li><a href="#" accesskey="2" title="" class="menuheader" data-show="organizations">Организации</a></li>
				<li><a href="#" accesskey="3" title="" class="menuheader" data-show="special">Специални</a></li>
				<li><a href="#" accesskey="4" title="" class="menuheader" data-show="other">Други</a></li>
				<li><a href="#" accesskey="5" title="" class="menuheader" data-show="statistics">Статистики</a></li>
			</ul>
		</div>
		<div id="menu_btn">
			<img src="images/google.png" />
			<a href="#"><img src="images/facebook.png" /></a>
			<img src="images/twitter.png" />
		</div>
	</div>
</div>
<div id="bline">&nbsp;</div>
<div id="header-featured">
	<div class="wide">
		<div id="p_text">
			Дарения чрез <span id="blue">SMS</span> в подкрепа на кампании за хора, каузи и инициативи на организации и на институции.
		</div>
		<div class="clearfix"></div>
	</div>
 </div>


<div id="dline">&nbsp;</div>

<div id="featured-wrapper">
	<div id="featured" class="container">

		<?php foreach(array('people' => $people, 'organizations' => $organizations, 'other' => $other, 'special' => $special) as $ck => $data): ?>
		<div id="<?php echo $ck; ?>" class="menucontent">
		<?php foreach($data as $dk => $dv): ?>
		<div class="column1">
			<div class="imgholder">
				<img src="data:image/png;base64,<?php echo $dv['picture']; ?>" alt="" />
			</div>
			<div>
				<p><?php echo $dv['subname']; ?></p>
			</div>
		</div>
		<?php endforeach; ?>
		</div>
		<?php endforeach; ?>

		<div id="statistics" class="menucontent">
			<table class="highchart" data-graph-container-before="1" data-graph-type="line" style="display: none;" data-graph-xaxis-end-on-tick="1">
				<thead>
					<tr>
						<th></th>
						<?php foreach($campaign_keys as $chartdata_key): ?>
							<th><?php echo $chartdata_key; ?></th>
						<?php endforeach; ?>
					</tr>
				</thead>
				<tbody>
					<?php foreach($chartdata as $date => $data): ?>
						<tr>
							<td>
								<?php
									$whole_date = strtotime($date . '-01');
									$month = $months[date("n", $whole_date) - 1];
									$year = date("Y", $whole_date);
									echo $month . ' ' . $year;
								?>
							</td>
							<?php foreach($campaign_keys as $chartdata_key): ?>
							<td>
								<?php echo $data[$chartdata_key]; ?>
							</td>
							<?php endforeach; ?>
						</tr>
					<?php endforeach; ?>
				</tbody>
			</table>
		</div>
	</div>
</div>

<footer>
	<div id="footer">
		<div id="menu_footer">
			<ul>
				<li class="current_page_item"><a href="#" accesskey="1" title="">Хора</a></li>
				<li><a href="#" accesskey="2" title="">Организации</a></li>
				<li><a href="#" accesskey="3" title="">Специални</a></li>
				<li><a href="#" accesskey="4" title="">Други</a></li>
				<li><a href="#" accesskey="4" title="">Статистики</a></li>
			</ul>
		</div>
	</div>
</footer>

</body>
</html>