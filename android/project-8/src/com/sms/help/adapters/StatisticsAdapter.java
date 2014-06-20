package com.sms.help.adapters;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sms.help.R;
import com.sms.help.R.array;
import com.sms.help.R.id;
import com.sms.help.R.layout;
import com.sms.help.R.string;
import com.sms.help.types.CampaignFullInfo;
import com.squareup.picasso.Picasso;

public class StatisticsAdapter extends ArrayAdapter<CampaignFullInfo> {

	private Context context;
	private ArrayList<CampaignFullInfo> data;

	public StatisticsAdapter(Context context, int resource,
			ArrayList<CampaignFullInfo> objects) {
		super(context, resource, objects);
		this.context = context;
		this.data = objects;
	}

	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public CampaignFullInfo getItem(int position) {
		return this.data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.data.get(position).hashCode();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = new ViewHolder();

		if (convertView == null) {

			convertView = View.inflate(context, R.layout.list_item_statistics,
					null);

			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.item_image);
			viewHolder.textViewTitle = (TextView) convertView
					.findViewById(R.id.item_name);
			viewHolder.textViewSendDate = (TextView) convertView
					.findViewById(R.id.send_date);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CampaignFullInfo info = this.data.get(position);

		Picasso.with(context).load(info.campaignImageURL).into(viewHolder.imageView);
		// viewHolder.imageView
		// .setImageBitmap(Utils.getImageBitmap(info.imageUri));
		viewHolder.textViewTitle.setText(info.campaignName);
		viewHolder.textViewSendDate.setText(context
				.getString(R.string.send_sms_title)
				+ parseUnixTimeToDateStatistics(info.SMSSendDate));

		convertView.setTag(R.id.item_image, info.campaignID);

		return convertView;

	}

	public String parseUnixTimeToDateStatistics(long unixTimeStamp) {

		long time = unixTimeStamp;
		java.util.Date date = new java.util.Date(time);
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.setTime(date);
		int day = myCalendar.get(Calendar.DAY_OF_MONTH);
		int month = myCalendar.get(Calendar.MONTH);
		int year = myCalendar.get(Calendar.YEAR);
		String onlyDate = String.valueOf(day)
				+ " "
				+ context.getResources().getStringArray(R.array.months_names)[month]
				+ " " + String.valueOf(year);

		return onlyDate;
	}

	static class ViewHolder {

		protected ImageView imageView;
		protected TextView textViewTitle;
		protected TextView textViewSendDate;

	}

}