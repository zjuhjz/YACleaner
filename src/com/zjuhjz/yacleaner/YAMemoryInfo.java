package com.zjuhjz.yacleaner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.widget.Toast;

import com.zjuhjz.yacleaner.tool.*;
import com.zjuhjz.yacleaner.db.YAProcessInfo;

public class YAMemoryInfo {
	final public static int KILL_PROCESSES_ALL = 1;
	final public static int KILL_PROCESSES_EXCEPT_WHITELIST = 2;
	
	public long totalMemory;
	public long totalUsed;
	public long availableMemory;
	private ActivityManager activityManager;
	private static List<RunningAppProcessInfo> runningAppProcesses = null;
	private static final String WHITE_LIST_FILE_NAME = "whiteList";
	private static final String TAG = "yacleanerlog";
	private static MemoryInfo mi = new MemoryInfo();
	public List<String> whiteList = new ArrayList<String>();
	HashMap<String, YAProcessInfo> yaProcessInfoList;

	Context context;
	List<HashMap<String, Object>> processInfoList;
	List<HashMap<String, String>> servicesInfoList;
	YAProcessInfo yaProcessInfo;
	CMDExecute cmdExecute = new CMDExecute();

	YAMemoryInfo(Context context) {
		processInfoList = new ArrayList<HashMap<String, Object>>();
		servicesInfoList = new ArrayList<HashMap<String, String>>();
		this.context = context;
		activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		loadWhiteList();
		refresh();
	}

	public boolean refresh() {
		refreshProcessInfo();
		refreshMemoryInfo();
		return true;
	}

	public boolean refreshMemoryInfo() {
		activityManager.getMemoryInfo(mi);
		availableMemory = mi.availMem / 1024 / 1024;
		return true;
	}

	public boolean loadWhiteList() {
		try {
			FileInputStream fileInputStream = context
					.openFileInput(WHITE_LIST_FILE_NAME);
			byte[] buffer = new byte[1024];
			StringBuffer fileContent = new StringBuffer("");
			Log.d(TAG, "file opened");
			while (fileInputStream.read(buffer) != -1) {
				fileContent.append(new String(buffer));
			}
			String data = new String(fileContent);
			whiteList.addAll(Arrays.asList(data.split("\n")));
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			try {
				FileOutputStream fileOutputStream = context.openFileOutput(
						WHITE_LIST_FILE_NAME, 0);
				fileOutputStream.close();
				Log.d(TAG, "file created");

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return true;
	}

	public int refreshProcessInfo() {
		ApplicationInfo ai;

		PackageManager pm = context.getPackageManager();

		// memory usage
		HashMap<String, Integer> memoryUsage = new HashMap<String, Integer>();

		// ///////initialize///////////

		// clear
		if (runningAppProcesses != null) {
			runningAppProcesses.clear();
		}
		if (processInfoList != null) {
			processInfoList.clear();
		}

		// /////initialize end/////////

		/*
		 * Use ps command to retrieve memory usage status. Put status into
		 * memoryUsage HashMap
		 */
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
						Integer.parseInt(itempieces[4]) / 1024);
			}

		}

		/*
		 * Get running processes info by ActivityManager.getRunningAppProcesses.
		 * Retrieve into yaProcessInfoList. There are several processes from one
		 * package, so we unite these processes into to one data structure
		 * YAProcessInfo if they are from the same package. To get detail
		 * information for particular package, we need getApplicationInfo(). The
		 * package name we get for the process, we use procInfo.pkgList[0] which
		 * we need get back to reconsider.
		 */
		runningAppProcesses = activityManager.getRunningAppProcesses();
		RunningAppProcessInfo procInfo;
		yaProcessInfoList = new HashMap<String, YAProcessInfo>();

		for (Iterator<RunningAppProcessInfo> iterator = runningAppProcesses
				.iterator(); iterator.hasNext();) {
			procInfo = iterator.next();
			try {
				ai = pm.getApplicationInfo(procInfo.pkgList[0], 0);
			} catch (final NameNotFoundException e) {
				ai = null;
			}
			final String applicationName = (String) (ai != null ? pm
					.getApplicationLabel(ai) : procInfo.processName);
			if (yaProcessInfoList.containsKey(ai.packageName)) {
				yaProcessInfo = yaProcessInfoList.get(ai.packageName);
			} else {
				yaProcessInfo = new YAProcessInfo();
				yaProcessInfo.appName = applicationName;
				yaProcessInfo.processName = procInfo.processName;
				yaProcessInfo.packageName = ai.packageName;
				yaProcessInfo.isWhiteList = whiteList.contains(ai.packageName);
				yaProcessInfo.icon = ai.loadIcon(pm);
				yaProcessInfo.isSystemApp = ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
				yaProcessInfoList.put(yaProcessInfo.packageName, yaProcessInfo);
			}

			yaProcessInfo.pid.add(procInfo.pid);
			yaProcessInfo.processNameList.add(procInfo.processName);
			try {
				yaProcessInfo.totalMemoryUsage += memoryUsage
						.get(procInfo.processName);
			} catch (Exception e) {
				yaProcessInfo.totalMemoryUsage += 0;
			}
		}

		/*
		 * As the ArrayListAdapter needs arrayList,so we choose the data we need
		 * to display on the list into yaProcessInfoList from yaProcessInfoList.
		 * So yaProcessInfoList is the data source. These code are shit. We need
		 * to fix. We should choose a better adapter.
		 */
		YAProcessInfo yaProcessInfo;
		// add info to processInfoList
		for (Map.Entry<String, YAProcessInfo> entry : yaProcessInfoList
				.entrySet()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			yaProcessInfo = entry.getValue();
			map.put("app_name", yaProcessInfo.appName);
			map.put("package_name", yaProcessInfo.packageName);
			// map.put("pid", yaProcessInfo.pid + "");
			map.put("memory_usage", yaProcessInfo.totalMemoryUsage + "MB");
			map.put("whitelist", yaProcessInfo.isWhiteList ? "1" : "0");
			// TODO add a filter
			map.put("is_system_app", yaProcessInfo.isSystemApp ? "1" : "0");
			map.put("icon", yaProcessInfo.icon);
			processInfoList.add(map);
		}
		Collections.sort(processInfoList, new ComparatorProcessList());
		return runningAppProcesses.size();
	}

	public boolean addToWhiteList(String packageName) {
		if (!whiteList.contains(packageName)) {
			whiteList.add(packageName);
			saveWhiteList();
		}
		return true;
	}

	public boolean removeFromWhiteList(String packageName) {
		if (whiteList.contains(packageName)) {
			whiteList.remove(packageName);
			Log.d(TAG, "remove " + packageName);
			saveWhiteList();
		}
		return true;
	}

	private boolean saveWhiteList() {
		StringBuffer fileContent = new StringBuffer("");
		HashMap<String, Object> procInfo;
		for (Iterator<HashMap<String, Object>> iterator = processInfoList
				.iterator(); iterator.hasNext();) {
			procInfo = iterator.next();
			if (procInfo.get("whitelist") == "1") {
				fileContent.append(procInfo.get("package_name") + "\n");
			}
		}
		try {
			FileOutputStream fileOutputStream = context.openFileOutput(
					WHITE_LIST_FILE_NAME, Context.MODE_PRIVATE);
			fileOutputStream.write(fileContent.toString().getBytes());

		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public void killProcesses(int option) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		HashMap<String, Object> processitem;
		String packageName;
		for (Iterator<HashMap<String, Object>> iterator = processInfoList
				.iterator(); iterator.hasNext();) {
			processitem = iterator.next();
			packageName = (String) processitem.get("package_name");
			//Log.d(TAG, packageName);
			if (whiteList.contains(packageName)&&option==YAMemoryInfo.KILL_PROCESSES_EXCEPT_WHITELIST) {
				Log.d(TAG, packageName+":except");
				continue;
			}
			activityManager.killBackgroundProcesses(packageName);
		}
	}
	
	public boolean killProcess(int position){
		if(processInfoList!=null){
			HashMap<String, Object> processitem = processInfoList.get(position);
			String packageName =   (String) processitem.get("package_name");
			String appName =   (String) processitem.get("app_name");
			activityManager.killBackgroundProcesses(packageName);
			/*
			 * Fakely remove the listItem.
			 */
			yaProcessInfoList.remove(packageName);
			processInfoList.remove(position);
			Toast.makeText(context, appName+" killed", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}
}
