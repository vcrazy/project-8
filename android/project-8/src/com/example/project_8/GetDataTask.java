package com.example.project_8;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class GetDataTask extends AsyncTask<Void, Void, Void> {

	private static final String URL = "";

	@Override
	protected Void doInBackground(Void... params) {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpPost;
		HttpResponse response;

		return null;
	}

}
