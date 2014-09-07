//
//  SMSHCampaignProfileView.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 7/12/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import <UIKit/UIKit.h>

@class SMSHCampaign;
@class SMSHCampaignProfileView;

@protocol SMSHCampaignProfileViewDelegate <NSObject>

@required
- (void) sendButtonWasPressedInCampaignProfileView:(SMSHCampaignProfileView *)view;

@end


@interface SMSHCampaignProfileView : UIView

@property (nonatomic, assign) id<SMSHCampaignProfileViewDelegate> delegate;

- (id)initWithFrame:(CGRect)frame campaign:(SMSHCampaign *)campaign;

@end
