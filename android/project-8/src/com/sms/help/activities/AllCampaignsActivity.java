package com.sms.help.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

public class AllCampaignsActivity extends Activity implements OnClickListener {

	private String chosenType = Constants.TYPE_PEOPLE;

	private ListView mlistView;
	private AllCampaignsAdapter adapter;

	private LinearLayout textViewPeople;
	private LinearLayout textViewOrganisation;
	private LinearLayout textViewSpecial;
	private LinearLayout textViewOther;
	private LinearLayout textViewMore;

	private ArrayList<CampaignBasicInfo> list = new ArrayList<CampaignBasicInfo>();

	private Loader loader;

	/* Database */
	DatabaseHelper db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_campaigns);

		/* Loader */
		loader = new Loader(AllCampaignsActivity.this);

		/* Init wiidgets */
		mlistView = (ListView) findViewById(R.id.list_view);

		textViewPeople = (LinearLayout) findViewById(R.id.tab_people);
		textViewPeople.setSelected(true);
		textViewOrganisation = (LinearLayout) findViewById(R.id.tab_organisations);
		textViewSpecial = (LinearLayout) findViewById(R.id.tab_special);
		textViewOther = (LinearLayout) findViewById(R.id.tab_other);
		textViewMore = (LinearLayout) findViewById(R.id.tab_more);

		/* Database instance */
		db = DatabaseHelper.getInstance(this);

		Log.e("Version", "version now is " + db.getVersion());

		SharedPreferences prefs = getSharedPreferences(Constants.SMSHELP_PREFS,
				Activity.MODE_PRIVATE);
		if (prefs.getBoolean(Constants.FIRST_START, true)) {

			if (Utils.haveNetworkConnection(this)) {

				// insert 0 as start version number
				db.insertVersion("0");
				// get the whole data here - version 0
				getDataAndLoad(false);
				// get the last version
				new GetDataVersionTask(this).execute();

				prefs.edit().putBoolean(Constants.FIRST_START, false).commit();

			} else {
				// work the old way
				getData();
			}

		} else {
			// work the old way
			getData();
		}

		/* On click listeners for tabs */
		textViewPeople.setOnClickListener(this);
		textViewOrganisation.setOnClickListener(this);
		textViewSpecial.setOnClickListener(this);
		textViewOther.setOnClickListener(this);
		textViewMore.setOnClickListener(this);

		/* On item click listener for the list view elements */
		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int campaignID = (Integer) view.getTag(R.id.item_image);

				CampaignFullInfo fullInfo = db.getCampaignByID(campaignID);

				if (fullInfo != null) {
					Intent intent = new Intent(AllCampaignsActivity.this,
							CampaignProfileActivity.class);
					intent.putExtra("full", fullInfo);
					startActivity(intent);
					// first one pushes the new activity in
					// second one pushes the old activity out
					overridePendingTransition(R.anim.in_new_activity,
							R.anim.out_old_activity);
				}

			}
		});

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
			textViewMore.setSelected(false);

			chosenType = Constants.TYPE_PEOPLE;
			loadData();
			break;
		case R.id.tab_organisations:
			textViewOther.setSelected(false);
			textViewSpecial.setSelected(false);
			textViewOrganisation.setSelected(true);
			textViewPeople.setSelected(false);
			textViewMore.setSelected(false);

			chosenType = Constants.TYPE_ORGANIZATION;
			loadData();
			break;
		case R.id.tab_special:
			textViewOther.setSelected(false);
			textViewSpecial.setSelected(true);
			textViewOrganisation.setSelected(false);
			textViewPeople.setSelected(false);
			textViewMore.setSelected(false);

			chosenType = Constants.TYPE_SPECIAL;
			loadData();
			break;
		case R.id.tab_other:
			textViewOther.setSelected(true);
			textViewSpecial.setSelected(false);
			textViewOrganisation.setSelected(false);
			textViewPeople.setSelected(false);
			textViewMore.setSelected(false);

			chosenType = Constants.TYPE_OTHER;
			loadData();
			break;
		case R.id.tab_more:

			Intent intent = new Intent(AllCampaignsActivity.this,
					ShowStatisticsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_new_activity,
					R.anim.out_old_activity);
			break;
		default:
			break;

		}

	}

	public void getDataAndLoad(boolean update) {

		GetDataTask task = new GetDataTask(this) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				// start threads to download images and write them to cache
				downloadAllImages();
			}

		};

		if (!loader.isShowing())
			loader.show();

		task.execute();

	}

	public void getData() {

		// DatabaseHelper db = DatabaseHelper.getInstance(this);
		int count = db.getCount();
		// db.close();

		if (Utils.haveNetworkConnection(this)) {

			/* Internet YES, Check DB */

			if (count == 0) {

				/* DB is EMPTY, Get DB from API */
				getDataAndLoad(false);

			} else {

				/* DB is NOT EMPTY, Check for new version */
				checkForNewDB();

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
				loadData();
			}

		}

	}

	private void checkForNewDB() {

		GetDataVersionTask task = new GetDataVersionTask(this) {

			@Override
			protected void onPostExecute(Boolean getData) {

				/* DB is NOT EMPTY, Check for new DB */
				if (getData) {

					/* NEW DB, Update DB */
					getDataAndLoad(true);
				} else {

					/* NO NEW DB, Load data from DB */
					loadData();

				}

			}

		};

		if (!loader.isShowing())
			loader.show();
		task.execute();

	}

	private void getBasicInfoFromDB() {

		/* Get data from DB */

		list = db.getBasicInfoByType(chosenType);
		// db.close();

		if (list == null)
			list = new ArrayList<CampaignBasicInfo>();

	}

	private void loadData() {

		/* Get data from DB */
		getBasicInfoFromDB();

		this.adapter = new AllCampaignsAdapter(this, R.layout.list_item_main,
				list);
		mlistView.setAdapter(adapter);

		if (loader.isShowing())
			loader.dismiss();

	}

	private void downloadAllImages() {

		// get all campaigns from db
		DatabaseHelper db = DatabaseHelper.getInstance(this);
		ArrayList<CampaignFullInfo> allCampaigns = db.getAllCampaigns();

		// TEST download images
		ArrayList<String> urls = new ArrayList<String>();
		for (CampaignFullInfo info : allCampaigns)
			urls.add(info.campaignImageURL);

		DownloadImages downloadTask = new DownloadImages(this) {

			@Override
			protected void onPostExecute(ArrayList<Bitmap> bitmaps) {

				// images are available now, load campaigns
				loadData();

			}
		};

		downloadTask.execute(urls);

	}

}
