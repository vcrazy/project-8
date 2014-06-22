package com.sms.help.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sms.help.types.CampaignBasicInfo;
import com.sms.help.types.CampaignFullInfo;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static DatabaseHelper sInstance;

	/** Database Version */
	private static final int DATABASE_VERSION = 2;

	/** Database Name */
	private static final String DATABASE_NAME = "SMSHelpDB";

	/**
	 * Table Names. Table Campaigns holds all of the information about the SMS
	 * campaigns. Table Version holds the last updated version of the data that
	 * this application has got from the API. Table Statistics holds the
	 * information about all campaigns that were supported by the user of this
	 * device.
	 */
	private static final String TABLE_CAMPAIGNS = "Campaigns";
	private static final String TABLE_VERSION = "Version";
	private static final String TABLE_STATISTICS = "Statistics";

	/** Common column names */
	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";

	/** Info Table - column names */
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
	private static final String KEY_STATISTICS_DATE = "statistics_date";

	/** Table Create Statements */
	private static final String CREATE_TABLE_CAMPAIGNS = "CREATE TABLE "
			+ TABLE_CAMPAIGNS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
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
			+ KEY_PHONE_NUMBER + " INTEGER," + KEY_PRICE_SMS + " DOUBLE,"
			+ KEY_TEXT_SMS + " TEXT," + KEY_CAMPAIGN_NAME + " TEXT,"
			+ KEY_CAMPAIGN_SUBNAME + " TEXT," + KEY_START_DATE + " INTEGER,"
			+ KEY_END_DATE + " INTEGER," + KEY_CAMPAIGN_TYPE + " TEXT,"
			+ KEY_TEXT_CAMPAIGN + " TEXT," + KEY_IMAGE + " MEDIUMTEXT,"
			+ KEY_CAMPAIGN_LINK + " TEXT," + KEY_STATISTICS_DATE + " LONG)";

	public static DatabaseHelper getInstance(Context context) {

		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context.getApplicationContext());
		}
		return sInstance;
	}

	private DatabaseHelper(Context context) {
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGNS);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSION);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);

		// create new tables
		onCreate(db);

	}

	/* ====== STATISTICS ====== */
	public void insertStatistics(long date, CampaignFullInfo campaign) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_STATISTICS_DATE, date);

		values.put(KEY_PHONE_NUMBER, campaign.SMSNumber);
		values.put(KEY_CAMPAIGN_ID, campaign.campaignID);
		values.put(KEY_PRICE_SMS, campaign.SMSPrice);
		values.put(KEY_TEXT_SMS, campaign.SMSText);
		values.put(KEY_CAMPAIGN_NAME, campaign.campaignName);
		values.put(KEY_CAMPAIGN_SUBNAME, campaign.campaignSubname);
		values.put(KEY_START_DATE, campaign.campaignStartDate);
		values.put(KEY_END_DATE, campaign.campaignEndDate);
		values.put(KEY_CAMPAIGN_TYPE, campaign.campaignType);
		values.put(KEY_TEXT_CAMPAIGN, campaign.campaignDescription);

		values.put(KEY_IMAGE, campaign.campaignImageURL);
		values.put(KEY_CAMPAIGN_LINK, campaign.campaignLink);

		db.insert(TABLE_STATISTICS, null, values);
		db.close();

	}

	public ArrayList<CampaignFullInfo> getAllStatistics() {

		ArrayList<CampaignFullInfo> list = new ArrayList<CampaignFullInfo>();

		String selectQuery = "SELECT * FROM " + TABLE_STATISTICS + " ORDER BY "
				+ KEY_STATISTICS_DATE + " DESC";

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

				long sentDate = c
						.getLong(c.getColumnIndex(KEY_STATISTICS_DATE));

				// create new object
				CampaignFullInfo info = new CampaignFullInfo(phoneNumber,
						campaignId, priceSMS, txtSms, campaignName,
						campaignSubname, startDate, endDate, campaignType,
						txtCampaign, image, campaignLink, sentDate, null);

				// adding to list
				list.add(info);

			} while (c.moveToNext());
		}

		c.close();
		db.close();

		return list;

	}

	/* ====== VERSION ====== */
	public boolean insertVersion(String version) {

		String selectQuery = "SELECT * FROM " + TABLE_VERSION;

		SQLiteDatabase db = this.getWritableDatabase();
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
		Log.e("Test", "return false");
		return false;
	}

	public String getVersion() {

		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_VERSION;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null && c.getCount() > 0)
			c.moveToFirst();
		else
			return null;

		return c.getString(c.getColumnIndex(KEY_VERSION));

	}

	/* ====== Campaign info ====== */

	/**
	 * Creating a campaign
	 */
	public void initCampaigns(ArrayList<CampaignFullInfo> list) {

		SQLiteDatabase db = this.getWritableDatabase();

		for (int i = 0; i < list.size(); i++) {
			CampaignFullInfo info = list.get(i);

			ContentValues values = new ContentValues();
			values.put(KEY_PHONE_NUMBER, info.SMSNumber);
			values.put(KEY_CAMPAIGN_ID, info.campaignID);
			values.put(KEY_PRICE_SMS, info.SMSPrice);
			values.put(KEY_TEXT_SMS, info.SMSText);
			values.put(KEY_CAMPAIGN_NAME, info.campaignName);

			values.put(KEY_CAMPAIGN_SUBNAME, info.campaignSubname);
			values.put(KEY_START_DATE, info.campaignStartDate);
			values.put(KEY_END_DATE, info.campaignEndDate);
			values.put(KEY_CAMPAIGN_TYPE, info.campaignType);
			values.put(KEY_TEXT_CAMPAIGN, info.campaignDescription);

			values.put(KEY_IMAGE, info.campaignImageURL);
			values.put(KEY_CAMPAIGN_LINK, info.campaignLink);

			values.put(KEY_CREATED_AT, getDateTime());

			// insert row
			if (getCampaignByID(info.campaignID) == null) {
				db.insert(TABLE_CAMPAIGNS, null, values);
			} else {
				db.close();
				updateCampaign(info);
			}

		}

		db.close();
		return;
	}

	/**
	 * getting basic info
	 * */
	public ArrayList<CampaignBasicInfo> getBasicInfoByType(String campaignType) {

		ArrayList<CampaignBasicInfo> list = new ArrayList<CampaignBasicInfo>();

		String selectQuery = "SELECT * FROM " + TABLE_CAMPAIGNS + " WHERE "
				+ KEY_CAMPAIGN_TYPE + " = '" + campaignType + "' "
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

				CampaignBasicInfo info = new CampaignBasicInfo(campaignId,
						image, campaignName, campaignSubname);
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
	public CampaignFullInfo getCampaignByID(int campaignID) {

		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_CAMPAIGNS + " WHERE "
				+ KEY_CAMPAIGN_ID + " = " + campaignID;

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
		CampaignFullInfo info = new CampaignFullInfo(phoneNumber, campaignId,
				priceSMS, txtSms, campaignName, campaignSubname, startDate,
				endDate, campaignType, txtCampaign, image, campaignLink, 0,
				null);
		c.close();
		db.close();
		return info;

	}

	/**
	 * getting all campaigns
	 * */
	public ArrayList<CampaignFullInfo> getAllCampaigns() {

		ArrayList<CampaignFullInfo> list = new ArrayList<CampaignFullInfo>();

		String selectQuery = "SELECT * FROM " + TABLE_CAMPAIGNS + " ORDER BY "
				+ KEY_START_DATE + " DESC";

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
				CampaignFullInfo info = new CampaignFullInfo(phoneNumber,
						campaignId, priceSMS, txtSms, campaignName,
						campaignSubname, startDate, endDate, campaignType,
						txtCampaign, image, campaignLink, 0, null);

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

		String countQuery = "SELECT  * FROM " + TABLE_CAMPAIGNS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();
		db.close();
		// return count
		return count;

	}

	/** delete campaign */
	public void deleteCampaign(int campaignID) {

		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_CAMPAIGNS, KEY_CAMPAIGN_ID + " = ?",
				new String[] { String.valueOf(campaignID) });

		db.close();

	}

	/** update campaign */

	public void updateCampaign(CampaignFullInfo campaign) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		if (campaign.SMSNumber > 0)
			values.put(KEY_PHONE_NUMBER, campaign.SMSNumber);

		if (campaign.SMSPrice > 0.0)
			values.put(KEY_PRICE_SMS, campaign.SMSPrice);

		if (campaign.SMSText != null && !campaign.SMSText.equals(""))
			values.put(KEY_TEXT_SMS, campaign.SMSText);

		if (campaign.campaignName != null && !campaign.campaignName.equals(""))
			values.put(KEY_CAMPAIGN_NAME, campaign.campaignName);

		if (campaign.campaignSubname != null
				&& !campaign.campaignSubname.equals(""))
			values.put(KEY_CAMPAIGN_SUBNAME, campaign.campaignSubname);

		if (campaign.campaignStartDate > 0)
			values.put(KEY_START_DATE, campaign.campaignStartDate);

		if (campaign.campaignEndDate > 0)
			values.put(KEY_END_DATE, campaign.campaignEndDate);

		if (campaign.campaignType != null && !campaign.campaignType.equals(""))
			values.put(KEY_CAMPAIGN_TYPE, campaign.campaignType);

		if (campaign.campaignDescription != null
				&& !campaign.campaignDescription.equals(""))
			values.put(KEY_TEXT_CAMPAIGN, campaign.campaignDescription);

		if (campaign.campaignImageURL != null
				&& !campaign.campaignImageURL.equals(""))
			values.put(KEY_IMAGE, campaign.campaignImageURL);

		if (campaign.campaignLink != null && !campaign.campaignLink.equals(""))
			values.put(KEY_CAMPAIGN_LINK, campaign.campaignLink);

		db.update(TABLE_CAMPAIGNS, values, KEY_CAMPAIGN_ID + " = ?",
				new String[] { String.valueOf(campaign.campaignID) });

		db.close();
		return;
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
