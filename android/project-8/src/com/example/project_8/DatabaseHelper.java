package com.example.project_8;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
	private static final String KEY_START_DATE = "start_date";// int
	private static final String KEY_END_DATE = "end_date";// int
	private static final String KEY_CAMPAIGN_TYPE = "campaign_type";
	private static final String KEY_TEXT_CAMPAIGN = "text_campaign";
	private static final String KEY_IMAGE = "image";
	private static final String KEY_CAMPAIGN_LINK = "campaign_link";

	// Table Create Statements
	// Campaigns table create statement
	private static final String CREATE_TABLE_CAMPAIGNS = "CREATE TABLE "
			+ TABLE_CAMPAIGNS_INFO + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_PHONE_NUMBER + " INTEGER," + KEY_CAMPAIGN_ID + " INTEGER,"
			+ KEY_PRICE_SMS + " DOUBLE," + KEY_TEXT_SMS + " TEXT,"
			+ KEY_CAMPAIGN_NAME + " TEXT," + KEY_CAMPAIGN_SUBNAME + " TEXT,"
			+ KEY_START_DATE + " INTEGER," + KEY_END_DATE + " INTEGER,"
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

	/* ====== Campaign info ====== */

	/**
	 * Creating a campaign
	 */
	public void insertCampaigns(ArrayList<FullInfo> list) {

		SQLiteDatabase db = this.getWritableDatabase();

		for (int i = 0; i < list.size(); i++) {
			FullInfo info = list.get(i);

			ContentValues values = new ContentValues();
			values.put(KEY_PHONE_NUMBER, info.phoneNumber);
			values.put(KEY_CAMPAIGN_ID, info.campaignId);
			values.put(KEY_PRICE_SMS, info.priceSMS);
			values.put(KEY_TEXT_SMS, info.txtSMS);
			values.put(KEY_CAMPAIGN_NAME, info.campaignName);

			values.put(KEY_CAMPAIGN_SUBNAME, info.campaignSubName);
			values.put(KEY_START_DATE, info.startDate);
			values.put(KEY_END_DATE, info.endDate);
			values.put(KEY_CAMPAIGN_TYPE, info.campaignType);
			values.put(KEY_TEXT_CAMPAIGN, info.txtCampaign);

			values.put(KEY_IMAGE, info.imageUri);
			values.put(KEY_CAMPAIGN_LINK, info.campaignLink);

			values.put(KEY_CREATED_AT, getDateTime());

			// insert row
			db.insert(TABLE_CAMPAIGNS_INFO, null, values);
		}

		db.close();
		return;
	}

	/**
	 * getting all campaigns
	 * */
	public ArrayList<FullInfo> getAllCampaigns() {

		ArrayList<FullInfo> list = new ArrayList<FullInfo>();

		String selectQuery = "SELECT * FROM " + TABLE_CAMPAIGNS_INFO
				+ " ORDER BY " + KEY_START_DATE + " DESC";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {

				// 'id'
				int campaignId = c.getInt(c.getColumnIndex(KEY_CAMPAIGN_ID));
				// 'name'
				String campaignName = c.getString(c
						.getColumnIndex(KEY_CAMPAIGN_NAME));
				// 'subname'
				String campaignSubname = c.getString(c
						.getColumnIndex(KEY_CAMPAIGN_SUBNAME));
				// 'type'
				String campaignType = c.getString(c
						.getColumnIndex(KEY_CAMPAIGN_TYPE));
				// 'text'
				String txtCampaign = c.getString(c
						.getColumnIndex(KEY_TEXT_CAMPAIGN));
				// 'donation'
				double priceSMS = c.getDouble(c.getColumnIndex(KEY_PRICE_SMS));
				// 'picture'
				String image = c.getString(c.getColumnIndex(KEY_IMAGE));
				// 'link'
				String campaignLink = c.getString(c
						.getColumnIndex(KEY_CAMPAIGN_LINK));
				// 'sms_text'
				String txtSms = c.getString(c.getColumnIndex(KEY_TEXT_SMS));
				// 'sms_number'
				int phoneNumber = c.getInt(c.getColumnIndex(KEY_PHONE_NUMBER));
				// 'date_from'
				int startDate = c.getInt(c.getColumnIndex(KEY_START_DATE));
				int endDate = c.getInt(c.getColumnIndex(KEY_END_DATE));

				// create new object
				FullInfo info = new FullInfo(phoneNumber, campaignId, priceSMS,
						txtSms, campaignName, campaignSubname, startDate,
						endDate, campaignType, txtCampaign, image, campaignLink);

				// adding to list
				list.add(info);

			} while (c.moveToNext());
		}

		c.close();
		db.close();
		return list;

	}

	/**
	 * get date time
	 * */
	private String getDateTime() {

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();

		return dateFormat.format(date);

	}

}
