package com.zjuhjz.yacleaner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.zjuhjz.yacleaner.db.IntentFilterInfo;
import com.zjuhjz.yacleaner.tool.Constants;

//TODO optimize AutoStartInfo data structure.change 
//     <HashMap<String, Object>intentsAppInfoList to class object
public class AutoStartInfo {
	// all broadcast Actions

	// Intents to App
	List<HashMap<String, Object>> intentsInfoList = null;
	List<HashMap<String, Object>> intentsAppInfoList = null;
	HashMap<String, Object> intentItem = null;
	HashMap<String, Object> intentsAppInfo = null;

	// App to Intents
	List<HashMap<String, Object>> appInfoList = null;
	List<HashMap<String, Object>> appIntentsInfoList = null;
	HashMap<String, Object> appItem = null;
	HashMap<String, Object> appIntentsInfo = null;

	
	
	private static final String TAG = "yacleanerlog";
	PackageManager packageManager = null;
	Context context = null;
	ArrayList<IntentFilterInfo> info;
	
	private boolean includeSystem = false;
	
	AutoStartInfo(Context context) {
		this.context = context;
		intentsInfoList = new ArrayList<HashMap<String, Object>>();
		appInfoList = new ArrayList<HashMap<String, Object>>();

		includeSystem = false;
		packageManager = context.getPackageManager();
		refresh();
	}
	
	private void loadReceiverReader(){
		ReceiverReader receiverReader = new ReceiverReader(context, null);
		info = receiverReader.load();
	}

	public void refresh() {
		//load();
		loadReceiverReader();
		refreshListDataSource();
	}
	
	public void changeIncludeSystemFlag(){
		includeSystem = !includeSystem;
		refreshListDataSource();
	}

	/*
	private void intentAppListSort(List<HashMap<String, Object>> list) {
		if (list == null)
			return;
		Collections.sort(list, new Comparator<HashMap<String, Object>>() {
			public int compare(HashMap<String, Object> arg0,
					HashMap<String, Object> arg1) {
				return ((Integer) arg0.get("enable_state"))
						.compareTo((Integer) arg1.get("enable_state"));
			}
		});
	}
	*/
	
	

	private void refreshListDataSource(){
		String intentName;
		Collections.sort(info, new Comparator<IntentFilterInfo>() {
			@Override
			public int compare(IntentFilterInfo lhs, IntentFilterInfo rhs) {
				if (lhs != null && rhs != null) {
					return lhs.componentInfo.packageInfo.packageName
							.compareTo(rhs.componentInfo.packageInfo.packageName);
				}
				return 0;
			}
		});
		appInfoList.clear();

		String appName;
		String lastAppName = "";
		for (IntentFilterInfo intentFilterInfo : info) {
			appName = intentFilterInfo.componentInfo.packageInfo.packageLabel == null ? intentFilterInfo.componentInfo.packageInfo.packageName
					: intentFilterInfo.componentInfo.packageInfo.packageLabel;
			intentName = intentFilterInfo.action;
			if (intentFilterInfo.componentInfo.packageInfo.isSystem&&(!includeSystem)) {
				continue;
			}
			if (!appName.equals(lastAppName)) {
				appItem = new HashMap<String, Object>();
				appInfoList.add(appItem);
				appItem.put("appName", appName);
				appItem.put("appIcon",
						intentFilterInfo.componentInfo.packageInfo.icon);
				appIntentsInfoList = new ArrayList<HashMap<String, Object>>();
				appItem.put("intentInfoList", appIntentsInfoList);
			}
			appIntentsInfo = new HashMap<String, Object>();
			appIntentsInfo.put("component_name",
					intentFilterInfo.componentInfo.componentName);
			appIntentsInfo.put("package_name",
					intentFilterInfo.componentInfo.packageInfo.packageName);

			appIntentsInfo.put("is_system",
					intentFilterInfo.componentInfo.packageInfo.isSystem);
			appIntentsInfo.put("enable_state",
					intentFilterInfo.componentInfo.currentEnabledState == 2 ? 0
							: 1);
			appIntentsInfo.put("default_enabled",
					intentFilterInfo.componentInfo.defaultEnabled);
			appIntentsInfo.put("icon",
					intentFilterInfo.componentInfo.packageInfo.icon);
			appIntentsInfo.put("intentName", intentName);
			appIntentsInfo.put("display", intentName + "\n"
					+ intentFilterInfo.componentInfo.componentName);
			appIntentsInfoList.add(appIntentsInfo);

			lastAppName = appName;

		}
	}
	
	/*private void refreshListDataSource2(boolean includeSystem){
		String intentName="";
		String lastIntentName = "";
		
		for (IntentFilterInfo intentFilterInfo : info) {
			if (intentFilterInfo.componentInfo.packageInfo.isSystem&&(!includeSystem)) {
				Log.d(TAG,intentFilterInfo.componentInfo.packageInfo.packageName);
				continue;
			}
			intentName = intentFilterInfo.action;
			if (!intentName.equals(lastIntentName)) {
				// sort
				intentAppListSort(intentsAppInfoList);

				intentItem = new HashMap<String, Object>();
				intentItem.put("IntentName", intentName);
				intentsAppInfoList = new ArrayList<HashMap<String, Object>>();
				intentItem.put("appInfoList", intentsAppInfoList);
				intentsInfoList.add(intentItem);
			}
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
	}
	*/
	
//
//	private void load() {
//		String intentName;
//		ReceiverReader receiverReader = new ReceiverReader(context, null);
//		info = receiverReader.load();
//		// sort
//
//		String lastIntentName = "";
//		List<String> broadcastActions = new ArrayList<String>();
//		for (int i = 0; i < Constants.broadcastActions.length; ++i) {
//			broadcastActions.add(Constants.broadcastActions[i][0]);
//		}
//
//		Collections.sort(info, new Comparator<IntentFilterInfo>() {
//			@Override
//			public int compare(IntentFilterInfo lhs, IntentFilterInfo rhs) {
//				if (lhs != null && rhs != null) {
//					return lhs.componentInfo.packageInfo.packageName
//							.compareTo(rhs.componentInfo.packageInfo.packageName);
//				}
//				return 0;
//			}
//		});
//
//		String appName;
//		String lastAppName = "";
//		for (IntentFilterInfo intentFilterInfo : info) {
//			appName = intentFilterInfo.componentInfo.packageInfo.packageLabel == null ? intentFilterInfo.componentInfo.packageInfo.packageName
//					: intentFilterInfo.componentInfo.packageInfo.packageLabel;
//			intentName = intentFilterInfo.action;
//			/*
//			 * Delete broadcastActions filter.
//			 * 
//			 * if (!broadcastActions.contains(intentName)) {
//				continue;
//			}*/
//			if (!appName.equals(lastAppName)) {
//				appItem = new HashMap<String, Object>();
//				appInfoList.add(appItem);
//				appItem.put("appName", appName);
//				appItem.put("appIcon",
//						intentFilterInfo.componentInfo.packageInfo.icon);
//				appIntentsInfoList = new ArrayList<HashMap<String, Object>>();
//				appItem.put("intentInfoList", appIntentsInfoList);
//			}
//			appIntentsInfo = new HashMap<String, Object>();
//			appIntentsInfo.put("component_name",
//					intentFilterInfo.componentInfo.componentName);
//			appIntentsInfo.put("package_name",
//					intentFilterInfo.componentInfo.packageInfo.packageName);
//
//			appIntentsInfo.put("is_system",
//					intentFilterInfo.componentInfo.packageInfo.isSystem);
//			appIntentsInfo.put("enable_state",
//					intentFilterInfo.componentInfo.currentEnabledState == 2 ? 0
//							: 1);
//			appIntentsInfo.put("default_enabled",
//					intentFilterInfo.componentInfo.defaultEnabled);
//			appIntentsInfo.put("icon",
//					intentFilterInfo.componentInfo.packageInfo.icon);
//			appIntentsInfo.put("intentName", intentName);
//			appIntentsInfo.put("display", intentName + "\n"
//					+ intentFilterInfo.componentInfo.componentName);
//			appIntentsInfoList.add(appIntentsInfo);
//
//			lastAppName = appName;
//
//		}
//		Log.d(TAG, "the total size :" + appInfoList.size());
//
//		for (IntentFilterInfo intentFilterInfo : info) {
//			intentName = intentFilterInfo.action;
//			/*if (!broadcastActions.contains(intentName)) {
//				continue;
//			}*/
//
//			if (!intentName.equals(lastIntentName)) {
//				// sort
//				intentAppListSort(intentsAppInfoList);
//
//				intentItem = new HashMap<String, Object>();
//				intentItem.put("IntentName", intentName);
//				intentsAppInfoList = new ArrayList<HashMap<String, Object>>();
//				intentItem.put("appInfoList", intentsAppInfoList);
//				intentsInfoList.add(intentItem);
//			}
//			intentsAppInfo = new HashMap<String, Object>();
//			intentsAppInfo.put("component_name",
//					intentFilterInfo.componentInfo.componentName);
//			intentsAppInfo.put("package_name",
//					intentFilterInfo.componentInfo.packageInfo.packageName);
//			intentsAppInfo.put("is_system",
//					intentFilterInfo.componentInfo.packageInfo.isSystem);
//			intentsAppInfo.put("enable_state",
//					intentFilterInfo.componentInfo.currentEnabledState);
//			intentsAppInfo.put("default_enabled",
//					intentFilterInfo.componentInfo.defaultEnabled);
//			intentsAppInfo
//					.put("display",
//							intentFilterInfo.componentInfo.componentName
//									+ "|"
//									+ intentFilterInfo.componentInfo.packageInfo.isSystem
//									+ "|"
//									+ intentFilterInfo.componentInfo.currentEnabledState);
//			intentsAppInfoList.add(intentsAppInfo);
//
//			// for (ResolveInfo resolveInfo : activities) {
//			// ActivityInfo activityInfo = resolveInfo.activityInfo;
//			// if(intentItem==null){
//			// intentItem = new HashMap<String,Object>();
//			// intentItem.put("IntentName", intentName);
//			// intentsAppInfoList = new ArrayList<HashMap<String,Object>>();
//			// intentItem.put("appInfoList", intentsAppInfoList);
//			// intentsInfoList.add(intentItem);
//			// }
//			// HashMap<String,Object> item = new HashMap<String,Object>();
//			//
//			// if (activityInfo != null){
//			// intentsAppInfo = new HashMap<String,Object>();
//			// intentsAppInfo.put("package_name", activityInfo.packageName);
//			// intentsAppInfoList.add(intentsAppInfo);
//			// }
//			// }
//			//
//
//			// intentItem = null;
//			lastIntentName = intentName;
//		}
//		intentAppListSort(intentsAppInfoList);
//		// Log.d(TAG, "begin to retrieve");
//	}
}
