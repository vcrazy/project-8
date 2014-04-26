<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Home extends MY_Controller {

	 public function __construct()
	 {
		 parent::__construct();

		 $this->load->model('Model_api');
		 $this->load->library('Dmstxtreader');
	 }

	public function index()
	{
		$current_campaigns = array();
		$campaigns = $this->Model_api->get_campaigns();

		$data = array(
			'people' => array(),
			'organization' => array(),
			'other' => array(),
			'special' => array()
		);

		$data['campaign_keys'] = array();

		foreach($campaigns as $campaign)
		{
			$campaign_sms = $campaign['sms_text'];
			$campaign_sms_text = explode(' ', $campaign_sms);
			$campaign_sms_text_special = array_pop($campaign_sms_text);

			if(!empty($campaign_sms_text) && $campaign_sms_text[0] === 'DMS')
			{
				$current_campaigns[$campaign_sms_text_special] = true;
				$data['campaign_keys'][] = $campaign_sms_text_special;
			}

			$data[$campaign['type']][] = $campaign;
		}

		$chartdata = new Dmstxtreader();
		$data['chartdata'] = $chartdata->get();
		$new_chartdata = array();

		foreach($data['chartdata'] as $k => &$chartdata)
		{
			$new_chartdata[$k] = array();

			foreach($chartdata as $ck => &$cv)
			{
				$new_chartdata[$k][$cv[0]] = $cv[1];
			}
		}

		$data['chartdata'] = $new_chartdata;

		foreach($data['chartdata'] as $date => $chartdata)
		{
			foreach($chartdata as $ck => $cv)
			{
				if(!isset($current_campaigns[$ck]))
				{
					unset($data['chartdata'][$date][$ck]);
				}
			}
		}

		foreach($data['campaign_keys'] as $k)
		{
			foreach($data['chartdata'] as $date => &$chartdata)
			{
				if(!isset($chartdata[$k]))
				{
					$chartdata[$k] = 0;
				}
			}
		}

		$data['months'] = array('Януари', 'Февруари', 'Март', 'Април', 'Май', 'Юни', 'Юли', 'Август', 'Септември', 'Октомври', 'Ноември', 'Декември');

		$this->load->view('home', $data);
	}
}
