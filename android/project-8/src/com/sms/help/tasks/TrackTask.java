package com.sms.help.tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

@Deprecated
public class TrackTask extends AsyncTask<String, Void, Boolean> {

	// ./track?phone_id=...&campaign_id=.
	private static final String URL = "http://ganev.bg/project-8/track";
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
			// String jsonResponse = EntityUtils.toString(response.getEntity());

			if (response.getStatusLine().getStatusCode() > 300) {
				return false;
			}

			// JSONObject json = new JSONObject(jsonResponse);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		// catch (JSONException e) {
		// e.printStackTrace();
		// return false;
		// }

		return true;
	}

}
