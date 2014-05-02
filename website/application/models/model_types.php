<?php

if(!defined('BASEPATH'))
	exit('No direct script access allowed');

class Model_types extends MY_Model
{
	protected $type = '';
	protected $allowed_types = array('people', 'organizations', 'special', 'other');

	public function __construct()
	{
		parent::__construct();

		$this->type = $this->router->fetch_class();

		if(!in_array($this->type, $this->allowed_types))
		{
			throw new Exception($this->type . ' not found.');
		}
	}

	public function get($id = FALSE)
	{
		if($id)
		{
			$this->db->select('*');
			$this->db->where('id', $id);
		}
		else
		{
			$this->db->select('id, subname, picture');
		}

		$this->db->from('campaigns');
		$this->db->where('type', $this->type);
		$this->db->order_by('date_from', 'desc');
		$query = $this->db->get();

		return $this->results($query);
	}
}
