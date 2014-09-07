//
//  SMSHCampaignsListHUD.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 7/20/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import <UIKit/UIKit.h>

@class SMSHCampaignsListHUD;

@protocol SMSHCampaignsListHUDDelegate <NSObject>

@required
- (void) retryButtonWasPressedInHUD:(SMSHCampaignsListHUD *)hud;
- (void) closeButtonWasPressedInHUD:(SMSHCampaignsListHUD *)hud;

@end


@interface SMSHCampaignsListHUD : UIView

@property (nonatomic, assign) id<SMSHCampaignsListHUDDelegate> delegate;

- (void) hideSpinner;
- (void) showSpinner;
- (void) hideErrorInfo;
- (void) showErrorInfo;
- (void) hideAll;

@end
