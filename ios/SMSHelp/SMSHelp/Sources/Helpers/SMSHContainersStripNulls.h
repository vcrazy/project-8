//
//  SMSHContainersStripNulls.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/21/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSMutableArray (StripNulls)

- (void)stripNullValues;

@end


@interface NSMutableDictionary (StripNulls)

- (void)stripNullValues;

@end
