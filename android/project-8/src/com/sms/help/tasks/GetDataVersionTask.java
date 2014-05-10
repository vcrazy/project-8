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

import com.sms.help.Constants;
import com.sms.help.DatabaseHelper;

public class GetDataVersionTask extends AsyncTask<Void, Void, Boolean> {

	// private static final String URL =
	// "http://ganev.bg/project-8/api/version";
	public Context context;
	private String version;

	public GetDataVersionTask(Context context) {

		this.context = context;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		boolean result = false;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(Constants.URL_VERSION);

		try {
			HttpResponse response = client.execute(get);
			String jsonResponse = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(jsonResponse);
			version = json.getString(Constants.VERSION);

			if (version != null) {
				// insert in db
				DatabaseHelper db = DatabaseHelper.getInstance(context);

				result = db.insertVersion(version);

				// db.close();

			} else {
				return false;
			}

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

		return result;

	}
}
