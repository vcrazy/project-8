package com.example.project_8;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapterStatistics extends ArrayAdapter<FullInfo> {

	private Context context;
	private ArrayList<FullInfo> data;

	public CustomAdapterStatistics(Context context, int resource,
			ArrayList<FullInfo> objects) {
		super(context, resource, objects);
		this.context = context;
		this.data = objects;
	}

	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public FullInfo getItem(int position) {
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
			viewHolder.textViewInfo = (TextView) convertView
					.findViewById(R.id.item_subname);
			viewHolder.textViewSendDate = (TextView) convertView
					.findViewById(R.id.send_date);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		FullInfo info = this.data.get(position);

		viewHolder.imageView
				.setImageBitmap(Utils.getImageBitmap(info.imageUri));
		viewHolder.textViewTitle.setText(info.campaignName);
		viewHolder.textViewInfo.setText(info.campaignSubName);
		// viewHolder.textViewInfo.setText(info.smsSendDate);

		convertView.setTag(R.id.item_image, info.campaignId);

		return convertView;

	}

	static class ViewHolder {

		protected ImageView imageView;
		protected TextView textViewTitle;
		protected TextView textViewInfo;
		protected TextView textViewSendDate;

	}

}
