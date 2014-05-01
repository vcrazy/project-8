<div class="contentheader">
	<span class="menutext">
		<?php echo $title; ?>
	</span>
	<hr />
</div>
<div class="menucontent">
	<?php foreach($data as $data_row): ?>
		<div class="column1">
			<div class="imgholder">
				<a href="<?php echo $type; ?>/<?php echo $data_row['id']; ?>" class="detailed_info">
					<img src="<?php echo $data_row['picture']; ?>" alt="" />
				</a>
			</div>
			<div>
				<a href="<?php echo $type; ?>/<?php echo $data_row['id']; ?>" class="detailed_info">
					<p>
						<?php echo $data_row['subname']; ?>
					</p>
				</a>
			</div>
		</div>
	<?php endforeach; ?>
</div>
