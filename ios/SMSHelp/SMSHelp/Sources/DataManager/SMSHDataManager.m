//
//  SMSHDataManager.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/21/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHDataManager.h"
#import "SynthesizeSingleton.h"

#import "SMSHStorageManager.h"
#import "SMSHNetworkManager.h"

#import "SMSHCampaign.h"

NSString * const kSMSHDataManagerUserDefaultsVersionKey = @"kSMSHDataManagerUserDefaultsVersionKey";

NSString * const kSMSHDataManagerCampaignTypePeople			= @"kSMSHDataManagerCampaignTypePeople";
NSString * const kSMSHDataManagerCampaignTypeOrganizations	= @"kSMSHDataManagerCampaignTypeOrganizations";
NSString * const kSMSHDataManagerCampaignTypeSpecial		= @"kSMSHDataManagerCampaignTypeSpecial";
NSString * const kSMSHDataManagerCampaignTypeOther			= @"kSMSHDataManagerCampaignTypeOther";

typedef enum
{
	SMSHDataManagerCurrentStateOK = 0,
	SMSHDataManagerCurrentStateLoading,
	SMSHDataManagerCurrentStateError
} SMSHDataManagerCurrentState;


@interface SMSHDataManager ()
{
	uint32_t _version;
	NSDictionary *_campaignTypes;
	
	SMSHDataManagerCurrentState _currentState;
}

@end


@implementation SMSHDataManager

SYNTHESIZE_SINGLETON_FOR_CLASS(SMSHDataManager, Instance);

- (id) init
{
	if ((self = [super init]))
	{
		NSString *version = [[NSUserDefaults standardUserDefaults] stringForKey:kSMSHDataManagerUserDefaultsVersionKey];
		
		_version = (uint32_t)[version intValue];
		
		[SMSHStorageManager sharedInstance]; // initializing database
		
		_campaignTypes = [[NSDictionary alloc] initWithObjectsAndKeys:
						  @"people", kSMSHDataManagerCampaignTypePeople,
						  @"organizations", kSMSHDataManagerCampaignTypeOrganizations,
						  @"special", kSMSHDataManagerCampaignTypeSpecial,
						  @"other", kSMSHDataManagerCampaignTypeOther,
						  nil];
		
		_currentState = SMSHDataManagerCurrentStateOK;
		
		[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(receivedCampaignsSuccess:) name:SMSHDidReceiveCampaignsSuccessNotification object:nil];
		[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(receivedCampaignsError:) name:SMSHDidReceiveCampaignsErrorNotification object:nil];
		
		[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationDidBecomeActive:) name:UIApplicationDidBecomeActiveNotification object:nil];
		
		[self requestCampaigns];
	}
	
	return self;
}

- (void) dealloc
{
	[_campaignTypes release];
	
	[super dealloc];
}

- (void) getCampaignsForType:(NSString *)type completion:(CampaignsForTypeCompletionBlock)completion
{
	NSArray *campaigns = nil;
	SMSHDataManagerCampaignsRequestCompletionType completionType = SMSHDataManagerCampaignsRequestCompletionTypeSuccess;
	
	NSString *typeId = _campaignTypes[type];
	
	if (typeId)
	{
		campaigns = [[SMSHStorageManager sharedInstance] getCampaignsForType:typeId];
	}
	else
	{
		campaigns = nil;
	}
	
	switch (_currentState)
	{
		case SMSHDataManagerCurrentStateOK:
		{
			completionType = SMSHDataManagerCampaignsRequestCompletionTypeSuccess;
		}
			break;
			
		case SMSHDataManagerCurrentStateLoading:
		{
			completionType = SMSHDataManagerCampaignsRequestCompletionTypeWaiting;
		}
			break;
			
		case SMSHDataManagerCurrentStateError:
		{
			completionType = SMSHDataManagerCampaignsRequestCompletionTypeError;
		}
			break;
			
		default:
			break;
	}
	
	completion(campaigns, completionType);
}

- (void) requestCampaigns
{
	if (_currentState != SMSHDataManagerCurrentStateLoading)
	{
		_currentState = SMSHDataManagerCurrentStateLoading;
		[[NSNotificationCenter defaultCenter] postNotificationName:SMSHCampaignsChangedNotification object:nil];
		
		[[SMSHNetworkManager sharedInstance] requestCampaignsWithCurrentVersion:_version];
	}
}

- (SMSHCampaign *)getCampaignWithId:(NSString *)campaignId
{
	return [[SMSHStorageManager sharedInstance] getCampaignWithId:campaignId];
}

- (void) saveImage:(UIImage *)image forCampaign:(SMSHCampaign *)campaign
{
	dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
		if ([[SMSHStorageManager sharedInstance] savePicture:image forCampaign:campaign])
		{
			SMSHCampaign *c = [self getCampaignWithId:campaign.campaignId];
			
			if (c != nil)
			{
				// post notification
				dispatch_async(dispatch_get_main_queue(), ^{
					[[NSNotificationCenter defaultCenter] postNotificationName:SMSHDataManagerDidSavePictureNotification 
																		object:nil 
																	  userInfo:@{@"campaign": c}];
				});
			}
		}
	});
}

#pragma mark - hud

- (void) hudRetryButtonWasPressed
{
	[self requestCampaigns];
}

- (void) hudCloseButtonWasPressed
{
	[[NSNotificationCenter defaultCenter] postNotificationName:SMSHCampaignsListHideHUDNotification object:nil];
}

#pragma mark - notifications

- (void) receivedCampaignsSuccess:(NSNotification *)notification
{
	_currentState = SMSHDataManagerCurrentStateOK;
	
	uint32_t version = (uint32_t)[notification.userInfo[@"version"] longLongValue];
	
	@synchronized(self)
	{
		if (version > _version)
		{
			_version = version;
			[[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"%u", _version] forKey:kSMSHDataManagerUserDefaultsVersionKey];
			[[NSUserDefaults standardUserDefaults] synchronize];
			
			[[SMSHStorageManager sharedInstance] addCampaigns:notification.userInfo[@"campaigns"]];
		}
	}
	
	[[NSNotificationCenter defaultCenter] postNotificationName:SMSHCampaignsChangedNotification object:nil];
}

- (void) receivedCampaignsError:(NSNotification *)notification
{
	_currentState = SMSHDataManagerCurrentStateError;
	
	[[NSNotificationCenter defaultCenter] postNotificationName:SMSHCampaignsChangedNotification object:nil];
}

- (void) applicationDidBecomeActive:(NSNotification *)notification
{
	[self requestCampaigns];
}

@end
