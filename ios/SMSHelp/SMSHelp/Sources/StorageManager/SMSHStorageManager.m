//
//  SMSHStorageManager.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 5/19/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHStorageManager.h"
#import "SynthesizeSingleton.h"
#import <sqlite3.h>
#import <CommonCrypto/CommonDigest.h>
#import "EGODatabase.h"
#import "EGODatabaseRequest.h"

#import "SMSHCampaign.h"

static NSString * const kSMSHStorageManagerDatabaseFilename = @"smshelp.db";
static NSString * const kSMSHStorageManagerCampaignsTableName = @"campaigns";

@interface SMSHStorageManager ()
{
	EGODatabase *_smsHelpDB;
	NSString *_databasePath;
	
}

@end

@implementation SMSHStorageManager

SYNTHESIZE_SINGLETON_FOR_CLASS(SMSHStorageManager, Instance);

- (id) init
{
	if ((self = [super init]))
	{
		// making sqlite threadsafe
		sqlite3_shutdown();
		sqlite3_config(SQLITE_CONFIG_SERIALIZED);
		sqlite3_initialize();
		
		// Get the documents directory
		NSArray *dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
		
		NSString *docsDir = [dirPaths objectAtIndex:0];
		
        // Build the path to the database file
		_databasePath = [[NSString alloc] initWithString: [docsDir stringByAppendingPathComponent:kSMSHStorageManagerDatabaseFilename]];
		
        NSFileManager *filemgr = [NSFileManager defaultManager];
		
        if ([filemgr fileExistsAtPath: _databasePath ] == NO) // The DB does not exist
        {
			_smsHelpDB = [[EGODatabase databaseWithPath:_databasePath] retain];
			
			if ([_smsHelpDB open]) // DB was created successfully
			{
				NSString *createSQL = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (inc INTEGER PRIMARY KEY AUTOINCREMENT, id TEXT, name TEXT, subname TEXT, type TEXT, description TEXT, price REAL, pictureUrlString TEXT, pictureDiskpath TEXT, urlString TEXT, smsText TEXT, smsNumber INTEGER, startDate REAL)", kSMSHStorageManagerCampaignsTableName];
				
				// TODO: try to add support for int_64 in EGODatabase.
				
				if (NO == [_smsHelpDB executeUpdate:createSQL])
				{
					DBG(@"Failed to create table '%@'.", kSMSHStorageManagerCampaignsTableName);
				}
			}
			else
			{
				DBG(@"Failed to open/create database");
			}
        }
		else
		{
			_smsHelpDB = [[EGODatabase databaseWithPath:_databasePath] retain];
		}
	}
	
	return self;
}

NSString * md5( NSString *str )
{
	const char *cStr = [str UTF8String];
	unsigned char result[CC_MD5_DIGEST_LENGTH];
	CC_MD5( cStr, (uint32_t)strlen(cStr), result );
	return [NSString 
			stringWithFormat: @"%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X",
			result[0], result[1],
			result[2], result[3],
			result[4], result[5],
			result[6], result[7],
			result[8], result[9],
			result[10], result[11],
			result[12], result[13],
			result[14], result[15]
			];
}


#pragma mark - 

/*!
 * Inserts campaigns in the database.
 */
- (void) addCampaignsToCache:(NSArray *)campaigns
{
	if ([_smsHelpDB open])
	{
		// add campaigns using a prepared statement
		NSString *insertSQL = [NSString stringWithFormat: @"INSERT INTO %@ (id, name, subname, type, description, price, pictureUrlString, urlString, smsText, smsNumber, startDate) VALUES (@id, @name, @subname, @type, @description, @price, @pictureUrlString, @urlString, @smsText, @smsNumber, @startDate)", kSMSHStorageManagerCampaignsTableName];
		
		sqlite3_stmt *statement;
		
		const char *insert_query = [insertSQL UTF8String];
		
		if (sqlite3_prepare_v2(_smsHelpDB.sqliteHandle, insert_query, -1, &statement, NULL) == SQLITE_OK)
		{
			sqlite3_exec(_smsHelpDB.sqliteHandle, "BEGIN TRANSACTION", NULL, NULL, NULL);
			
			for (SMSHCampaign *c in campaigns)
			{
				sqlite3_bind_text(statement, 1, [c.campaignId UTF8String], -1, SQLITE_TRANSIENT);
				sqlite3_bind_text(statement, 2, [c.name UTF8String], -1, SQLITE_TRANSIENT);
				sqlite3_bind_text(statement, 3, [c.subname UTF8String], -1, SQLITE_TRANSIENT);
				sqlite3_bind_text(statement, 4, [c.type UTF8String], -1, SQLITE_TRANSIENT);
				sqlite3_bind_text(statement, 5, [c.text UTF8String], -1, SQLITE_TRANSIENT);
				sqlite3_bind_double(statement, 6, c.price);
				sqlite3_bind_text(statement, 7, [c.pictureUrlString UTF8String], -1, SQLITE_TRANSIENT);
				sqlite3_bind_text(statement, 8, [c.urlString UTF8String], -1, SQLITE_TRANSIENT);
				sqlite3_bind_text(statement, 9, [c.smsText UTF8String], -1, SQLITE_TRANSIENT);
				sqlite3_bind_int64(statement, 10, c.smsNumber);
				sqlite3_bind_double(statement, 11, [c.startDate timeIntervalSince1970]);
				
				sqlite3_step(statement);
				
				sqlite3_clear_bindings(statement);
				sqlite3_reset(statement);
			}
			
			int result = sqlite3_exec(_smsHelpDB.sqliteHandle, "END TRANSACTION", NULL, NULL, NULL);
			
			sqlite3_finalize(statement);
			
			if (SQLITE_OK == result)
			{
				// do nothing
			}
			else
			{
				DBG(@"addCampaignsToCache: Inserting campaigns failed.");
			}
		}
	}
}

/*!
 * Queries the database for campaigns by type. Type examples: "people", "organizations", ...
 */
- (NSArray *) getCampaignsForType:(NSString *)type
{
	NSMutableArray *campaigns = [[NSMutableArray alloc] init];
	
	if ([_smsHelpDB open])
	{
		NSString *querySQL = [NSString stringWithFormat: @"SELECT id, name, subname, type, description, price, pictureUrlString, pictureDiskpath, urlString, smsText, smsNumber, startDate FROM %@ WHERE type=\"%@\" ORDER BY startDate DESC", kSMSHStorageManagerCampaignsTableName, type];
		
		EGODatabaseResult *result = [_smsHelpDB executeQuery:querySQL];
		
		if (SQLITE_OK == result.errorCode)
		{
			for (EGODatabaseRow *row in result)
			{
				SMSHCampaign *c = [[SMSHCampaign alloc] init];
				
				c.campaignId			= [row stringForColumnIndex:0];
				c.name					= [row stringForColumnIndex:1];
				c.subname				= [row stringForColumnIndex:2];
				c.type					= [row stringForColumnIndex:3];
				c.text					= [row stringForColumnIndex:4];
				c.price					= [row doubleForColumnIndex:5];
				c.pictureUrlString		= [row stringForColumnIndex:6];
				c.pictureFilename		= [row stringForColumnIndex:7];
				c.urlString				= [row stringForColumnIndex:8];
				c.smsText				= [row stringForColumnIndex:9];
				c.smsNumber				= [row intForColumnIndex:10];
				c.startDate				= [NSDate dateWithTimeIntervalSince1970:[row doubleForColumnIndex:11]];
				
				[campaigns addObject:c];
				[c release];
			}
		}
	}
	
	return [campaigns autorelease];
}

- (SMSHCampaign *) getCampaignWithId:(NSString *)campaignId
{
	SMSHCampaign *campaign = nil;
	
	if ([_smsHelpDB open])
	{
		NSString *querySQL = [NSString stringWithFormat: @"SELECT id, name, subname, type, description, price, pictureUrlString, pictureDiskpath, urlString, smsText, smsNumber, startDate FROM %@ WHERE id=\"%@\"", kSMSHStorageManagerCampaignsTableName, campaignId];
		
		EGODatabaseResult *result = [_smsHelpDB executeQuery:querySQL];
		
		if (SQLITE_OK == result.errorCode)
		{
			campaign = [[SMSHCampaign alloc] init];
			
			for (EGODatabaseRow *row in result)
			{	
				campaign.campaignId				= [row stringForColumnIndex:0];
				campaign.name					= [row stringForColumnIndex:1];
				campaign.subname				= [row stringForColumnIndex:2];
				campaign.type					= [row stringForColumnIndex:3];
				campaign.text					= [row stringForColumnIndex:4];
				campaign.price					= [row doubleForColumnIndex:5];
				campaign.pictureUrlString		= [row stringForColumnIndex:6];
				campaign.pictureFilename		= [row stringForColumnIndex:7];
				campaign.urlString				= [row stringForColumnIndex:8];
				campaign.smsText				= [row stringForColumnIndex:9];
				campaign.smsNumber				= [row intForColumnIndex:10];
				campaign.startDate				= [NSDate dateWithTimeIntervalSince1970:[row doubleForColumnIndex:11]];
			}
		}
	}
	
	return [campaign autorelease];
}

/*!
 * Saves an image to disk and stores the path to it in the database for the corresponding campaign
 */
- (BOOL) savePicture:(UIImage *)image forCampaign:(SMSHCampaign *)campaign
{
	NSString *urlString = campaign.pictureUrlString;
	
	if (NO == is_string_valid(trim_string(urlString)))
	{
		return NO;
	}
	
	// Remove the previous image for this campaing from the disk.
	if ([_smsHelpDB open])
	{
		NSString *querySQL = [NSString stringWithFormat: @"SELECT pictureDiskpath FROM %@ WHERE id=\"%@\"", kSMSHStorageManagerCampaignsTableName, campaign.campaignId];
		
		EGODatabaseResult* result = [_smsHelpDB executeQuery:querySQL];
		if (result.errorCode != SQLITE_OK)
		{
			DBG(@"savePicture: Failed to query database.");
		}
		
		if (result.count > 0)
		{
			EGODatabaseRow *row = [result rowAtIndex:0];
			
			NSString *diskPath = [row stringForColumnIndex:0];
			
			if (diskPath.length > 0)
			{
				BOOL removeOldImageSuccess = [[NSFileManager defaultManager] removeItemAtPath:diskPath error:nil];
				
				if (NO == removeOldImageSuccess)
				{
					DBG(@"SavePicture: Failed to remove old image form disk.");
				}
			}
		}
		else
		{
			// do nothing - we need to proceed and cache the image
		}
	}
	else
	{
		DBG(@"Failed to open database.");
	}
	
	// Generate a filename for the image and save it to disk.
	NSArray *dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	
	NSString *docsDir = [dirPaths objectAtIndex:0];
	
	NSString *baseFilename = md5(urlString);
	
	NSString *filepath = [docsDir stringByAppendingPathComponent: baseFilename];
	
	NSFileManager *filemgr = [NSFileManager defaultManager];
	
	int i = 0;
	while ([filemgr fileExistsAtPath: filepath ])
	{
		++i;
		filepath = [docsDir stringByAppendingPathComponent:[NSString stringWithFormat:@"%@_%i", baseFilename, i]];
	}
	
	NSData *imageData = UIImageJPEGRepresentation(image, 1.0f);
	
	BOOL writeResult = [imageData writeToFile:filepath atomically:YES];
	
	if (NO == writeResult)
	{
		// something went wrong with saving the image to disk - returning
		DBG(@"Failed to save image to disk.");
		return NO;
	}
	else
	{
		//DBG(@"Image was saved: %@", filepath);
	}
	
	
	// Update the database with the new PictureUrlString and the diskpath for the new image.
	if ([_smsHelpDB open])
	{
		NSString *insertSQL = [NSString stringWithFormat: @"UPDATE %@ SET pictureDiskpath=\"%@\" WHERE id=\"%@\"", kSMSHStorageManagerCampaignsTableName, filepath, campaign.campaignId];
		
		
		if ([_smsHelpDB executeUpdate:insertSQL])
		{
			// do nothing
			DBG(@"Image was recorded in the database.");
			return YES;
		}
		else
		{
			DBG(@"Image was NOT recorded in the db. Removing from disk.");
			// record was not added to the db - erase file from disk
			NSError *error = nil;
			if ([filemgr removeItemAtPath:filepath error:&error])
			{
				DBG(@"Image was removed form disk.");
			}
			else
			{
				DBG(@"Image was NOT removed form disk: %@; %@", error.localizedDescription, error.localizedFailureReason);
			}
			return NO;
		}
	}
	
	return NO;
}

- (void) updateCampaign:(SMSHCampaign *)campaign
{
	if ([campaign.status isEqualToString:@"update"])
	{
		BOOL shouldDeleteOldPicture = NO;
		
		NSMutableString *fields = [[NSMutableString alloc] initWithString:@""];
		
		if (is_string_valid(campaign.name))
		{
			[fields appendFormat:@" name=\"%@\",", campaign.name];
		}
		
		if (is_string_valid(campaign.subname))
		{
			[fields appendFormat:@" subname=\"%@\",", campaign.subname];
		}
		
		if (is_string_valid(campaign.type))
		{
			[fields appendFormat:@" type=\"%@\",", campaign.type];
		}
		
		if (is_string_valid(campaign.text))
		{
			[fields appendFormat:@" description=\"%@\",", campaign.text];
		}
		
		if (campaign.price > 0.0)
		{
			[fields appendFormat:@" price=%lf,", campaign.price];
		}
		
		if (is_string_valid(campaign.pictureUrlString))
		{
			[fields appendFormat:@" pictureUrlString=\"%@\",", campaign.pictureUrlString];
			
			shouldDeleteOldPicture = YES;
		}
		
		if (is_string_valid(campaign.urlString))
		{
			[fields appendFormat:@" urlString=\"%@\",", campaign.urlString];
		}
		
		if (is_string_valid(campaign.smsText))
		{
			[fields appendFormat:@" smsText=\"%@\",", campaign.smsText];
		}
		
		if (campaign.smsNumber > 0)
		{
			[fields appendFormat:@" smsNumber=%lld,", campaign.smsNumber];
		}
		
		if (campaign.startDate != nil && [campaign.startDate timeIntervalSince1970] > 0.1)
		{
			[fields appendFormat:@" startDate=%lf,", [campaign.startDate timeIntervalSince1970]];
		}
		
		if (fields.length > 0)
		{
			if (shouldDeleteOldPicture)
			{
				NSString *querySQL = [NSString stringWithFormat: @"SELECT pictureDiskpath FROM %@ WHERE id=\"%@\"", kSMSHStorageManagerCampaignsTableName, campaign.campaignId];
				
				EGODatabaseResult* result = [_smsHelpDB executeQuery:querySQL];
				if (result.errorCode != SQLITE_OK)
				{
					DBG(@"campaign update: Failed to query database.");
				}
				
				if (result.count > 0)
				{
					EGODatabaseRow *row = [result rowAtIndex:0];
					
					NSString *diskPath = [row stringForColumnIndex:0];
					
					BOOL removeOldImageSuccess = [[NSFileManager defaultManager] removeItemAtPath:diskPath error:nil];
					
					if (NO == removeOldImageSuccess)
					{
						DBG(@"campaign update: Failed to remove old image form disk.");
					}
					
					NSString *updateSQL = [NSString stringWithFormat: @"UPDATE %@ SET pictureDiskpath=\"\" WHERE id=\"%@\"", kSMSHStorageManagerCampaignsTableName, campaign.campaignId];
					
					[_smsHelpDB executeUpdate:updateSQL];
				}
			}
			
			[fields replaceCharactersInRange:NSMakeRange(fields.length - 1, 1) withString:@""];
			
			NSString *updateSQL = [NSString stringWithFormat:@"UPDATE %@ SET %@ WHERE id=\"%@\"", kSMSHStorageManagerCampaignsTableName, fields, campaign.campaignId];
			
			if (NO == [_smsHelpDB executeUpdate:updateSQL])
			{
				DBG(@"Failed to update campaign '%@'.", campaign.campaignId);
			}
		}
		
		[fields release];
	}
	else if ([campaign.status isEqualToString:@"delete"])
	{
		if ([_smsHelpDB open])
		{
			NSString *querySQL = [NSString stringWithFormat: @"SELECT pictureDiskpath FROM %@ WHERE id=\"%@\"", kSMSHStorageManagerCampaignsTableName, campaign.campaignId];
			
			EGODatabaseResult* result = [_smsHelpDB executeQuery:querySQL];
			if (result.errorCode != SQLITE_OK)
			{
				DBG(@"campaign delete: Failed to query database.");
			}
			
			if (result.count > 0)
			{
				EGODatabaseRow *row = [result rowAtIndex:0];
				
				NSString *diskPath = [row stringForColumnIndex:0];
				
				if (diskPath.length > 0)
				{
					BOOL removeOldImageSuccess = [[NSFileManager defaultManager] removeItemAtPath:diskPath error:nil];
					
					if (NO == removeOldImageSuccess)
					{
						DBG(@"campaign delete: Failed to remove old image form disk.");
					}
				}
			}
			
			NSString *deleteSQL = [NSString stringWithFormat:@"DELETE FROM %@ WHERE id=\"%@\"", kSMSHStorageManagerCampaignsTableName, campaign.campaignId];
			
			if (NO == [_smsHelpDB executeUpdate:deleteSQL])
			{
				DBG(@"Failed to delete campaign '%@'.", campaign.campaignId);
			}
		}
		else
		{
			DBG(@"Failed to open database.");
		}
	}
	else
	{
		[self addCampaignsToCache:@[campaign]];
	}
}

/*!
 * Parses the list of campaigns received from the server and decides which are to be updated and which are to be inserted. 
 */
- (void) addCampaigns:(NSArray *)campaigns
{
	NSMutableArray *forInsertion = [[NSMutableArray alloc] init];
	NSMutableArray *forUpdate = [[NSMutableArray alloc] init];
	
	for (SMSHCampaign *c in campaigns)
	{
		if (0 == c.status.length || [c.status isEqualToString:@"insert"])
		{
			[forInsertion addObject:c];
		}
		else
		{
			[forUpdate addObject:c];
		}
	}
	
	for (SMSHCampaign *c in forUpdate)
	{
		[self updateCampaign:c];
	}
	
	[self addCampaignsToCache:forInsertion];
}


@end
