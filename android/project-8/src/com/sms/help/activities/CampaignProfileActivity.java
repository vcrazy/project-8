package com.sms.help.activities;

import java.text.DecimalFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sms.help.R;
import com.sms.help.Utils;
import com.sms.help.db.DatabaseHelper;
import com.sms.help.types.CampaignFullInfo;

public class CampaignProfileActivity extends Activity implements
		OnClickListener {

	private static final int REQUEST_CODE_SMS = 10;

	/* Widgets */
	private ImageView imageViewPicture;
	private TextView textViewCampaignName;
	private TextView textViewCampaignSubname;
	private TextView textViewCampaignDescription;
	private LinearLayout layoutCampaignStartDate;
	private TextView textViewCampaignStartDate;
	private TextView textViewSMSPrice;
	private TextView textViewSMSNumber;
	private TextView textViewCampaignLink;
	private Button buttonSendSMS;

	/* Full data */
	private CampaignFullInfo campaign;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campaign_profile);

		/* Get full info */
		if (getIntent().hasExtra("full")) {
			campaign = (CampaignFullInfo) getIntent().getSerializableExtra(
					"full");
		}

		/* Get widgets */
		initWidgets();

		/* Load campaign information */
		loadCampaignInformation();

		/* On Click Listeners */
		textViewCampaignLink.setOnClickListener(this);
		buttonSendSMS.setOnClickListener(this);
	}

	/** On Back Pressed */
	@Override
	public void onBackPressed() {
		super.onBackPressed();

		overridePendingTransition(R.anim.in_old_activity,
				R.anim.out_new_activity);
	}

	/** Get all widgets */
	private void initWidgets() {

		imageViewPicture = (ImageView) findViewById(R.id.imageview_picture);
		textViewCampaignName = (TextView) findViewById(R.id.textview_campaign_name);
		textViewCampaignSubname = (TextView) findViewById(R.id.textview_campaign_subname);
		layoutCampaignStartDate = (LinearLayout) findViewById(R.id.layout_camapign_startdate);
		textViewCampaignStartDate = (TextView) findViewById(R.id.textview_campaign_startdate);
		textViewCampaignDescription = (TextView) findViewById(R.id.textview_campaign_description);
		textViewCampaignLink = (TextView) findViewById(R.id.textview_campaign_link);

		textViewSMSPrice = (TextView) findViewById(R.id.textview_sms_price);
		textViewSMSNumber = (TextView) findViewById(R.id.textview_sms_number);

		buttonSendSMS = (Button) findViewById(R.id.button_send_sms);

	}

	/** Load campaign information */
	private void loadCampaignInformation() {

		Bitmap image = Utils
				.readImageFromCache(this, campaign.campaignImageURL);
		if (image != null)
			imageViewPicture.setImageBitmap(image);

		textViewCampaignName.setText(campaign.campaignName);
		textViewCampaignSubname.setText(campaign.campaignSubname);

		if (campaign.campaignStartDate != 0) {
			textViewCampaignStartDate
					.setText(parseUnixTimeToDate(campaign.campaignStartDate));
		} else {
			layoutCampaignStartDate.setVisibility(View.GONE);

		}

		textViewCampaignDescription.setText(campaign.campaignDescription);

		DecimalFormat df = new DecimalFormat("#.##");
		String moneyConverted = String.valueOf(df.format(campaign.SMSPrice));
		textViewSMSPrice.setText(moneyConverted + " "
				+ getString(R.string.bg_money));

		textViewSMSNumber.setText(Integer.toString(campaign.SMSNumber));

	}

	/** Parse unix time stamp from database to date */
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

	/** On Click Listener for campaign link and send sms button */
	@Override
	public void onClick(View v) {

		if (v.equals(textViewCampaignLink)) {

			openLink();

		} else if (v.equals(buttonSendSMS)) {

			sendSMS();

		}

	}

	/** Open link in browser */
	private void openLink() {

		Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(campaign.campaignLink));
		startActivity(browserIntent);

	}

	/** Send SMS */
	private void sendSMS() {

		try {

			String uri = "smsto:" + String.valueOf(campaign.SMSNumber);
			Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
			smsIntent.putExtra("sms_body", campaign.SMSText);
			smsIntent.putExtra("compose_mode", true);
			startActivityForResult(smsIntent, REQUEST_CODE_SMS);

			/*
			 * if SMS composer opened with success, insert into database for
			 * statistics
			 */
			DatabaseHelper db = DatabaseHelper
					.getInstance(CampaignProfileActivity.this);
			db.insertStatistics(System.currentTimeMillis(), 0,
					campaign.campaignID);

		} catch (ActivityNotFoundException e) {

		}

	}

}
