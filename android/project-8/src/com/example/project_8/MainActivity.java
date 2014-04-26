package com.example.project_8;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView mlistView;
	private CustomAdapter adapter;

	private ArrayList<FullInfo> fullList = new ArrayList<FullInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_navigation);
		mlistView = (ListView) findViewById(R.id.list_view);
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
