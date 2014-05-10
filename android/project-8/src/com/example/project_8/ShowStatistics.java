package com.example.project_8;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.sms.help.R;

public class ShowStatistics extends Activity {
	private ListView mlistView;
	private CustomAdapterStatistics adapter;
	private ArrayList<FullInfo> list = new ArrayList<FullInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_statistics);

		mlistView = (ListView) findViewById(R.id.list_view_statistics);

		loadData();
	}

	private void getBasicInfoFromDB() {

		/* Get data from DB */
		DatabaseHelper db = new DatabaseHelper(ShowStatistics.this);
		list = db.getAllStatistics();
		db.close();

		if (list == null)
			list = new ArrayList<FullInfo>();

	}

	private void loadData() {

		/* Get data from DB */
		getBasicInfoFromDB();

		this.adapter = new CustomAdapterStatistics(this,
				R.layout.list_item_statistics, list);
		mlistView.setAdapter(adapter);

		// if (loader.isShowing())
		// loader.dismiss();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		overridePendingTransition(R.anim.in_main, R.anim.out_campaign);
	}

}
