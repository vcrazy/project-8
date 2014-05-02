<?php

if(!defined('BASEPATH'))
	exit('No direct script access allowed');

abstract class Types extends MY_Controller
{
	protected $type = '';
	protected $allowed_types = array('people', 'organizations', 'special', 'other');
	protected $titles = array('Хора', 'Организации', 'Специални', 'Други');

	public function __construct()
	{
		parent::__construct();

		$this->type = $this->router->fetch_class();

		if(!in_array($this->type, $this->allowed_types))
		{
			throw new Exception($this->type . ' not found.');
		}
	}

	public function index($id = FALSE)
	{
		$this->load->model('Model_types');

		$data = $this->Model_types->get($id);

		if($data === FALSE)
		{
			$data = array();
		}

		$months = array('Януари', 'Февруари', 'Март', 'Април', 'Май', 'Юни', 'Юли', 'Август', 'Септември', 'Октомври', 'Ноември', 'Декември');

		$view_data = array(
			'type' => $this->type,
			'title' => $this->titles[array_search($this->type, $this->allowed_types)],
			'months' => $months,
			'data' => $id ? $data[0] : $data,
			'view' => $id ? 'type' : 'types'
		);

		$this->load->view('main', $view_data);
	}
}
