//
//  SMSHCampaign.h
//  SMSHelp
//
//  Created by Vladimir Goranov on 5/19/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SMSHCampaign : NSObject

@property (nonatomic, retain) NSString *campaignId;
@property (nonatomic, retain) NSString *name;
@property (nonatomic, retain) NSString *subname; // this is ususally the DMS text, except for non-DMS campaigns
@property (nonatomic, retain) NSString *type;
@property (nonatomic, retain) NSString *text;
@property (nonatomic, assign) double price; // the amount of currency the user pays for the sms (in BGN). This is not the sum that will actually be donated
@property (nonatomic, retain) NSString *pictureUrlString;
@property (nonatomic, copy) NSString *pictureFilename; // the filename of the picture on disk. If not nil this must be used instead of pictureUrlString
@property (nonatomic, retain) NSString *urlString;
@property (nonatomic, retain) NSString *smsText; // the text that has to be in the sms message
@property (nonatomic, assign) long long smsNumber; // the number the sms has to be sent to
@property (nonatomic, retain) NSDate *startDate;
@property (nonatomic, retain) NSString *status; // this is used only when updatig the database
@property (nonatomic, assign) CGFloat cellHeight; // the cached cell height for this campaign

+ (SMSHCampaign *) campaignWithDictionary:(NSDictionary *)dictionary forKey:(NSString *)key;

@end
