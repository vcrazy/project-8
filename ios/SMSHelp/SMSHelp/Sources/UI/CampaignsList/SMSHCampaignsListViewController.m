//
//  SMSHCampaignsListViewController.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/28/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHCampaignsListViewController.h"

#import "SMSHCampaignProfileViewController.h"

#import "SMSHDataManager.h"

#import "SMSHCampaignCell.h"
#import "SMSHCampaignsListHUD.h"

#import "SMSHCampaign.h"

CGFloat const kSMSHCampaignsListViewControllerHUDAnimationDuration = 0.3;


@interface SMSHCampaignsListViewController () <UITableViewDataSource, UITableViewDelegate, SMSHCampaignsListHUDDelegate>
{
	NSMutableArray *_campaigns;
	UITableView *_tableView;
	SMSHCampaignsListViewControllerType _type;
	NSString *_campaignsType;
	SMSHCampaignsListHUD *_hud;
}

@property (nonatomic, retain) NSString *campaignsType;

@end


@implementation SMSHCampaignsListViewController

@synthesize campaignsType = _campaignsType;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self)
	{
        // Custom initialization
		
		_campaigns = [[NSMutableArray alloc] init];
		_campaignsType = nil;
    }
	
    return self;
}

- (id) initWithType:(SMSHCampaignsListViewControllerType)type
{
	if ((self = [self initWithNibName:nil bundle:nil]))
	{
		_type = type;
		
		switch (_type)
		{
			case SMSHCampaignsListViewControllerTypePeople:
			{
				self.campaignsType = kSMSHDataManagerCampaignTypePeople;
			}
				break;
				
			case SMSHCampaignsListViewControllerTypeOrganizations:
			{
				self.campaignsType = kSMSHDataManagerCampaignTypeOrganizations;
			}
				break;
				
			case SMSHCampaignsListViewControllerTypeSpecial:
			{
				self.campaignsType = kSMSHDataManagerCampaignTypeSpecial;
			}
				break;
				
			case SMSHCampaignsListViewControllerTypeOther:
			{
				self.campaignsType = kSMSHDataManagerCampaignTypeOther;
			}
				break;
				
			default:
				break;
		}
		
		[[NSNotificationCenter defaultCenter] addObserver:self 
												 selector:@selector(didReceiveCampaignsChanged:) 
													 name:SMSHCampaignsChangedNotification 
												   object:nil];
		[[NSNotificationCenter defaultCenter] addObserver:self 
												 selector:@selector(didRecieveHideHUD:) 
													 name:SMSHCampaignsListHideHUDNotification 
												   object:nil];
	}
	
	return self;
}

- (void) dealloc
{
	[[NSNotificationCenter defaultCenter] removeObserver:self];
	
	[_campaigns release], _campaigns = nil;
	[_campaignsType release], _campaignsType = nil;
	
	[super dealloc];
}

- (void) loadView
{
	CGRect bounds = [[UIApplication sharedApplication] keyWindow].bounds;
	CGFloat statusBarHeight = 20.0f; //[UIApplication sharedApplication].statusBarHidden ? 0.0f : [UIApplication sharedApplication].statusBarFrame.size.height;
	CGFloat navBarHeight = self.navigationController.navigationBarHidden ? 0.0f : self.navigationController.navigationBar.frame.size.height;
	CGFloat tabBarHeight = self.navigationController.tabBarController.tabBar.height;
	
	UIView *mainView = [[UIView alloc] initWithFrame:cgr(0.0f, 0.0f, bounds.size.width, bounds.size.height - statusBarHeight - navBarHeight - tabBarHeight)];
	mainView.backgroundColor = [UIColor whiteColor];
	self.view = mainView;
	[mainView release];
	
	_tableView = [[UITableView alloc] initWithFrame:self.view.bounds];
	_tableView.delegate = self;
	_tableView.dataSource = self;
	_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
	_tableView.backgroundColor = [UIColor whiteColor];
	[self.view addSubview:_tableView];
	[_tableView release];
	
	_hud = [[SMSHCampaignsListHUD alloc] initWithFrame:cgr(0.0f, 0.0f, self.view.boundsWidth, 0.0f)];
	_hud.nextY = _tableView.originY;
	_hud.delegate = self;
	[_tableView.superview insertSubview:_hud belowSubview:_tableView];
	[_hud release];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
	
	self.edgesForExtendedLayout = UIRectEdgeNone;
	
	[self.navigationItem setTitleView:[[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"images/logo"]] autorelease]];
	
	[[SMSHDataManager sharedInstance] 
	 getCampaignsForType:self.campaignsType 
	 completion:^(NSArray *campaigns, SMSHDataManagerCampaignsRequestCompletionType completionType) {
		 if (SMSHDataManagerCampaignsRequestCompletionTypeSuccess == completionType)
		 {
			 dispatch_async(dispatch_get_main_queue(), ^{
				 [_campaigns removeAllObjects];
				 [_campaigns addObjectsFromArray:campaigns];
				 [self hideHUD];
				 [_tableView reloadData];
			 });
		 }
		 else if (SMSHDataManagerCampaignsRequestCompletionTypeWaiting == completionType)
		 {
			 dispatch_async(dispatch_get_main_queue(), ^{
				 [_campaigns removeAllObjects];
				 [_campaigns addObjectsFromArray:campaigns];
				 [self showLoadingHUD];
				 [_tableView reloadData];
			 });
		 }
		 else if (SMSHDataManagerCampaignsRequestCompletionTypeError == completionType)
		 {
			 dispatch_async(dispatch_get_main_queue(), ^{
				 [_campaigns removeAllObjects];
				 [_campaigns addObjectsFromArray:campaigns];
				 [self showErrorHUD];
				 [_tableView reloadData];
			 });
		 }
	 }];
}

- (void) viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	
//	self.navigationItem.backBarButtonItem.title = @" ";
	
	UIButton *backButton = [UIButton buttonWithType:UIButtonTypeCustom];
	backButton.tintColor = [UIColor whiteColor];
	[backButton setImage:[UIImage imageNamed:@"images/nav_bar_button_back"] forState:UIControlStateNormal];
	[backButton addTarget:self action:@selector(backButtonWasPressed:) forControlEvents:UIControlEventTouchUpInside];
	
	UIBarButtonItem *backButtonItem = [[UIBarButtonItem alloc] initWithCustomView:backButton];
	self.navigationItem.backBarButtonItem = backButtonItem;
	self.navigationItem.backBarButtonItem.title = @" ";
	[backButtonItem release];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) backButtonWasPressed:(UIButton *)sender
{
	[self.navigationController popViewControllerAnimated:YES];
}

- (void) showLoadingHUD
{
	[_hud showSpinner];
	
	[UIView animateWithDuration:kSMSHCampaignsListViewControllerHUDAnimationDuration 
						  delay:0.0 
						options:UIViewAnimationOptionCurveEaseInOut 
					 animations:^{
						 _hud.originY = 0.0f;
						 _tableView.originY = _hud.nextY;
						 _tableView.height = self.view.boundsHeight - _hud.height;
					 } 
					 completion:nil];
}

- (void) showErrorHUD
{
	[_hud showErrorInfo];
	
	[UIView animateWithDuration:kSMSHCampaignsListViewControllerHUDAnimationDuration 
						  delay:0.0 
						options:UIViewAnimationOptionCurveEaseInOut 
					 animations:^{
						 _hud.originY = 0.0f;
						 _tableView.frame = cgr(_tableView.originX, _hud.nextY, _tableView.width, self.view.boundsHeight - _hud.height);
					 } 
					 completion:nil];
}

- (void) hideHUD
{
	[UIView animateWithDuration:kSMSHCampaignsListViewControllerHUDAnimationDuration 
						  delay:0.0 
						options:UIViewAnimationOptionCurveEaseInOut 
					 animations:^{
						 _tableView.frame = cgr(_tableView.originX, 0.0f, _tableView.width, self.view.boundsHeight);
						 _hud.nextY = _tableView.originY;
					 } 
					 completion:nil];
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
	
    return (NSInteger)(_campaigns.count);
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"CampaignCell";
    SMSHCampaignCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (nil == cell)
	{
		cell = [[[SMSHCampaignCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
	}
    
    // Configure the cell...
	
	[cell setCampaign:_campaigns[indexPath.row]];
    
    return cell;
}

/*
 // Override to support conditional editing of the table view.
 - (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the specified item to be editable.
 return YES;
 }
 */

/*
 // Override to support editing the table view.
 - (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
 {
 if (editingStyle == UITableViewCellEditingStyleDelete) {
 // Delete the row from the data source
 [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
 }   
 else if (editingStyle == UITableViewCellEditingStyleInsert) {
 // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
 }   
 }
 */

/*
 // Override to support rearranging the table view.
 - (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
 {
 }
 */

/*
 // Override to support conditional rearranging of the table view.
 - (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the item to be re-orderable.
 return YES;
 }
 */

/*
 #pragma mark - Navigation
 
 // In a story board-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
 {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 
 */

#pragma mark - Table view delegate

- (CGFloat) tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	CGFloat h = 0.0f;
	SMSHCampaign *c = _campaigns[indexPath.row];
	if (c.cellHeight > 0.0f)
	{
		h = c.cellHeight;
	}
	else
	{
		h = [SMSHCampaignCell heightForCellWithCampaign:c];
	}
	
	return h;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     [detailViewController release];
     */
	
	SMSHCampaignProfileViewController *cpvc = [[SMSHCampaignProfileViewController alloc] initWithCampaign:_campaigns[indexPath.row]];
	cpvc.hidesBottomBarWhenPushed = YES;
	[self.navigationController pushViewController:cpvc animated:YES];
	[cpvc release];
}

#pragma mark - notifications

- (void) didReceiveCampaignsChanged:(NSNotification *)notification
{
	[[SMSHDataManager sharedInstance] 
	 getCampaignsForType:self.campaignsType 
	 completion:^(NSArray *campaigns, SMSHDataManagerCampaignsRequestCompletionType completionType) {
		 if (SMSHDataManagerCampaignsRequestCompletionTypeSuccess == completionType)
		 {
			 dispatch_async(dispatch_get_main_queue(), ^{
				 [_campaigns removeAllObjects];
				 [_campaigns addObjectsFromArray:campaigns];
				 [self hideHUD];
				 [_tableView reloadData];
			 });
		 }
		 else if (SMSHDataManagerCampaignsRequestCompletionTypeWaiting == completionType)
		 {
			 dispatch_async(dispatch_get_main_queue(), ^{
				 [_campaigns removeAllObjects];
				 [_campaigns addObjectsFromArray:campaigns];
				 [self showLoadingHUD];
				 [_tableView reloadData];
			 });
		 }
		 else if (SMSHDataManagerCampaignsRequestCompletionTypeError == completionType)
		 {
			 dispatch_async(dispatch_get_main_queue(), ^{
				 [_campaigns removeAllObjects];
				 [_campaigns addObjectsFromArray:campaigns];
				 [self showErrorHUD];
				 [_tableView reloadData];
			 });
		 }
	}];
}

- (void) didRecieveHideHUD:(NSNotification *)notification
{
	dispatch_async(dispatch_get_main_queue(), ^{
		[self hideHUD]; 
	});
}

#pragma mark - SMSHCampaignsListHUDDelegate

- (void) retryButtonWasPressedInHUD:(SMSHCampaignsListHUD *)hud
{
	[[SMSHDataManager sharedInstance] hudRetryButtonWasPressed];
}

- (void) closeButtonWasPressedInHUD:(SMSHCampaignsListHUD *)hud
{
	[[SMSHDataManager sharedInstance] hudCloseButtonWasPressed];
}

@end
