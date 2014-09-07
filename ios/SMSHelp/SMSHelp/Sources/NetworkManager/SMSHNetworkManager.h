//
//  SMSHNetworkManager.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/15/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SMSHNetworkManager : NSObject

+ (SMSHNetworkManager *) sharedInstance;

- (void) requestCampaignsWithCurrentVersion:(uint32_t)version;

@end
