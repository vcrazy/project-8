<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<script type="text/javascript" src="jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="jquery.slidertron-1.3.js"></script>

<script type="text/javascript" src="highcharts.js"></script>
<script type="text/javascript" src="exporting.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#organizations, #special, #other, #statistics').hide();

		$('.menuheader').click(function(){
			$('.menucontent, .itemcontent').hide();
			$('#' + $(this).data('show')).show();
			$('.column1, .contentheader').show();
			$('.contentheader .menutext').text($(this).text());
		});

		$('.detailed_info').click(function(){
			$('.column1, .itemcontent, .contentheader').hide();
			$('#detailed_info_' + $(this).data('id')).show();
			return false;
		});

		var categories = [];

		<?php foreach(array_keys($chartdata) as $date): ?>
			<?php
				$whole_date = strtotime($date . '-01');
				$month = $months[date("n", $whole_date) - 1];
				$year = date("Y", $whole_date);
			?>
			categories.push('<?php echo $month . ' ' . $year; ?>');
		<?php endforeach; ?>

		var series = [];

		<?php $nums = array(); ?>
		<?php foreach($campaign_keys as $chartdata_key): ?>
			<?php foreach($chartdata as $date => $data): ?>
				<?php
					if(!isset($nums[$chartdata_key])) $nums[$chartdata_key] = 0;

					$nums[$chartdata_key] += $data[$chartdata_key];
				?>
			<?php endforeach; ?>
		<?php endforeach; ?>
		<?php rsort($nums); ?>
		var visible_limit = <?php echo $nums[9] ?: 0; ?>,
			max = 0;

		<?php foreach($campaign_keys as $chartdata_key): ?>
			var d = [],
				sum = 0;
				<?php foreach($chartdata as $date => $data): ?>
					sum += <?php echo $data[$chartdata_key]; ?>;
					max = Math.max(max, <?php echo $data[$chartdata_key]; ?>);
					d.push(<?php echo $data[$chartdata_key]; ?>);
				<?php endforeach; ?>
			series.push({
				name: '<?php echo $chartdata_key; ?>',
				data: d,
				visible: visible_limit ? sum >= visible_limit : false
			});
		<?php endforeach; ?>

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
</script>
<link href="reset.css" rel="stylesheet" type="text/css" media="all" />
<link href="default.css" rel="stylesheet" type="text/css" media="all" />
<link href="fonts.css" rel="stylesheet" type="text/css" media="all" />
<link href="fonts_1.css" rel="stylesheet" type="text/css" media="all" />

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
		<div class="menu_btn">
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

		<div class="contentheader">
			<span class="menutext">
				Хора
			</span>
			<hr />
		</div>
		<?php foreach(array('people' => $people, 'organizations' => $organizations, 'other' => $other, 'special' => $special) as $ck => $data): ?>
		<div id="<?php echo $ck; ?>" class="menucontent">
		<?php foreach($data as $dk => $dv): ?>
		<div class="column1">
			<div class="imgholder">
				<a href="#" class="detailed_info" data-id="<?php echo $dv['id']; ?>">
					<img src="data:image/png;base64,<?php echo $dv['picture']; ?>" alt="" />
				</a>
			</div>
			<div>
				<a href="#" class="detailed_info" data-id="<?php echo $dv['id']; ?>">
					<p><?php echo $dv['subname']; ?></p>
				</a>
			</div>
		</div>
		<div class="itemcontent" id="detailed_info_<?php echo $dv['id']; ?>">
			<table>
				<tr>
					<td style="vertical-align: top;">
						<table>
							<?php if($dv['date_from']): ?>
							<tr>
								<td>
									<div class="cstart">
										<?php
											$whole_date = $dv['date_from'];
											$month = $months[date("n", $whole_date) - 1];
											$year = date("Y", $whole_date);
										?>
										Стартирала на <br />
										<?php echo date("j", $dv['date_from']) . '. ' . $month . ' ' . $year; ?>
									</div>
								</td>
							</tr>
							<?php endif; ?>
							<tr>
								<td>
									<div class="cimage">
										<img src="data:image/png;base64,<?php echo $dv['picture']; ?>" alt="" />
									</div>
								</td>
							</tr>
							<?php if($dv['sms_text']): ?>
							<tr>
								<td>
									<div class="ccode">
										<span>Код на SMS</span> <br />
										<?php echo $dv['sms_text']; ?>
									</div>
								</td>
							</tr>
							<?php endif; ?>
							<tr>
								<td>
									<div class="ctxt">
										<span>Номер -</span> <?php echo $dv['sms_number']; ?>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="ctxt">
										<span>Стойност -</span> <?php echo $dv['donation']; ?> <span class="ctxts" style="text-transform: none;">лв.</span>
									</div>
								</td>
							</tr>
						</table>
					</td>
					<td style="vertical-align: top;">
						<table class="cell">
							<tr>
								<td>
									<div class="cname">
										<?php echo $dv['name']; ?>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="ctext">
										<?php echo str_replace("\n", '<br /><br />', $dv['text']); ?>
										<br /><br />
										<a href="<?php echo $dv['link']; ?>" target="_blank" class="clink">Линк към страницата</a>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<?php endforeach; ?>
		</div>
		<?php endforeach; ?>

		<div id="statistics" class="menucontent">
			
		</div>
	</div>
</div>

<footer>
	<div id="footer">
		<div id="menu_footer">
			<ul>
				<li class="current_page_item"><a href="#" accesskey="1" title="" class="menuheader" data-show="people">Хора</a></li>
				<li><a href="#" accesskey="2" title="" class="menuheader" data-show="organizations">Организации</a></li>
				<li><a href="#" accesskey="3" title="" class="menuheader" data-show="special">Специални</a></li>
				<li><a href="#" accesskey="4" title="" class="menuheader" data-show="other">Други</a></li>
				<li><a href="#" accesskey="5" title="" class="menuheader" data-show="statistics">Статистики</a></li>
			</ul>
			<div class="menu_btn" style="right: 0px;">
				<img src="images/google.png" />
				<a href="#"><img src="images/facebook.png" /></a>
				<img src="images/twitter.png" />
			</div>
		</div>
	</div>
</footer>

</body>
</html>