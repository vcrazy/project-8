//
//  EGODatabase.m
//  EGODatabase
//
//  Created by Shaun Harrison on 3/6/09.
//  Copyright (c) 2009 enormego
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
//

/* 
 * Some of the code below is based on code from FMDB
 * (Mostly bindObject, and the error checking)
 *
 * We've reworked it, rewritten a lot of it, and feel we've improved on it.
 * However credit is still due to FMDB.
 *
 * @see http://code.google.com/p/flycode/soureturnCodee/browse/#svn/trunk/fmdb
 */
#define WARNING_MSG "FATAL: object is not an NSObject subclass. Are you using int? use [NSNumber numberWithInt:1] \n"
#define VAToArray(firstarg) ({\
NSMutableArray* valistArray = [NSMutableArray array];\
id obj = nil;\
va_list arguments;\
va_start(arguments, sql);\
@try { \
while ((obj = va_arg(arguments, id))) {\
if([obj isKindOfClass:[NSObject class]]) [valistArray addObject:obj];\
else printf(WARNING_MSG); \
}\
}   \
@catch(NSException *exception){ \
 printf(WARNING_MSG); \
} \
va_end(arguments);\
valistArray;\
})



#import "EGODatabase.h"



@interface EGODatabase (Private)
- (BOOL)bindStatement:(sqlite3_stmt*)statement toParameters:(NSArray*)parameters;
- (void)bindObject:(id)obj toColumn:(int)idx inStatement:(sqlite3_stmt*)pStmt;
@end

@implementation EGODatabase
@synthesize sqliteHandle=handle;

+ (id)databaseWithPath:(NSString*)aPath {
	return [[[[self class] alloc] initWithPath:aPath] autorelease];
}

- (id)initWithPath:(NSString*)aPath {
	if((self = [super init])) {
		databasePath = [aPath retain];
		executeLock = [[NSLock alloc] init];
	}
	
	return self;
}

- (EGODatabaseRequest*)requestWithQueryAndParameters:(NSString*)sql, ... {
	return [self requestWithQuery:sql parameters:VAToArray(sql)];
}

- (EGODatabaseRequest*)requestWithQuery:(NSString*)sql {
	return [self requestWithQuery:sql parameters:nil];
}

- (EGODatabaseRequest*)requestWithQuery:(NSString*)sql parameters:(NSArray*)parameters {
	EGODatabaseRequest* request = [[[EGODatabaseRequest alloc] initWithQuery:sql parameters:parameters] autorelease];

	request.database = self;
	request.requestKind = EGODatabaseSelectRequest;
	
	return request;
}



- (EGODatabaseRequest*)requestWithUpdateAndParameters:(NSString*)sql, ... {
	return [self requestWithUpdate:sql parameters:VAToArray(sql)];
}

- (EGODatabaseRequest*)requestWithUpdate:(NSString*)sql {
	return [self requestWithUpdate:sql parameters:nil];
}

- (EGODatabaseRequest*)requestWithUpdate:(NSString*)sql parameters:(NSArray*)parameters {
	EGODatabaseRequest* request = [[[EGODatabaseRequest alloc] initWithQuery:sql parameters:parameters] autorelease];

	request.database = self;
	request.requestKind = EGODatabaseUpdateRequest;
	
	return request;
}

/*- (void)test:(NSString*)sql,... {
	
	NSLog(@"VAToArray :%@",VAToArray(sql));
}*/
- (BOOL)open {
	
	
	//[self test:@"str",2,nil];
	if(opened) return YES;
	
	int err = sqlite3_open([databasePath fileSystemRepresentation], &handle);

	if(err != SQLITE_OK) {
		EGODBDebugLog(@"[EGODatabase] Error opening DB: %d", err);
		return NO;
	}
	
	opened = YES;
	return YES;
}

- (void)close {
	if(!handle) return;
	sqlite3_close(handle);
	opened = NO;
}

- (BOOL)executeUpdateWithParameters:(NSString*)sql,... {
	return [self executeUpdate:sql parameters:VAToArray(sql)];
}

- (BOOL)executeUpdate:(NSString*)sql {
	return [self executeUpdate:sql parameters:nil];
}

- (BOOL)executeUpdate:(NSString*)sql parameters:(NSArray*)parameters {
	EGODBLockLog(@"[Update] Waiting for Lock (%@): %@ %@", [sql md5], sql, [NSThread isMainThread] ? @"** Alert: Attempting to lock on main thread **" : @"");
	[executeLock lock];
	EGODBLockLog(@"[Update] Got Lock (%@)", [sql md5]);
	
	if(![self open]) {
		EGODBLockLog(@"%@ released lock", [sql md5]);
		[executeLock unlock];
		return NO;
	}

	int returnCode = 0;
	sqlite3_stmt* statement = NULL;

	returnCode = sqlite3_prepare(handle, [sql UTF8String], -1, &statement, 0);
	
	if (SQLITE_BUSY == returnCode) {
		EGODBLockLog(@"[EGODatabase] Query Failed, Database Busy:\n%@\n\n", sql);
		EGODBLockLog(@"%@ released lock", [sql md5]);
		[executeLock unlock];
		return NO;
	} else if (SQLITE_OK != returnCode) {
		EGODBDebugLog(@"[EGODatabase] Query Failed, Error: %d \"%@\"\n%@\n\n", [self lastErrorCode], [self lastErrorMessage], sql);
		sqlite3_finalize(statement);
		EGODBLockLog(@"%@ released lock", [sql md5]);
		[executeLock unlock];
		return NO;
	}
	
	
	if (![self bindStatement:statement toParameters:parameters]) {
		EGODBDebugLog(@"[EGODatabase] Invalid bind cound for number of arguments.");
		sqlite3_finalize(statement);
		EGODBLockLog(@"%@ released lock", [sql md5]);
		[executeLock unlock];
		return NO;
	}
	
	returnCode = sqlite3_step(statement);
	
	if (SQLITE_BUSY == returnCode) {
		EGODBDebugLog(@"[EGODatabase] Query Failed, Database Busy:\n%@\n\n", sql);
	} else if (SQLITE_DONE == returnCode || SQLITE_ROW == returnCode) {
		
	} else if (SQLITE_ERROR == returnCode) {
		EGODBDebugLog(@"[EGODatabase] sqlite3_step Failed: (%d: %@) SQLITE_ERROR\n%@\n\n", returnCode, [self lastErrorMessage], sql);
	} else if (SQLITE_MISUSE == returnCode) {
		EGODBDebugLog(@"[EGODatabase] sqlite3_step Failed: (%d: %@) SQLITE_MISUSE\n%@\n\n", returnCode, [self lastErrorMessage], sql);
	} else {
		EGODBDebugLog(@"[EGODatabase] sqlite3_step Failed: (%d: %@) UNKNOWN_ERROR\n%@\n\n", returnCode, [self lastErrorMessage], sql);
	}

	returnCode = sqlite3_finalize(statement);

	EGODBLockLog(@"%@ released lock", [sql md5]);
	[executeLock unlock];
	
	return (returnCode == SQLITE_OK);
}

- (sqlite3_int64)last_insert_rowid
{
  if (handle) {
     return sqlite3_last_insert_rowid(handle);
  } else {
    EGODBDebugLog(@"[EGODatabase] Can't get last rowid of nil sqlite");
    return 0;
  }
}


- (EGODatabaseResult*)executeQueryWithParameters:(NSString*)sql,... {
	return [self executeQuery:sql parameters:VAToArray(sql)];
}

- (EGODatabaseResult*)executeQuery:(NSString*)sql {
	return [self executeQuery:sql parameters:nil];
}

- (EGODatabaseResult*)executeQuery:(NSString*)sql parameters:(NSArray*)parameters {
	EGODBLockLog(@"[Query] Waiting for Lock (%@): %@", [sql md5], sql);
	[executeLock lock];
	EGODBLockLog(@"[Query] Got Lock (%@)", [sql md5]);
	
	EGODatabaseResult* result = [[[EGODatabaseResult alloc] init] autorelease];

	if(![self open]) {
		EGODBLockLog(@"%@ released lock", [sql md5]);
		[executeLock unlock];
		return result;
	}
	
	int returnCode = 0;
	sqlite3_stmt* statement = NULL;
	
	returnCode = sqlite3_prepare(handle, [sql UTF8String], -1, &statement, 0);
	result.errorCode = [self lastErrorCode];
	result.errorMessage = [self lastErrorMessage];
	
	if (SQLITE_BUSY == returnCode) {
		EGODBDebugLog(@"[EGODatabase] Query Failed, Database Busy:\n%@\n\n", sql);
		EGODBLockLog(@"%@ released lock", [sql md5]);
		[executeLock unlock];
		return result;
	} else if (SQLITE_OK != returnCode) {
		EGODBDebugLog(@"[EGODatabase] Query Failed, Error: %d \"%@\"\n%@\n\n", [self lastErrorCode], [self lastErrorMessage], sql);
		sqlite3_finalize(statement);
		EGODBLockLog(@"%@ released lock", [sql md5]);
		[executeLock unlock];
		return result;
	}
	
	if (![self bindStatement:statement toParameters:parameters]) {
		EGODBDebugLog(@"[EGODatabase] Invalid bind cound for number of arguments.");
		sqlite3_finalize(statement);
		EGODBLockLog(@"%@ released lock", [sql md5]);
		[executeLock unlock];
		return result;
	}
	
	int columnCount = sqlite3_column_count(statement);
	int x;
	
	for(x=0;x<columnCount;x++) {
		if(sqlite3_column_name(statement,x) != NULL) {
			[result.columnNames addObject:[NSString stringWithUTF8String:sqlite3_column_name(statement,x)]];
		} else {
			[result.columnNames addObject:[NSString stringWithFormat:@"%d", x]];
		}

		if(sqlite3_column_decltype(statement,x) != NULL) {
			[result.columnTypes addObject:[NSString stringWithUTF8String:sqlite3_column_decltype(statement,x)]];
		} else {
			[result.columnTypes addObject:@""];
		}
	}
	
	while(sqlite3_step(statement) == SQLITE_ROW) {
		EGODatabaseRow* row = [[EGODatabaseRow alloc] initWithDatabaseResult:result];
		for(x=0;x<columnCount;x++) {
			if (SQLITE_BLOB == sqlite3_column_type(statement, x)) {
				[row.columnData addObject:[NSData dataWithBytes:sqlite3_column_blob(statement,x)
														 length:sqlite3_column_bytes(statement,x)]];
			} else if (sqlite3_column_text(statement,x) != NULL) {
				[row.columnData addObject:[[[NSString alloc] initWithUTF8String:(char *)sqlite3_column_text(statement,x)] autorelease]];
			} else {
				[row.columnData addObject:@""];
			}
		}
		
		[result addRow:row];
		[row release];
	}
	
	sqlite3_finalize(statement);

	EGODBLockLog(@"%@ released lock", [sql md5]);
	[executeLock unlock];

	return result;
}

- (NSString*)lastErrorMessage {
	if([self hadError]) {
		return [NSString stringWithUTF8String:sqlite3_errmsg(handle)];
	} else {
		return nil;
	}
}

// Transaction helpers

- (BOOL)rollback {
    BOOL b = [self executeUpdate:@"ROLLBACK TRANSACTION;"];
    if (b) {
        inTransaction = NO;
    }
    return b;
}

- (BOOL)commit {
    BOOL b =  [self executeUpdate:@"COMMIT TRANSACTION;"];
    if (b) {
        inTransaction = NO;
    }
    return b;
}

- (BOOL)beginDeferredTransaction {
    BOOL b =  [self executeUpdate:@"BEGIN DEFERRED TRANSACTION;"];
    if (b) {
        inTransaction = YES;
    }
    return b;
}

- (BOOL)beginTransaction {
    BOOL b =  [self executeUpdate:@"BEGIN EXCLUSIVE TRANSACTION;"];
    if (b) {
        inTransaction = YES;
    }
    return b;
}

- (BOOL)inTransaction {
    return inTransaction;
}
- (void)setInTransaction:(BOOL)flag {
    inTransaction = flag;
}


- (BOOL)hadError {
	return [self lastErrorCode] != SQLITE_OK;
}

- (int)lastErrorCode {
	return sqlite3_errcode(handle);
}

- (BOOL)bindStatement:(sqlite3_stmt*)statement toParameters:(NSArray*)parameters {
	int index = 0;
	int queryCount = sqlite3_bind_parameter_count(statement);
	
	for(id obj in parameters) {
		index++;
		[self bindObject:obj toColumn:index inStatement:statement];
	}
	
	return index == queryCount;
}

- (void)bindObject:(id)obj toColumn:(int)idx inStatement:(sqlite3_stmt*)pStmt {
	if ((!obj) || ((NSNull *)obj == [NSNull null])) {
		sqlite3_bind_null(pStmt, idx);
	} else if ([obj isKindOfClass:[NSData class]]) {
		sqlite3_bind_blob(pStmt, idx, [obj bytes], (int)[obj length], SQLITE_STATIC);
	} else if ([obj isKindOfClass:[NSDate class]]) {
		sqlite3_bind_double(pStmt, idx, [obj timeIntervalSince1970]);
	} else if ([obj isKindOfClass:[NSNumber class]]) {
		if (strcmp([obj objCType], @encode(BOOL)) == 0) {
			sqlite3_bind_int(pStmt, idx, ([obj boolValue] ? 1 : 0));
		} else if (strcmp([obj objCType], @encode(int)) == 0) {
			sqlite3_bind_int64(pStmt, idx, [obj longValue]);
		} else if (strcmp([obj objCType], @encode(long)) == 0) {
			sqlite3_bind_int64(pStmt, idx, [obj longValue]);
		} else if (strcmp([obj objCType], @encode(float)) == 0) {
			sqlite3_bind_double(pStmt, idx, [obj floatValue]);
		} else if (strcmp([obj objCType], @encode(double)) == 0) {
			sqlite3_bind_double(pStmt, idx, [obj doubleValue]);
		} else {
			sqlite3_bind_text(pStmt, idx, [[obj description] UTF8String], -1, SQLITE_STATIC);
		}
	} else {
		sqlite3_bind_text(pStmt, idx, [[obj description] UTF8String], -1, SQLITE_STATIC);
	}
}

#define DEG2RAD(degrees) ((degrees) * M_PI / 180.0) // degrees * pi over 180
 
static void distanceFunc(sqlite3_context *context, int argc, sqlite3_value **argv)
{
	// check that we have four arguments (lat1, lon1, lat2, lon2)
	assert(argc == 4);
	
	// check that all four arguments are non-null
	if (sqlite3_value_type(argv[0]) == SQLITE_NULL || sqlite3_value_type(argv[1]) == SQLITE_NULL || sqlite3_value_type(argv[2]) == SQLITE_NULL || sqlite3_value_type(argv[3]) == SQLITE_NULL)
	{
		sqlite3_result_null(context);
		return;
	}
	
	// get the four argument values
	double lat1 = sqlite3_value_double(argv[0]);
	double lon1 = sqlite3_value_double(argv[1]);
	double lat2 = sqlite3_value_double(argv[2]);
	double lon2 = sqlite3_value_double(argv[3]);
	
	// convert lat1 and lat2 into radians now, to avoid doing it twice below
	double lat1rad = DEG2RAD(lat1);
	double lat2rad = DEG2RAD(lat2);
	
	// apply the spherical law of cosines to our latitudes and longitudes, and set the result appropriately
	// 6378136.6 is the approximate radius of the earth in meters
	sqlite3_result_double(context, acos(sin(lat1rad) * sin(lat2rad) + cos(lat1rad) * cos(lat2rad) * cos(DEG2RAD(lon2) - DEG2RAD(lon1))) * 6378136.6);
}

-(void)createDistanceFunc{
	sqlite3_create_function(handle, "distance", 4, SQLITE_UTF8, NULL, &distanceFunc, NULL, NULL);
}

- (void)dealloc {
	[self close];
	[executeLock release];
	[databasePath release];
	[super dealloc];
}

@end
