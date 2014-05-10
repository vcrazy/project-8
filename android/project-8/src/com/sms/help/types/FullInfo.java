package com.sms.help.types;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FullInfo extends BasicInfo implements Serializable {
	private static final long serialVersionUID = -997713783095087974L;

	public int phoneNumber;
	// public int campaignId;
	public double priceSMS;
	public String txtSMS;
	public int startDate;
	public int endDate;
	public String campaignType;
	public String txtCampaign;
	public String campaignLink;
	public long smsSendDate;

	public FullInfo(int phoneNumber, int campaignId, double priceSMS,
			String txtSMS, String campaignName, String campaignSubName,
			int startDate, int endDate, String campaignType,
			String txtCampaign, String image, String campaignLink) {

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

	}

	public static ArrayList<FullInfo> parseData(JSONArray json) {

		ArrayList<FullInfo> list = new ArrayList<FullInfo>();

		// get json object from json array
		for (int i = 0; i < json.length(); i++) {

			try {
				JSONObject jsonData = json.getJSONObject(i);
				// 'id'
				int campaignId = jsonData.getInt("id");
				// 'name'
				String campaignName = jsonData.getString("name");
				// 'subname'
				String campaignSubname = jsonData.getString("subname");
				// 'type'
				String campaignType = jsonData.getString("type");
				// 'text'
				String txtCampaign = jsonData.getString("text");
				// 'donation'
				double priceSMS = Double.valueOf(jsonData.getInt("donation"));
				// 'picture'
				String image = jsonData.getString("picture");
				// 'link'
				String campaignLink = jsonData.getString("link");
				// 'sms_text'
				String txtSms = jsonData.getString("sms_text");
				// 'sms_number'
				int phoneNumber = jsonData.getInt("sms_number");
				// 'date_from'
				int startDate = jsonData.getInt("date_from");
				int endDate = 0;

				// create new object
				FullInfo info = new FullInfo(phoneNumber, campaignId, priceSMS,
						txtSms, campaignName, campaignSubname, startDate,
						endDate, campaignType, txtCampaign, image, campaignLink);

				list.add(info);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		return list;

	}
}
