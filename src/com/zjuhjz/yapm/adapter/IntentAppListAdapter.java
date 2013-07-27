package com.zjuhjz.yapm.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zjuhjz.yapm.R;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class IntentAppListAdapter extends SimpleAdapter {
	private List<? extends Map<String, ?>> data;
	HashMap<String, Object> intentsAppInfo = null;
	int enableState;
	TextView textView;

	public IntentAppListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data = data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		intentsAppInfo = (HashMap<String, Object>) data.get(position);
		enableState = (Integer) intentsAppInfo.get("enable_state");
		view.setBackgroundColor(Color.parseColor("#00FFFF"));
		textView = (TextView)view.findViewById(R.id.com_zjuhjz_yacleaner_IntentsAppList_appname);
		textView.setTextColor(Color.BLUE);
		//TODO default enable state need to be 
		// confirm: PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
		if(enableState==PackageManager.COMPONENT_ENABLED_STATE_DISABLED){
			textView.setTextColor(Color.RED);	
		}
		
		return view;
	}

}
