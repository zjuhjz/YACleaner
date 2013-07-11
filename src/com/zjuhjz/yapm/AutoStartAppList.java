package com.zjuhjz.yapm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zjuhjz.yapm.R;
import com.zjuhjz.yapm.adapter.AutoStartAppListAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class AutoStartAppList extends ListFragment implements
		OnItemClickListener {
	AutoStartAppListAdapter autoStartAppListAdapter;
	public static final String TAG = "yacleanerlog";
	AutoStartInfo autoStartInfo;
	static final int INCLUDE_SYSTEM_ID = Menu.FIRST;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().getPackageManager();
		autoStartInfo = new AutoStartInfo(getActivity());
		this.getListView().setOnItemClickListener(this);
		showAppInfo();
		registerForContextMenu(getListView());
	}
	
	public void onStart() {
        super.onStart();
        //getListView().setEmptyView(emptyView);
        // The activity is about to become visible.
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.activity_autostart_app_list,
				container, false);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem populateItem = menu.add(Menu.NONE, INCLUDE_SYSTEM_ID, 0,
				"All App");
		populateItem.setIcon(R.drawable.ic_include_system_package);
		MenuItemCompat.setShowAsAction(populateItem,
				MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case INCLUDE_SYSTEM_ID:
			if (autoStartInfo != null && autoStartAppListAdapter != null) {
				autoStartInfo.changeIncludeSystemFlag();
				autoStartAppListAdapter.notifyDataSetChanged();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.autostartapp_contextmenu, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	private void showAppInfo() {
		Context context = getActivity();
		autoStartAppListAdapter = new AutoStartAppListAdapter(context,
				autoStartInfo.appInfoList, R.layout.autostart_app_list_item,
				new String[] { "appName", "historyStatus" }, new int[] {
						R.id.autostart_app_name, R.id.autostart_status });
		setListAdapter(autoStartAppListAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		arg1.showContextMenu();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int id = item.getItemId();
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		HashMap<String, Object> mAppitem = autoStartInfo.appInfoList
				.get(info.position);
		if (id == R.id.block_gentle) {
			autoStartInfo.blockGentle(mAppitem);
			// List<HashMap<String,Object>> mIntentsInfoList =
			// (List<HashMap<String,Object>>)mAppitem.get("intentInfoList");
		} else if (id == R.id.block_strong) {
			autoStartInfo.blockStrong(mAppitem);
		} else if (id == R.id.block_complete) {
			autoStartInfo.blockCompelete(mAppitem);
		} else if (id == R.id.unblock) {
			autoStartInfo.unBlockAll(mAppitem);
		} else if (id == R.id.block_manually){
            HashMap<String,Boolean> mComponentList = (HashMap<String,Boolean>)mAppitem.get("componentList");
            Intent intent = new Intent(getActivity(),ReceiverList.class);
            intent.putExtra("ComponentList",mComponentList);
            intent.putExtra("PackageName",(String)mAppitem.get("package_name"));
            Log.d(TAG,"packageName:"+mAppitem.get("package_name"));
            //startActivity(intent);
            startActivityForResult(intent, 1);
        }
		autoStartAppListAdapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        HashMap<String,Boolean> mComponentList = (HashMap<String,Boolean>)data.getSerializableExtra("ComponentList");
        String packageName = data.getStringExtra("PackageName");
        Iterator iterator = mComponentList.entrySet().iterator();
        HashMap<String,Object> component ;
        List<HashMap<String,Object>> componentList = new ArrayList<HashMap<String, Object>>();
        Map.Entry entry;

        for (;iterator.hasNext();){
            entry = (Map.Entry)iterator.next();
            component = new HashMap<String, Object>();
            component.put("packageName",packageName);
            component.put("componentName",entry.getKey());
            component.put("enable",entry.getValue());
            componentList.add(component);
        }
        ToggleAsyncTask toggleAsyncTask = new ToggleAsyncTask(getActivity());
        toggleAsyncTask.execute(componentList);
    }
}
