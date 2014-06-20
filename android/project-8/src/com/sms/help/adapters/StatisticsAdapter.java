package com.sms.help.adapters;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sms.help.R;
import com.sms.help.Utils;
import com.sms.help.types.CampaignFullInfo;

public class StatisticsAdapter extends ArrayAdapter<CampaignFullInfo> {

	private Context context;
	private ArrayList<CampaignFullInfo> listCampaigns;

	public StatisticsAdapter(Context context, int resource,
			ArrayList<CampaignFullInfo> objects) {
		super(context, resource, objects);

		this.context = context;
		this.listCampaigns = objects;
	}

	@Override
	public int getCount() {
		return this.listCampaigns.size();
	}

	@Override
	public CampaignFullInfo getItem(int position) {
		return this.listCampaigns.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.listCampaigns.get(position).hashCode();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = new ViewHolder();

		if (convertView == null) {

			convertView = View.inflate(context, R.layout.list_item_statistics,
					null);

			viewHolder.imageViewPicture = (ImageView) convertView
					.findViewById(R.id.imageview_picture);
			viewHolder.textViewCampaignName = (TextView) convertView
					.findViewById(R.id.textview_campaign_name);
			viewHolder.textViewSMSSentDate = (TextView) convertView
					.findViewById(R.id.textview_sms_sentdate);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CampaignFullInfo campaign = getItem(position);

		Bitmap image = Utils.readImageFromCache(context,
				campaign.campaignImageURL);
		if (image != null)
			viewHolder.imageViewPicture.setImageBitmap(image);

		viewHolder.textViewCampaignName.setText(campaign.campaignName);
		viewHolder.textViewSMSSentDate.setText(context
				.getString(R.string.sent_sms_date)
				+ parseUnixTimeToDateStatistics(campaign.SMSSentDate));

		convertView.setTag(R.id.imageview_picture, campaign.campaignID);

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

		protected ImageView imageViewPicture;
		protected TextView textViewCampaignName;
		protected TextView textViewSMSSentDate;

	}

}
