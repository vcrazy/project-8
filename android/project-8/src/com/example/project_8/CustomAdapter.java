package com.example.project_8;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomAdapter extends ArrayAdapter<BasicInfo> {

	private Context context;
	private ArrayList<BasicInfo> data;
	private Picasso picasso;

	public CustomAdapter(Context context, int resource,
			ArrayList<BasicInfo> objects) {
		super(context, resource, objects);
		this.context = context;
		this.data = objects;
		this.picasso = Picasso.with(context);
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
					.findViewById(R.id.item_title);
			viewHolder.textViewInfo = (TextView) convertView
					.findViewById(R.id.item_more_info);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BasicInfo info = this.data.get(position);

		picasso.load(info.imageUri).into(viewHolder.imageView);
		viewHolder.textViewTitle.setText(info.title);
		viewHolder.textViewInfo.setText(info.info);

		convertView.setTag(R.id.item_image, info);

		return convertView;

	}

	static class ViewHolder {
		protected ImageView imageView;
		protected TextView textViewTitle;
		protected TextView textViewInfo;

	}

}
