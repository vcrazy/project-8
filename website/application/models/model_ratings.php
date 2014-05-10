<?php

if(!defined('BASEPATH'))
	exit('No direct script access allowed');

class Model_ratings extends MY_Model
{
	public function get()
	{
		$this->db->select('(SELECT COUNT(id) FROM actions WHERE phone_id = p.phone_id) AS actions_count, p.phone_id');
		$this->db->from('actions AS p');
		$this->db->group_by('p.phone_id');
		$this->db->order_by('actions_count', 'desc');
		$query = $this->db->get();

		$result = $this->results($query);

		if($result === FALSE)
		{
			$result = array();
		}

		return $result;
	}
}
