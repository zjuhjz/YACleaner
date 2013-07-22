package com.zjuhjz.yapm.adapter;

import java.util.HashMap;
import java.util.List;
import com.zjuhjz.yapm.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class ProcessListAdapter extends SimpleAdapter {
	private List<HashMap<String, Object>> data;
	HashMap<String,Object> processInfo;
	public ProcessListAdapter(Context context,
			List<HashMap<String, Object>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.data=data;
		// TODO Auto-generated constructor stub
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view = super.getView(position, convertView, parent);
		ImageView imageView = (ImageView)view.findViewById(R.id.process_list_app_icon);
		view.setBackgroundColor(android.R.attr.colorBackground);
		processInfo = data.get(position);
		imageView.setImageDrawable((Drawable)processInfo.get("icon"));
		if (processInfo.get("whitelist")=="1"){
			view.setBackgroundColor(Color.parseColor("#F0F0F0"));
            ((TextView)view.findViewById(R.id.whitelist_label)).setText("white list");
		}
		return view;
	}
	
	public boolean remove(int position){
		if(data!=null){
			data.remove(position);
		}
		return true;
	}

}
