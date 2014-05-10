package com.sms.help;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import com.sms.help.R;

public class Loader extends Dialog {

	// private Context mContext;
	View v;

	public Loader(Context context) {

		super(context);

		// mContext = context;
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.loader);
		v = getWindow().getDecorView();
		v.setBackgroundResource(android.R.color.transparent);

		setCancelable(false);

	}

	@Override
	public void dismiss() {
		if (this != null && this.isShowing()) {
			super.dismiss();
		}
	}

}
