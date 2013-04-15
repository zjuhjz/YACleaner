package com.zjuhjz.yacleaner;

//import com.example.android.supportv4.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//import com.example.android.supportv4.app.LoaderThrottleSupport.MainTable;

import android.content.ContentResolver;
//import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ProcessList extends ListFragment {

	private static List<RunningAppProcessInfo> procList = null;
	static final int POPULATE_ID = Menu.FIRST;
	static final int CLEAR_ID = Menu.FIRST + 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_process_list);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getProcessInfo();
		showProcessInfo();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem populateItem = menu.add(Menu.NONE, POPULATE_ID, 0, "Populate");
		populateItem.setIcon(R.drawable.ic_menu_refresh);
		MenuItemCompat.setShowAsAction(populateItem,
				MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.activity_process_list, container,
				false);
		return view;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case POPULATE_ID:
			getProcessInfo();
			showProcessInfo();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showProcessInfo() {
		// 更新进程列表
		Context ctext = getActivity();
		final PackageManager pm = ctext.getApplicationContext().getPackageManager();
		ApplicationInfo ai;
		List<HashMap<String, String>> infoList = new ArrayList<HashMap<String, String>>();
		for (Iterator<RunningAppProcessInfo> iterator = procList.iterator(); iterator
				.hasNext();) {
			RunningAppProcessInfo procInfo = iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("pkg_name", procInfo.processName);
			map.put("proc_id", procInfo.pid + "");
			
			try {
			    ai = pm.getApplicationInfo( procInfo.processName, 0);
			} catch (final NameNotFoundException e) {
			    ai = null;
			}
			final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : procInfo.processName);
			
			map.put("app_name", applicationName);
			infoList.add(map);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(ctext, infoList,
				R.layout.process_list_item, new String[] { "app_name" },
				new int[] { R.id.process_name });
		setListAdapter(simpleAdapter);
		TextView textview = (TextView) getView().findViewById(
				R.id.total_process_num);
		textview.setText("Total Process Num: "
				+ Integer.toString(infoList.size()));
	}

	public int getProcessInfo() {
		Context ctext = getActivity();
		ActivityManager activityManager = (ActivityManager) ctext
				.getSystemService(Context.ACTIVITY_SERVICE);
		procList = activityManager.getRunningAppProcesses();
		return procList.size();
	}

}
