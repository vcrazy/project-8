package com.example.project_8;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	// people, organization, other, special
	String TYPE_PEOPLE = "people";
	String TYPE_ORGANIZATION = "organization";
	String TYPE_OTHER = "other";
	String TYPE_SPECIAL = "special";

	private String chosenType = TYPE_PEOPLE;

	private ListView mlistView;
	private CustomAdapter adapter;

	private ArrayList<BasicInfo> list = new ArrayList<BasicInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_navigation);
		mlistView = (ListView) findViewById(R.id.list_view);
		loadData();

		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int campaignID = (Integer) view.getTag(R.id.item_image);

				DatabaseHelper db = new DatabaseHelper(MainActivity.this);
				FullInfo fullInfo = db.getCampaignByID(campaignID);
				db.close();
				if (fullInfo != null) {
					Log.e("FULL", fullInfo.campaignName);
				}

			}
		});
	}

	public void loadData() {

		GetDataTask task = new GetDataTask(this) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				getBasicInfoFromDB();

				adapter = new CustomAdapter(MainActivity.this,
						R.layout.list_item, list);
				mlistView.setAdapter(adapter);
			}

		};

		// if internet and if db is empty
		DatabaseHelper db = new DatabaseHelper(this);
		int count = db.getCount();
		db.close();
		if (count == 0)
			task.execute();
		else {

			getBasicInfoFromDB();

			this.adapter = new CustomAdapter(this, R.layout.list_item, list);
			mlistView.setAdapter(adapter);
		}

	}

	private void getBasicInfoFromDB() {

		DatabaseHelper db = new DatabaseHelper(MainActivity.this);
		list = db.getBasicInfoByType(chosenType);
		db.close();
		Toast.makeText(MainActivity.this, "Campaigns " + list.size(),
				Toast.LENGTH_SHORT).show();

		if (list == null)
			list = new ArrayList<BasicInfo>();

	}
}
