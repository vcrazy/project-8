package com.example.project_8;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CampaignProfileActivity extends Activity {

	private static final int REQUEST_CODE = 10;

	/* Widgets */
	private ImageView imageView;
	private TextView textViewName;
	private TextView textViewcampaignSubName;
	private TextView textViewDescription;
	private TextView textViewStartDate;
	private TextView textViewEndDate;
	private Button btnSendSms;

	/* Full data */
	private FullInfo campaignInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campaign_info_layout);

		/* Get full info */
		if (getIntent().hasExtra("full")) {
			campaignInfo = (FullInfo) getIntent().getSerializableExtra("full");
		}

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
				sendSMS(String.valueOf(campaignInfo.phoneNumber),
						campaignInfo.txtSMS);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.e("TEST", "Request code " + requestCode + " RESULT = " + resultCode
				+ " is result canceled " + (resultCode == RESULT_CANCELED));
	}

	private void sendSMS(String formattedNumbers, String txtSms) {

		String uri = "smsto:" + formattedNumbers;
		Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
		smsIntent.putExtra("sms_body", txtSms);
		smsIntent.putExtra("compose_mode", true);
		startActivityForResult(smsIntent, REQUEST_CODE);
	}
}
