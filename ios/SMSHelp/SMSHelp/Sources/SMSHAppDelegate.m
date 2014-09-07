//
//  SMSHAppDelegate.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 5/19/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHAppDelegate.h"

#import "SMSHDataManager.h"

#import "SMSHCampaignsListViewController.h"

@interface SMSHAppDelegate () <UITabBarControllerDelegate>
{
	UIView *_peopleTabView;
	UIView *_organizationsTabView;
	UIView *_specialTabView;
	UIView *_otherTabView;
	
	UILabel *_peopleTabTitle;
	UILabel *_organizationsTabTitle;
	UILabel *_specialTabTitle;
	UILabel *_otherTabTitle;
	
	UIImageView *_peopleTabImageView;
	UIImageView *_organizationsTabImageView;
	UIImageView *_specialTabImageView;
	UIImageView *_otherTabImageView;
}

@end

@implementation SMSHAppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
	NSSetUncaughtExceptionHandler(&uncaughtExceptionHandler);
	
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    // Override point for customization after application launch.
    self.window.backgroundColor = [UIColor whiteColor];
    [self.window makeKeyAndVisible];
	
	[[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
	
	[SMSHDataManager sharedInstance];
	
	[[UITabBar appearance] setItemSpacing:1.0f];
	[[UITabBar appearance] setBackgroundColor:[UIColor whiteColor]];
	[[UITabBar appearance] setTintColor:[UIColor clearColor]];
	[[UITabBar appearance] setItemPositioning:UITabBarItemPositioningCentered];
	
	[[UINavigationBar appearance] setBarTintColor:kSMSHColorLightBlue];
	
	UIImage *backButtonImage = [[UIImage imageNamed:@"images/nav_bar_button_back"] resizableImageWithCapInsets:UIEdgeInsetsMake(0.0f, 1.0f, 0.0f, 1.0f)];
	
	[[UINavigationBar appearance] setBackIndicatorImage:backButtonImage];
	[[UINavigationBar appearance] setBackIndicatorTransitionMaskImage:backButtonImage];
	
	[[UINavigationBar appearance] setTintColor:[UIColor whiteColor]];
	
	[[UIBarButtonItem appearanceWhenContainedIn:[UIImagePickerController class], nil] setBackButtonBackgroundImage:nil forState:UIControlStateNormal barMetrics:UIBarMetricsDefault];
	
	UITabBarController *tbc = [[UITabBarController alloc] initWithNibName:nil bundle:nil];
	tbc.delegate = self;
	
	// people campaigns
	SMSHCampaignsListViewController *peopleCampaignsVC = [[SMSHCampaignsListViewController alloc] initWithType:SMSHCampaignsListViewControllerTypePeople];
	UINavigationController *peopleCampaignsNC = [[UINavigationController alloc] initWithRootViewController:peopleCampaignsVC];
	[peopleCampaignsVC release];
	
	// organizations campaigns
	SMSHCampaignsListViewController *organizationsCampaignsVC = [[SMSHCampaignsListViewController alloc] initWithType:SMSHCampaignsListViewControllerTypeOrganizations];
	UINavigationController *organizationsCampaignsNC = [[UINavigationController alloc] initWithRootViewController:organizationsCampaignsVC];
	[organizationsCampaignsVC release];
	
	// special campaigns
	SMSHCampaignsListViewController *specialCampaignsVC = [[SMSHCampaignsListViewController alloc] initWithType:SMSHCampaignsListViewControllerTypeSpecial];
	UINavigationController *specialCampaignsNC = [[UINavigationController alloc] initWithRootViewController:specialCampaignsVC];
	[specialCampaignsVC release];
	
	// other campaigns
	SMSHCampaignsListViewController *otherCampaignsVC = [[SMSHCampaignsListViewController alloc] initWithType:SMSHCampaignsListViewControllerTypeOther];
	UINavigationController *otherCampaignsNC = [[UINavigationController alloc] initWithRootViewController:otherCampaignsVC];
	[otherCampaignsVC release];
	
	[tbc setViewControllers:@[peopleCampaignsNC, organizationsCampaignsNC, specialCampaignsNC, otherCampaignsNC]];
	
	[peopleCampaignsNC release];
	[organizationsCampaignsNC release];
	[specialCampaignsNC release];
	[otherCampaignsNC release];
	
	// the tab bar controller will not be released - it needs to stay on screen for as long as the app lives
	
	CGFloat tabWidth = tbc.tabBar.width / (CGFloat)(tbc.viewControllers.count);
	CGFloat tabHeight = tbc.tabBar.height;
	
	_peopleTabView = [[UIView alloc] initWithFrame:cgr(0.0f * tabWidth, 0.0f, tabWidth, tabHeight)];
	_peopleTabView.backgroundColor = kSMSHColorLightBlue;
	_peopleTabView.userInteractionEnabled = NO;
	
	_organizationsTabView = [[UIView alloc] initWithFrame:cgr(1.0f * tabWidth, 0.0f, tabWidth, tabHeight)];
	_organizationsTabView.backgroundColor = kSMSHColorDarkBlue;
	_organizationsTabView.userInteractionEnabled = NO;
	
	_specialTabView = [[UIView alloc] initWithFrame:cgr(2.0f * tabWidth, 0.0f, tabWidth, tabHeight)];
	_specialTabView.backgroundColor = kSMSHColorDarkBlue;
	_specialTabView.userInteractionEnabled = NO;
	
	_otherTabView = [[UIView alloc] initWithFrame:cgr(3.0f * tabWidth, 0.0f, tabWidth, tabHeight)];
	_otherTabView.backgroundColor = kSMSHColorDarkBlue;
	_otherTabView.userInteractionEnabled = NO;
	
	CGFloat tabTitleBottomY = tabHeight - 6.0f;
	CGFloat tabImageWidth = normalizedLengthFromLength(25.0f);
	
	// people tab
	_peopleTabTitle = [self newTabTitlelabelWithWidth:tabWidth bottomY:tabTitleBottomY];
	_peopleTabTitle.text = @"ХОРА";
//	_peopleTabTitle.backgroundColor = _peopleTabView.backgroundColor;
//	_peopleTabTitle.opaque = YES;
	[_peopleTabView addSubview:_peopleTabTitle];
	[_peopleTabTitle release];
	
	_peopleTabImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:kSMSHTabIconPeople]];
	_peopleTabImageView.size = cgs(tabImageWidth, tabImageWidth);
	_peopleTabImageView.center = cgp(_peopleTabView.boundsWidth / 2.0f, roundf(_peopleTabTitle.originY / 2.0f));
	[_peopleTabView addSubview:_peopleTabImageView];
	[_peopleTabImageView release];
	
	// organizations tab
	_organizationsTabTitle = [self newTabTitlelabelWithWidth:tabWidth bottomY:tabTitleBottomY];
	_organizationsTabTitle.text = @"ОРГАНИЗАЦИИ";
//	_organizationsTabTitle.backgroundColor = _organizationsTabView.backgroundColor;
//	_organizationsTabTitle.opaque = YES;
	[_organizationsTabView addSubview:_organizationsTabTitle];
	[_organizationsTabTitle release];
	
	_organizationsTabImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:kSMSHTabIconOrganizations]];
	_organizationsTabImageView.size = cgs(tabImageWidth, tabImageWidth);
	_organizationsTabImageView.center = cgp(_organizationsTabView.boundsWidth / 2.0f, roundf(_organizationsTabTitle.originY / 2.0f));
	[_organizationsTabView addSubview:_organizationsTabImageView];
	[_organizationsTabImageView release];
	
	// special tab
	_specialTabTitle = [self newTabTitlelabelWithWidth:tabWidth bottomY:tabTitleBottomY];
	_specialTabTitle.text = @"СПЕЦИАЛНИ";
//	_specialTabTitle.backgroundColor = _specialTabView.backgroundColor;
//	_specialTabTitle.opaque = YES;
	[_specialTabView addSubview:_specialTabTitle];
	[_specialTabTitle release];
	
	_specialTabImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:kSMSHTabIconSpecial]];
	_specialTabImageView.size = cgs(tabImageWidth, tabImageWidth);
	_specialTabImageView.center = cgp(_specialTabView.boundsWidth / 2.0f, roundf(_specialTabTitle.originY / 2.0f));
	[_specialTabView addSubview:_specialTabImageView];
	[_specialTabImageView release];
	
	// other tab
	_otherTabTitle = [self newTabTitlelabelWithWidth:tabWidth bottomY:tabTitleBottomY];
	_otherTabTitle.text = @"ДРУГИ";
//	_otherTabTitle.backgroundColor = _otherTabView.backgroundColor;
//	_otherTabTitle.opaque = YES;
	[_otherTabView addSubview:_otherTabTitle];
	[_otherTabTitle release];
	
	_otherTabImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:kSMSHTabIconOther]];
	_otherTabImageView.size = cgs(tabImageWidth, tabImageWidth);
	_otherTabImageView.center = cgp(_otherTabView.boundsWidth / 2.0f, roundf(_otherTabTitle.originY / 2.0f));
	[_otherTabView addSubview:_otherTabImageView];
	[_otherTabImageView release];
	
	
	[tbc.tabBar addSubview:_peopleTabView];
	[tbc.tabBar addSubview:_organizationsTabView];
	[tbc.tabBar addSubview:_specialTabView];
	[tbc.tabBar addSubview:_otherTabView];
	
	[_peopleTabView release];
	[_organizationsTabView release];
	[_specialTabView release];
	[_otherTabView release];
	
	[self.window addSubview:tbc.view];
	self.window.rootViewController = tbc;
	
	// making sure the views are loaded during launch image time. This provides smoother navigation.
	UIView *auxView;
	auxView = peopleCampaignsVC.view;
	auxView = organizationsCampaignsVC.view;
	auxView = specialCampaignsVC.view;
	auxView = otherCampaignsVC.view;
	
    return YES;
}

void uncaughtExceptionHandler(NSException *exception)
{
    NSLog(@"CRASH: %@", exception);
    NSLog(@"Stack Trace: %@", [exception callStackSymbols]);
    // Internal error reporting
}

- (void)applicationWillResignActive:(UIApplication *)application
{
	// Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
	// Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
	// Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
	// If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
	// Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
	// Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
	// Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

- (UILabel *) newTabTitlelabelWithWidth:(CGFloat)width bottomY:(CGFloat)bottomY
{
	UILabel *label = [[UILabel alloc] initWithFrame:cgr(0.0f, 0.0f, width, 9.0f)];
	label.bottomY = bottomY;
	label.textColor = [UIColor whiteColor];
	label.backgroundColor = [UIColor clearColor];
	label.textAlignment = NSTextAlignmentCenter;
	label.font = [UIFont systemFontOfSize:9.0f];
	label.userInteractionEnabled = NO;
	
	return label;
}

#pragma mark - UITabBarControllerDelegate

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController
{
	if (viewController == [tabBarController.viewControllers objectAtIndex:0])
	{
		_peopleTabView.backgroundColor			= kSMSHColorLightBlue;
		_organizationsTabView.backgroundColor	= kSMSHColorDarkBlue;
		_specialTabView.backgroundColor			= kSMSHColorDarkBlue;
		_otherTabView.backgroundColor			= kSMSHColorDarkBlue;
	}
	else if (viewController == [tabBarController.viewControllers objectAtIndex:1])
	{
		_peopleTabView.backgroundColor			= kSMSHColorDarkBlue;
		_organizationsTabView.backgroundColor	= kSMSHColorLightBlue;
		_specialTabView.backgroundColor			= kSMSHColorDarkBlue;
		_otherTabView.backgroundColor			= kSMSHColorDarkBlue;
	}
	else if (viewController == [tabBarController.viewControllers objectAtIndex:2])
	{
		_peopleTabView.backgroundColor			= kSMSHColorDarkBlue;
		_organizationsTabView.backgroundColor	= kSMSHColorDarkBlue;
		_specialTabView.backgroundColor			= kSMSHColorLightBlue;
		_otherTabView.backgroundColor			= kSMSHColorDarkBlue;
	}
	else if (viewController == [tabBarController.viewControllers objectAtIndex:3])
	{
		_peopleTabView.backgroundColor			= kSMSHColorDarkBlue;
		_organizationsTabView.backgroundColor	= kSMSHColorDarkBlue;
		_specialTabView.backgroundColor			= kSMSHColorDarkBlue;
		_otherTabView.backgroundColor			= kSMSHColorLightBlue;
	}
}

@end
