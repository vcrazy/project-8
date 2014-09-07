//
//  SMSHCampaignsListHUD.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 7/20/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHCampaignsListHUD.h"

//static CGFloat normalizedLengthFromLength(CGFloat length)
//{
//	return roundf([UIScreen mainScreen].bounds.size.width * length / (720.0f / 2.0f));
//}

@interface SMSHCampaignsListHUD ()
{
	UIActivityIndicatorView *_spinner;
	
	UILabel *_textLabel;
	UIButton *_retryButton;
	UIButton *_closeButton;
	
	id<SMSHCampaignsListHUDDelegate> _delegate;
}

@end


@implementation SMSHCampaignsListHUD

@synthesize delegate = _delegate;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:cgr(frame.origin.x, frame.origin.y, frame.size.width, 0.0f)];
    if (self)
	{
        // Initialization code
		
		self.backgroundColor = [UIColor colorWithWhite:247.0f/255.0f alpha:1.0f];
		
		_delegate = nil;
		
		CGFloat paddingX = normalizedLengthFromLength(12.0f);
		CGFloat paddingY = normalizedLengthFromLength(10.0f);
		
		_textLabel = [[UILabel alloc] initWithFrame:cgr(paddingX, paddingY, self.boundsWidth - 2 * paddingX, 0.0f)];
		_textLabel.backgroundColor = self.backgroundColor;
		_textLabel.opaque = YES;
		_textLabel.textColor = kSMSHColorDarkBlue;
		_textLabel.textAlignment = NSTextAlignmentCenter;
		_textLabel.font = [UIFont systemFontOfSize:normalizedLengthFromLength(14.0f)];
		_textLabel.numberOfLines = 0;
		_textLabel.text = @"Имаше проблем с изтеглянето на новите кампании. Моля, опитайте отново по-късно.";
		[self addSubview:_textLabel];
		[_textLabel release];
		
		CGSize s = [_textLabel.text boundingRectWithSize:cgs(_textLabel.boundsWidth, FLT_MAX) 
												 options:(NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading) 
											  attributes:@{NSFontAttributeName: _textLabel.font} 
												 context:nil].size;
		_textLabel.height = ceilf(s.height);
		
		_retryButton = [[UIButton alloc] initWithFrame:cgr(paddingX, _textLabel.nextY + paddingY, roundf((self.boundsWidth - 3 * paddingX) / 2.0f), normalizedLengthFromLength(25.0f))];
		_retryButton.backgroundColor = kSMSHColorDarkBlue;
		_retryButton.titleLabel.font = [UIFont boldSystemFontOfSize:normalizedLengthFromLength(11.0f)];
		[_retryButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
		[_retryButton setTitle:@"ОПИТАЙ ПАК" forState:UIControlStateNormal];
		[_retryButton addTarget:self action:@selector(retryButtonWasPressed:) forControlEvents:UIControlEventTouchUpInside];
		[self addSubview:_retryButton];
		[_retryButton release];
		
		_closeButton = [[UIButton alloc] initWithFrame:cgr(_retryButton.nextX + paddingX, _retryButton.originY, _retryButton.width, _retryButton.height)];
		_closeButton.backgroundColor = _retryButton.backgroundColor;
		_closeButton.titleLabel.font = _retryButton.titleLabel.font;
		[_closeButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
		[_closeButton setTitle:@"СКРИЙ" forState:UIControlStateNormal];
		[_closeButton addTarget:self action:@selector(closeButtonWasPressed:) forControlEvents:UIControlEventTouchUpInside];
		[self addSubview:_closeButton];
		[_closeButton release];
		
		super.frame = cgr(super.frame.origin.x, super.frame.origin.y, super.frame.size.width, _retryButton.nextY + paddingY);
		
		_spinner = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
		_spinner.center = cgp(roundf(self.boundsWidth / 2.0f), roundf(self.boundsHeight / 2.0f));
		[self addSubview:_spinner];
		[_spinner release];
		
		UIView *separator = [[UIView alloc] initWithFrame:cgr(0.0f, self.boundsHeight - 0.5f, self.boundsWidth, 0.5f)];
		separator.backgroundColor = kSMSHColorSeparator;
		[self addSubview:separator];
		[separator release];
    }
	
    return self;
}

- (id) initWithWidth:(CGFloat)width
{
	if ((self = [self initWithFrame:cgr(0.0f, 0.0f, width, 0.0f)]))
	{
		
	}
	
	return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/


#pragma mark - properties

- (void) setFrame:(CGRect)frame
{
	[super setFrame:cgr(frame.origin.x, frame.origin.y, self.width, self.height)];
}

#pragma mark - 
// I know this can be done shortly with methods like setSpinnerHidden:(BOOL), 
// but I prefer using the 'hide' and 'show' words explicitly

- (void) hideSpinner
{
	[_spinner stopAnimating];
	_spinner.hidden = YES;
}

- (void) showSpinner
{
	[self hideAll];
	
	_spinner.hidden = NO;
	[_spinner startAnimating];
}

- (void) hideErrorInfo
{
	_textLabel.hidden = YES;
	_retryButton.hidden = YES;
	_closeButton.hidden = YES;
}

- (void) showErrorInfo
{
	[self hideAll];
	
	_textLabel.hidden = NO;
	_retryButton.hidden = NO;
	_closeButton.hidden = NO;
}

- (void) hideAll
{
	[self hideSpinner];
	[self hideErrorInfo];
}

#pragma mark - 

- (void) retryButtonWasPressed:(UIButton *)sender
{
	if (_delegate && [_delegate respondsToSelector:@selector(retryButtonWasPressedInHUD:)])
	{
		[_delegate retryButtonWasPressedInHUD:self];
	}
}

- (void) closeButtonWasPressed:(UIButton *)sender
{
	if (_delegate && [_delegate respondsToSelector:@selector(closeButtonWasPressedInHUD:)])
	{
		[_delegate closeButtonWasPressedInHUD:self];
	}
}

@end
