package com.sms.help.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.sms.help.R;
import com.sms.help.adapters.StatisticsAdapter;
import com.sms.help.db.DatabaseHelper;
import com.sms.help.types.CampaignFullInfo;

public class StatisticsActivity extends Activity {

	private ListView listViewStatistics;
	private StatisticsAdapter adapterStatistics;
	private ArrayList<CampaignFullInfo> listCampaigns = new ArrayList<CampaignFullInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		listViewStatistics = (ListView) findViewById(R.id.listview);
		findViewById(R.id.imageview_statistics).setVisibility(View.GONE);

		loadData();

	}

	/** Get list of campaigns from database */
	private void getCampaignsFromDB() {

		DatabaseHelper db = DatabaseHelper.getInstance(StatisticsActivity.this);
		listCampaigns = db.getAllStatistics();

		if (listCampaigns == null)
			listCampaigns = new ArrayList<CampaignFullInfo>();

	}

	/** Load data */
	private void loadData() {

		getCampaignsFromDB();

		this.adapterStatistics = new StatisticsAdapter(this,
				R.layout.list_item_statistics, listCampaigns);
		listViewStatistics.setAdapter(adapterStatistics);

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

}
