package com.zjuhjz.yacleaner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import com.zjuhjz.yacleaner.tool.*;

public class YAMemoryInfo {
	public long totalMemory;
	public long totalUsed;

	private static List<RunningAppProcessInfo> runningAppProcesses = null;
	private static MemoryInfo mi = new MemoryInfo();
	// static final int POPULATE_ID = Menu.FIRST;
	// static final int CLEAR_ID = Menu.FIRST + 1;
	Context context;
	List<HashMap<String, String>> processInfoList;
	List<HashMap<String, String>> servicesInfoList;
	CMDExecute cmdExecute = new CMDExecute();

	YAMemoryInfo(Context context) {
		processInfoList = new ArrayList<HashMap<String, String>>();
		servicesInfoList = new ArrayList<HashMap<String, String>>();
		this.context = context;
		refresh();
	}

	public boolean refresh() {
		refreshProcessInfo();

		return true;
	}

	public int refreshProcessInfo() {
		// ApplicationInfo ai
		ApplicationInfo ai;

		// PackageManager pm
		PackageManager pm = context.getPackageManager();

		// memory usage
		HashMap<String, String> memoryUsage = new HashMap<String, String>();

		// ///////initialize///////////
		// ActivityManager activityManager
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// clear
		if (runningAppProcesses != null) {
			runningAppProcesses.clear();
		}
		if (processInfoList != null) {
			processInfoList.clear();
		}

		runningAppProcesses = activityManager.getRunningAppProcesses();
		// /////initialize end/////////
		String result = null;
		String[] items = null;
		String[] itempieces;
		try {
			String[] args = { "/system/bin/ps" };
			result = cmdExecute.run(args, ".");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			items = result.split("\n");
		}

		for (String item : items) {
			itempieces = item.trim().split("\\s+");
			if (itempieces.length > 8) {
				memoryUsage.put(itempieces[8],
						Integer.toString(Integer.parseInt(itempieces[4]) / 1024));
			}

		}

		// get Memory info
		// activityManager.getMemoryInfo(mi);

		// add info to processInfoList
		for (Iterator<RunningAppProcessInfo> iterator = runningAppProcesses
				.iterator(); iterator.hasNext();) {
			RunningAppProcessInfo procInfo = iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			try {
				ai = pm.getApplicationInfo(procInfo.processName, 0);
			} catch (final NameNotFoundException e) {
				ai = null;
			}
			final String applicationName = (String) (ai != null ? pm
					.getApplicationLabel(ai) : procInfo.processName);
			map.put("app_name", applicationName);
			map.put("package_name", procInfo.processName);
			map.put("pid", procInfo.pid + "");
			map.put("memory_usage", memoryUsage.get(procInfo.processName)
					+ "MB");
			processInfoList.add(map);
		}
		return runningAppProcesses.size();
	}

}
