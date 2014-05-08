<?php foreach($types_data as $k => $data_row): ?>
	<div class="column1 <?php echo $k % 5 === 4 ? 'no-margin' : ''; ?>">
		<div class="imgholder">
			<a href="<?php echo $type; ?>/<?php echo $data_row['id']; ?>" class="detailed_info">
				<img src="<?php echo $data_row['picture']; ?>" alt="" />
			</a>
		</div>
		<div class="infoholder">
			<a href="<?php echo $type; ?>/<?php echo $data_row['id']; ?>" class="detailed_info">
				<p class="name">
					<?php echo $data_row['name']; ?>
				</p>
				<p class="subname">
					<?php echo $data_row['subname']; ?>
				</p>
				<?php if($data_row['date_from']): ?>
				<p class="date_from">
					<?php echo date("d.m.Y", $data_row['date_from']) ?>
				</p>
				<?php endif; ?>
			</a>
		</div>
	</div>
	<?php if($k % 5 === 4): ?>
		<div class="clearfix spacer"></div>
	<?php endif; ?>
<?php endforeach; ?>
