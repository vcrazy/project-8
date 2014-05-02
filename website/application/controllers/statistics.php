<?php

if(!defined('BASEPATH'))
	exit('No direct script access allowed');

class Statistics extends MY_Controller
{
	public function index()
	{
		$this->load->library('Dmstxtreader');

		$data_reader = new Dmstxtreader();
		$data = $data_reader->get();

		// $data: key = date; value = [[dms name, dms value], [..], ..]

		$new_data = array();
		foreach($data as $date => $date_value)
		{
			if(!isset($new_data[$date]))
			{
				$new_data[$date] = array();
			}

			foreach($date_value as $data_element)
			{
				$new_data[$date][$data_element[0]] = $data_element[1];
			}
		}
		unset($data);

		// $new_data: key = date; value = [key = dms name, value = dms value, ..]

		$month_data = array();
		$months = array('Януари', 'Февруари', 'Март', 'Април', 'Май', 'Юни', 'Юли', 'Август', 'Септември', 'Октомври', 'Ноември', 'Декември');
		foreach(array_keys($new_data) as $date)
		{
			$whole_date = strtotime($date . '-01');
			$month = $months[date("n", $whole_date) - 1];
			$year = date("Y", $whole_date);

			$month_data[] = $month . ' ' . $year;
		}

		$this->load->model('Model_campaigns');
		$campaign_subnames = $this->Model_campaigns->get_subnames(array('people', 'organizations', 'other'));

		$data_formatted = array();
		$visibles = array();
		$visible_max = 0;

		foreach($campaign_subnames as $subname)
		{
			$subname = preg_replace('/^DMS\s*/', '', $subname);

			$data_formatted[$subname] = array();

			foreach($new_data as $date => $date_value)
			{
				$value = 0;
				if(isset($date_value[$subname]))
				{
					$value = $date_value[$subname];
				}

				$data_formatted[$subname][] = $value;

				if(!isset($visibles[$subname]))
				{
					$visibles[$subname] = 0;
				}

				$visibles[$subname] += $value;
				$visible_max = max($visible_max, $value);
			}
		}

		$visible_sums = array();

		foreach($data_formatted as $subname => $subname_data)
		{
			$visible_sums[$subname] = array_sum($subname_data);
		}

		$visible_min = $visible_sums;
		rsort($visible_min);

		$visible_min = isset($visible_min[4]) ? $visible_min[4] : $visible_min[count($visible_sums) - 1];

		$view_data = array(
			'data' => $data_formatted,
			'min_show' => $visible_min,
			'max_show' => $visible_max,
			'visibles' => $visible_sums,
			'month_data' => $month_data,
			'type' => 'statistics',
			'view' => 'statistics'
		);

		$this->load->view('main', $view_data);
	}
}
