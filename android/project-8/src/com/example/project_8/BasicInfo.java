package com.example.project_8;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

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

	public static Bitmap getImageBitmap(String image) {

		if (image != null) {
			byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
			Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString,
					0, decodedString.length);
			return decodedBitmap;
		} else
			return null;
	}

}
