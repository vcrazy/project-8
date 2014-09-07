//
//  SMSHContainersStripNulls.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/21/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHContainersStripNulls.h"

@implementation NSMutableArray (StripNulls)

- (void)stripNullValues
{
	int i;
    for (i = (int)[self count] - 1; i >= 0; --i)
    {
        id value = [self objectAtIndex:i];
        if (value == [NSNull null])
        {
            [self removeObjectAtIndex:i];
        }
        else if ([value isKindOfClass:[NSArray class]] ||
                 [value isKindOfClass:[NSDictionary class]])
        {
            if (![value respondsToSelector:@selector(setObject:forKey:)] &&
                ![value respondsToSelector:@selector(addObject:)])
            {
                value = [value mutableCopy];
                [self replaceObjectAtIndex:i withObject:value];
				[value release];
            }
            [value stripNullValues];
        }
    }
}

@end


@implementation NSMutableDictionary (StripNulls)

- (void)stripNullValues
{
    for (NSString *key in [self allKeys])
    {
        id value = [self objectForKey:key];
        if (value == [NSNull null])
        {
            [self removeObjectForKey:key];
        }
        else if ([value isKindOfClass:[NSArray class]] ||
                 [value isKindOfClass:[NSDictionary class]])
        {
            if (![value respondsToSelector:@selector(setObject:forKey:)] &&
                ![value respondsToSelector:@selector(addObject:)])
            {
                value = [value mutableCopy];
                [self setObject:value forKey:key];
				[value release];
            }
            [value stripNullValues];
        }
    }
}

@end
