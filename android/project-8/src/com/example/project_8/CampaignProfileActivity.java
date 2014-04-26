package com.example.project_8;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CampaignProfileActivity extends Activity {

	/* Widgets */
	private ImageView imageView;
	private TextView textViewName;
	private TextView textViewcampaignSubName;
	private TextView textViewDescription;
	private TextView textViewStartDate;
	private TextView textViewEndDate;
	private Button btnSendSms;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campaign_info_layout);

		/* Get widgets */
		imageView = (ImageView) findViewById(R.id.imageView);
		textViewName = (TextView) findViewById(R.id.name);
		textViewcampaignSubName = (TextView) findViewById(R.id.campaignSubName);
		textViewDescription = (TextView) findViewById(R.id.description);
		textViewStartDate = (TextView) findViewById(R.id.startDate);
		textViewEndDate = (TextView) findViewById(R.id.endDate);
		btnSendSms = (Button) findViewById(R.id.send_sms);

		btnSendSms.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendSMS("17777", "msg");
			}
		});
	}

	private void sendSMS(String formattedNumbers, String txtSms) {
		// with intent
		String uri = "smsto:" + formattedNumbers;
		Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
		smsIntent.putExtra("sms_body", txtSms);// F1RST
		// SMS
		smsIntent.putExtra("compose_mode", true);
		startActivity(smsIntent);
	}
}
