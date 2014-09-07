//
//  SMSHLoadCampaignsRequest.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/21/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHLoadCampaignsRequest.h"

#import "SMSHCampaign.h"

@implementation SMSHLoadCampaignsRequest

+ (SMSHLoadCampaignsRequest *) requestWithCurrentVersion:(uint32_t)version
{
	NSURLRequest *urlRequest = [self urlRequestWithHttpMethod:HTTPMethodGET parameters:@{@"version": [NSString stringWithFormat:@"%u", version]}];
	
	SMSHLoadCampaignsRequest *r = [[self alloc] initWithRequest:urlRequest];
	r.JSONReadingOptions = NSJSONReadingMutableContainers;
	
	return r;
}

- (void) processResponse
{
//	DBG(@"campaigns success: %@", self.responseString);
//	DBG(@"request: %@", self.request.URL.absoluteString);
	
	NSDictionary *data = self.responseJSON;
	NSNumber *version = data[@"version"];
	NSDictionary *campaignsData = data[@"campaigns"];
	
	if ([campaignsData isKindOfClass:[NSDictionary class]])
	{
		NSMutableArray *campaigns = [[NSMutableArray alloc] init];
		
		NSArray *campaignsIds = [campaignsData allKeys];
		
		for (NSString *cId in campaignsIds)
		{
			SMSHCampaign *c = [SMSHCampaign campaignWithDictionary:campaignsData[cId] forKey:cId];
			
			[campaigns addObject:c];
		}
		
		NSDictionary *userInfo = @{@"campaigns": campaigns,
								   @"version": version};
		[[NSNotificationCenter defaultCenter] postNotificationName:SMSHDidReceiveCampaignsSuccessNotification object:nil userInfo:userInfo];
		
		[campaigns release];
	}
	else
	{
		[[NSNotificationCenter defaultCenter] postNotificationName:SMSHDidReceiveCampaignsSuccessNotification object:nil userInfo:nil];
	}
}

- (void) processErrorResponse
{
//	DBG(@"campaigns error: %@", self.responseString);
	DBG(@"request: %@", self.request.URL.absoluteString);
	DBG(@"status code: %d", (int)(self.response.statusCode));
	
	[[NSNotificationCenter defaultCenter] postNotificationName:SMSHDidReceiveCampaignsErrorNotification object:nil];
}

@end
