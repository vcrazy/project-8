<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Api extends MY_Controller {

	 public function __construct()
	 {
		 parent::__construct();

		 $this->load->model('Model_api');
	 }

	public function index()
	{
		$campaigns = $this->Model_api->get_campaigns();
		$rating = $this->Model_api->get_ratings();

		$data = array('campaigns' => $campaigns, 'ratings' => $rating);

		echo json_encode($data);
	}
}
