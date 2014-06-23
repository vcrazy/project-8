package com.sms.help.tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sms.help.Constants;

public class GetDataVersionTask extends AsyncTask<Void, Void, String> {

	// private static final String URL =
	// "http://ganev.bg/project-8/api/version";
	public Context context;
	private String version;

	public GetDataVersionTask(Context context) {

		this.context = context;
	}

	@Override
	protected String doInBackground(Void... params) {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(Constants.URL_VERSION);

		try {

			HttpResponse response = client.execute(get);
			String jsonResponse = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(jsonResponse);
			version = json.getString(Constants.VERSION);

			Log.e("Test", "In get version, version is " + version);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return version;

	}
}
