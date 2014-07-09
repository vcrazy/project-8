<?php

if(!defined('BASEPATH'))
	exit('No direct script access allowed');

class Redcross
{
	private $url = 'http://www.redcross.bg';

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
		$data = array();

		$data[] = array(
			'name' => 'Програма "Топъл обяд"',
			'subname' => 'Топъл обяд',
			'type' => 'special',
			'text' => "Програма \"Топъл обяд\" е създадена през 2004 г. и се реализира в рамките на Партньорска мрежа за благотворителност на БЧК. Основната цел на програмата е да се подпомогне физическото развитие на деца в неравностойно положение и чрез топлия обяд те да се върнат в училище. С различни кампании се набират средства и се финансират проекти \"Топъл обяд\" за ученици от цялата страна.
Много често топлият обяд в трапезариите на БЧК се оказва единственият повод нуждаещите се български деца да посещават учебните занятия. Те получават безплатен обяд всеки ден през учебната година. Целта е да се подпомогне нормалното физическо развитие на децата, както и да се съдейства за намаляване заболяваемостта им, произтичаща от нередовното и непълноценно хранене. Осигуряването на храна на някои особено нуждаещи се ученици чрез включването им в проекта \"Топъл обяд\" е единствената възможност те да посещават редовно учебните занятия.",
			'donation' => 1,
			'picture' => 'http://www.redcross.bg/images/Fundraizing/2011_KFC_Christmass_01.JPG',
			'link' => 'http://www.redcross.bg/fundraizing/cpn_fund/cpn_hotmeal.html',
			'sms_text' => '',
			'sms_number' => 1255,
			'date_from' => 0
		);

		$this->get_special_info();
	}

	protected function get_special_info()
	{
		require_once 'phpQuery.php';

		$url_content = file_get_contents($this->url . '/campaign.html');

		if($url_content === FALSE)
		{
			return;
		}

		phpQuery::newDocumentHTML($url_content);

		$links = $this->get_links();

		$data = $this->get_data($links, 'special');

		$this->data = array_merge($this->data, $data);
	}

	protected function get_links()
	{
		$all_links = array();

		foreach(pq('#info .title a') as $a)
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
			$link_content = file_get_contents($this->url . $link);

			if($link_content === FALSE)
			{
				continue;
			}

			if(!preg_match('/1466/', $link_content))
			{
				continue;
			}

			phpQuery::newDocumentHTML($link_content);

			foreach(pq('.title2') as $c)
			{
				$campaign_name = trim($c->nodeValue);
				break;
			}

			foreach(pq('#info img') as $c)
			{
				$picture = '';

				foreach($c->attributes as $att_k => $att_v)
				{
					if($att_k === 'src')
					{
						$picture = preg_replace('/^\.{2}/', $this->url, $att_v->value);

						list($width, $height) = getimagesize($picture);

						if($width > 100)
						{
							break 2;
						}
					}
				}
			}

			$data[] = array(
				'name' => $campaign_name,
				'subname' => $campaign_name,
				'type' => $campaign_type,
				'text' => $campaign_name . "\n" . 'Специална кампания на Български Червен Кръст',
				'donation' => 1,
				'picture' => $picture,
				'link' => $this->url . $link,
				'sms_text' => '',
				'sms_number' => 1466,
				'date_from' => 0
			);
		}

		return $data;
	}
}
