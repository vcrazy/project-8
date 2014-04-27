package com.example.project_8;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class TrackTask extends AsyncTask<String, Void, Boolean> {

	// ./track?phone_id=...&campaign_id=.
	private static final String URL = "http://ganev.bg/project-8/www/track";
	private static final String PHONE_ID_PARAM = "?phone_id=";
	private static final String CAMPAIGN_ID_PARAM = "&campaign_id=";

	@Override
	protected Boolean doInBackground(String... params) {

		String phoneID = params[0];
		String campaignID = params[1];

		String url = URL + PHONE_ID_PARAM + phoneID + CAMPAIGN_ID_PARAM
				+ campaignID;

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);

		try {
			HttpResponse response = client.execute(get);
			String jsonResponse = EntityUtils.toString(response.getEntity());

			if (response.getStatusLine().getStatusCode() > 300) {
				return false;
			}

			JSONObject json = new JSONObject(jsonResponse);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		Log.e("TEST", "track info sent");
		return true;
	}

}
