<div class="contentheader">
	<span class="menutext">
		Статистики
	</span>
	<hr />
</div>
<div class="menucontent">
	<div id="statistics">
		<script type="text/javascript">
			var series = [],
				max = <?php echo $max_show; ?>,
				categories = ["<?php echo implode('","', $month_data); ?>"];

			<?php foreach($data as $subname => $data_value): ?>
				series.push({
					name: '<?php echo $subname; ?>',
					data: [<?php echo implode(',', $data_value); ?>],
					visible: <?php if ($visibles[$subname] >= $min_show): ?> true <?php else: ?> false <?php endif; ?>
				});
			<?php endforeach; ?>
		</script>
		<script type="text/javascript" src="js/statistics.js"></script>
	</div>
</div>
