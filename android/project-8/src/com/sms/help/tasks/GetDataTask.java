package com.sms.help.tasks;

import java.io.IOException;
import java.util.ArrayList;

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
import com.sms.help.db.DatabaseHelper;
import com.sms.help.types.CampaignFullInfo;

public class GetDataTask extends AsyncTask<Void, Void, Boolean> {

	public Context context;
	private ArrayList<CampaignFullInfo> list;

	public GetDataTask(Context context) {

		this.context = context;

	}

	@Override
	protected Boolean doInBackground(Void... params) {

		String url = Constants.URL_DATA;

		// boolean update = params[0];
		DatabaseHelper db = DatabaseHelper.getInstance(context);
		String version = db.getVersion();
		if (version == null)
			version = "0";

		if (!version.equals("0"))
			url += Constants.VERSION_PARAM + version;

		Log.e("Test", url);

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);

		try {
			HttpResponse response = client.execute(get);
			String jsonResponse = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(jsonResponse);

			JSONObject jsonData = json.getJSONObject("campaigns");

			list = CampaignFullInfo.parseData(this.context, jsonData);

			if (list != null) {

				// int count = db.getCount();
				// if (count <= 0) {
				// db.insertCampaigns(list, update);
				// }

				if (version.equals("0")) {
					db.initCampaigns(list);
				} else {
					// Update campaigns
					updateCampaigns();

				}

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

	private void updateCampaigns() {

		for (int i = 0; i < list.size(); i++) {

			CampaignFullInfo item = list.get(i);
			String status = list.get(i).status;

			DatabaseHelper db = DatabaseHelper.getInstance(context);

			// delete
			if (status.equalsIgnoreCase("delete")) {
				db.deleteCampaign(item.campaignID);
			}
			// insert
			else if (status.equalsIgnoreCase("insert")) {
				ArrayList<CampaignFullInfo> l = new ArrayList<CampaignFullInfo>();
				l.add(item);
				db.initCampaigns(l);

			}
			// update
			else if (status.equalsIgnoreCase("update")) {
				db.updateCampaign(item);
			}

		}

	}
}
