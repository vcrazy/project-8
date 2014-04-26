<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Worker extends MY_Controller {

	public function __construct()
	{
		parent::__construct();

		set_time_limit(0);

		$this->load->model('Model_worker');
	}

	public function index()
	{
		$dms_data = $this->worker_dms();
		$unicef_data = $this->worker_unicef();
		$redcross_data = $this->worker_redcross();
		$bgkoleda_data = $this->worker_bgkoleda();

		$data = array_merge($dms_data, $unicef_data, $redcross_data, $bgkoleda_data);

		$result = $this->Model_worker->save($data);

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
