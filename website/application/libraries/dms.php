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

		$this->get_info($name);
	}

	public function get()
	{
		return $this->data;
	}

	protected function get_info($campaign_type)
	{
		$links = $this->get_links();

		$data = $this->get_data($links, $campaign_type);

		$this->data = array_merge($this->data, $data);
	}

	protected function get_links()
	{
		$all_links = array();

		foreach(pq('a.ca_list') as $a)
		{
			$href = '';

			foreach($a->attributes as $att_k => $att_v)
			{
				if($att_k === 'href')
				{
					$href = $att_v->value;
					break;
				}
			}

			if($href)
			{
				$all_links[] = $href;
			}
		}

		return $all_links;
	}

	protected function get_data($links, $campaign_type)
	{
		$data = array();

		foreach($links as $link)
		{
			$link_content = file_get_contents($this->base_url . $link);

			phpQuery::newDocumentHTML($link_content);

			foreach(pq('.container > h1') as $c)
			{
				$campaign_name = trim($c->nodeValue);
				break;
			}

			$strong_data = array();
			foreach(pq('.nd_text p:first strong') as $c)
			{
				$strong_data[] = trim($c->nodeValue);
			}
			$sms_number = array_pop($strong_data);
			$sms_number = (int)preg_replace('/\D/', '', $sms_number);
			$sms_text = implode(' ', $strong_data);

			foreach(pq('.nd_date:last') as $c)
			{
				$date_start = $c->nodeValue;
				$date_start = str_replace(array('Януари', 'Февруари', 'Март', 'Април', 'Май', 'Юни', 'Юли', 'Август', 'Септември', 'Октомври', 'Ноември', 'Декември', ' г.'), array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', ''), $date_start);
				$date_start = strtotime($date_start);
				break;
			}

			$paragraphs = array();
			foreach(pq('.nd_text p') as $c)
			{
				$paragraphs[] = trim($c->nodeValue);
			}
			$paragraphs = array_filter($paragraphs, function($paragraph)
			{
				$regex = '/да намерите тук\.$/';
				return !preg_match($regex, $paragraph);
			});

			$donation = 1; // 1 lv

			foreach(pq('.nd_img img') as $c)
			{
				$picture = '';

				foreach($c->attributes as $att_k => $att_v)
				{
					if($att_k === 'src')
					{
						$picture = $att_v->value;
						$picture_base64 = base64_encode(file_get_contents($picture));
						break;
					}
				}
			}

			$campaign_link = $this->base_url . $link;

			$data[] = array(
				'name' => $campaign_name,
				'subname' => $sms_text,
				'type' => $campaign_type,
				'text' => implode("\n", $paragraphs),
				'donation' => $donation,
				'picture' => $picture_base64,
				'link' => $campaign_link,
				'sms_text' => $sms_text,
				'sms_number' => $sms_number,
				'date_from' => $date_start
			);
		}

		return $data;
	}
}
