<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Model_campaigns extends MY_Model
{
	public function get_subnames($types = array())
	{
		if(empty($types))
		{
			return FALSE;
		}

		$this->db->select('subname');
		$this->db->from('campaigns');
		$this->db->where_in('type', $types);
		$this->db->order_by('subname', 'asc');
		$query = $this->db->get();

		return $this->to_array_single($query, 'subname');
	}
}
