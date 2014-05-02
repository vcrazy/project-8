<?php

if(!defined('BASEPATH'))
	exit('No direct script access allowed');

class Model_track extends MY_Model
{
	public function insert($phone_id, $campaign_id)
	{
		return $this->db->insert('actions', array(
			'phone_id' => $phone_id,
			'campaign_id' => $campaign_id
		));
	}
}
