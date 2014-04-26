package com.example.project_8;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

public class MainActivity extends SherlockActivity implements
		ActionBar.TabListener {
	private ListView mlistView;
	private CustomAdapter adapter;

	private ArrayList<FullInfo> fullList = new ArrayList<FullInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(com.actionbarsherlock.R.style.Sherlock___Theme); // Used for
																	// theme
																	// switching
																	// in
																	// samples
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_navigation);
		mlistView = (ListView) findViewById(R.id.list_view);
		loadData();
		mlistView.setAdapter(adapter);

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (int i = 1; i <= 4; i++) {
			ActionBar.Tab tab = getSupportActionBar().newTab();
			tab.setText("Tab " + i);
			tab.setTabListener(this);
			getSupportActionBar().addTab(tab);
		}
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

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction transaction) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {
		// mSelected.setText("Selected: " + tab.getText());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
	}

}
