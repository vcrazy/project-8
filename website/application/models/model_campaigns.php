<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Model_campaigns extends MY_Model
{
	public function get()
	{
		$this->db->from('campaigns');
		$this->db->order_by('date_from', 'desc');
		$query = $this->db->get();

		$results = $this->results($query);

		if($results === FALSE)
		{
			$results = array();
		}

		return $results;
	}

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

	public function get_version()
	{
		$this->db->select('MAX(version) AS version');
		$this->db->from('campaigns_log');
		$query = $this->db->get();

		$result = $this->single($query, 'version');

		return $result ?: 0;
	}

	public function get_diff($from_version, $to_version)
	{
		$this->db->select('*');
		$this->db->from('campaigns_log');
		$this->db->where('version >', $from_version);
		$this->db->where('version <=', $to_version);
		$this->db->order_by('version', 'desc');

		$query = $this->db->get();

		$result = $this->results($query);
		
		if($result === FALSE)
		{
			$result = array();
		}

		$diff = array();

		foreach($result as $change)
		{
			$id = $change['campaign_id'];

			unset($change['id']);
			unset($change['campaign_id']);
			unset($change['version']);
			unset($change['date_change']);

			if(!isset($diff[$id]))
			{
				foreach($change as $change_key => $change_value)
				{
					if($change_value !== NULL)
					{
						$diff[$id][$change_key] = $change_value;
					}
				}
			}

			if($diff[$id] === 'deleted')
			{
				continue;
			}

			foreach($change as $change_key => $change_value)
			{
				if($change_value !== NULL && !isset($diff[$id][$change_key]))
				{
					$diff[$id][$change_key] = $change_value;
				}
			}
		}

		return $diff;
	}

	public function insert($data)
	{
		$result = TRUE;

		foreach($data as $data_part)
		{
			$result = $this->db->insert('campaigns', $data_part) && $result;
			sleep(1);
		}

		$version = $this->get_version();

		foreach($data as $data_part)
		{
			$data_part['version'] = $version + 1;
			$data_part['campaign_id'] = $data_part['id'];
			$data_part['status'] = 'insert';
			unset($data_part['id']);

			$result = $this->db->insert('campaigns_log', $data_part) && $result;
			sleep(1);
		}

		return $result;
	}

	public function update($data)
	{
		$result = TRUE;

		foreach($data as $id => $data_part)
		{
			$this->db->where('id', $id);
			$result = $this->db->update('campaigns', $data_part) && $result;
			sleep(1);
		}

		$version = $this->get_version();

		foreach($data as $id => $data_part)
		{
			$data_part['version'] = $version + 1;
			$data_part['campaign_id'] = $id;
			$data_part['status'] = 'update';

			$result = $this->db->insert('campaigns_log', $data_part) && $result;
			sleep(1);
		}

		return $result;
	}

	public function delete($data)
	{
		$result = TRUE;

		if(!empty($data))
		{
			$this->db->where_in('id', $data);
			$result = $this->db->delete('campaigns');
			sleep(1);
		}

		$version = $this->get_version();

		foreach($data as $id)
		{
			$data_part = array(
				'version' => $version + 1,
				'campaign_id' => $id,
				'status' => 'delete'
			);

			$result = $this->db->insert('campaigns_log', $data_part) && $result;
			sleep(1);
		}

		return $result;
	}
}
