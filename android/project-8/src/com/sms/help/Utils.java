package com.sms.help;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Utils {

	public static Bitmap getImageBitmap(String image) {

		if (image != null) {
			byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
			Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString,
					0, decodedString.length);
			return decodedBitmap;
		} else
			return null;
	}

	public static boolean haveNetworkConnection(Context context) {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	public static void noInternetDialog(final Context context) {

		View customView = View.inflate(context, R.layout.dialog_no_internet,
				null);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);

		builder.setCustomTitle(customView);

		final AlertDialog dialog = builder.create();
		// View v = dialog.getWindow().getDecorView();
		// v.setBackgroundResource(android.R.color.transparent);

		Button okButton = (Button) customView.findViewById(R.id.button);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				((Activity) context).finish();

			}
		});

		dialog.show();

	}

	public static Bitmap readImageFromCache(Context context, String name) {

		Bitmap returnBitmap = null;

		String path = context.getExternalFilesDir(null).toString();

		try {

			File file = new File(path, URLEncoder.encode(name, "utf-8"));

			if (file.exists()) {

				FileInputStream streamIn = new FileInputStream(file);

				returnBitmap = BitmapFactory.decodeStream(streamIn);

				streamIn.close();
			}

		} catch (IOException e) {

		}

		return returnBitmap;

	}

	public static void deleteImageFromCache(Context context, String name) {

		String path = context.getExternalFilesDir(null).toString();

		try {

			File file = new File(path, URLEncoder.encode(name, "utf-8"));

			if (file.exists()) {

				file.delete();
			}

		} catch (IOException e) {

		}

	}
}
