<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Model_worker extends MY_Model
{
	public function insert($data)
	{
		$result = TRUE;

		foreach($data as $data_part)
		{
			$result = $this->db->insert('campaigns', $data_part) && $result;
			sleep(1);
		}

		return $result;
	}

	public function update($data)
	{
		$result = TRUE;

		foreach($data as $data_part)
		{
			$this->db->where('subname', $data_part['subname']);
			$result = $this->db->update('campaigns', $data_part) && $result;
			sleep(1);
		}

		return $result;
	}

	public function delete($data)
	{
		$result = TRUE;

		if(!empty($data))
		{
			$this->db->where_in('subname', $data);
			$result = $this->db->delete('campaigns');
			sleep(1);
		}

		return $result;
	}
}
