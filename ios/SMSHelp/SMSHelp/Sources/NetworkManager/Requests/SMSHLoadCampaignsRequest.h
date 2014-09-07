//
//  SMSHLoadCampaignsRequest.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/21/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "AFHTTPRequestOperation+SMSHelp.h"

@interface SMSHLoadCampaignsRequest : AFJSONRequestOperation

+ (SMSHLoadCampaignsRequest *) requestWithCurrentVersion:(uint32_t)version;

@end
