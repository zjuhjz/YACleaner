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

import com.zjuhjz.yacleaner.tool.Constants;;

public class AutoStartInfo {
	//all broadcast Actions
	
	List<HashMap<String,String>> appInfoList = null;
	private static final String TAG = "yacleanerlog";
	HashMap<String,String> appInfo = null;
	Context context;
	AutoStartInfo(Context context){
		this.context = context;
		appInfoList = new ArrayList<HashMap<String,String>>();
		refresh();
	}
	public void refresh(){
		loadIntentsInfo();
	}
	private void loadIntentsInfo(){
		final Intent intent = new Intent("android.intent.action.BOOT_COMPLETED");
		String intentName;
		for(int i = 0; i < Constants.broadcastActions.length; ++i){
			intentName = Constants.broadcastActions[i][0];
			
		}
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
