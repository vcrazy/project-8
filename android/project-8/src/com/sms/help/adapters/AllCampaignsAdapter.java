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
	private ArrayList<CampaignBasicInfo> data;

	public AllCampaignsAdapter(Context context, int resource,
			ArrayList<CampaignBasicInfo> objects) {
		super(context, resource, objects);
		this.context = context;
		this.data = objects;
	}

	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public CampaignBasicInfo getItem(int position) {
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

			convertView = View.inflate(context, R.layout.list_item_main, null);

			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.item_image);
			viewHolder.textViewTitle = (TextView) convertView
					.findViewById(R.id.item_name);
			viewHolder.textViewInfo = (TextView) convertView
					.findViewById(R.id.item_subname);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CampaignBasicInfo info = this.data.get(position);

		// Picasso.with(context).load(info.imageUri).into(viewHolder.imageView);

		// viewHolder.imageView.setImageURI(Uri.parse(info.imageUri));
		// .setImageBitmap(Utils.getImageBitmap(info.imageUri));
		viewHolder.textViewTitle.setText(info.campaignName);
		viewHolder.textViewInfo.setText(info.campaignSubname);

		// load images from cache
		Bitmap image = Utils.readImageFromCache(context, info.campaignImageURL);
		if (image != null)
			viewHolder.imageView.setImageBitmap(image);

		convertView.setTag(R.id.item_image, info.campaignID);

		return convertView;

	}

	static class ViewHolder {

		protected ImageView imageView;
		protected TextView textViewTitle;
		protected TextView textViewInfo;

	}

}
