package com.sms.help.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class FullInfo extends BasicInfo implements Serializable {
	private static final long serialVersionUID = -997713783095087974L;

	public int phoneNumber = 0;
	// public int campaignId;
	public double priceSMS = 0.0;
	public String txtSMS;
	public int startDate = 0;
	public int endDate = 0;
	public String campaignType;
	public String txtCampaign;
	public String campaignLink;
	public long smsSendDate;

	public String status;

	public FullInfo(int phoneNumber, int campaignId, double priceSMS,
			String txtSMS, String campaignName, String campaignSubName,
			int startDate, int endDate, String campaignType,
			String txtCampaign, String image, String campaignLink, String status) {

		super(campaignId, image, campaignName, campaignSubName);

		this.phoneNumber = phoneNumber;
		// this.campaignId = campaignId;
		this.priceSMS = priceSMS;
		this.txtSMS = txtSMS;
		this.startDate = startDate;
		this.endDate = endDate;
		this.campaignType = campaignType;
		this.txtCampaign = txtCampaign;
		this.campaignLink = campaignLink;

		this.status = status;

	}

	public static ArrayList<FullInfo> parseData(Context context, JSONObject json) {

		Iterator keys = json.keys();

		final ArrayList<FullInfo> list = new ArrayList<FullInfo>();

		// get json object from json array
		while (keys.hasNext()) {

			try {
				String key = String.valueOf(keys.next());
				JSONObject jsonData = json.getJSONObject(key);
				// 'id'
				final int campaignId = Integer.valueOf(key);
				// 'name'
				final String campaignName = jsonData.getString("name");
				// 'subname'
				final String campaignSubname = jsonData.getString("subname");
				// 'type'
				final String campaignType = jsonData.getString("type");
				// 'text'
				final String txtCampaign = jsonData.getString("text");
				// 'donation'
				final double priceSMS = Double.valueOf(jsonData
						.getInt("donation"));
				// 'picture'
				final String image = jsonData.getString("picture");
				// 'link'
				final String link = jsonData.getString("link");
				// 'sms_text'
				final String txtSms = jsonData.getString("sms_text");
				// 'sms_number'
				final int phoneNumber = jsonData.getInt("sms_number");
				// 'date_from'
				final int startDate = jsonData.getInt("date_from");
				final int endDate = 0;

				// 'status'
				final String status = jsonData.getString("status");

				// create new object
				FullInfo info = new FullInfo(phoneNumber, campaignId, priceSMS,
						txtSms, campaignName, campaignSubname, startDate,
						endDate, campaignType, txtCampaign, image, link, status);

				list.add(info);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		return list;

	}

}
