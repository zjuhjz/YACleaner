package com.zjuhjz.yacleaner;

//import com.example.android.supportv4.R;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import android.widget.TextView;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.app.ActivityManager;

import com.zjuhjz.yacleaner.adapter.ProcessListAdapter;
import com.zjuhjz.yacleaner.db.YAProcessInfo;

import java.util.HashMap;
import java.util.Iterator;
import android.content.Context;

public class ProcessList extends ListFragment implements OnItemClickListener {
	public static final String TAG = "yacleanerlog";

	//private static List<RunningAppProcessInfo> procList = null;
	YAMemoryInfo yaMemoryInfo;
	static final int REFRESH_ID = Menu.FIRST;
	static final int CLEAR_ID = Menu.FIRST + 1;
	ProcessListAdapter simpleAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		yaMemoryInfo = new YAMemoryInfo(getActivity());
		//this.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		this.getListView().setOnItemClickListener(this);
		ListView listView = this.getListView();
		SwipeDismissListViewTouchListener touchListener =
		new SwipeDismissListViewTouchListener(
				                  listView,
				                  new SwipeDismissListViewTouchListener.OnDismissCallback() {
				                      public void onDismiss(ListView listView, int[] reverseSortedPositions) {
				                          for (int position : reverseSortedPositions) {
				                        	  yaMemoryInfo.killProcess(position);
				                          }
				                          simpleAdapter.notifyDataSetChanged();
				                      }
				                  });
		listView.setOnTouchListener(touchListener);
		listView.setOnScrollListener(touchListener.makeScrollListener());
		
		
		
		
		registerForContextMenu(getListView());
		showProcessInfo();
	}

	/*
	 * Change context menu title at run time
	 * @see android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.processlist_contextmenu, menu);
		MenuItem menuItem = menu.getItem(0);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		HashMap<String, Object> processInfo= yaMemoryInfo.processInfoList.get(info.position);
		if(processInfo.get("whitelist")=="0"){
			menuItem.setTitle(getResources().getString(R.string.process_addto_whitelist));
		}
		else{
			menuItem.setTitle(getResources().getString(R.string.process_removefrom_whitelist));
		}
		//Log.d(TAG, "on create context menu");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id==R.id.process_addto_whitelist){
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			HashMap<String,Object> processInfo= yaMemoryInfo.processInfoList.get(info.position);
			if(processInfo.get("whitelist")=="0"){
				processInfo.put("whitelist", "1");
				yaMemoryInfo.addToWhiteList((String)processInfo.get("package_name"));
				Log.d(TAG, processInfo.get("package_name")+"added");
			}
			else{
				processInfo.put("whitelist", "0");
				yaMemoryInfo.removeFromWhiteList((String)processInfo.get("package_name"));
				Log.d(TAG, processInfo.get("package_name")+"removed");
			}
		}
		simpleAdapter.notifyDataSetChanged();
		return super.onContextItemSelected(item);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem populateItem = menu.add(Menu.NONE, REFRESH_ID, 0, "Refresh");
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
		final View button = view.findViewById(R.id.clean);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				yaMemoryInfo.killProcesses(YAMemoryInfo.KILL_PROCESSES_EXCEPT_WHITELIST);
				showProcessInfo();
			}
		});
		return view;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case REFRESH_ID:
			showProcessInfo();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onPause(){
		super.onPause();
	}
	
	
	
//	public void killAllProcesses() {
//		Context context = getActivity();
//		ActivityManager activityManager = (ActivityManager) context
//				.getSystemService(Context.ACTIVITY_SERVICE);
//		HashMap<String, Object> processitem;
//		YAProcessInfo yaProcessInfo;
//		for (Iterator<HashMap<String, Object>> iterator = yaMemoryInfo.processInfoList
//				.iterator(); iterator.hasNext();) {
//			processitem = iterator.next();
//			yaProcessInfo = yaMemoryInfo.yaProcessInfoList.get(processitem
//					.get("package_name"));
//			for(String processName:yaProcessInfo.processNameList){
//				activityManager.killBackgroundProcesses(processName);
//			}
//		}
//	}

	public void showProcessInfo() {
		// initial and refresh memoryinfo
		Context context = getActivity();
		yaMemoryInfo.refresh();

		// TODO improve "string from"
		simpleAdapter = new ProcessListAdapter(context,
				yaMemoryInfo.processInfoList, R.layout.process_list_item,
				new String[] { "app_name", "memory_usage" }, new int[] {
						R.id.process_name, R.id.process_memory });
		setListAdapter(simpleAdapter);
		TextView textview = (TextView) getView().findViewById(
				R.id.total_process_num);
		textview.setText("Total Process Num: "
				+ Integer.toString(yaMemoryInfo.processInfoList.size())
				+ "\nfree RAM:" + yaMemoryInfo.availableMemory + " MB");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		arg1.showContextMenu();
		
		/*
		Log.d("yacleanerdebug", position + "");
		//arg1.setSelected(true);
		HashMap<String,Object> processInfo = yaMemoryInfo.processInfoList.get(position);
		if(processInfo.get("whitelist")=="0"){
			processInfo.put("whitelist", "1");
			yaMemoryInfo.addToWhiteList((String)processInfo.get("package_name"));
			Log.d(TAG, processInfo.get("package_name")+"added");
		}
		else{
			processInfo.put("whitelist", "0");
			yaMemoryInfo.removeFromWhiteList((String)processInfo.get("package_name"));
			Log.d(TAG, processInfo.get("package_name")+"removed");
		}
		simpleAdapter.notifyDataSetChanged();
		*/
	}

}
