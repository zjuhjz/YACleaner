package com.zjuhjz.yacleaner;

//import com.example.android.supportv4.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.content.Context;

public class ProcessList extends ListFragment {

	private static List<RunningAppProcessInfo> procList = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_process_list);
	}
	@Override
	public void  onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getProcessInfo();
        showProcessInfo();
	}
	public void showProcessInfo() {
		Context ctext= getActivity();
        // 更新进程列表
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
