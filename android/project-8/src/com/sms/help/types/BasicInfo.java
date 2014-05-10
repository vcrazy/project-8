package com.sms.help.types;

import java.io.Serializable;

public class BasicInfo implements Serializable {
	private static final long serialVersionUID = 7112406630921673067L;

	public int campaignId;
	public String imageUri;
	public String campaignName;
	public String campaignSubName;

	public BasicInfo(int campaignId, String imageUri, String campaignName,
			String campaignSubName) {
		this.campaignId = campaignId;
		this.imageUri = imageUri;
		this.campaignName = campaignName;
		this.campaignSubName = campaignSubName;
	}

}
