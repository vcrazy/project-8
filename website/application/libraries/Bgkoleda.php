<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Bgkoleda
{
	protected $data = array();

	public function __construct()
	{
		require_once 'phpQuery.php';

		$this->get_info();
	}

	public function get()
	{
		return $this->data;
	}

	protected function get_info()
	{
		$data = array(); // ...

		$this->data = $data;
	}
}
