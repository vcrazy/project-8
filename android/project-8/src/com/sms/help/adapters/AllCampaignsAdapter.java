package com.sms.help.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sms.help.R;
import com.sms.help.Utils;
import com.sms.help.types.CampaignBasicInfo;

public class AllCampaignsAdapter extends ArrayAdapter<CampaignBasicInfo> {

	private Context context;
	private ArrayList<CampaignBasicInfo> listCampaigns;

	public AllCampaignsAdapter(Context context, int resource,
			ArrayList<CampaignBasicInfo> objects) {
		super(context, resource, objects);

		this.context = context;
		this.listCampaigns = objects;

	}

	@Override
	public int getCount() {
		return this.listCampaigns.size();
	}

	@Override
	public CampaignBasicInfo getItem(int position) {
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

			convertView = View.inflate(context, R.layout.list_item_campaign,
					null);

			viewHolder.imageViewPicture = (ImageView) convertView
					.findViewById(R.id.imageview_picture);
			viewHolder.textViewCampaignName = (TextView) convertView
					.findViewById(R.id.textview_campaign_name);
			viewHolder.textViewCampaignSubname = (TextView) convertView
					.findViewById(R.id.textview_campaign_subname);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CampaignBasicInfo info = getItem(position);

		// load images from cache
		Bitmap image = Utils.readImageFromCache(context, info.campaignImageURL);
		if (image != null)
			viewHolder.imageViewPicture.setImageBitmap(image);

		viewHolder.textViewCampaignName.setText(info.campaignName);
		viewHolder.textViewCampaignSubname.setText(info.campaignSubname);

		convertView.setTag(R.id.imageview_picture, info.campaignID);

		return convertView;

	}

	static class ViewHolder {

		protected ImageView imageViewPicture;
		protected TextView textViewCampaignName;
		protected TextView textViewCampaignSubname;

	}

}
