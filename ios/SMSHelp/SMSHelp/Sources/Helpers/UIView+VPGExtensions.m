//
//  UIView+VPGExtensions.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/29/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "UIView+VPGExtensions.h"

@implementation UIView (VPGExtensions)

- (CGPoint) position
{
	return CGPointMake(self.frame.origin.x, self.frame.origin.y);
}

- (void) setPosition:(CGPoint)position
{
	self.frame = CGRectMake(position.x, position.y, self.frame.size.width, self.frame.size.height);
}

- (CGFloat) originX
{
	return self.frame.origin.x;
}

- (void) setOriginX:(CGFloat)originX
{
	self.frame = CGRectMake(originX, self.frame.origin.y, self.frame.size.width, self.frame.size.height);
}

- (CGFloat) endX
{
	return self.frame.origin.x + self.frame.size.width - 1;
}

- (void) setEndX:(CGFloat)endX
{
	self.frame = CGRectMake(endX - self.frame.size.width + 1, self.frame.origin.y, self.frame.size.width, self.frame.size.height);
}

- (CGFloat) centerX
{
	return self.frame.origin.x + roundf(self.frame.size.width / 2.0f);
}

- (void) setCenterX:(CGFloat)centerX
{
	self.frame = cgr(centerX - roundf(self.frame.size.width / 2.0f), self.frame.origin.y, self.frame.size.width, self.frame.size.height);
}

- (CGFloat) originY
{
	return self.frame.origin.y;
}

- (void) setOriginY:(CGFloat)originY
{
	self.frame = CGRectMake(self.frame.origin.x, originY, self.frame.size.width, self.frame.size.height);
}

- (CGFloat) bottomY
{
	return self.frame.origin.y + self.frame.size.height - 1;
}

- (void) setBottomY:(CGFloat)bottomY
{
	self.frame = CGRectMake(self.frame.origin.x, bottomY - self.frame.size.height + 1, self.frame.size.width, self.frame.size.height);
}

- (CGFloat) nextX
{
	return self.frame.origin.x + self.frame.size.width;
}

- (void) setNextX:(CGFloat)nextX
{
	self.frame = CGRectMake(nextX - self.frame.size.width, self.frame.origin.y, self.frame.size.width, self.frame.size.height);
}

- (CGFloat) nextY
{
	return self.frame.origin.y + self.frame.size.height;
}

- (void) setNextY:(CGFloat)nextY
{
	self.frame = CGRectMake(self.frame.origin.x, nextY - self.frame.size.height, self.frame.size.width, self.frame.size.height);
}

- (CGSize) size
{
	return CGSizeMake(self.frame.size.width, self.frame.size.height);
}

- (void) setSize:(CGSize)size
{
	self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, size.width, size.height);
}

- (CGFloat) width
{
	return self.frame.size.width;
}

- (void) setWidth:(CGFloat)width
{
	self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, width, self.frame.size.height);
}

- (CGFloat) height
{
	return self.frame.size.height;
}

- (void) setHeight:(CGFloat)height
{
	self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, self.frame.size.width, height);
}

- (CGFloat) boundsWidth
{
	return self.bounds.size.width;
}

- (CGFloat) boundsHeight
{
	return self.bounds.size.height;
}

- (void) removeAllSubviews
{
	NSArray *subviews = [[NSArray alloc] initWithArray:self.subviews];
	
	for (UIView *v in subviews)
	{
		[v removeFromSuperview];
	}
	
#if !__has_feature(objc_arc)
	[subviews release];
#endif
}

@end
