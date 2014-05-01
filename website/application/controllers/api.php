<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Api extends MY_Controller {

	public function __construct()
	{
		parent::__construct();

		$this->load->model('Model_campaigns');
	}

	public function index()
	{
		$this->load->model('Model_ratings');

		$user_version = (int)$this->input->get('version');
		$current_version = (int)$this->Model_campaigns->get_version();

		$campaigns_diff = $this->Model_campaigns->get_diff($user_version, $current_version);
		$rating = $this->Model_ratings->get();

		$data = array('campaigns' => $campaigns_diff, 'ratings' => $rating, 'version' => $current_version);

		echo json_encode($data);
	}

	public function version()
	{
		$version = (int)$this->Model_campaigns->get_version();

		$data = array(
			'version' => $version
		);

		echo json_encode($data);
	}
}
