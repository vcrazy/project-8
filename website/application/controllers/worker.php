<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Worker extends MY_Controller {

	public function __construct()
	{
		parent::__construct();

		set_time_limit(0);

		$this->load->model('Model_worker');
		$this->load->model('Model_api');
	}

	public function index()
	{
		$dms_data = $this->worker_dms();
		$unicef_data = $this->worker_unicef();
		$redcross_data = $this->worker_redcross();
		$bgkoleda_data = $this->worker_bgkoleda();

		$new_data = array_merge($dms_data, $unicef_data, $redcross_data, $bgkoleda_data);
		$old_data = $this->Model_api->get_campaigns();

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
				$data_to_update[] = $new_data_element;
			}
		}

		foreach(array_keys($old_data_transformed) as $subname)
		{
			if(!isset($new_data_transformed[$subname]))
			{
				$data_to_delete[] = $subname;
			}
		}

		$insert_result = $this->Model_worker->insert($data_to_insert);
		$update_result = $this->Model_worker->update($data_to_update);
		$delete_result = $this->Model_worker->delete($data_to_delete);

		$result = $insert_result && $update_result && $delete_result;

		echo 'Result: ' . (int)$result;
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
