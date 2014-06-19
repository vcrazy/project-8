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
				String campaignName = jsonData.isNull("name") ? "" : jsonData
						.getString("name");
				// 'subname'
				String campaignSubname = jsonData.isNull("subname") ? ""
						: jsonData.getString("subname");
				// 'type'
				String campaignType = jsonData.isNull("type") ? "" : jsonData
						.getString("type");

				// 'text'
				String txtCampaign = jsonData.isNull("text") ? "" : jsonData
						.getString("text");

				// 'donation'
				double priceSMS = jsonData.isNull("donation") ? 0.0 : Double
						.valueOf(jsonData.getInt("donation"));
				// 'picture'
				String image = jsonData.isNull("picture") ? "" : jsonData
						.getString("picture");
				// 'link'
				String link = jsonData.isNull("link") ? "" : jsonData
						.getString("link");
				// 'sms_text'
				String txtSms = jsonData.isNull("sms_text") ? "" : jsonData
						.getString("sms_text");
				// 'sms_number'
				int phoneNumber = jsonData.isNull("sms_number") ? 0 : jsonData
						.getInt("sms_number");
				// 'date_from'
				final int startDate = jsonData.isNull("date_from") ? 0
						: jsonData.getInt("date_from");
				final int endDate = 0;

				// 'status'
				String status = jsonData.isNull("status") ? "" : jsonData
						.getString("status");

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
