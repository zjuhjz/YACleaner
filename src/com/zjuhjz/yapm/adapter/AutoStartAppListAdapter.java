package com.zjuhjz.yapm.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zjuhjz.yapm.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class AutoStartAppListAdapter extends SimpleAdapter {
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
