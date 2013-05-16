package com.zjuhjz.yacleaner;

import java.util.HashMap;
import java.util.List;

import com.zjuhjz.yacleaner.customclass.IntentAppListAdapter;
import com.zjuhjz.yacleaner.customclass.ProcessListAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class MenuSetAutostartApp extends ListFragment implements OnItemClickListener {
	List<HashMap<String,Object>> intentsAppInfoList = null;
	Context context;
	
	public MenuSetAutostartApp(List<HashMap<String,Object>> intentsAppInfoList,Context context){
		super();
		this.intentsAppInfoList = intentsAppInfoList;
		this.context = context;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] menuItems = new String[]{getResources().getString(R.string.menu_set_autostart_1),
				getResources().getString(R.string.menu_set_autostart_2),
				getResources().getString(R.string.menu_set_autostart_3),
				getResources().getString(R.string.menu_set_autostart_4)};
		getListView().setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.menu_item_set_autostart_app, R.id.menu_item_set_autostart_app_item_name,menuItems));
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.menu_set_autostart_app, container,
				false);
		return view;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
}
