package com.zjuhjz.yacleaner.customclass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zjuhjz.yacleaner.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AutoStartAppListAdapter extends SimpleAdapter {
	private static final String TAG = "yacleanerlog";
	// private String[] colours = new String[] {"#CCCCCC", "#FFFFFF"};
	private List<HashMap<String, Object>>  data;
	// HashMap<String,String> processInfo;

	@SuppressWarnings("unchecked")
	public AutoStartAppListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data = (List<HashMap<String, Object>>)data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		ImageView imageView = (ImageView)view.findViewById(R.id.autostart_app_icon);
		imageView.setImageDrawable((Drawable)data.get(position).get("appIcon"));
		return view;
	}

}
