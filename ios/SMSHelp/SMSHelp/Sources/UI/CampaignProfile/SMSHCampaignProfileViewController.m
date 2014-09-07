//
//  SMSHCampaignProfileViewController.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 7/12/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHCampaignProfileViewController.h"
#import <MessageUI/MessageUI.h>

#import "SMSHCampaignProfileView.h"

#import "SMSHCampaign.h"

@interface SMSHCampaignProfileViewController () <SMSHCampaignProfileViewDelegate, MFMessageComposeViewControllerDelegate>
{
	SMSHCampaignProfileView *_view;
	SMSHCampaign *_campaign;
}

@property (nonatomic, retain) SMSHCampaign *campaign; // property to handle retain/release for us

@end


@implementation SMSHCampaignProfileViewController

@synthesize campaign = _campaign;

- (id) initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self)
	{
        // Custom initialization
		
		_campaign = nil;
    }
	
    return self;
}

- (id) initWithCampaign:(SMSHCampaign *)campaign
{
    self = [self initWithNibName:nil bundle:nil];
    if (self)
	{
        // Custom initialization
		
		self.campaign = campaign;
    }
	
    return self;
}

- (void) dealloc
{
	self.campaign = nil;
	
	[super dealloc];
}

- (void) loadView
{
	CGRect bounds = [[UIApplication sharedApplication] keyWindow].bounds;
	CGFloat statusBarHeight = 20.0f; //[UIApplication sharedApplication].statusBarHidden ? 0.0f : [UIApplication sharedApplication].statusBarFrame.size.height;
	CGFloat navBarHeight = self.navigationController.navigationBarHidden ? 0.0f : self.navigationController.navigationBar.frame.size.height;
	
	_view = [[SMSHCampaignProfileView alloc] initWithFrame:cgr(0.0f, 0.0f, bounds.size.width, bounds.size.height - statusBarHeight - navBarHeight) campaign:self.campaign];
	_view.delegate = self;
	self.view = _view;
	[_view release];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
	
	self.edgesForExtendedLayout = UIRectEdgeNone;
	
//	self.navigationItem.backBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"" style:UIBarButtonItemStylePlain target:nil action:nil];
	
	
	[self.navigationItem setTitleView:[[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"images/logo"]] autorelease]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

#pragma mark - SMSHCampaignProfileViewDelegate

- (void) sendButtonWasPressedInCampaignProfileView:(SMSHCampaignProfileView *)view
{
	if([MFMessageComposeViewController canSendText])
	{
		MFMessageComposeViewController *controller = [[[MFMessageComposeViewController alloc] init] autorelease];
		controller.body = self.campaign.smsText;
		controller.recipients = @[[NSString stringWithFormat:@"%lld", self.campaign.smsNumber]];
		controller.messageComposeDelegate = self;
		[self presentViewController:controller animated:YES completion:nil];
	}
}

#pragma mark - MFMessageComposeViewControllerDelegate

- (void)messageComposeViewController:(MFMessageComposeViewController *)controller didFinishWithResult:(MessageComposeResult)result
{
	if (MessageComposeResultSent == result)
	{
		// TODO: record sent sms
	}
	
	[controller dismissViewControllerAnimated:YES completion:nil];
}

@end
