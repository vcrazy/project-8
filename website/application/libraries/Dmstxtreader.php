<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Dmstxtreader
{
	protected $data = array();

	public function __construct()
	{
		$data = array();
		$files = array();

		if ($handle = opendir('pdfdata')) {
			while (false !== ($entry = readdir($handle))) {
				if(strpos($entry, '.') !== 0)
				{
					$files[] = $entry;
				}
			}

			closedir($handle);
		}

		foreach($files as $file)
		{
			$date = explode(".txt", $file);
			$date = $date[0];
			$data[$date] = $this->get_data(file_get_contents('pdfdata/' . $file));
		}

		$this->data = $data;
	}

	protected function get_data($file)
	{
		$data = array();
		$file = explode("\n", $file);

		$file = array_map(function($e)
		{
			return explode(' ', trim($e));
		}, $file);

		foreach($file as $f)
		{
			$g = array_splice($f, -2);
			$g[1] = (int)$g[1];
			$data[] = $g;
		}

		return $data;
	}

	public function get()
	{
		return $this->data;
	}
}
