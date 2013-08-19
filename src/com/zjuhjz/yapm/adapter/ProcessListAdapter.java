package com.zjuhjz.yapm.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zjuhjz.yapm.ProcessList;
import com.zjuhjz.yapm.R;
import com.zjuhjz.yapm.YAMemoryInfo;
import com.zjuhjz.yapm.db.IntentInfoObject;
import com.zjuhjz.yapm.db.YAProcessInfo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class ProcessListAdapter extends BaseAdapter {
    YAProcessInfo processInfo;
    LayoutInflater inflater;
    Context context;
    List<YAProcessInfo> data;
    final static String TAG = ProcessList.TAG;
    public ProcessListAdapter(Context context,List<YAProcessInfo> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
        View mView = convertView;
        if (mView==null){
            mView = inflater.inflate(R.layout.process_list_item,null);
        }
		ImageView imageView = (ImageView)mView.findViewById(R.id.process_list_app_icon);
        TextView processName = (TextView)mView.findViewById(R.id.process_name);
        TextView processMemory = (TextView)mView.findViewById(R.id.process_memory);
        mView.setBackgroundColor(android.R.attr.colorBackground);
		processInfo = data.get(position);
		imageView.setImageDrawable(processInfo.icon);
        processName.setText(processInfo.appName);
        processMemory.setText(processInfo.totalMemoryUsage+"MB");
		if (processInfo.isWhiteList){
            mView.setBackgroundColor(Color.parseColor("#F0F0F0"));
            ((TextView)mView.findViewById(R.id.whitelist_label)).setText("white list");
		}
		return mView;
	}
	
	public boolean remove(int position){
		if(data!=null){
//            processInfos[position].packageName
			data.remove(position);
		}
		return true;
	}

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


}
