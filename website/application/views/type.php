<div class="itemcontent">
	<table>
		<tr>
			<td style="vertical-align: top;">
				<table class="firstcolumn">
					<?php if($data['date_from']): ?>
					<tr>
						<td>
							<div class="cstart">
								<?php
									$whole_date = $data['date_from'];
									$month = $months[date("n", $whole_date) - 1];
									$year = date("Y", $whole_date);
								?>
								Стартирала на <br />
								<?php echo date("j", $data['date_from']) . ' ' . $month . ' ' . $year . ' Г.'; ?>
							</div>
						</td>
					</tr>
					<?php endif; ?>
					<tr>
						<td>
							<div class="cimage">
								<img src="<?php echo $data['picture']; ?>" alt="" />
							</div>
						</td>
					</tr>
					<?php if($data['sms_text']): ?>
					<tr>
						<td>
							<div class="ccode">
								<span>Код на SMS</span> <br />
								<?php echo $data['sms_text']; ?>
							</div>
						</td>
					</tr>
					<?php endif; ?>
					<tr>
						<td>
							<div class="ctxt">
								Номер - <span><?php echo implode(' ', preg_split('/^(\d{2})/', $data['sms_number'], -1, PREG_SPLIT_DELIM_CAPTURE)); ?></span>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="ctxt">
								Стойност - <span><?php echo $data['donation']; ?>лв.</span>
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
								<?php echo $data['name']; ?>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="ctext">
								<?php echo str_replace("\n", '<br /><br />', $data['text']); ?>
								<br /><br />
								<a href="<?php echo $data['link']; ?>" target="_blank" class="clink">Препратка към кампанията</a>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
