package com.sms.help.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sms.help.Constants;
import com.sms.help.Loader;
import com.sms.help.R;
import com.sms.help.Utils;
import com.sms.help.adapters.AllCampaignsAdapter;
import com.sms.help.db.DatabaseHelper;
import com.sms.help.tasks.DownloadImages;
import com.sms.help.tasks.GetDataTask;
import com.sms.help.tasks.GetDataVersionTask;
import com.sms.help.types.CampaignBasicInfo;
import com.sms.help.types.CampaignFullInfo;

public class AllCampaignsActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private String currentCampaignType = Constants.TYPE_PEOPLE;

	private ListView listViewCampaigns;
	private AllCampaignsAdapter adapterCampaigns;

	private LinearLayout textViewPeople;
	private LinearLayout textViewOrganisation;
	private LinearLayout textViewSpecial;
	private LinearLayout textViewOther;

	private ArrayList<CampaignBasicInfo> listCampaigns = new ArrayList<CampaignBasicInfo>();

	private Loader loader;

	/* Database */
	DatabaseHelper databaseHelper;

	private String currentVersion;
	private String newVersion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_campaigns);

		/* Loader */
		loader = new Loader(AllCampaignsActivity.this);

		/* Database instance */
		databaseHelper = DatabaseHelper.getInstance(this);

		currentVersion = databaseHelper.getVersion();

		/* Get all widgets */
		initWidgets();

		SharedPreferences prefs = getSharedPreferences(Constants.SMSHELP_PREFS,
				Activity.MODE_PRIVATE);
		if (prefs.getBoolean(Constants.FIRST_START, true)) {

			if (Utils.haveNetworkConnection(this)) {

				onFirstStart();

				prefs.edit().putBoolean(Constants.FIRST_START, false).commit();

			} else {

				getData();
			}

		} else {

			getData();
		}

		/* On click listeners for tabs */
		textViewPeople.setOnClickListener(this);
		textViewOrganisation.setOnClickListener(this);
		textViewSpecial.setOnClickListener(this);
		textViewOther.setOnClickListener(this);

		/* On item click listener for the list view elements */
		listViewCampaigns.setOnItemClickListener(this);

	}

	/** Get all widgets */
	private void initWidgets() {

		listViewCampaigns = (ListView) findViewById(R.id.list_view);

		textViewPeople = (LinearLayout) findViewById(R.id.tab_people);
		textViewPeople.setSelected(true);
		textViewOrganisation = (LinearLayout) findViewById(R.id.tab_organisations);
		textViewSpecial = (LinearLayout) findViewById(R.id.tab_special);
		textViewOther = (LinearLayout) findViewById(R.id.tab_other);

	}

	/** On First Start */
	private void onFirstStart() {

		databaseHelper.insertVersion("0");

		getCampaignsFromAPI();

		new GetDataVersionTask(this).execute();

	}

	/** On Back Click */
	public void onBackClick(View v) {

		onBackPressed();

	}

	/** On Back Pressed */
	@Override
	public void onBackPressed() {
		super.onBackPressed();

		overridePendingTransition(R.anim.in_old_activity,
				R.anim.out_new_activity);
	}

	/** On Statistics Click */
	public void onStatisticsClick(View v) {

		Intent intent = new Intent(AllCampaignsActivity.this,
				StatisticsActivity.class);
		startActivity(intent);

		overridePendingTransition(R.anim.in_new_activity,
				R.anim.out_old_activity);

	}

	/** On click listener for tabs */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.tab_people:
			textViewOther.setSelected(false);
			textViewSpecial.setSelected(false);
			textViewOrganisation.setSelected(false);
			textViewPeople.setSelected(true);

			currentCampaignType = Constants.TYPE_PEOPLE;
			loadCampaigns();
			break;
		case R.id.tab_organisations:
			textViewOther.setSelected(false);
			textViewSpecial.setSelected(false);
			textViewOrganisation.setSelected(true);
			textViewPeople.setSelected(false);

			currentCampaignType = Constants.TYPE_ORGANIZATION;
			loadCampaigns();
			break;
		case R.id.tab_special:
			textViewOther.setSelected(false);
			textViewSpecial.setSelected(true);
			textViewOrganisation.setSelected(false);
			textViewPeople.setSelected(false);

			currentCampaignType = Constants.TYPE_SPECIAL;
			loadCampaigns();
			break;
		case R.id.tab_other:
			textViewOther.setSelected(true);
			textViewSpecial.setSelected(false);
			textViewOrganisation.setSelected(false);
			textViewPeople.setSelected(false);

			currentCampaignType = Constants.TYPE_OTHER;
			loadCampaigns();
			break;

		default:
			break;

		}

	}

	/** On Item Click Listener for the ListView */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		int campaignID = (Integer) view.getTag(R.id.imageview_picture);

		CampaignFullInfo fullInfo = databaseHelper.getCampaignByID(campaignID);

		if (fullInfo != null) {
			Intent intent = new Intent(AllCampaignsActivity.this,
					CampaignProfileActivity.class);
			intent.putExtra("full", fullInfo);
			startActivity(intent);
			/*
			 * first one pushes the new activity in, second one pushes the old
			 * activity out
			 */
			overridePendingTransition(R.anim.in_new_activity,
					R.anim.out_old_activity);
		}

	}

	/** Get campaigns from API */
	public void getCampaignsFromAPI() {

		GetDataTask task = new GetDataTask(this) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				/* start threads to download images and write them to cache */
				downloadAllImages();

				/* Insert new version after successful update. */
				if (result)
					databaseHelper.insertVersion(newVersion);
			}

		};

		if (!loader.isShowing())
			loader.show();

		task.execute();

	}

	/** Get data */
	public void getData() {

		int count = databaseHelper.getCount();

		if (Utils.haveNetworkConnection(this)) {

			/* Internet YES, Check DB */

			if (count == 0) {

				/* DB is EMPTY, Get DB from API */
				getCampaignsFromAPI();

			} else {

				/* DB is NOT EMPTY, Check for new version */
				checkForNewVersion();

			}

		} else {

			/* Internet NO, Check DB */

			if (count == 0) {

				/* DB is EMPTY, Show Internet Message */
				if (loader.isShowing())
					loader.dismiss();

				Utils.noInternetDialog(AllCampaignsActivity.this);

			} else {

				/* DB is NOT EMPTY, Load data from DB */
				loadCampaigns();
			}

		}

	}

	/** Check for new version of the API */
	private void checkForNewVersion() {

		GetDataVersionTask task = new GetDataVersionTask(this) {

			@Override
			protected void onPostExecute(String version) {

				/*
				 * The task returns the new version from the API. Check if the
				 * new version is greater than the current one.
				 */

				currentVersion = databaseHelper.getVersion();
				newVersion = version;

				if (newVersion != null) {

					if (Integer.valueOf(currentVersion) < Integer
							.valueOf(newVersion)) {

						/*
						 * Get new data from the API. Then insert the new
						 * version in the database.
						 */
						/* NEW DB, Update DB */
						getCampaignsFromAPI();

					} else {

						/* NO NEW DB, Load data from DB */
						loadCampaigns();

					}

				}

			}

		};

		if (!loader.isShowing())
			loader.show();
		task.execute();

	}

	/** Get campaigns from database by type */
	private void getCampaignsFromDB() {

		/* Get data from DB */
		listCampaigns = databaseHelper.getBasicInfoByType(currentCampaignType);

		if (listCampaigns == null)
			listCampaigns = new ArrayList<CampaignBasicInfo>();

	}

	/** Load campaigns */
	private void loadCampaigns() {

		/* Get data from DB */
		getCampaignsFromDB();

		this.adapterCampaigns = new AllCampaignsAdapter(this,
				R.layout.list_item_campaign, listCampaigns);
		listViewCampaigns.setAdapter(adapterCampaigns);

		if (loader.isShowing())
			loader.dismiss();

	}

	/** Download all images and save them to cache folder */
	private void downloadAllImages() {

		// get all campaigns from db
		DatabaseHelper db = DatabaseHelper.getInstance(this);
		ArrayList<CampaignFullInfo> allCampaigns = db.getAllCampaigns();

		ArrayList<String> urls = new ArrayList<String>();
		for (CampaignFullInfo info : allCampaigns)
			urls.add(info.campaignImageURL);

		DownloadImages downloadTask = new DownloadImages(this) {

			@Override
			protected void onPostExecute(ArrayList<Bitmap> bitmaps) {

				// images are available now, load campaigns
				loadCampaigns();

			}
		};

		downloadTask.execute(urls);

	}

}
