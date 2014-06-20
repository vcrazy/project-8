package com.sms.help.types;

import java.io.Serializable;

public class CampaignBasicInfo implements Serializable {
	private static final long serialVersionUID = 7112406630921673067L;

	public int campaignID;
	public String campaignImageURL = "";
	public String campaignName = "";
	public String campaignSubname = "";

	public CampaignBasicInfo(int campaignID, String imageURL,
			String campaignName, String campaignSubname) {

		this.campaignID = campaignID;
		this.campaignImageURL = imageURL;
		this.campaignName = campaignName;
		this.campaignSubname = campaignSubname;

	}

}
