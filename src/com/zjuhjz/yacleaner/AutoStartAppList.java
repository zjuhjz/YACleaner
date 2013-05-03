package com.zjuhjz.yacleaner;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class AutoStartAppList extends ListFragment implements OnItemClickListener {
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		//this.getListView().setOnItemClickListener(this);
		PackageManager packageManager = getActivity().getPackageManager();
		
		List<String> startupApps = new ArrayList<String>();
        final Intent intent = new Intent("android.intent.action.BOOT_COMPLETED");
        final List<ResolveInfo> activities = packageManager.queryBroadcastReceivers(intent, 0);
        for (ResolveInfo resolveInfo : activities) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo != null)
                startupApps.add(activityInfo.name);
        }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		
	}
}
