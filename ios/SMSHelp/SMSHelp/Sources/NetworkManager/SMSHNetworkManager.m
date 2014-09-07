//
//  SMSHNetworkManager.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/15/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHNetworkManager.h"
#import "SynthesizeSingleton.h"
#import "AFNetworking.h"
#import "AFHTTPRequestOperation+SMSHelp.h"
#import "SMSHContainersStripNulls.h"

#import "SMSHLoadCampaignsRequest.h"


#define SET_COMPLETION_BLOCK_FOR_REQUEST(_operation_) {__block id _operation_no_retain = (_operation_); [(_operation_) setCompletionBlock:^{[self requestDone:(_operation_no_retain)];}];}

@interface SMSHNetworkManager ()
{
	AFHTTPClient *_httpClient;
}

@property (nonatomic, retain) AFHTTPClient *httpClient;

- (void) requestDone:(AFHTTPRequestOperation *)operation;
- (void) requestSucceededWithOperation:(AFHTTPRequestOperation *)operation;
- (void) requestFailedWithOperation:(AFHTTPRequestOperation *)operation;

@end


@implementation SMSHNetworkManager

@synthesize httpClient = _httpClient;

SYNTHESIZE_SINGLETON_FOR_CLASS(SMSHNetworkManager, Instance);

- (id) init
{
	if ((self = [super init]))
	{
		[AFNetworkActivityIndicatorManager sharedManager].enabled = YES;
		
		_httpClient = [[AFHTTPClient alloc] initWithBaseURL:[NSURL URLWithString:@""]];
		
		[AFHTTPRequestOperation addAcceptableContentTypes:[NSSet setWithObjects:@"application/zip", @"text/*", @"application/json", @"image/png", @"image/jpeg", nil]];
		
		[AFJSONRequestOperation addAcceptableContentTypes:[NSSet setWithObjects:@"text/html", nil]];
	}
	
	return self;
}

- (void) dealloc
{
	[_httpClient release];
	
	[super dealloc];
}


#pragma mark - Requests

- (void) requestCampaignsWithCurrentVersion:(uint32_t)version
{
	SMSHLoadCampaignsRequest *lcr = [SMSHLoadCampaignsRequest requestWithCurrentVersion:version];
	SET_COMPLETION_BLOCK_FOR_REQUEST(lcr);
	
	[self.httpClient enqueueHTTPRequestOperation:lcr];
}






#pragma mark - Requestcompletion

- (void) requestDone:(AFHTTPRequestOperation *)operation
{
	if ([operation isKindOfClass:[AFJSONRequestOperation class]])
	{
		id responseObject = ((AFJSONRequestOperation *)operation).responseJSON;
		
		if ([responseObject isKindOfClass:[NSMutableArray class]])
		{
			[(NSMutableArray *)responseObject stripNullValues];
		}
		else if ([responseObject isKindOfClass:[NSMutableDictionary class]])
		{
			[(NSMutableDictionary *)responseObject stripNullValues];
		} 
	}
	
	if (operation.error)
	{
		DBG(@"error: %@", operation.error.localizedDescription);
		DBG(@"failed because: %@", operation.error.localizedFailureReason);
		[self requestFailedWithOperation:operation];
	}
	else
	{
		[self requestSucceededWithOperation:operation];
	}
}

- (void) requestSucceededWithOperation:(AFHTTPRequestOperation *)operation
{
	[operation processResponse];
}

- (void) requestFailedWithOperation:(AFHTTPRequestOperation *)operation
{
	[operation processErrorResponse];
}

@end
