package com.sms.help.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.sms.help.R;
import com.sms.help.adapters.StatisticsAdapter;
import com.sms.help.db.DatabaseHelper;
import com.sms.help.types.CampaignFullInfo;

public class ShowStatisticsActivity extends Activity {
	private ListView mlistView;
	private StatisticsAdapter adapter;
	private ArrayList<CampaignFullInfo> list = new ArrayList<CampaignFullInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_statistics);

		mlistView = (ListView) findViewById(R.id.list_view_statistics);

		loadData();
	}

	private void getBasicInfoFromDB() {

		/* Get data from DB */
		DatabaseHelper db = DatabaseHelper
				.getInstance(ShowStatisticsActivity.this);
		list = db.getAllStatistics();
		// db.close();

		if (list == null)
			list = new ArrayList<CampaignFullInfo>();

	}

	private void loadData() {

		/* Get data from DB */
		getBasicInfoFromDB();

		this.adapter = new StatisticsAdapter(this,
				R.layout.list_item_statistics, list);
		mlistView.setAdapter(adapter);

		// if (loader.isShowing())
		// loader.dismiss();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		overridePendingTransition(R.anim.in_old_activity,
				R.anim.out_new_activity);
	}

}
