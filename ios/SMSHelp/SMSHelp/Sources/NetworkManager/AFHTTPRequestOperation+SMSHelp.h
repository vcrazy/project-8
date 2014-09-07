//
//  AFHTTPRequestOperation+SMSHelp.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/15/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AFHTTPRequestOperation.h"
#import "AFJSONRequestOperation.h"

extern NSString *baseURLString;

typedef enum
{
	HTTPMethodGET,
	HTTPMethodPOST,
	HTTPMethodPUT,
	HTTPMethodDELETE
} HTTPMethod;

@interface AFHTTPRequestOperation (SMSHelp)

- (NSString *) validateResponse;
- (void) processResponse;
- (void) processErrorResponse;

+ (NSURLRequest *) urlRequestWithHttpMethod:(HTTPMethod)httpMethod parameters:(NSDictionary *)parameters;

@end
