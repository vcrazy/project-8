//
//  SMSHCampaign.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 5/19/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHCampaign.h"

@interface SMSHCampaign ()
{
	NSString *_campaignId;
	NSString *_name;
	NSString *_subname;
	NSString *_type;
	NSString *_text;
	double _price;
	NSString *_pictureUrlString;
	NSString *_pictureFilename;
	NSString *_urlString;
	NSString *_smsText;
	long long _smsNumber;
	NSDate *_startDate;
	NSString *_status;
	CGFloat _cellHeight;
}

@end


@implementation SMSHCampaign

@synthesize campaignId = _campaignId;
@synthesize name = _name;
@synthesize subname = _subname;
@synthesize type = _type;
@synthesize text = _text;
@synthesize price = _price;
@synthesize pictureUrlString = _pictureUrlString;
@synthesize pictureFilename = _pictureFilename;
@synthesize urlString = _urlString;
@synthesize smsText = _smsText;
@synthesize smsNumber = _smsNumber;
@synthesize startDate = _startDate;
@synthesize status = _status;
@synthesize cellHeight = _cellHeight;

+ (SMSHCampaign *) campaignWithDictionary:(NSDictionary *)dictionary forKey:(NSString *)key
{
	if (dictionary != nil && key != nil)
	{
		return [[self alloc] initWithDictionary:dictionary forKey:key];
	}
	
	return nil;
}

- (id) initWithDictionary:(NSDictionary *)dictionary forKey:(NSString *)key
{
	if ((self = [self init]))
	{
		[self inflateWithDictionary:dictionary forKey:key];
	}
	
	return self;
}

- (id) init
{
	if ((self = [super init]))
	{
		_campaignId = nil;
		_name = nil;
		_subname = nil;
		_type = nil;
		_text = nil;
		_price = 0.0;
		_pictureUrlString = nil;
		_pictureFilename = nil;
		_urlString = nil;
		_smsText = nil;
		_smsNumber = 0;
		_startDate = nil;
		_status = nil;
		_cellHeight = 0.0f;
		
		[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(didReceiveManagerSavedPicture:) name:SMSHDataManagerDidSavePictureNotification object:nil];
	}
	
	return self;
}

- (void) dealloc
{
	[[NSNotificationCenter defaultCenter] removeObserver:self];
	
	[_campaignId release], _campaignId = nil;
	[_name release], _name = nil;
	[_subname release], _subname = nil;
	[_type release], _type = nil;
	[_text release], _text = nil;
	[_pictureUrlString release], _pictureUrlString = nil;
	[_pictureFilename release], _pictureFilename = nil;
	[_urlString release], _urlString = nil;
	[_smsText release], _smsText = nil;
	[_startDate release], _startDate = nil;
	[_status release], _status = nil;
	
	[super dealloc];
}

- (void) inflateWithDictionary:(NSDictionary *)dict forKey:(NSString *)key
{
	self.campaignId = key;
	self.name = dict[@"name"];
	self.subname = dict[@"subname"];
	self.type = dict[@"type"];
	self.text = dict[@"text"];
	self.pictureUrlString = dict[@"picture"];
	self.urlString = dict[@"link"];
	self.smsText = dict[@"sms_text"];
	self.status = dict[@"status"];
	self.startDate = [NSDate dateWithTimeIntervalSince1970:(NSTimeInterval)[dict[@"date_from"] longLongValue]];
	self.smsNumber = [dict[@"sms_number"] longLongValue];
	
	NSNumber *price = dict[@"donation"];
	if (price)
	{
		const char *type = [price objCType];
		if (strcmp(type, @encode (double)) == 0)
		{
			self.price = [price doubleValue];
		}
		else if (strcmp(type, @encode (long long)) == 0)
		{
			self.price = (double)[price longLongValue];
		}
		else
		{
			self.price = [price doubleValue];
		}
	}
	else
	{
		self.price = 0.0;
	}
}

- (void) setPictureFilename:(NSString *)pictureFilename
{
	if (pictureFilename != _pictureFilename)
	{
		[_pictureFilename release];
		_pictureFilename = [pictureFilename copy];
	}
	
	if (_pictureFilename.length > 0)
	{
		[[NSNotificationCenter defaultCenter] removeObserver:self name:SMSHDataManagerDidSavePictureNotification object:nil];
	}
}

#pragma mark - notifications

- (void) didReceiveManagerSavedPicture:(NSNotification *)notification
{
	SMSHCampaign *c = notification.userInfo[@"campaign"];
	
	if (c != nil && c.pictureFilename.length > 0 && [self.campaignId isEqualToString:c.campaignId])
	{
		self.pictureFilename = c.pictureFilename;
	}
}

@end
