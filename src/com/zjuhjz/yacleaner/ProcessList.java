package com.zjuhjz.yacleaner;

//import com.example.android.supportv4.R;

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
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.content.Context;

public class ProcessList extends ListFragment {

	private static List<RunningAppProcessInfo> procList = null;
	static final int POPULATE_ID = Menu.FIRST;
    static final int CLEAR_ID = Menu.FIRST+1;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_process_list);
	}
	@Override
	public void  onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        getProcessInfo();
        showProcessInfo();
	}
	@Override 
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem populateItem = menu.add(Menu.NONE, POPULATE_ID, 0, "Populate");
        MenuItemCompat.setShowAsAction(populateItem, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItem clearItem = menu.add(Menu.NONE, CLEAR_ID, 0, "Clear");
        MenuItemCompat.setShowAsAction(clearItem, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//super.onCreateView(inflater, container, savedInstanceState);
		View view=inflater.inflate(R.layout.activity_process_list, container,false);
		return view;
	}
	public void showProcessInfo() {
		Context ctext= getActivity();
        // ���½����б�
        List<HashMap<String,String>> infoList = new ArrayList<HashMap<String,String>>();
        for (Iterator<RunningAppProcessInfo> iterator = procList.iterator(); iterator.hasNext();) {
            RunningAppProcessInfo procInfo = iterator.next();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("proc_name", procInfo.processName);
            map.put("proc_id", procInfo.pid+"");
            infoList.add(map);
        }
        
        SimpleAdapter simpleAdapter = new SimpleAdapter(
        		ctext,
                infoList, 
                R.layout.process_list_item, 
                new String[]{"proc_name"},
                new int[]{R.id.process_name} );
        setListAdapter(simpleAdapter);
    }
	public int getProcessInfo() {
		Context ctext= getActivity();
        ActivityManager activityManager = (ActivityManager) ctext.getSystemService(Context.ACTIVITY_SERVICE);
        procList = activityManager.getRunningAppProcesses();
        return procList.size();
	}
	

}