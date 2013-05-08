package com.zjuhjz.yacleaner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class AutoStartInfo {
	List<HashMap<String,String>> appInfoList = null;
	private static final String TAG = "yacleanerlog";
	HashMap<String,String> appInfo = null;
	Context context;
	AutoStartInfo(Context context){
		this.context = context;
		appInfoList = new ArrayList<HashMap<String,String>>();
	}
	public void refresh(){
		
	}
	private void loadIntentsInfo(){
		final Intent intent = new Intent("android.intent.action.BOOT_COMPLETED");
		PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> activities = packageManager.queryBroadcastReceivers(intent, 0);
        
        for (ResolveInfo resolveInfo : activities) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo != null){
            	appInfo = new HashMap<String,String>();
            	appInfo.put("package_name", activityInfo.packageName);
            	appInfoList.add(appInfo);
            	Log.d(TAG,"autostart "+activityInfo.name);
            }
        }
	}
	
	
}
