//
//  AFHTTPRequestOperation+SMSHelp.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/15/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "AFHTTPRequestOperation+SMSHelp.h"

NSString *baseURLString = @"http://smshelp-bg.com/api";

@implementation AFHTTPRequestOperation (SMSHelp)

- (void)processResponse
{
    // To be overridden by subclasses
}

- (void) processErrorResponse
{
	// To be overridden by subclasses
}

- (NSString *)validateResponse
{
    NSInteger statusCode = self.response.statusCode;
    NSString *errorMessage = nil;
    
    switch (statusCode)
    {
        case 200:
        case 201:
        {
            break;
        }
            
        case 302:
        case 401:
        {
            // In the case of FFCRM, bad login API requests receive a 302,
            // with a redirection body taking to the login form
            errorMessage = @"Unauthorized";
            break;
        }
            
        case 404:
        {
            errorMessage = @"The specified path cannot be found (404)";
            break;
        }
            
        case 500:
        {
            errorMessage = @"The server experienced an error (500)";
            break;
        }
            
        default:
        {
            errorMessage = [NSString stringWithFormat:@"The communication with the server failed with error %d", (int)statusCode];
            break;
        }
    }
    
    return errorMessage;
}

+ (NSURLRequest *) urlRequestWithHttpMethod:(HTTPMethod)httpMethod parameters:(NSDictionary *)parameters
{
	NSMutableString *urlString = [[[NSMutableString alloc] initWithString:baseURLString] autorelease];
	
	NSMutableString *parametersString = [[[NSMutableString alloc] initWithString:@""] autorelease];
	
	NSArray *parameterNames = [parameters allKeys];
	
	for (NSString *name in parameterNames)
	{
		[parametersString appendFormat:@"%@=%@&", name, parameters[name]];
	}
	
	if (parametersString.length > 0)
	{
		[parametersString deleteCharactersInRange:NSMakeRange(parametersString.length - 1, 1)];
	}
	
	urlString = [[urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding] mutableCopy];
	parametersString = [[parametersString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding] mutableCopy];
	
	NSMutableURLRequest *urlRequest = [[NSMutableURLRequest alloc] init];
	
	switch (httpMethod)
	{
		case HTTPMethodGET:
		{
			urlRequest.HTTPMethod = @"GET";
			urlRequest.URL = [NSURL URLWithString:[NSString stringWithFormat:@"%@?%@", urlString, parametersString]];
		}
			break;
			
		case HTTPMethodPOST:
		{
			urlRequest.HTTPMethod = @"POST";
			urlRequest.URL = [NSURL URLWithString:urlString];
			urlRequest.HTTPBody = [parametersString dataUsingEncoding:NSUTF8StringEncoding];
		}
			break;
			
		default:
			break;
	}
	
	[urlString release];
	[parametersString release];
	
	return [urlRequest autorelease];
}

@end
