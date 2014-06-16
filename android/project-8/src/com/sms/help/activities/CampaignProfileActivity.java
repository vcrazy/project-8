package com.sms.help.activities;

import java.text.DecimalFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sms.help.DatabaseHelper;
import com.sms.help.R;
import com.sms.help.types.FullInfo;
import com.squareup.picasso.Picasso;

public class CampaignProfileActivity extends Activity {

	private static final int REQUEST_CODE_SMS = 10;

	/* Widgets */
	private ImageView imageView;
	private TextView textViewName;
	private TextView textViewcampaignSubName;
	private TextView textViewDescription;
	private TextView textViewStartDate;
	private TextView textViewEndDate;
	private TextView textViewStartDateTitle;
	private TextView textViewPriceSMS;
	private TextView textViewNumberSMS;
	private TextView textViewLinkToWeb;
	private Button btnSendSms;

	/* Full data */
	private FullInfo campaignInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campaign_profile);

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
		textViewStartDateTitle = (TextView) findViewById(R.id.startDateTitle);

		textViewPriceSMS = (TextView) findViewById(R.id.pricesms);
		textViewNumberSMS = (TextView) findViewById(R.id.numbersms);
		textViewLinkToWeb = (TextView) findViewById(R.id.link_to_web);
		btnSendSms = (Button) findViewById(R.id.send_sms);

		textViewName.setText(campaignInfo.campaignName);
		textViewcampaignSubName.setText(campaignInfo.campaignSubName);
		DecimalFormat df = new DecimalFormat("#.##");
		String moneyConverted = String
				.valueOf(df.format(campaignInfo.priceSMS));
		textViewPriceSMS.setText(moneyConverted + " "
				+ getString(R.string.bg_money));

		// imageView.setImageBitmap(Utils.getImageBitmap(campaignInfo.imageUri));
		Picasso.with(this).load(campaignInfo.imageUri).into(imageView);

		textViewDescription.setText(campaignInfo.txtCampaign);

		textViewNumberSMS.setText(Integer.toString(campaignInfo.phoneNumber));

		textViewLinkToWeb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(campaignInfo.campaignLink));
				startActivity(browserIntent);
			}
		});
		if (campaignInfo.startDate != 0) {
			textViewStartDate
					.setText(parseUnixTimeToDate(campaignInfo.startDate));
		} else {
			textViewStartDate.setVisibility(View.GONE);
			textViewStartDateTitle.setVisibility(View.GONE);
		}

		btnSendSms.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				sendSMS(String.valueOf(campaignInfo.phoneNumber),
						campaignInfo.txtSMS);

				DatabaseHelper db = DatabaseHelper
						.getInstance(CampaignProfileActivity.this);
				db.insertStatistics(System.currentTimeMillis(), 0,
						campaignInfo.campaignId);
				// db.close();

			}
		});
	}

	public String parseUnixTimeToDate(long unixTimeStamp) {

		long time = unixTimeStamp * (long) 1000;
		java.util.Date date = new java.util.Date(time);
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.setTime(date);
		int day = myCalendar.get(Calendar.DAY_OF_MONTH);
		int month = myCalendar.get(Calendar.MONTH);
		int year = myCalendar.get(Calendar.YEAR);
		String onlyDate = String.valueOf(day) + " "
				+ getResources().getStringArray(R.array.months_names)[month]
				+ " " + String.valueOf(year);

		return onlyDate;
	}

	/* Version 1.1. Remove tracking for campaigns. */
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// if (requestCode == REQUEST_CODE_SMS) {
	//
	// sendTrackInfo(campaignInfo.campaignId);
	//
	// }
	// }

	private void sendSMS(String formattedNumbers, String txtSms) {
		try {
			String uri = "smsto:" + formattedNumbers;
			Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
			smsIntent.putExtra("sms_body", txtSms);
			smsIntent.putExtra("compose_mode", true);
			startActivityForResult(smsIntent, REQUEST_CODE_SMS);
		} catch (ActivityNotFoundException e) {

		}
	}

	// private void sendTrackInfo(int campaignID) {
	//
	// TelephonyManager telephonyManager = (TelephonyManager)
	// getSystemService(Context.TELEPHONY_SERVICE);
	// String deviceID = telephonyManager.getDeviceId();
	//
	// TrackTask task = new TrackTask();
	// task.execute(deviceID, String.valueOf(campaignID));
	//
	// }

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		overridePendingTransition(R.anim.in_old_activity,
				R.anim.out_new_activity);
	}
}
