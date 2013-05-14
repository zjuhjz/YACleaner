package com.zjuhjz.yacleaner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.zjuhjz.yacleaner.db.IntentFilterInfo;
import com.zjuhjz.yacleaner.tool.ComparatorIntentFilterList;
import com.zjuhjz.yacleaner.tool.ComparatorProcessList;
import com.zjuhjz.yacleaner.tool.Constants;

//TODO optimize AutoStartInfo data structure.change 
//     <HashMap<String, Object>intentsAppInfoList to class object
public class AutoStartInfo {
	// all broadcast Actions

	List<HashMap<String, Object>> appInfoList = null;

	// Intents to App
	List<HashMap<String, Object>> intentsInfoList = null;
	List<HashMap<String, Object>> intentsAppInfoList = null;
	HashMap<String, Object> intentItem = null;
	HashMap<String, Object> intentsAppInfo = null;

	       private static final String TAG = "yacleanerlog";
	PackageManager packageManager = null;
	Context context = null;

	AutoStartInfo(Context context) {
		this.context = context;
		intentsInfoList = new ArrayList<HashMap<String, Object>>();
		packageManager = context.getPackageManager();
		refresh();
	}

	public void refresh() {
		loadIntentsInfo();
	}

	private void intentAppListSort(List<HashMap<String, Object>> list) {
		if (list==null) return;
		Collections.sort(list, new Comparator<HashMap<String, Object>>() {
			public int compare(HashMap<String, Object> arg0,
					HashMap<String, Object> arg1) {
				return ((Integer) arg0.get("enable_state"))
						.compareTo((Integer) arg1.get("enable_state"));
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void loadIntentsInfo() {
		String intentName;
		Intent intent;
		List<ResolveInfo> activities;
		ReceiverReader receiverReader = new ReceiverReader(context, null);
		ArrayList<IntentFilterInfo> info = receiverReader.load();
		// sort
		Collections.sort(info, new ComparatorIntentFilterList());
		
		for (IntentFilterInfo intentFilterInfo : info) {
//			Log.d(TAG, "component"
//					+ intentFilterInfo.componentInfo.componentName + " package"
//					+ intentFilterInfo.componentInfo.packageInfo.packageName);
		}
		String lastIntentName = null;
		List<String> broadcastActions = new ArrayList<String>();
		for (int i = 0; i < Constants.broadcastActions.length; ++i) {
			broadcastActions.add(Constants.broadcastActions[i][0]);
		}
		for (IntentFilterInfo intentFilterInfo : info) {
			intentName = intentFilterInfo.action;// Constants.broadcastActions[i][0];
			if (!broadcastActions.contains(intentName)) {
				continue;
			}
			// Log.d(TAG, intentName);
			// intent= new Intent(intentName);
			// activities = packageManager.queryBroadcastReceivers(intent,
			// PackageManager.GET_RESOLVED_FILTER);
			if (!intentName.equals(lastIntentName)) {
				// sort

				intentAppListSort(intentsAppInfoList);

				intentItem = new HashMap<String, Object>();
				intentItem.put("IntentName", intentName);
				intentsAppInfoList = new ArrayList<HashMap<String, Object>>();
				intentItem.put("appInfoList", intentsAppInfoList);
				intentsInfoList.add(intentItem);
			}
			HashMap<String, Object> item = new HashMap<String, Object>();
			intentsAppInfo = new HashMap<String, Object>();
			intentsAppInfo.put("component_name",
					intentFilterInfo.componentInfo.componentName);
			intentsAppInfo.put("package_name",
					intentFilterInfo.componentInfo.packageInfo.packageName);
			intentsAppInfo.put("is_system",
					intentFilterInfo.componentInfo.packageInfo.isSystem);
			intentsAppInfo.put("enable_state",
					intentFilterInfo.componentInfo.currentEnabledState);
			intentsAppInfo.put("default_enabled",
					intentFilterInfo.componentInfo.defaultEnabled);
			intentsAppInfo
					.put("display",
							intentFilterInfo.componentInfo.componentName
									+ "|"
									+ intentFilterInfo.componentInfo.packageInfo.isSystem
									+ "|"
									+ intentFilterInfo.componentInfo.currentEnabledState);
			intentsAppInfoList.add(intentsAppInfo);

			// for (ResolveInfo resolveInfo : activities) {
			// ActivityInfo activityInfo = resolveInfo.activityInfo;
			// if(intentItem==null){
			// intentItem = new HashMap<String,Object>();
			// intentItem.put("IntentName", intentName);
			// intentsAppInfoList = new ArrayList<HashMap<String,Object>>();
			// intentItem.put("appInfoList", intentsAppInfoList);
			// intentsInfoList.add(intentItem);
			// }
			// HashMap<String,Object> item = new HashMap<String,Object>();
			//
			// if (activityInfo != null){
			// intentsAppInfo = new HashMap<String,Object>();
			// intentsAppInfo.put("package_name", activityInfo.packageName);
			// intentsAppInfoList.add(intentsAppInfo);
			// }
			// }
			//

			// intentItem = null;
			lastIntentName = intentName;
		}
		intentAppListSort(intentsAppInfoList);
		// Log.d(TAG, "begin to retrieve");
	}
}
