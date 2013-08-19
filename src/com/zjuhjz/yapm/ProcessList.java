package com.zjuhjz.yapm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Menu;




import com.zjuhjz.yapm.adapter.ProcessListAdapter;
import com.zjuhjz.yapm.db.YAProcessInfo;

import java.util.HashMap;

public class ProcessList extends SherlockListFragment implements OnItemClickListener {
	public static final String TAG = "yacleanerlog";

	// private static List<RunningAppProcessInfo> procList = null;
	YAMemoryInfo yaMemoryInfo;
	static final int REFRESH_ID = Menu.FIRST;
	static final int CLEAR_ID = Menu.FIRST + 1;
	ProcessListAdapter processListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		yaMemoryInfo = new YAMemoryInfo(getActivity());
		// this.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		this.getListView().setOnItemClickListener(this);
		ListView listView = this.getListView();
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				listView,
				new SwipeDismissListViewTouchListener.OnDismissCallback() {
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							yaMemoryInfo.killProcess(position);
						}
						processListAdapter.notifyDataSetChanged();
					}
				});
		listView.setOnTouchListener(touchListener);
		listView.setOnScrollListener(touchListener.makeScrollListener());

		registerForContextMenu(getListView());
		showProcessInfo();
	}

	/*
	 * Change context menu title at run time
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu
	 * , android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.processlist_contextmenu, menu);
        android.view.MenuItem menuItem = menu.getItem(0);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		YAProcessInfo processInfo = yaMemoryInfo.yaProcessInfos.get(info.position);
		if (!processInfo.isWhiteList) {
			menuItem.setTitle(getResources().getString(
					R.string.process_addto_whitelist));
		} else {
			menuItem.setTitle(getResources().getString(
					R.string.process_removefrom_whitelist));
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}


    @Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.process_addto_whitelist) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			YAProcessInfo yaProcessInfo= yaMemoryInfo.yaProcessInfos
					.get(info.position);
			if (!yaProcessInfo.isWhiteList) {
                yaProcessInfo.isWhiteList=true;
				yaMemoryInfo.addToWhiteList(yaProcessInfo
						.packageName);
			} else {
                yaProcessInfo.isWhiteList=false;
				yaMemoryInfo.removeFromWhiteList(yaProcessInfo.packageName);
			}
		}
        else if (id == R.id.process_detail){
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            String packageName = (String)yaMemoryInfo.yaProcessInfos.get(info.position).packageName;
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);
            startActivity(intent);
        }
		processListAdapter.notifyDataSetChanged();
		return super.onContextItemSelected(item);
	}

    @Override
    public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        MenuItem populateItem = menu.add(Menu.NONE, REFRESH_ID, 0, "Refresh");
        populateItem.setIcon(R.drawable.ic_menu_refresh);
        populateItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

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
				yaMemoryInfo
						.killProcesses(YAMemoryInfo.KILL_PROCESSES_EXCEPT_WHITELIST);
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
	public void onPause() {
		super.onPause();
	}

	public void showProcessInfo() {
		// initial and refresh memoryinfo
		Context context = getActivity();
		yaMemoryInfo.refresh();

		// TODO improve "string from"
		processListAdapter = new ProcessListAdapter(context,
				yaMemoryInfo.yaProcessInfos);
        yaMemoryInfo.setAdapter(processListAdapter);

		setListAdapter(processListAdapter);
		TextView textview = (TextView) getView().findViewById(
				R.id.total_process_num);

		textview.setText("Process Num: "
				+ Integer.toString(yaMemoryInfo.yaProcessInfos.size())
				+ "\nfree RAM:" + yaMemoryInfo.availableMemory + " MB");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		arg1.showContextMenu();
	}

}
