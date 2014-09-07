//
//  SMSHCampaignCell.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/29/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import <UIKit/UIKit.h>

@class SMSHCampaign;

@interface SMSHCampaignCell : UITableViewCell

+ (CGFloat) heightForCellWithCampaign:(SMSHCampaign *)campaign;

- (void) setCampaign:(SMSHCampaign *)campaign;

@end
