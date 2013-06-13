package com.zjuhjz.yapm;

import java.util.HashMap;
import java.util.List;

import com.zjuhjz.yapm.R;
import com.zjuhjz.yapm.adapter.IntentAppListAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class IntentsAppList extends ListFragment implements OnItemClickListener {
	List<HashMap<String,Object>> intentsAppInfoList = null;
	Context context;
	
	public IntentsAppList(List<HashMap<String,Object>> intentsAppInfoList,Context context){
		super();
		this.intentsAppInfoList = intentsAppInfoList;
		this.context = context;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		//stub
		showAppInfo();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.activity_process_list, container,
				false);
		final View button = view.findViewById(R.id.clean);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		return view;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	private void showAppInfo(){
		IntentAppListAdapter intentAppListAdapter = new IntentAppListAdapter(context,
				intentsAppInfoList, R.layout.intents_app_list_item,
				new String[] { "display" }, new int[] {
						R.id.com_zjuhjz_yacleaner_IntentsAppList_appname, });
		setListAdapter(intentAppListAdapter);
	}
	
}
