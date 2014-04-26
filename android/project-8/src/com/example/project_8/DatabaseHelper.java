package com.example.project_8;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "SMSHelpDB";

	// Table Names
	private static final String TABLE_CAMPAIGNS_INFO = "Campaigns";

	// Common column names
	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";

	// Info Table - column names
	private static final String KEY_PHONE_NUMBER = "phone_number";// int
	private static final String KEY_CAMPAIGN_ID = "campaign_id";// int
	private static final String KEY_PRICE_SMS = "price_sms";// double
	private static final String KEY_TEXT_SMS = "text_sms";
	private static final String KEY_CAMPAIGN_NAME = "campaign_name";
	private static final String KEY_CAMPAIGN_SUBNAME = "campaign_subname";
	private static final String KEY_START_DATE = "start_date";
	private static final String KEY_END_DATE = "end_date";
	private static final String KEY_CAMPAIGN_TYPE = "campaign_type";
	private static final String KEY_TEXT_CAMPAIGN = "text_campaign";
	private static final String KEY_IMAGE = "image";
	private static final String KEY_CAMPAIGN_LINK = "campaign_link";

	// Table Create Statements
	// Codeset table create statement
	private static final String CREATE_TABLE_CAMPAIGNS = "CREATE TABLE "
			+ TABLE_CAMPAIGNS_INFO + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_PHONE_NUMBER + " INTEGER," + KEY_CAMPAIGN_ID + " INTEGER,"
			+ KEY_PRICE_SMS + " DOUBLE," + KEY_TEXT_SMS + " TEXT,"
			+ KEY_CAMPAIGN_NAME + " TEXT," + KEY_CAMPAIGN_SUBNAME + " TEXT,"
			+ KEY_START_DATE + " TEXT," + KEY_END_DATE + " TEXT,"
			+ KEY_CAMPAIGN_TYPE + " TEXT," + KEY_TEXT_CAMPAIGN + " TEXT,"

			+ KEY_IMAGE + " TEXT," + KEY_CAMPAIGN_LINK + " TEXT,"
			+ KEY_CREATED_AT + " DATETIME" + ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_CAMPAIGNS);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGNS_INFO);

		// create new tables
		onCreate(db);

	}

}
