package com.example.project_8;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

	private static final String TABLE_VERSION = "Version";

	private static final String TABLE_STATISTICS = "Statistics";

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
	private static final String KEY_VERSION = "version";
	private static final String KEY_STATISTICS_ID = "statistic_id";
	private static final String KEY_STATISTICS_DATE = "statistic_date";
	private static final String KEY_SEND_STATISTICS = "statistic_flag";

	// Table Create Statements
	// Campaigns table create statement
	private static final String CREATE_TABLE_CAMPAIGNS = "CREATE TABLE "
			+ TABLE_CAMPAIGNS_INFO + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_PHONE_NUMBER + " INTEGER," + KEY_CAMPAIGN_ID + " INTEGER,"
			+ KEY_PRICE_SMS + " DOUBLE," + KEY_TEXT_SMS + " TEXT,"
			+ KEY_CAMPAIGN_NAME + " TEXT," + KEY_CAMPAIGN_SUBNAME + " TEXT,"
			+ KEY_START_DATE + " INTEGER," + KEY_END_DATE + " INTEGER,"
			+ KEY_CAMPAIGN_TYPE + " TEXT," + KEY_TEXT_CAMPAIGN + " TEXT,"
			+ KEY_IMAGE + " MEDIUMTEXT," + KEY_CAMPAIGN_LINK + " TEXT,"
			+ KEY_CREATED_AT + " DATETIME" + ")";

	private static final String CREATE_TABLE_VERSION = "CREATE TABLE "
			+ TABLE_VERSION + "(" + KEY_VERSION + " TEXT PRIMARY KEY)";

	private static final String CREATE_TABLE_STATISTICS = "CREATE TABLE "
			+ TABLE_STATISTICS + "(" + KEY_STATISTICS_ID
			+ " INTEGER PRIMARY KEY," + KEY_CAMPAIGN_ID + " INTEGER,"
			+ KEY_STATISTICS_DATE + " INTEGER," + KEY_SEND_STATISTICS
			+ " SMALLINT)";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_CAMPAIGNS);
		db.execSQL(CREATE_TABLE_VERSION);
		db.execSQL(CREATE_TABLE_STATISTICS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGNS_INFO);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSION);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);

		// create new tables
		onCreate(db);

	}

	/* ====== STATISTICS ====== */
	public boolean insertStatistics(int statistics_date, int statistics_flag,
			int campaign_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_STATISTICS_DATE, statistics_date);
		values.put(KEY_CAMPAIGN_ID, campaign_id);
		values.put(KEY_SEND_STATISTICS, statistics_flag);
		db.insert(TABLE_STATISTICS, null, values);
		db.close();
		return true;
	}

	public boolean updateStatistics(int statistics_id, int statistics_date,
			int statistics_flag) {
		String whereId = KEY_STATISTICS_ID + "=?";

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SEND_STATISTICS, statistics_flag);
		db.update(TABLE_STATISTICS, values, whereId,
				new String[] { String.valueOf(statistics_id) });
		db.close();
		return true;
	}

	public ArrayList<FullInfo> getAllStatistics() {

		ArrayList<FullInfo> list = new ArrayList<FullInfo>();

		String selectQuery = "SELECT * FROM " + TABLE_STATISTICS + " ORDER BY "
				+ KEY_STATISTICS_DATE + " DESC";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		ArrayList<HashMap<String, Integer>> allList = new ArrayList<HashMap<String, Integer>>();

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				// 'id'
				int campaignId = c.getInt(c.getColumnIndex(KEY_CAMPAIGN_ID));
				int sendDate = c.getInt(c.getColumnIndex(KEY_STATISTICS_DATE));
				HashMap<String, Integer> hm = new HashMap<String, Integer>();
				hm.put("id", campaignId);
				hm.put("date", sendDate);
				allList.add(hm);
			} while (c.moveToNext());
		}
		c.close();
		db.close();

		for (int i = 0; i < allList.size(); i++) {
			FullInfo getCompaignInfo = getCampaignByID(allList.get(i).get("id"));
			getCompaignInfo.smsSendDate = allList.get(i).get("date");
			list.add(getCompaignInfo);
		}

		// Iterator<HashMap<String, Integer>> it = allList.iterator();
		// while (it.hasNext()) {
		// HashMap<String, Integer> obj = it.next();
		// FullInfo getCompaignInfo = getCampaignByID(obj.get("id"));
		// getCompaignInfo.smsSendDate = obj.get("date");
		// list.add(getCompaignInfo);
		// }
		return list;

	}

	/* ====== VERSION ====== */
	public boolean insertVersion(String version) {

		String selectQuery = "SELECT * FROM " + TABLE_VERSION;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			String currentVersion = c.getString(c.getColumnIndex(KEY_VERSION));
			if (!currentVersion.equals(version)) {
				ContentValues values = new ContentValues();
				values.put(KEY_VERSION, version);
				int result = db.update(TABLE_VERSION, values, null, null);
				if (result > 0)
					return true;
			}
		} else {
			ContentValues values = new ContentValues();
			values.put(KEY_VERSION, version);
			long result = -1;
			result = db.insert(TABLE_VERSION, null, values);
			if (result > -1)
				return true;
		}

		c.close();
		db.close();
		return false;
	}

	/* ====== Campaign info ====== */

	/**
	 * Creating a campaign
	 */
	public void insertCampaigns(ArrayList<FullInfo> list, boolean update) {

		SQLiteDatabase db = this.getWritableDatabase();

		if (update) {
			db.execSQL("DROP TABLE IF EXISTS '" + TABLE_CAMPAIGNS_INFO + "'");
			db.execSQL(CREATE_TABLE_CAMPAIGNS);
		}

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
	 * getting basic info
	 * */
	public ArrayList<BasicInfo> getBasicInfoByType(String campaignType) {

		ArrayList<BasicInfo> list = new ArrayList<BasicInfo>();

		String selectQuery = "SELECT * FROM " + TABLE_CAMPAIGNS_INFO
				+ " WHERE " + KEY_CAMPAIGN_TYPE + " = '" + campaignType + "' "
				+ "ORDER BY " + KEY_START_DATE + " DESC";

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
				// 'picture'
				String image = c.getString(c.getColumnIndex(KEY_IMAGE));

				BasicInfo info = new BasicInfo(campaignId, image, campaignName,
						campaignSubname);
				// adding to list
				list.add(info);

			} while (c.moveToNext());
		}

		c.close();
		db.close();
		return list;

	}

	/**
	 * get single codeset
	 */
	public FullInfo getCampaignByID(int campaignID) {

		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_CAMPAIGNS_INFO
				+ " WHERE " + KEY_CAMPAIGN_ID + " = " + campaignID;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null && c.getCount() > 0)
			c.moveToFirst();
		else
			return null;

		// 'id'
		int campaignId = c.getInt(c.getColumnIndex(KEY_CAMPAIGN_ID));
		// 'name'
		String campaignName = c.getString(c.getColumnIndex(KEY_CAMPAIGN_NAME));
		// 'subname'
		String campaignSubname = c.getString(c
				.getColumnIndex(KEY_CAMPAIGN_SUBNAME));
		// 'type'
		String campaignType = c.getString(c.getColumnIndex(KEY_CAMPAIGN_TYPE));
		// 'text'
		String txtCampaign = c.getString(c.getColumnIndex(KEY_TEXT_CAMPAIGN));
		// 'donation'
		double priceSMS = c.getDouble(c.getColumnIndex(KEY_PRICE_SMS));
		// 'picture'
		String image = c.getString(c.getColumnIndex(KEY_IMAGE));
		// 'link'
		String campaignLink = c.getString(c.getColumnIndex(KEY_CAMPAIGN_LINK));
		// 'sms_text'
		String txtSms = c.getString(c.getColumnIndex(KEY_TEXT_SMS));
		// 'sms_number'
		int phoneNumber = c.getInt(c.getColumnIndex(KEY_PHONE_NUMBER));
		// 'date_from'
		int startDate = c.getInt(c.getColumnIndex(KEY_START_DATE));
		int endDate = c.getInt(c.getColumnIndex(KEY_END_DATE));

		// create new object
		FullInfo info = new FullInfo(phoneNumber, campaignId, priceSMS, txtSms,
				campaignName, campaignSubname, startDate, endDate,
				campaignType, txtCampaign, image, campaignLink);
		c.close();
		db.close();
		return info;

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
	 * getting campaigns count
	 */
	public int getCount() {

		String countQuery = "SELECT  * FROM " + TABLE_CAMPAIGNS_INFO;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();
		db.close();
		// return count
		return count;

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
