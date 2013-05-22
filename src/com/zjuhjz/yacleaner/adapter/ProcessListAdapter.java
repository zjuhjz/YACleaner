package com.zjuhjz.yacleaner.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zjuhjz.yacleaner.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;



public class ProcessListAdapter extends SimpleAdapter {
	private static final String TAG = "yacleanerlog";
	private String[] colours = new String[] {"#CCCCCC", "#FFFFFF"};
	private List<HashMap<String, Object>> data;
	HashMap<String,Object> processInfo;
	public ProcessListAdapter(Context context,
			List<HashMap<String, Object>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data=data;
		// TODO Auto-generated constructor stub
	}
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view = super.getView(position, convertView, parent);
		ImageView imageView = (ImageView)view.findViewById(R.id.process_list_app_icon);
		view.setBackgroundColor(Color.parseColor("#FFFFFF"));
		processInfo = (HashMap<String, Object> )data.get(position);
		//imageView.setImageResource((Integer)processInfo.get("icon"));
		if (processInfo.get("whitelist")=="1"){
			view.setBackgroundColor(Color.parseColor("#00FFFF"));
		}
		return view;
	}

}
