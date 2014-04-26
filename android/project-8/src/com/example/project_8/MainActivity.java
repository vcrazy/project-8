package com.example.project_8;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView mlistView;
	private CustomAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_navigation);
		mlistView = (ListView) findViewById(R.id.list_view);
		loadData();
		mlistView.setAdapter(adapter);
	}

	public void loadData() {
		ArrayList<BasicInfo> list = new ArrayList<BasicInfo>();
		BasicInfo obj1 = new BasicInfo(
				"http://dmsbg.com/files/projects_file2_1398501015.jpg",
				"Da pomognem MIRO", "info1");
		BasicInfo obj2 = new BasicInfo(
				"http://dmsbg.com/files/projects_file2_1397180374.jpg",
				"Da pomognem Vanq", "info2");
		list.add(obj1);
		list.add(obj2);
		this.adapter = new CustomAdapter(this, R.layout.list_item, list);
	}

}
