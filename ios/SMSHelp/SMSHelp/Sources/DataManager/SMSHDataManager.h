//
//  SMSHDataManager.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/21/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import <Foundation/Foundation.h>

@class SMSHCampaign;

extern NSString * const kSMSHDataManagerCampaignTypePeople;
extern NSString * const kSMSHDataManagerCampaignTypeOrganizations;
extern NSString * const kSMSHDataManagerCampaignTypeSpecial;
extern NSString * const kSMSHDataManagerCampaignTypeOther;

typedef enum
{
	SMSHDataManagerCampaignsRequestCompletionTypeSuccess = 0,
	SMSHDataManagerCampaignsRequestCompletionTypeWaiting,
	SMSHDataManagerCampaignsRequestCompletionTypeError
} SMSHDataManagerCampaignsRequestCompletionType;

typedef void (^ CampaignsForTypeCompletionBlock)(NSArray *campaigns, SMSHDataManagerCampaignsRequestCompletionType completionType);

@interface SMSHDataManager : NSObject

+ (SMSHDataManager *) sharedInstance;

- (void) getCampaignsForType:(NSString *)type completion:(CampaignsForTypeCompletionBlock)completion;

- (void) saveImage:(UIImage *)image forCampaign:(SMSHCampaign *)campaign;

- (void) hudRetryButtonWasPressed;
- (void) hudCloseButtonWasPressed;

- (SMSHCampaign *)getCampaignWithId:(NSString *)campaignId;

@end
