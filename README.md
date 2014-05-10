SMS Help /project-8/
=========

Base url: http://smshelp-bg.com

API url: /api

**TL;DR and online usage: use /api**

**Offline or more friendly usage below**

Basic usage from client:
```
current version = currently saved version || 0;


if (current version > 0)
{
	1.) GET: /api/version
	new version = [version from 1.]
}

if (current version == 0 OR currently version < new version)
{
	2.) GET: /api?version=[current version]
	currently saved version = new version
}
```

**The basic usage should do the work every time.**

**GET: /api?version=x returns the diff between the current API version and the requested one (client's)!**

If requested without the ?version parameter the full database is returned. It's about 600KB.

===

Data format:
```
{
	campaigns:
	{
		campaign_id [int-like string]: 
		{
			name, [text]
			subname, [text]
			type, [string, max 127, people|organizations|other|special]
			text, [text]
			donation, [int/float]
			picture, [string, url, max 255]
			link, [text, url]
			sms_text, [text]
			sms_number, [int]
			date_from, [int, unix timestamp],
			status [string, max 7, insert|update|delete, available only when requested with ?version=[int, > 0]]
		},
		campaign_id2:
		{
			...
		},
		...
	},
	ratings:
	{
		// currently not in use
	},
	version: [api version, int]
}
```

Sample initial request /api:
```
{
	"campaigns":
	{
		"1":
		{
			"name":"Да помогнем отново на Татяна",
			"subname":"DMS TATIANA",
			"type":"people",
			"text":"Изпратете дарителски...",
			"donation":1,
			"picture":"http:\/\/dmsbg.com\/files\/projects_file2_1398501015.jpg",
			"link":"http:\/\/www.dmsbg.com\/index.php?page=4&spage=3&item=436",
			"sms_text":"DMS TATIANA",
			"sms_number":17777,
			"date_from":1398463200
		},
		...
	},
	"ratings": [],
	"version": 81
}
```

Sample request when the application's saved api version is 80 /api?version=80:
```
{
	"campaigns":
	{
		"74":
		{
			"picture":"http:\/\/sohip.org\/wp-content\/uploads\/2012\/09\/unicef-logo1.jpeg",
			"status":"update"
		}
	},
	"ratings": [],
	"version": 81
}
```

When data is returned check the _status_ field.

If it is _delete_ the record is deleted and you should delete it, too.

When _update_ - only the updated fields are returned and you'll have to update it.

When _insert_ - the record is new and you have to save it in your preferred database.

If the _status_ field is not available you are requesting data for the first time so you have to insert all of it.

The images are returned as links to the image and you'll have to get and save them for offline usage on your own.

===

DEV info

Website:
Set up a virtual host with:
DocumentRoot "path_to/website/www"
ServerName project-8.loc
