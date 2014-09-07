//
//  SMSHCampaignsListViewController.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/28/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum
{
	SMSHCampaignsListViewControllerTypePeople,
	SMSHCampaignsListViewControllerTypeOrganizations,
	SMSHCampaignsListViewControllerTypeSpecial,
	SMSHCampaignsListViewControllerTypeOther
} SMSHCampaignsListViewControllerType;


@interface SMSHCampaignsListViewController : UIViewController

- (id) initWithType:(SMSHCampaignsListViewControllerType)type;

@end
