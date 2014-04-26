<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Home extends MY_Controller {

	 public function __construct()
	 {
		 parent::__construct();

		 $this->load->model('Model_api');
	 }

	public function index()
	{
		$data = array(
			'campaigns' => $this->Model_api->get_campaigns()
		);

		$this->load->view('home', $data);
	}
}
