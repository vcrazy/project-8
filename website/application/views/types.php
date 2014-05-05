<div class="contentheader">
	<span class="menutext">
		<?php // echo $title; ?>
		Най-нови кампании
	</span>
	<hr />
</div>
<div class="menucontent">
	<?php foreach(array_splice($data, 0, 5) as $k => $data_row): ?>
		<div class="column1 <?php echo $k % 5 === 4 ? 'no-margin' : ''; ?>">
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
	<div class="clearfix"></div>
</div>

<?php if(!empty($data)): ?>
<div class="contentheader">
	<span class="menutext">
		<?php // echo $title; ?>
		Всички кампании
	</span>
	<hr />
</div>
<div class="menucontent">
	<?php foreach($data as $k => $data_row): ?>
		<div class="column1 <?php echo $k % 5 === 4 ? 'no-margin' : ''; ?>">
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
	<div class="clearfix"></div>
</div>
<?php endif; ?>
