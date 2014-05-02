<?php

if(!defined('BASEPATH'))
	exit('No direct script access allowed');

class Worker extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		set_time_limit(15 * 60);

		$this->load->model('Model_campaigns');
	}

	public function index()
	{
		$start = microtime(TRUE);

		$dms_data = $this->worker_dms();
		$unicef_data = $this->worker_unicef();
		$redcross_data = $this->worker_redcross();
		$bgkoleda_data = $this->worker_bgkoleda();

		$new_data = array_merge($dms_data, $unicef_data, $redcross_data, $bgkoleda_data);
		$old_data = $this->Model_campaigns->get();

		$new_data_transformed = array();
		foreach($new_data as $e)
		{
			$new_data_transformed[$e['subname']] = $e;
		}

		$old_data_transformed = array();
		foreach($old_data as $e)
		{
			$old_data_transformed[$e['subname']] = $e;
		}

		$data_to_insert = array();
		$data_to_update = array();
		$data_to_delete = array();

		foreach($new_data_transformed as $subname => $new_data_element)
		{
			if(!isset($old_data_transformed[$subname]))
			{
				$data_to_insert[] = $new_data_element;
			}
			else
			{
				$changes = array();

				foreach($old_data_transformed[$subname] as $k => $old_value)
				{
					if($k === 'id')
					{
						continue;
					}

					$new_value = $new_data_element[$k];

					if($old_value != $new_value)
					{
						$changes[$k] = $new_value;
					}
				}

				if(!empty($changes))
				{
					$id = $old_data_transformed[$subname]['id'];
					$data_to_update[$id] = $changes;
				}
			}
		}

		foreach(array_keys($old_data_transformed) as $subname)
		{
			if(!isset($new_data_transformed[$subname]))
			{
				$data_to_delete[] = $old_data_transformed[$subname]['id'];
			}
		}

		$insert_result = $this->Model_campaigns->insert($data_to_insert);
		$update_result = $this->Model_campaigns->update($data_to_update);
		$delete_result = $this->Model_campaigns->delete($data_to_delete);

		$result = $insert_result && $update_result && $delete_result;

		$end = microtime(TRUE);

		$this->load->library('email');

		$this->email->from('grisho@smshelp.ganev.bg', 'Grisho');
		$this->email->to('vihren.k.ganev@gmail.com');

		$this->email->subject('Morning SMShelp report');
		$this->email->message('Inserted: ' . count($data_to_insert) . '; Updated: ' . count($data_to_update) . '; Deleted: ' . count($data_to_delete) . '; Result: ' . (int) $result . '; Time: ' . ($end - $start) . 's.');

		$this->email->send();

		echo ':)';
	}

	public function worker_dms()
	{
		$this->load->library('dms');

		return $this->dms->get();
	}

	public function worker_unicef()
	{
		$this->load->library('unicef');

		return $this->unicef->get();
	}

	public function worker_redcross()
	{
		$this->load->library('redcross');

		return $this->redcross->get();
	}

	public function worker_bgkoleda()
	{
		$this->load->library('bgkoleda');

		return $this->bgkoleda->get();
	}
}
