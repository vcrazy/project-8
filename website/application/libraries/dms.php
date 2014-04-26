<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Dms
{
	public $base_url = 'http://www.dmsbg.com/';
	public $campaigns = array(
		'people' => 'index.php?page=4&spage=3',
		'organizations' => 'index.php?page=4&spage=4',
		'other' => 'index.php?page=4&spage=5'
	);

	protected $data = array();

	public function __construct()
	{
		require_once 'phpQuery.php';

		foreach(array_keys($this->campaigns) as $campaign_key)
		{
			$this->$campaign_key();
		}
	}

	public function __call($name, $arguments)
	{
		if(!in_array($name, array_keys($this->campaigns)))
		{
			return;
		}

		$url = $this->base_url . $this->campaigns[$name];

		$url_content = file_get_contents($url);

		phpQuery::newDocumentHTML($url_content);

		$this->get_info();
	}

	public function get()
	{
		return $this->data;
	}

	protected function get_info()
	{
		$all_people_links = array();

		foreach(pq('a.ca_list') as $a)
		{
			$href = '';

			foreach($a->attributes as $att_k => $att_v)
			{
				if($att_k === 'href')
				{
					$href = $att_v->value;
					break;
					die;
				}
			}

			if($href)
			{
				$all_people_links[] = $href;
			}
		}

		$all_people_pages = array();

		foreach($all_people_links as $people_link)
		{
			$people_link_content = file_get_contents($this->base_url . $people_link);

			phpQuery::newDocumentHTML($people_link_content);

			foreach(pq('.container > h1') as $k => $c)
			{
				$name = trim($c->nodeValue);
			}

			// ... get all info
		}

//		$this->data = array_merge($this->data, $new_data);
	}
}
