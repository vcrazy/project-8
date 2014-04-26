package com.example.project_8;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

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

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);

		builder.setTitle(R.string.app_name);

		builder.setMessage("Необходима е връзка с Интернет");

		builder.setNeutralButton("Ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				((Activity) context).finish();

			}
		});

		builder.create().show();

	}
}
