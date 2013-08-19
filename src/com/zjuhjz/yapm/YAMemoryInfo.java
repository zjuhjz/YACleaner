package com.zjuhjz.yapm;

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

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zjuhjz.yapm.db.IntentFilterInfo;
import com.zjuhjz.yapm.db.YAProcessInfo;
import com.zjuhjz.yapm.tool.*;

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
    private BaseAdapter adapter  = null;
	HashMap<String, YAProcessInfo> yaProcessInfoMap;
    List<YAProcessInfo> yaProcessInfos;

    YAMemoryInfoLoadTask yaMemoryInfoLoadTask;

	Context context;
//	List<HashMap<String, Object>> processInfoList;
	List<HashMap<String, String>> servicesInfoList;
	YAProcessInfo yaProcessInfo;
	CMDExecute cmdExecute = new CMDExecute();
    List<Integer> pids;
	YAMemoryInfo(Context context) {
//		processInfoList = new ArrayList<HashMap<String, Object>>();
		servicesInfoList = new ArrayList<HashMap<String, String>>();
		this.context = context;
		activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		loadWhiteList();
		refresh();
	}

    public void setAdapter(BaseAdapter adapter){
        this.adapter = adapter;
    }

	public boolean refresh() {
		refreshProcessInfo();
		refreshMemoryInfo();
		return true;
	}

	public boolean refreshMemoryInfo() {
		activityManager.getMemoryInfo(mi);
		availableMemory = mi.availMem / (1024 * 1024);
		return true;
	}

	public boolean loadWhiteList() {
		try {
			FileInputStream fileInputStream = context
					.openFileInput(WHITE_LIST_FILE_NAME);
			byte[] buffer = new byte[1024];
			StringBuffer fileContent = new StringBuffer("");
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
        HashMap<Integer, Integer> memoryUsageByid = new HashMap<Integer, Integer>();

		// ///////initialize///////////

		// clear
		if (runningAppProcesses != null) {
			runningAppProcesses.clear();
		}
        if (yaProcessInfos!=null)
            yaProcessInfos.clear();
//		if (processInfoList != null) {
//			processInfoList.clear();
//		}

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
                memoryUsageByid.put(Integer.parseInt(itempieces[1]),
						Integer.parseInt(itempieces[4])/1024);
                //Log.d(TAG,itempieces[1]+" "+itempieces[4] );

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
        pids = new ArrayList<Integer>();
        yaProcessInfoMap = new HashMap<String, YAProcessInfo>();
        yaProcessInfos = new ArrayList<YAProcessInfo>();
		for (Iterator<RunningAppProcessInfo> iterator = runningAppProcesses
				.iterator(); iterator.hasNext();) {
			procInfo = iterator.next();
            pids.add(procInfo.pid);
			try {
				ai = pm.getApplicationInfo(procInfo.pkgList[0], 0);
			} catch (final NameNotFoundException e) {
				ai = null;
			}
			final String applicationName = (String) (ai != null ? pm
					.getApplicationLabel(ai) : procInfo.processName);
			if (yaProcessInfoMap.containsKey(ai.packageName)) {
				yaProcessInfo = yaProcessInfoMap.get(ai.packageName);
			} else {
				yaProcessInfo = new YAProcessInfo();
				yaProcessInfo.appName = applicationName;
				yaProcessInfo.processName = procInfo.processName;
				yaProcessInfo.packageName = ai.packageName;
				yaProcessInfo.isWhiteList = whiteList.contains(ai.packageName);
				yaProcessInfo.icon = ai.loadIcon(pm);
				yaProcessInfo.isSystemApp = ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
                yaProcessInfoMap.put(yaProcessInfo.packageName, yaProcessInfo);
                yaProcessInfos.add(yaProcessInfo);
                try {
                    yaProcessInfo.totalMemoryUsage += (memoryUsageByid.get(procInfo.pid));
                    //Log.d(TAG,procInfo.pid+":"+yaProcessInfo.totalMemoryUsage+"added"+memoryUsageByid.get(procInfo.pid));
                } catch (Exception e) {
                    yaProcessInfo.totalMemoryUsage += 0;
                }
			}
			yaProcessInfo.pid.add(procInfo.pid);
			yaProcessInfo.processNameList.add(procInfo.processName);
		}

		Collections.sort(yaProcessInfos, new ComparatorProcessList());
        if (memoryUsageByid.size()<2){
            if (yaMemoryInfoLoadTask==null)
            {
                yaMemoryInfoLoadTask = new YAMemoryInfoLoadTask(pm);
                yaMemoryInfoLoadTask.execute();
            }
            else {
                if (yaMemoryInfoLoadTask.getStatus()!=YAMemoryInfoLoadTask.Status.RUNNING){
                    yaMemoryInfoLoadTask = new YAMemoryInfoLoadTask(pm);
                    yaMemoryInfoLoadTask.execute();
                }
            }
        }
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
			saveWhiteList();
		}
		return true;
	}

	private boolean saveWhiteList() {
		StringBuffer fileContent = new StringBuffer("");
		HashMap<String, Object> procInfo;
		for (YAProcessInfo i: yaProcessInfos) {
			if (i.isWhiteList) {
				fileContent.append(i.packageName + "\n");
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
		String packageName;
		for (YAProcessInfo i : yaProcessInfos) {
			packageName =  i.packageName;
			if (whiteList.contains(packageName)&&option==YAMemoryInfo.KILL_PROCESSES_EXCEPT_WHITELIST) {
				continue;
			}
			activityManager.killBackgroundProcesses(packageName);
		}
	}
	
	public boolean killProcess(int position){
		if(yaProcessInfos!=null){
			YAProcessInfo item = yaProcessInfos.get(position);
			String packageName = item.packageName;
			String appName = item.appName;
			activityManager.killBackgroundProcesses(packageName);
			/*
			 * Fakely remove the listItem.
			 */
            yaProcessInfoMap.remove(packageName);
            yaProcessInfos.remove(position);
			Toast.makeText(context, appName+" killed", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}
    public class YAMemoryInfoLoadTask extends AsyncTask<Void, Integer, Integer> {
        ApplicationInfo ai;
        PackageManager pm;
        YAMemoryInfoLoadTask(PackageManager pm ){
            this.pm = pm;
        }
        @Override
        protected void onPreExecute() {
        }


        @Override
        protected Integer doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<Integer, Integer> memoryUsageByid = new HashMap<Integer, Integer>();

            yaProcessInfoMap = new HashMap<String, YAProcessInfo>();
            RunningAppProcessInfo procInfo;
            //
            int[] ints = new int[pids.size()];
            int k = 0;

            Log.d(TAG,"start get list");
            for (Integer i : pids){
                ints[k++] = i.intValue();
            }
            Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(ints);
            k = 0;
            for (Debug.MemoryInfo memoryInfo:memoryInfos){
                if (memoryInfo!=null){
                    memoryUsageByid.put(ints[k++], memoryInfo.getTotalPrivateDirty()/1024);
                }
                else{
                    memoryUsageByid.put(ints[k++],0);
                }
            }
            Log.d(TAG,"list got");
            for (Iterator<RunningAppProcessInfo> iterator = runningAppProcesses
                    .iterator(); iterator.hasNext();) {
                procInfo = iterator.next();
                try {
                    ai = pm.getApplicationInfo(procInfo.pkgList[0], 0);
                } catch (final NameNotFoundException e) {
                    ai = null;
                }
                if (yaProcessInfoMap.containsKey(ai.packageName)) {
                    yaProcessInfo = yaProcessInfoMap.get(ai.packageName);
                    yaProcessInfo.pid.add(procInfo.pid);
                    yaProcessInfo.processNameList.add(procInfo.processName);
                    try {
                        yaProcessInfo.totalMemoryUsage += memoryUsageByid.get(procInfo.pid);
                    } catch (Exception e) {
                        yaProcessInfo.totalMemoryUsage += 0;
                    }
                }
                publishProgress();
            }
            Log.d(TAG,"refreshed");
            return 0;
        }

        protected void onPostExecute(Integer v) {

        }

        protected void onProgressUpdate(Integer... progress) {
            adapter.notifyDataSetChanged();
        }


    }
}
