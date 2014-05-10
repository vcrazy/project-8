<?php

if(!defined('BASEPATH'))
	exit('No direct script access allowed');

class Track extends MY_Controller
{

	public function __construct()
	{
		parent::__construct();

		$this->load->model('Model_track');
	}

	public function index()
	{
		if(isset($_GET['phone_id']) && isset($_GET['campaign_id']))
		{
			$result = $this->Model_track->insert($_GET['phone_id'], (int) $_GET['campaign_id']);

			echo json_encode(array('ok' => (int) $result));
		}
		else
		{
			echo json_encode(array('ok' => 0));
		}
	}

}
