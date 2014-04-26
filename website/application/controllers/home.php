<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Home extends MY_Controller {

	 public function __construct()
	 {
		 parent::__construct();

		 $this->load->model('Model_api');
	 }

	public function index()
	{
		$campaings = $this->Model_api->get_campaigns();

		$data = array(
			'people' => array(),
			'organization' => array(),
			'other' => array(),
			'special' => array()
		);

		foreach($campaings as $campaign)
		{
			$data[$campaign['type']][] = $campaign;
		}

		$this->load->view('home', $data);
	}
}
