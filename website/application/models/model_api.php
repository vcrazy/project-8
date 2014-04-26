<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Model_api extends MY_Model
{
	public function get_campaigns()
	{
		$this->db->from('campaigns');
		$this->db->order_by('date_from', 'desc');
		$query = $this->db->get();

		return $this->results($query);
	}

	public function get_ratings()
	{
		$this->db->select('(SELECT COUNT(id) FROM actions WHERE phone_id = p.phone_id) AS actions_count, p.phone_id');
		$this->db->from('actions AS p');
		$this->db->group_by('p.phone_id');
		$this->db->order_by('actions_count', 'desc');
		$query = $this->db->get();

		return $this->results($query);
	}
}
