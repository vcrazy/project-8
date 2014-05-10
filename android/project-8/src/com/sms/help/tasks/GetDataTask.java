package com.sms.help.tasks;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.sms.help.DatabaseHelper;
import com.sms.help.types.FullInfo;

public class GetDataTask extends AsyncTask<Boolean, Void, Boolean> {

	private static final String URL = "http://ganev.bg/project-8/api";
	public Context context;
	private ArrayList<FullInfo> list;

	public GetDataTask(Context context) {

		this.context = context;

	}

	@Override
	protected Boolean doInBackground(Boolean... params) {

		boolean update = params[0];

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(URL);

		try {
			HttpResponse response = client.execute(get);
			String jsonResponse = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(jsonResponse);

			JSONArray jsonData = json.getJSONArray("campaigns");
			list = FullInfo.parseData(jsonData);

			if (list != null) {
				// insert in db
				DatabaseHelper db = DatabaseHelper.getInstance(context);

				int count = db.getCount();
				if (count <= 0) {
					db.insertCampaigns(list, update);
				}

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

		return true;

	}
}
