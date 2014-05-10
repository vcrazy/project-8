package com.example.project_8;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sms.help.R;

public class CustomAdapter extends ArrayAdapter<BasicInfo> {

	private Context context;
	private ArrayList<BasicInfo> data;

	public CustomAdapter(Context context, int resource,
			ArrayList<BasicInfo> objects) {
		super(context, resource, objects);
		this.context = context;
		this.data = objects;
	}

	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public BasicInfo getItem(int position) {
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

			convertView = View.inflate(context, R.layout.list_item, null);

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

		BasicInfo info = this.data.get(position);

		viewHolder.imageView
				.setImageBitmap(Utils.getImageBitmap(info.imageUri));
		viewHolder.textViewTitle.setText(info.campaignName);
		viewHolder.textViewInfo.setText(info.campaignSubName);

		convertView.setTag(R.id.item_image, info.campaignId);

		return convertView;

	}

	static class ViewHolder {

		protected ImageView imageView;
		protected TextView textViewTitle;
		protected TextView textViewInfo;

	}

}
