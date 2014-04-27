package com.example.project_8;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {
	// people, organizations, other, special
	String TYPE_PEOPLE = "people";
	String TYPE_ORGANIZATION = "organizations";
	String TYPE_OTHER = "other";
	String TYPE_SPECIAL = "special";

	private String chosenType = TYPE_PEOPLE;

	private ListView mlistView;
	private CustomAdapter adapter;

	private LinearLayout textViewPeople;
	private LinearLayout textViewOrganisation;
	private LinearLayout textViewSpecial;
	private LinearLayout textViewOther;

	private ArrayList<BasicInfo> list = new ArrayList<BasicInfo>();

	private Loader loader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loader = new Loader(MainActivity.this);

		mlistView = (ListView) findViewById(R.id.list_view);

		textViewPeople = (LinearLayout) findViewById(R.id.tab_people);
		textViewPeople.setSelected(true);
		textViewPeople.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				textViewOther.setSelected(false);
				textViewSpecial.setSelected(false);
				textViewOrganisation.setSelected(false);
				textViewPeople.setSelected(true);

				chosenType = TYPE_PEOPLE;
				loadData();
			};
		});

		textViewOrganisation = (LinearLayout) findViewById(R.id.tab_organisations);
		textViewOrganisation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				textViewOther.setSelected(false);
				textViewSpecial.setSelected(false);
				textViewOrganisation.setSelected(true);
				textViewPeople.setSelected(false);

				chosenType = TYPE_ORGANIZATION;
				loadData();
			};
		});

		textViewSpecial = (LinearLayout) findViewById(R.id.tab_special);
		textViewSpecial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				textViewOther.setSelected(false);
				textViewSpecial.setSelected(true);
				textViewOrganisation.setSelected(false);
				textViewPeople.setSelected(false);

				chosenType = TYPE_SPECIAL;
				loadData();
			};
		});

		textViewOther = (LinearLayout) findViewById(R.id.tab_other);
		textViewOther.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				textViewOther.setSelected(true);
				textViewSpecial.setSelected(false);
				textViewOrganisation.setSelected(false);
				textViewPeople.setSelected(false);

				chosenType = TYPE_OTHER;
				loadData();
			};
		});

		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int campaignID = (Integer) view.getTag(R.id.item_image);

				DatabaseHelper db = new DatabaseHelper(MainActivity.this);
				FullInfo fullInfo = db.getCampaignByID(campaignID);
				db.close();
				if (fullInfo != null) {
					Intent intent = new Intent(MainActivity.this,
							CampaignProfileActivity.class);
					intent.putExtra("full", fullInfo);
					startActivity(intent);
					// first one pushes the new activity in
					// second one pushes the old activity out
					overridePendingTransition(R.anim.in_campaign,
							R.anim.out_main);
				}

			}
		});

		getData();

	}

	public void getDataAndLoad(boolean update) {

		GetDataTask task = new GetDataTask(this) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				loadData();
			}

		};

		if (!loader.isShowing())
			loader.show();

		task.execute(update);

	}

	public void getData() {

		DatabaseHelper db = new DatabaseHelper(this);
		int count = db.getCount();
		db.close();

		if (Utils.haveNetworkConnection(this)) {

			Log.e("TEST", "INTERNET YES");
			/* Internet YES, Check DB */

			if (count == 0) {
				Log.e("TEST", "INTERNET YES, EMPTY DB");
				/* DB is EMPTY, Get DB from API */
				getDataAndLoad(false);

			} else {

				Log.e("TEST", "INTERNET YES, CHECK FOR NEW DB");
				/* DB is NOT EMPTY, Check for new version */
				checkForNewDB();

			}

		} else {

			Log.e("TEST", "INTERNET NO");
			/* Internet NO, Check DB */

			if (count == 0) {

				Log.e("TEST", "INTERNET NO, EMPTY DB");
				/* DB is EMPTY, Show Internet Message */
				if (loader.isShowing())
					loader.dismiss();

				Utils.noInternetDialog(MainActivity.this);

			} else {
				Log.e("TEST", "INTERNET NO, LOAD DB");
				/* DB is NOT EMPTY, Load data from DB */
				loadData();
			}

		}

	}

	private void checkForNewDB() {

		GetDataVersion task = new GetDataVersion(this) {

			@Override
			protected void onPostExecute(Boolean getData) {

				/* DB is NOT EMPTY, Check for new DB */
				if (getData) {
					Log.e("TEST", "CHECK FOR NEW DB, NEW DB");
					/* NEW DB, Update DB */
					getDataAndLoad(true);
				} else {
					Log.e("TEST", "CHECK FOR NEW DB, NO NEW DB");
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
		DatabaseHelper db = new DatabaseHelper(MainActivity.this);
		list = db.getBasicInfoByType(chosenType);
		db.close();

		if (list == null)
			list = new ArrayList<BasicInfo>();

	}

	private void loadData() {

		/* Get data from DB */
		getBasicInfoFromDB();

		this.adapter = new CustomAdapter(this, R.layout.list_item, list);
		mlistView.setAdapter(adapter);

		if (loader.isShowing())
			loader.dismiss();

	}

}
