package com.zjuhjz.yacleaner.customclass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zjuhjz.yacleaner.R;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class IntentAppListAdapter extends SimpleAdapter {
	private static final String TAG = "yacleanerlog";
	// private String[] colours = new String[] {"#CCCCCC", "#FFFFFF"};
	private List<? extends Map<String, ?>> data;
	HashMap<String, Object> intentsAppInfo = null;
	int enableState;
	boolean defaulEnable;
	TextView textView;
	// HashMap<String,String> processInfo;

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
		//defaulEnable = (Integer) intentsAppInfo.get("enable_state");
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