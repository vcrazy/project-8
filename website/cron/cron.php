<?php

define('CLI', TRUE);

if(preg_match('/^Windows/', $_SERVER['OS']))
{
	define('ENVIRONMENT', 'development');
}
else
{
	define('ENVIRONMENT', 'production');
}

// time limit n minutes
set_time_limit(15 * 60);

require_once('../www/index.php');
