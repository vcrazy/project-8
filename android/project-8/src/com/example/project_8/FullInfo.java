package com.example.project_8;

public class FullInfo extends BasicInfo {
	private int phoneNumber;
	private int compaignId;
	private double priceSMS;
	private String txtSMS;
	private String compaignName;
	private String compaignSubName;
	private String startDate;
	private String endDate;
	private String campaignType;
	private String txtCompaign;
	private String image;
	private String compaignLink;

	public FullInfo(int phoneNumber, int compaignId, double priceSMS,
			String txtSMS, String compaignName, String compaignSubName,
			String startDate, String endDate, String campaignType,
			String txtCompaign, String image, String compaignLink) {
		super(image, compaignName, compaignSubName);
		// TODO Auto-generated constructor stub
	}
}
