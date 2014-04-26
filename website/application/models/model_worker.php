<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Model_worker extends MY_Model
{
	public function save($data)
	{
		$result = TRUE;

		foreach($data as $data_part)
		{
			$result = $this->db->insert('campaigns', $data_part) && $result;
			sleep(1);
		}

		return $result;
	}
}
