package com.example.project_8;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CampaignProfileActivity extends Activity {

	/* Widgets */
	private ImageView imageView;
	private TextView textViewName;
	private TextView textViewDate;
	private TextView textViewDescription;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campaign_info_layout);

		/* Get widgets */
		imageView = (ImageView) findViewById(R.id.imageView);
		textViewName = (TextView) findViewById(R.id.name);
		textViewDate = (TextView) findViewById(R.id.date);
		textViewDescription = (TextView) findViewById(R.id.description);

	}

}
