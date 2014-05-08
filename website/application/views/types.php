<div class="contentheader">
	<span class="menutext">
		Най-нови кампании
	</span>
	<hr />
</div>
<div class="menucontent">
	<?php $types_data = array_splice($data, 0, 5); ?>
	<?php include 'types_cell.php'; ?>
</div>

<?php if(!empty($data)): ?>
<div class="contentheader">
	<span class="menutext">
		Всички кампании
	</span>
	<hr />
</div>
<div class="menucontent">
	<?php $types_data = $data; ?>
	<?php include 'types_cell.php'; ?>
</div>
<?php endif; ?>
