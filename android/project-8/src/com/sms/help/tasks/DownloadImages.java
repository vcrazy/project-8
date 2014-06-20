package com.sms.help.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class DownloadImages extends
		AsyncTask<ArrayList<String>, Void, ArrayList<Bitmap>> {

	private Context context;

	public DownloadImages(Context context) {
		this.context = context;
	}

	@Override
	protected ArrayList<Bitmap> doInBackground(ArrayList<String>... params) {

		ArrayList<String> urls = params[0];
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

		for (int i = 0; i < urls.size(); i++) {

			try {
				URL url = new URL(urls.get(i));

				URLConnection connection = url.openConnection();
				connection.setUseCaches(true);

				Bitmap bitmap = BitmapFactory
						.decodeStream((InputStream) connection.getContent());
				bitmaps.add(bitmap);

				saveImageToCache(urls.get(i), bitmap);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return bitmaps;
	}

	private void saveImageToCache(String name, Bitmap bitmap)
			throws IOException {

		String path = context.getExternalCacheDir().toString();

		OutputStream fOut = null;
		File file = new File(path, URLEncoder.encode(name, "utf-8"));

		file.getParentFile().mkdirs();
		fOut = new FileOutputStream(file);

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		fOut.flush();
		fOut.close();

	}

}
