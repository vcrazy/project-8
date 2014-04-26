package com.example.project_8;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	// people, organizations, other, special
	String TYPE_PEOPLE = "people";
	String TYPE_ORGANIZATION = "organizations";
	String TYPE_OTHER = "other";
	String TYPE_SPECIAL = "special";

	private String chosenType = TYPE_PEOPLE;

	private ListView mlistView;
	private CustomAdapter adapter;
	private TextView textViewPeople;
	private TextView textViewOrganisation;
	private TextView textViewSpecial;
	private TextView textViewOther;

	private ArrayList<BasicInfo> list = new ArrayList<BasicInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_navigation);
		mlistView = (ListView) findViewById(R.id.list_view);

		textViewPeople = (TextView) findViewById(R.id.tab_people);
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

		textViewOrganisation = (TextView) findViewById(R.id.tab_organisations);
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

		textViewSpecial = (TextView) findViewById(R.id.tab_special);
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

		textViewOther = (TextView) findViewById(R.id.tab_other);
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
		getData();

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
				}

			}
		});
	}

	public void getData() {

		GetDataTask task = new GetDataTask(this) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				loadData();
			}

		};

		// if internet and if db is empty
		DatabaseHelper db = new DatabaseHelper(this);
		int count = db.getCount();
		db.close();
		if (count == 0)
			task.execute();
		else {

			loadData();
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

	private void loadData() {
		getBasicInfoFromDB();

		this.adapter = new CustomAdapter(this, R.layout.list_item, list);
		mlistView.setAdapter(adapter);
	}
}
