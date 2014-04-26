package com.example.project_8;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView mlistView;
	private CustomAdapter adapter;
	private TextView textViewPeople;
	private TextView textViewOrganisation;
	private TextView textViewSpecial;
	private TextView textViewOther;

	private ArrayList<FullInfo> fullList = new ArrayList<FullInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_navigation);
		mlistView = (ListView) findViewById(R.id.list_view);
		textViewPeople = (TextView) findViewById(R.id.tab_people);
		textViewPeople.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textViewOther.setSelected(false);
				textViewSpecial.setSelected(false);
				textViewOrganisation.setSelected(false);
				textViewPeople.setSelected(true);
			};
		});

		textViewOrganisation = (TextView) findViewById(R.id.tab_organisations);
		textViewOrganisation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textViewOther.setSelected(false);
				textViewSpecial.setSelected(false);
				textViewOrganisation.setSelected(true);
				textViewPeople.setSelected(false);
			};
		});

		textViewSpecial = (TextView) findViewById(R.id.tab_special);
		textViewSpecial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textViewOther.setSelected(false);
				textViewSpecial.setSelected(true);
				textViewOrganisation.setSelected(false);
				textViewPeople.setSelected(false);
			};
		});

		textViewOther = (TextView) findViewById(R.id.tab_other);
		textViewOther.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textViewOther.setSelected(true);
				textViewSpecial.setSelected(false);
				textViewOrganisation.setSelected(false);
				textViewPeople.setSelected(false);
			};
		});
		loadData();
		mlistView.setAdapter(adapter);
	}

	public void loadData() {

		GetDataTask task = new GetDataTask(this) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				DatabaseHelper db = new DatabaseHelper(MainActivity.this);
				fullList = db.getAllCampaigns();

				Toast.makeText(MainActivity.this,
						"Campaigns " + fullList.size(), Toast.LENGTH_SHORT)
						.show();
			}

		};

		task.execute();

		ArrayList<BasicInfo> list = new ArrayList<BasicInfo>();
		BasicInfo obj1 = new BasicInfo(null, "Da pomognem MIRO", "info1");
		BasicInfo obj2 = new BasicInfo(null, "Da pomognem Vanq", "info2");
		list.add(obj1);
		list.add(obj2);
		this.adapter = new CustomAdapter(this, R.layout.list_item, list);
	}

}
