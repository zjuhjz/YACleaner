package com.zjuhjz.yacleaner;

import java.util.HashMap;
import java.util.List;

import com.zjuhjz.yacleaner.customclass.AutoStartAppListAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
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
		registerForContextMenu(getListView());  
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.activity_autostart_app_list, container,
				false);
		return view;
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
				autoStartInfo.appInfoList,
				R.layout.autostart_app_list_item,
				new String[] { "appIcon","appName" },
				new int[] { R.id.autostart_app_icon,R.id.autostart_app_name });
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
		 AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo(); 
		 if(id==R.id.block_gentle){
			 HashMap<String,Object> mAppitem = autoStartInfo.appInfoList.get(info.position);
			 List<HashMap<String,Object>> mIntentsInfoList = (List<HashMap<String,Object>>)mAppitem.get("intentInfoList");
			 
			 
		 }
		 
		 
		 
		 return super.onContextItemSelected(item);
	 }
	 
	 private boolean blockGentle(HashMap<String,Object> list){
		 
		 
		 
		 
		 return true;
	 }
	 
}
