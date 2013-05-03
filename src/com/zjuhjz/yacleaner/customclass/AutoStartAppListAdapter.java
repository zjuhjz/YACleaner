package com.zjuhjz.yacleaner.customclass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class AutoStartAppListAdapter extends SimpleAdapter {
	private static final String TAG = "yacleanerlog";
	// private String[] colours = new String[] {"#CCCCCC", "#FFFFFF"};
	private List<? extends Map<String, ?>> data;

	// HashMap<String,String> processInfo;

	public AutoStartAppListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		return view;
	}

}
