//
//  UIView+VPGExtensions.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/29/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIView (VPGExtensions)

@property (nonatomic, assign) CGPoint position;
@property (nonatomic, assign) CGFloat originX;
@property (nonatomic, assign) CGFloat endX;
@property (nonatomic, assign) CGFloat centerX;
@property (nonatomic, assign) CGFloat originY;
@property (nonatomic, assign) CGFloat bottomY;
@property (nonatomic, assign) CGFloat nextX; // the x value of the first point after the view (horizontally), in the coordinate system of the superview
@property (nonatomic, assign) CGFloat nextY; // the y value of the first point after the view (vertically), in the coordinate system of the superview
@property (nonatomic, assign) CGSize size;
@property (nonatomic, assign) CGFloat width;
@property (nonatomic, assign) CGFloat height;
@property (nonatomic, readonly) CGFloat boundsWidth;
@property (nonatomic, readonly) CGFloat boundsHeight;

- (void) removeAllSubviews;

@end
