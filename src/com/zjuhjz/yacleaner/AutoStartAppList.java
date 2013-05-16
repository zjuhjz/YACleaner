package com.zjuhjz.yacleaner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.zjuhjz.yacleaner.customclass.AutoStartAppListAdapter;
import com.zjuhjz.yacleaner.customclass.ProcessListAdapter;
import com.zjuhjz.yacleaner.db.IntentFilterInfo;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.FragmentActivity;

public class AutoStartAppList extends ListFragment implements
		OnItemClickListener {
	AutoStartAppListAdapter autoStartAppListAdapter;
	private static final String TAG = "yacleanerlog";
	AutoStartInfo autoStartInfo;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		PackageManager packageManager = getActivity().getPackageManager();
		HashMap<String, String> appInfo = null;
		autoStartInfo = new AutoStartInfo(getActivity());
		this.getListView().setOnItemClickListener(this);
		showAppInfo();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.activity_autostart_app_list, container,
				false);
		return view;
	}

	private void showAppInfo() {
		Context context = getActivity();
		autoStartAppListAdapter = new AutoStartAppListAdapter(context,
				autoStartInfo.appInfoList,
				R.layout.autostart_app_list_item,
				new String[] { "appIcon","appName" },
				new int[] { R.id.autostart_app_icon,R.id.autostart_app_name });
		setListAdapter(autoStartAppListAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// Log.d(TAG, "click position:"+position);


		
		
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		HashMap<String, Object> appIntentsInfo = autoStartInfo.appInfoList
				.get(position);
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> intentsAppInfoList = (List<HashMap<String, Object>>) appIntentsInfo
				.get("intentInfoList");
		MenuSetAutostartApp intentsAppList = new MenuSetAutostartApp(
				intentsAppInfoList, this.getActivity());
		fragmentTransaction.add(android.R.id.content, intentsAppList);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		
//		try {
//			FragmentManager fragmentManager = getFragmentManager();
//			FragmentTransaction fragmentTransaction = fragmentManager
//					.beginTransaction();
//			HashMap<String, Object> appIntentsInfo = autoStartInfo.appInfoList
//					.get(position);
//			
//			@SuppressWarnings({ "unchecked", "unused" })
//			List<HashMap<String, Object>> intentsAppInfoList = (List<HashMap<String, Object>>) appIntentsInfo
//					.get("intentInfoList");
//			IntentsAppList intentsAppList = new IntentsAppList(
//					intentsAppInfoList, this.getActivity());
//			fragmentTransaction.add(android.R.id.content, intentsAppList);
//			fragmentTransaction.addToBackStack(null);
//			fragmentTransaction.commit();
//		} catch (Exception e) {
//
//		} finally {
//
//		}

	}
}
