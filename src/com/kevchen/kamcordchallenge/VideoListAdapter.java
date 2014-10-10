package com.kevchen.kamcordchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoListAdapter extends ArrayAdapter<Video> {

	Context context;
	int layoutResourceId;
	Video data[] = null;

	public VideoListAdapter(Context context, int layoutResourceId, Video[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		DataHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new DataHolder();
			holder.title = (TextView) row.findViewById(R.id.title);
			try {
				holder.thumbnail = (ImageView) row.findViewById(R.id.thumbnail);
			} catch (Exception e) {
				System.out.println("Could not get picture");
			}
			row.setTag(holder);
		} else {
			holder = (DataHolder) row.getTag();
		}

		Video vid = data[position];
		holder.title.setText(vid.title + "\n");

		if (vid.thumbnail != null)
			try {
				holder.thumbnail.setImageBitmap(vid.thumbnail);
			} catch (Exception e) {
				System.out.println("Could not get picture");
			}

		return row;
	}

	static class DataHolder {
		TextView video_url;
		TextView title;
		ImageView thumbnail;
	}

}
