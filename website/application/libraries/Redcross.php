<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Redcross
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
		$data = array();

		$data[] = array(
			'name' => 'Програма "Топъл обяд"',
			'subname' => 'Топъл обяд',
			'type' => 'special',
			'text' => "Програма \"Топъл обяд\" е създадена през 2004 г. и се реализира в рамките на Партньорска мрежа за благотворителност на БЧК. Основната цел на програмата е да се подпомогне физическото развитие на деца в неравностойно положение и чрез топлия обяд те да се върнат в училище. С различни кампании се набират средства и се финансират проекти \"Топъл обяд\" за ученици от цялата страна.
Много често топлият обяд в трапезариите на БЧК се оказва единственият повод нуждаещите се български деца да посещават учебните занятия. Те получават безплатен обяд всеки ден през учебната година. Целта е да се подпомогне нормалното физическо развитие на децата, както и да се съдейства за намаляване заболяваемостта им, произтичаща от нередовното и непълноценно хранене. Осигуряването на храна на някои особено нуждаещи се ученици чрез включването им в проекта \"Топъл обяд\" е единствената възможност те да посещават редовно учебните занятия.",
			'donation' => 1,
			'picture' => base64_encode(file_get_contents('http://www.redcross.bg/images/Fundraizing/2011_KFC_Christmass_01.JPG')),
			'link' => 'http://www.redcross.bg/fundraizing/cpn_fund/cpn_hotmeal.html',
			'sms_text' => '',
			'sms_number' => 1255,
			'date_from' => 0
		);

		$data[] = array(
			'name' => 'Подкрепа на бежанците в България',
			'subname' => 'Бежанци',
			'type' => 'special',
			'text' => "Национална благотворителна кампания в подкрепа на бежанците в България
Кампанията се организира в подкрепа на усилията на държавните институции за преодоляване на кризисната ситуация и осигуряване на хуманитарна подкрепа от първа необходимост за стотици бежански семейства, останали без дом и препитание.",
			'donation' => 1,
			'picture' => base64_encode(file_get_contents('http://www.redcross.bg/images/RMS_Donations/2014_04_RFL_01.JPG')),
			'link' => 'http://www.redcross.bg/campaign/refujees_campaign.html',
			'sms_text' => '',
			'sms_number' => 1466,
			'date_from' => 0
		);

		$this->data = $data;
	}
}
