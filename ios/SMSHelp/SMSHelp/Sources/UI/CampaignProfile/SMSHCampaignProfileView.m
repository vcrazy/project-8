//
//  SMSHCampaignProfileView.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 7/12/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHCampaignProfileView.h"
#import "UIImageView+WebCache.h"

#import "SMSHDataManager.h"

#import "SMSHCampaign.h"

#import "SMSHWebLinkArrow.h"

@interface SMSHCampaignProfileView ()
{
	SMSHCampaign *_campaign;
	
	id<SMSHCampaignProfileViewDelegate> _delegate;
}

@property (nonatomic, retain) SMSHCampaign *campaign;

@end


@implementation SMSHCampaignProfileView

@synthesize campaign = _campaign;
@synthesize delegate = _delegate;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self)
	{
        // Initialization code
		
		_campaign = nil;
		_delegate = nil;
		
		self.backgroundColor = [UIColor whiteColor];
    }
	
    return self;
}

- (id)initWithFrame:(CGRect)frame campaign:(SMSHCampaign *)campaign
{
	self = [self initWithFrame:frame];
	{
		self.campaign = campaign;
		
		// all sizes and distances in this view will be a part of a reference distance - the screen's width
		// this is an attempt to prepare a view which looks well on different sized screens (iPhone 6's bigger screens)
		CGFloat refLength = self.boundsWidth;
		CGFloat sendSMSButtonHeight = roundf(0.1555556f * refLength);
		CGFloat headerPaddingX = roundf(0.041666667f * refLength);
		CGFloat headerPaddingY = roundf(0.055555556f * refLength);
		
		UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:cgr(0.0f, 0.0f, self.boundsWidth, 0.0f)];
		scrollView.backgroundColor = self.backgroundColor;
		scrollView.opaque = YES;
		[self addSubview:scrollView];
		[scrollView release];
		
		UIView *header = [[UIView alloc] initWithFrame:cgr(0.0f, 0.0f, scrollView.boundsWidth, 0.0f)];
		header.backgroundColor = kSMSHColorDarkBlue;
		header.opaque = YES;
		[self addSubview:header];
		[header release];
		
		
		// avatar
		UIImageView *avatar = [[UIImageView alloc] initWithFrame:cgr(headerPaddingX, headerPaddingY, roundf(0.25f * refLength), roundf(0.25f * refLength))];
		avatar.layer.cornerRadius = avatar.height * 0.5f;
		avatar.layer.borderWidth = 2.0f;
		avatar.layer.borderColor = [UIColor whiteColor].CGColor;
		avatar.layer.masksToBounds = YES;
		avatar.backgroundColor = kSMSHColorPlaceholderImage;
		[header addSubview:avatar];
		[avatar release];
		
		if (self.campaign.pictureFilename.length > 0)
		{
			avatar.image = [UIImage imageWithContentsOfFile:self.campaign.pictureFilename];
		}
		else
		{
			[avatar sd_setImageWithURL:[NSURL URLWithString:self.campaign.pictureUrlString] 
					  placeholderImage:nil 
							   options:SDWebImageRetryFailed 
							 completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
								  if (!error)
								  {
									  [[SMSHDataManager sharedInstance] saveImage:image forCampaign:campaign];
								  }
								  else
								  {
									  DBG(@"Failed to retrieve image from network for url: %@", imageURL.absoluteString);
								  }
							  }];
		}
		
		// title
		UILabel *title = [[UILabel alloc] initWithFrame:cgr(avatar.endX + 1.0f + headerPaddingX, 0.0f, header.boundsWidth - avatar.width - 3 * headerPaddingX, 0.0f)];
		title.backgroundColor = header.backgroundColor;
		title.opaque = YES;
		title.textColor = [UIColor whiteColor];
		title.textAlignment = NSTextAlignmentLeft;
		title.font = [UIFont boldSystemFontOfSize:roundf(0.0472222f * refLength)];
		title.numberOfLines = 0;
		title.text = self.campaign.name;
		[header addSubview:title];
		[title release];
		
		CGSize s = [title.text boundingRectWithSize:cgs(title.boundsWidth, FLT_MAX) 
											options:(NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading) 
										 attributes:@{NSFontAttributeName: title.font} 
											context:nil].size;
		title.height = ceilf(s.height);
		
		// subtitle
		CGFloat subtitleFontSize = roundf(0.0527778f * refLength);
		UILabel *subtitle = [[UILabel alloc] initWithFrame:cgr(avatar.endX + 1.0f + headerPaddingX, 0.0f, header.boundsWidth - avatar.width - 3 * headerPaddingX, subtitleFontSize)];
		subtitle.backgroundColor = header.backgroundColor;
		subtitle.opaque = YES;
		subtitle.textColor = kSMSHColorLightBlue;
		subtitle.textAlignment = NSTextAlignmentLeft;
		subtitle.font = [UIFont boldSystemFontOfSize:subtitleFontSize];
		subtitle.text = self.campaign.subname;
		[header addSubview:subtitle];
		[subtitle release];
		
		// title and subtitle positions; header height
		CGFloat distBetweenTitleAndSubtitle = 0.0361111f * refLength;
		subtitle.originY = title.bottomY + 1.0f + distBetweenTitleAndSubtitle;
		CGFloat titlesTextHeight = subtitle.bottomY + 1.0f - title.originY;
		
		if (titlesTextHeight < avatar.height)
		{
			title.originY = avatar.originY + floorf((avatar.height - titlesTextHeight) / 2.0f);
		}
		else
		{
			title.originY = avatar.originY;
		}
		
		subtitle.originY = title.bottomY + 1.0f + distBetweenTitleAndSubtitle;
		
		header.height = MAX(avatar.bottomY, subtitle.bottomY) + 1.0f + headerPaddingY;
		
		
		/////////////////
		// start date, phone number, price
		/////////////////
		
		CGFloat separatorThickness = 2.0f; // TODO: decide if this thickness is to be an absolute value or a percent of the refLength
		CGFloat infoSectionItemTitleHeight   = roundf(0.1583333f * refLength);
		CGFloat infoSectionItemContentHeight = roundf(0.0777778f * refLength);
		CGFloat infoSectionItemWidth		 = floorf((scrollView.boundsWidth - 4 * separatorThickness) / 3.0f);
		
		CGFloat infoSectionItemIconWidth	= roundf(0.0694444f * refLength);
		CGFloat infoSectionItemIconY		= roundf(0.0166667f * refLength);
		
		CGFloat infoSectionItemTitleTextY			= roundf(0.1083333f * refLength);
		CGFloat infoSectionItemTitleTextFontSize	= roundf(0.0305556f * refLength);
		CGFloat infoSectionItemContentTextFontSize	= roundf(0.0388889f * refLength);
		
		UIView *topSeparator = [[UIView alloc] initWithFrame:cgr(0.0f, header.bottomY + 1.0f, scrollView.boundsWidth, separatorThickness)];
		topSeparator.backgroundColor = kSMSHColorSeparator;
		[self addSubview:topSeparator];
		[topSeparator release];
		
		// vertical separators will be constituted of 2 parts - upper and lower. The upper part will have a solid color while the lower will be colored with a gradient
		
		// leftmost separator
		UIView *initialUpperSeparator = [[UIView alloc] initWithFrame:cgr(0.0f, topSeparator.bottomY + 1.0f, separatorThickness, infoSectionItemTitleHeight)];
		initialUpperSeparator.backgroundColor = kSMSHColorSeparator;
		[self addSubview:initialUpperSeparator];
		[initialUpperSeparator release];
		
		UIView *initialLowerSeparator = [[UIView alloc] initWithFrame:cgr(0.0f, initialUpperSeparator.bottomY + 1.0f, separatorThickness, infoSectionItemContentHeight)];
		initialLowerSeparator.backgroundColor = kSMSHColorSeparator;
		[self addSubview:initialLowerSeparator];
		[initialLowerSeparator release];
		CAGradientLayer *gradient = [CAGradientLayer layer];
		gradient.frame = initialLowerSeparator.bounds;
		gradient.colors = [NSArray arrayWithObjects:(id)[kSMSHColorSeparator CGColor], (id)[scrollView.backgroundColor CGColor], nil];
		[initialLowerSeparator.layer insertSublayer:gradient atIndex:0];
		
		
		
		/// start date item
		UIView *startDateItemView = [[UIView alloc] initWithFrame:cgr(initialUpperSeparator.nextX, topSeparator.nextY, infoSectionItemWidth, infoSectionItemTitleHeight + infoSectionItemContentHeight)];
		startDateItemView.backgroundColor = scrollView.backgroundColor;
		startDateItemView.opaque = YES;
		[self addSubview:startDateItemView];
		[startDateItemView release];
		
		// title image
		UIImageView *iconStartDate = [[UIImageView alloc] initWithImage:[UIImage imageNamed:kSMSHImageIconStartDate]];
		iconStartDate.frame = cgr(0.0f, infoSectionItemIconY, MIN(iconStartDate.image.size.width, infoSectionItemIconWidth), MIN(iconStartDate.image.size.width, infoSectionItemIconWidth));
		iconStartDate.centerX = startDateItemView.boundsWidth / 2.0f;
		iconStartDate.backgroundColor = startDateItemView.backgroundColor;
		iconStartDate.opaque = YES;
		[startDateItemView addSubview:iconStartDate];
		[iconStartDate release];
		
		// title text
		UILabel *startDateTitleText = [[UILabel alloc] initWithFrame:cgr(0.0f, infoSectionItemTitleTextY, startDateItemView.boundsWidth, infoSectionItemTitleTextFontSize)];
		startDateTitleText.backgroundColor = startDateItemView.backgroundColor;
		startDateTitleText.opaque = YES;
		startDateTitleText.textAlignment = NSTextAlignmentCenter;
		startDateTitleText.textColor = kSMSHColorLightBlue;
		startDateTitleText.font = [UIFont boldSystemFontOfSize:infoSectionItemTitleTextFontSize];
		startDateTitleText.text = @"СТАРТИРАЛА";
		[startDateItemView addSubview:startDateTitleText];
		[startDateTitleText release];
		
		// content
		UILabel *startDateContent = [[UILabel alloc] initWithFrame:cgr(0.0f, infoSectionItemTitleHeight, startDateItemView.boundsWidth, infoSectionItemContentHeight)];
		startDateContent.backgroundColor = kSMSHColorLightBlue;
		startDateContent.opaque = YES;
		startDateContent.textAlignment = NSTextAlignmentCenter;
		startDateContent.textColor = [UIColor whiteColor];
		startDateContent.font = [UIFont boldSystemFontOfSize:infoSectionItemContentTextFontSize];
		[startDateItemView addSubview:startDateContent];
		[startDateContent release];
		
		NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
		[dateFormatter setDateFormat:@"dd MMM yyyy"];
		dateFormatter.locale = [NSLocale localeWithLocaleIdentifier:@"bg_BG"];
		startDateContent.text = [dateFormatter stringFromDate:self.campaign.startDate];
		
		
		/// separator after start date
		UIView *startDateUpperSeparator = [[UIView alloc] initWithFrame:cgr(startDateItemView.nextX, topSeparator.bottomY + 1.0f, separatorThickness, infoSectionItemTitleHeight)];
		startDateUpperSeparator.backgroundColor = kSMSHColorSeparator;
		[self addSubview:startDateUpperSeparator];
		[startDateUpperSeparator release];
		
		UIView *startDateLowerSeparator = [[UIView alloc] initWithFrame:cgr(startDateUpperSeparator.originX, startDateUpperSeparator.bottomY + 1.0f, separatorThickness, infoSectionItemContentHeight)];
		startDateLowerSeparator.backgroundColor = kSMSHColorSeparator;
		[self addSubview:startDateLowerSeparator];
		[startDateLowerSeparator release];
		gradient = [CAGradientLayer layer];
		gradient.frame = startDateLowerSeparator.bounds;
		gradient.colors = [NSArray arrayWithObjects:(id)[kSMSHColorSeparator CGColor], (id)[scrollView.backgroundColor CGColor], nil];
		[startDateLowerSeparator.layer insertSublayer:gradient atIndex:0];
		
		
		
		/// phone number
		UIView *phoneNumberItemView = [[UIView alloc] initWithFrame:cgr(startDateUpperSeparator.nextX, topSeparator.nextY, infoSectionItemWidth, infoSectionItemTitleHeight + infoSectionItemContentHeight)];
		phoneNumberItemView.backgroundColor = scrollView.backgroundColor;
		phoneNumberItemView.opaque = YES;
		[self addSubview:phoneNumberItemView];
		[phoneNumberItemView release];
		
		// title image
		UIImageView *iconPhoneNumber = [[UIImageView alloc] initWithImage:[UIImage imageNamed:kSMSHImageIconPhoneNumber]];
		iconPhoneNumber.frame = cgr(0.0f, infoSectionItemIconY, MIN(iconPhoneNumber.image.size.width, infoSectionItemIconWidth), MIN(iconPhoneNumber.image.size.width, infoSectionItemIconWidth));
		iconPhoneNumber.centerX = phoneNumberItemView.boundsWidth / 2.0f;
		iconPhoneNumber.backgroundColor = phoneNumberItemView.backgroundColor;
		iconPhoneNumber.opaque = YES;
		[phoneNumberItemView addSubview:iconPhoneNumber];
		[iconPhoneNumber release];
		
		// title text
		UILabel *phoneNumberTitleText = [[UILabel alloc] initWithFrame:cgr(0.0f, infoSectionItemTitleTextY, phoneNumberItemView.boundsWidth, infoSectionItemTitleTextFontSize)];
		phoneNumberTitleText.backgroundColor = phoneNumberItemView.backgroundColor;
		phoneNumberTitleText.opaque = YES;
		phoneNumberTitleText.textAlignment = NSTextAlignmentCenter;
		phoneNumberTitleText.textColor = kSMSHColorLightBlue;
		phoneNumberTitleText.font = [UIFont boldSystemFontOfSize:infoSectionItemTitleTextFontSize];
		phoneNumberTitleText.text = @"НОМЕР";
		[phoneNumberItemView addSubview:phoneNumberTitleText];
		[phoneNumberTitleText release];
		
		// content
		UILabel *phoneNumberContent = [[UILabel alloc] initWithFrame:cgr(0.0f, infoSectionItemTitleHeight, phoneNumberItemView.boundsWidth, infoSectionItemContentHeight)];
		phoneNumberContent.backgroundColor = kSMSHColorDarkBlue;
		phoneNumberContent.opaque = YES;
		phoneNumberContent.textAlignment = NSTextAlignmentCenter;
		phoneNumberContent.textColor = [UIColor whiteColor];
		phoneNumberContent.font = [UIFont boldSystemFontOfSize:infoSectionItemContentTextFontSize];
		phoneNumberContent.text = [NSString stringWithFormat:@"%lld", self.campaign.smsNumber];
		[phoneNumberItemView addSubview:phoneNumberContent];
		[phoneNumberContent release];
		
		// alternative send button - the phone number will also have a send button
		UIButton *phoneNumberSendButton = [[UIButton alloc] initWithFrame:phoneNumberItemView.frame];
		[phoneNumberSendButton addTarget:self action:@selector(sendButtonWasPressed:) forControlEvents:UIControlEventTouchUpInside];
		[phoneNumberItemView.superview addSubview:phoneNumberSendButton];
		[phoneNumberSendButton release];
		
		
		
		/// separator after phone number
		UIView *phoneNumberUpperSeparator = [[UIView alloc] initWithFrame:cgr(phoneNumberItemView.nextX, topSeparator.bottomY + 1.0f, separatorThickness, infoSectionItemTitleHeight)];
		phoneNumberUpperSeparator.backgroundColor = kSMSHColorSeparator;
		[self addSubview:phoneNumberUpperSeparator];
		[phoneNumberUpperSeparator release];
		
		UIView *phoneNumberLowerSeparator = [[UIView alloc] initWithFrame:cgr(phoneNumberUpperSeparator.originX, phoneNumberUpperSeparator.bottomY + 1.0f, separatorThickness, infoSectionItemContentHeight)];
		phoneNumberLowerSeparator.backgroundColor = kSMSHColorSeparator;
		[self addSubview:phoneNumberLowerSeparator];
		[phoneNumberLowerSeparator release];
		gradient = [CAGradientLayer layer];
		gradient.frame = phoneNumberLowerSeparator.bounds;
		gradient.colors = [NSArray arrayWithObjects:(id)[kSMSHColorSeparator CGColor], (id)[scrollView.backgroundColor CGColor], nil];
		[phoneNumberLowerSeparator.layer insertSublayer:gradient atIndex:0];
		
		
		
		/// price
		UIView *priceItemView = [[UIView alloc] 
								 initWithFrame:cgr(phoneNumberUpperSeparator.nextX, 
												   topSeparator.nextY, 
												   scrollView.boundsWidth - 2 * infoSectionItemWidth - 4 * separatorThickness, 
												   infoSectionItemTitleHeight + infoSectionItemContentHeight)];
		priceItemView.backgroundColor = scrollView.backgroundColor;
		priceItemView.opaque = YES;
		[self addSubview:priceItemView];
		[priceItemView release];
		
		// title image
		UIImageView *iconPrice = [[UIImageView alloc] initWithImage:[UIImage imageNamed:kSMSHImageIconPrice]];
		iconPrice.frame = cgr(0.0f, infoSectionItemIconY, MIN(iconPhoneNumber.image.size.width, infoSectionItemIconWidth), MIN(iconPhoneNumber.image.size.width, infoSectionItemIconWidth));
		iconPrice.centerX = priceItemView.boundsWidth / 2.0f;
		iconPrice.backgroundColor = priceItemView.backgroundColor;
		iconPrice.opaque = YES;
		[priceItemView addSubview:iconPrice];
		[iconPrice release];
		
		// title text
		UILabel *priceTitleText = [[UILabel alloc] initWithFrame:cgr(0.0f, infoSectionItemTitleTextY, priceItemView.boundsWidth, infoSectionItemTitleTextFontSize)];
		priceTitleText.backgroundColor = priceItemView.backgroundColor;
		priceTitleText.opaque = YES;
		priceTitleText.textAlignment = NSTextAlignmentCenter;
		priceTitleText.textColor = kSMSHColorLightBlue;
		priceTitleText.font = [UIFont boldSystemFontOfSize:infoSectionItemTitleTextFontSize];
		priceTitleText.text = @"СТОЙНОСТ";
		[priceItemView addSubview:priceTitleText];
		[priceTitleText release];
		
		// content
		UILabel *priceContent = [[UILabel alloc] initWithFrame:cgr(0.0f, infoSectionItemTitleHeight, priceItemView.boundsWidth, infoSectionItemContentHeight)];
		priceContent.backgroundColor = kSMSHColorLightBlue;
		priceContent.opaque = YES;
		priceContent.textAlignment = NSTextAlignmentCenter;
		priceContent.textColor = [UIColor whiteColor];
		priceContent.font = [UIFont boldSystemFontOfSize:infoSectionItemContentTextFontSize];
		priceContent.text = [NSString stringWithFormat:(self.campaign.price - floor(self.campaign.price) < 0.01 ? @"%.0f лв" : @"%.2f лв"), self.campaign.price];
		[priceItemView addSubview:priceContent];
		[priceContent release];
		
		
		/// separator after price
		UIView *priceUpperSeparator = [[UIView alloc] initWithFrame:cgr(priceItemView.nextX, topSeparator.bottomY + 1.0f, separatorThickness, infoSectionItemTitleHeight)];
		priceUpperSeparator.backgroundColor = kSMSHColorSeparator;
		[self addSubview:priceUpperSeparator];
		[priceUpperSeparator release];
		
		UIView *priceLowerSeparator = [[UIView alloc] initWithFrame:cgr(priceUpperSeparator.originX, priceUpperSeparator.bottomY + 1.0f, separatorThickness, infoSectionItemContentHeight)];
		priceLowerSeparator.backgroundColor = kSMSHColorSeparator;
		[self addSubview:priceLowerSeparator];
		[priceLowerSeparator release];
		gradient = [CAGradientLayer layer];
		gradient.frame = priceLowerSeparator.bounds;
		gradient.colors = [NSArray arrayWithObjects:(id)[kSMSHColorSeparator CGColor], (id)[scrollView.backgroundColor CGColor], nil];
		[priceLowerSeparator.layer insertSublayer:gradient atIndex:0];
		
		
		
		///////////************///////////
		// description
		///////////************///////////
		CGFloat descriptionPaddingX = roundf(0.1166667f * refLength);
		CGFloat descriptionPaddingY = roundf(0.0833333f * refLength);
		
		scrollView.originY = startDateItemView.nextY;
		scrollView.height = self.boundsHeight - sendSMSButtonHeight - scrollView.originY;
		
		UILabel *description = [[UILabel alloc] initWithFrame:cgr(descriptionPaddingX, descriptionPaddingY, scrollView.boundsWidth - 2 * descriptionPaddingX, 0.0f)];
		description.backgroundColor = scrollView.backgroundColor;
		description.opaque = YES;
		description.textAlignment = NSTextAlignmentLeft;
		description.textColor = [UIColor colorWithWhite:153.0f/255.0f alpha:1.0f];
		description.numberOfLines = 0;
		description.font = [UIFont systemFontOfSize:roundf(0.0388889f * refLength)];
		description.text = self.campaign.text;
		[scrollView addSubview:description];
		[description release];
		
		s = [description.text boundingRectWithSize:cgs(description.boundsWidth, FLT_MAX) options:(NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading) attributes:@{NSFontAttributeName: description.font} context:nil].size;
		description.height = ceilf(s.height);
		
		scrollView.contentSize = cgs(scrollView.boundsWidth, description.nextY + descriptionPaddingY);
		
		
		// web link
		if (self.campaign.urlString.length > 0 && [[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:self.campaign.urlString]])
		{
			CGFloat webLinkFontSize = roundf(0.0388889f * refLength);
			UILabel *webLinkLabel = [[UILabel alloc] initWithFrame:cgr(descriptionPaddingX, description.nextY + roundf(0.0416667f * refLength), 0.0f, webLinkFontSize + roundf(0.0277778f * refLength))];
			webLinkLabel.backgroundColor = scrollView.backgroundColor;
			webLinkLabel.opaque = YES;
			webLinkLabel.textAlignment = NSTextAlignmentLeft;
			webLinkLabel.textColor = kSMSHColorDarkBlue;
			webLinkLabel.numberOfLines = 1;
			webLinkLabel.font = [UIFont systemFontOfSize:webLinkFontSize];
			webLinkLabel.text = @"Линк към страницата";
			webLinkLabel.userInteractionEnabled = YES;
			[scrollView addSubview:webLinkLabel];
			[webLinkLabel release];
			
			s = [webLinkLabel.text boundingRectWithSize:cgs(scrollView.boundsWidth - 2 * descriptionPaddingX, webLinkLabel.boundsHeight) options:0 attributes:@{NSFontAttributeName: webLinkLabel.font} context:nil].size;
			webLinkLabel.width = ceilf(s.width);
			
			UITapGestureRecognizer *tgr = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(webLinkWasTapped:)];
			[webLinkLabel addGestureRecognizer:tgr];
			[tgr release];
			
			SMSHWebLinkArrow *arrow = [[SMSHWebLinkArrow alloc] initWithFrame:cgr(webLinkLabel.nextX + roundf(0.0277778f * refLength), 0.0f, roundf(0.0125f * refLength), roundf(0.0222222f * refLength))];
			arrow.center = cgp(arrow.center.x, webLinkLabel.center.y + 1.0f);
			arrow.backgroundColor = scrollView.backgroundColor;
			arrow.opaque = YES;
			[scrollView addSubview:arrow];
			[arrow release];
			
			scrollView.contentSize = cgs(scrollView.boundsWidth, webLinkLabel.nextY + roundf(0.0416667f * refLength));
		}
		
		CGFloat sendButtonPadding = 4.0f;
		UIButton *sendButton = [[UIButton alloc] initWithFrame:cgr(sendButtonPadding, 0.0f, self.boundsWidth - 2 * sendButtonPadding, sendSMSButtonHeight - 2 * sendButtonPadding)];
		sendButton.nextY = self.boundsHeight - sendButtonPadding;
		sendButton.backgroundColor = kSMSHColorLightBlue;
		sendButton.titleLabel.font = [UIFont systemFontOfSize:roundf(0.0472222f * refLength)];
		sendButton.titleLabel.textAlignment = NSTextAlignmentCenter;
		[sendButton setTitle:@"ПОМОГНИ" forState:UIControlStateNormal];
		[sendButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
		[sendButton addTarget:self action:@selector(sendButtonWasPressed:) forControlEvents:UIControlEventTouchUpInside];
		[self addSubview:sendButton];
		[sendButton release];
		
		
		// scroll view top and bottom overlay gradients
		UIImageView *topGradient = [[UIImageView alloc] initWithImage:[UIImage imageNamed:kSMSHGradientWhite]];
		topGradient.width = scrollView.width;
		topGradient.height = roundf(topGradient.width * 0.04f);
		topGradient.position = scrollView.position;
		[scrollView.superview insertSubview:topGradient aboveSubview:scrollView];
		[topGradient release];
		
		UIImageView *bottomGradient = [[UIImageView alloc] initWithImage:[UIImage imageNamed:kSMSHGradientWhite]];
		bottomGradient.width = scrollView.width;
		bottomGradient.height = roundf(bottomGradient.width * 0.04f);
		bottomGradient.originX = scrollView.originX;
		bottomGradient.bottomY = scrollView.bottomY;
		bottomGradient.transform = CGAffineTransformMakeScale(1.0f, -1.0f);
		[scrollView.superview insertSubview:bottomGradient aboveSubview:scrollView];
		[bottomGradient release];
	}
	
	return self;
}

- (void) dealloc
{
	[_campaign release], _campaign = nil;
	
	[super dealloc];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

- (void) webLinkWasTapped:(UITapGestureRecognizer *)tgr
{
	if ([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:self.campaign.urlString]])
	{
		[[UIApplication sharedApplication] openURL:[NSURL URLWithString:self.campaign.urlString]];
	}
}

- (void) sendButtonWasPressed:(UIButton *)sender
{
	if (_delegate && [_delegate respondsToSelector:@selector(sendButtonWasPressedInCampaignProfileView:)])
	{
		[_delegate sendButtonWasPressedInCampaignProfileView:self];
	}
}

@end
