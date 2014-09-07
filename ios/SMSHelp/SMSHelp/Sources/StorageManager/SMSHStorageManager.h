//
//  SMSHStorageManager.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 5/19/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import <Foundation/Foundation.h>

@class SMSHCampaign;

@interface SMSHStorageManager : NSObject

+ (SMSHStorageManager *) sharedInstance;

- (void) addCampaigns:(NSArray *)campaigns;
- (NSArray *) getCampaignsForType:(NSString *)type;
- (BOOL) savePicture:(UIImage *)image forCampaign:(SMSHCampaign *)campaign;
- (SMSHCampaign *) getCampaignWithId:(NSString *)campaignId;

@end
