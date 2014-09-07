//
//  SMSHWebLinkArrow.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 7/19/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHWebLinkArrow.h"

@implementation SMSHWebLinkArrow

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self)
	{
        // Initialization code
		
    }
	
    return self;
}


// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
	
	rect = self.bounds;
	
	CGContextRef context = UIGraphicsGetCurrentContext();
	
	CGContextMoveToPoint(context, 0.0f, 0.0f);
	CGContextAddLineToPoint(context, 0.0f, rect.size.height);
	CGContextAddLineToPoint(context, rect.size.width, rect.size.height / 2.0f);
	CGContextAddLineToPoint(context, 0.0f, 0.0f);
	CGContextClosePath(context);
	
	[kSMSHColorLightBlue setFill];
	CGContextFillPath(context);
}


@end
