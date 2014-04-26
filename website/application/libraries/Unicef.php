<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Unicef
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

		$text = "Децатa имат нужда от грижа всеки ден oт годината, не само по специални поводи.
Твоите месечни дарения ще помогнат на децата в нужда да растат в семейство, да ходят на училище, да бъдат здрави, да имат бъдеще.

Tвоите месечни дарения по програма БЛАГОДЕТЕЛ ще подкрепят дейността на УНИЦЕФ по света и проектите ни в България:
- Семейство за всяко дете
- Програма за ранно детско развитие - Най-добър старт в живота
- Работилници за родители - Да пораснем заедно
- Подкрепа на деца с увреждания
- Природни бедствия и хуманитарни кризи";

		$data[] = array(
			'name' => 'Стани благодетел',
			'subname' => 'Unicef 2',
			'type' => 'special',
			'text' => $text,
			'donation' => 2,
			'picture' => base64_encode(file_get_contents('http://www.unicef.bg/cache/e4534b1b1e70cc76de82f3e1585c556c.png')),
			'link' => 'http://www.unicef.bg/kampanii/Blagodetel-prez-mobilen-telefon/24',
			'sms_text' => '2',
			'sms_number' => 1021,
			'date_from' => 0
		);

		$data[] = array(
			'name' => 'Стани благодетел',
			'subname' => 'Unicef 5',
			'type' => 'special',
			'text' => $text,
			'donation' => 5,
			'picture' => base64_encode(file_get_contents('http://www.unicef.bg/cache/e4534b1b1e70cc76de82f3e1585c556c.png')),
			'link' => 'http://www.unicef.bg/kampanii/Blagodetel-prez-mobilen-telefon/24',
			'sms_text' => '5',
			'sms_number' => 1021,
			'date_from' => 0
		);

		$this->data = $data;
	}
}
