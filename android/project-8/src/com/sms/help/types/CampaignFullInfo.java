package com.sms.help.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class CampaignFullInfo extends CampaignBasicInfo implements Serializable {
	private static final long serialVersionUID = -997713783095087974L;

	public int SMSNumber = 0;
	public double SMSPrice = 0.0;
	public String SMSText;
	public int campaignStartDate = 0;
	public int campaignEndDate = 0;
	public String campaignType;
	public String campaignDescription;
	public String campaignLink;
	public long SMSSendDate;

	public String status;

	public CampaignFullInfo(int number, int id, double price, String text,
			String name, String subname, int startDate, int endDate,
			String type, String description, String image, String link,
			String status) {

		super(id, image, name, subname);

		this.SMSNumber = number;
		this.SMSPrice = price;
		this.SMSText = text;
		this.campaignStartDate = startDate;
		this.campaignEndDate = endDate;
		this.campaignType = type;
		this.campaignDescription = description;
		this.campaignLink = link;

		this.status = status;

	}

	public static ArrayList<CampaignFullInfo> parseData(Context context,
			JSONObject json) {

		Iterator keys = json.keys();

		final ArrayList<CampaignFullInfo> list = new ArrayList<CampaignFullInfo>();

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
				CampaignFullInfo info = new CampaignFullInfo(phoneNumber,
						campaignId, priceSMS, txtSms, campaignName,
						campaignSubname, startDate, endDate, campaignType,
						txtCampaign, image, link, status);

				list.add(info);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		return list;

	}

}
