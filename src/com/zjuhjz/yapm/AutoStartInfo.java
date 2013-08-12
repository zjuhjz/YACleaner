package com.zjuhjz.yapm;

import android.Manifest.permission;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zjuhjz.yapm.db.AppItemObject;
import com.zjuhjz.yapm.db.DB;
import com.zjuhjz.yapm.db.IntentFilterInfo;
import com.zjuhjz.yapm.db.IntentInfoObject;
import com.zjuhjz.yapm.tool.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO optimize AutoStartInfo data structure.change 
//     <HashMap<String, Object>intentsAppInfoList to class object
public class AutoStartInfo {
    // all broadcast Actions
    public final static int NON_EXSISTS = -1;
    public final static int ALL_DISABLED = 0;
    public final static int PARTIALLY_DISABLED = 1;
    public final static int ALl_ENABLED = 2;

    // Intents to App
    List<HashMap<String, Object>> intentsInfoList = null;

    // App to Intents
    //List<HashMap<String, Object>> appInfoList = null;
    List<HashMap<String, Object>> appIntentsInfoList = null;
    //HashMap<String, Object> appItem = null;
    HashMap<String, Object> appIntentsInfo = null;


    public List<AppItemObject> appItemObjects;


    HashMap<String, Boolean> componentList = null;
    private static final String TAG = "yacleanerlog";
    PackageManager packageManager = null;
    Context context = null;
    ArrayList<IntentFilterInfo> info = null;

    private boolean includeSystem = false;
    final private static String HISTORY_FILE_NAME = "blockHistory";
    HashMap<String, String> historyList;


    AutoStartInfo(Context context) {
        this.context = context;
        intentsInfoList = new ArrayList<HashMap<String, Object>>();
        //appInfoList = new ArrayList<HashMap<String, Object>>();
        appItemObjects = new ArrayList<AppItemObject>();
        includeSystem = false;
        packageManager = context.getPackageManager();
        historyList = new HashMap<String, String>();
        loadHistory();
        refresh();
    }

    private void loadReceiverReader() {
        AutostartInfoLoadTask autostartInfoLoadTask = new AutostartInfoLoadTask();
        autostartInfoLoadTask.execute();
    }

    public boolean loadHistory() {
        try {
            FileInputStream fileInputStream = context
                    .openFileInput(HISTORY_FILE_NAME);
            byte[] buffer = new byte[1024];
            StringBuffer fileContent = new StringBuffer("");
            while (fileInputStream.read(buffer) != -1) {
                fileContent.append(new String(buffer));
            }
            String data = new String(fileContent);
            String[] dataList = data.split("\n");
            String[] dataItem;

            for (String i : dataList) {
                dataItem = i.split(" ");
                if (dataItem.length == 2) {
                    historyList.put(dataItem[0], dataItem[1]);
                }
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            try {
                FileOutputStream fileOutputStream = context.openFileOutput(
                        HISTORY_FILE_NAME, 0);
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

    private boolean saveHistory(){
        StringBuffer fileContent = new StringBuffer("");
        for (Map.Entry<String, String> entry : historyList.entrySet()) {
            fileContent.append(entry.getKey() + " " + entry.getValue() + "\n");
        }
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(
                    HISTORY_FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(fileContent.toString().getBytes());
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void refreshListDataSource() {
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
        //appInfoList.clear();
        appItemObjects.clear();




        String appName;
        String lastAppName = "";
        String historyStatus;
        AppItemObject appItemObject = null;
        IntentInfoObject intentInfoObject;
        int bootIntent = 0;
        int autoIntent = 0;
        int mEnable;

        for (IntentFilterInfo intentFilterInfo : info) {
            appName = intentFilterInfo.componentInfo.packageInfo.packageLabel == null ? intentFilterInfo.componentInfo.packageInfo.packageName
                    : intentFilterInfo.componentInfo.packageInfo.packageLabel;
            intentName = intentFilterInfo.action;
            if (intentFilterInfo.componentInfo.packageInfo.isSystem
                    && (!includeSystem)) {
                continue;
            }
            if (!appName.equals(lastAppName)) {
                /*if (appItem!=null){
                    appItem.put("bootIntent",bootIntent);
                    appItem.put("autoIntent",autoIntent);
                }*/

                if (appItemObject!= null){
                    appItemObject.bootIntent = bootIntent;
                    appItemObject.autoIntent = autoIntent;
                }

                //////////////////////////////////////////////////////////////////////////
                appItemObject = new AppItemObject();
                appItemObjects.add(appItemObject);
                appItemObject.appName = appName;
                appItemObject.appIcon = intentFilterInfo.componentInfo.packageInfo.icon;
                appItemObject.packageName = intentFilterInfo.componentInfo.packageInfo.packageName;
                appItemObject.bootIntent = -1;
                appItemObject.autoIntent = -1;
                appItemObject.intentInfoObjects = new ArrayList<IntentInfoObject>();
                //////////////////////////////////////////////////////////////////////////

                /*appItem = new HashMap<String, Object>();
                appInfoList.add(appItem);
                appItem.put("appName", appName);
                appItem.put("appIcon",
                        intentFilterInfo.componentInfo.packageInfo.icon);
                appItem.put("package_name",
                        intentFilterInfo.componentInfo.packageInfo.packageName);*/
                historyStatus = historyList
                        .get(intentFilterInfo.componentInfo.packageInfo.packageName);
                if (historyStatus == null) {
                    historyStatus = "default";
                }
                componentList = new HashMap<String, Boolean>();
                bootIntent = -1;
                autoIntent = -1;
            }
            appIntentsInfo = new HashMap<String, Object>();
            mEnable = intentFilterInfo.componentInfo.currentEnabledState == 2 ? 0 : 1;
            if (DB.bootIntentList.contains(intentName)){
                if (bootIntent== NON_EXSISTS){
                    if (mEnable==0){
                        bootIntent = ALL_DISABLED;
                    }
                    else if (mEnable==1){
                        bootIntent = ALl_ENABLED;
                    }
                }
                else if (bootIntent == ALL_DISABLED){
                    if (mEnable==0){
                        bootIntent = ALL_DISABLED;
                    }
                    else if (mEnable==1){
                        bootIntent = PARTIALLY_DISABLED;
                    }
                }
                else if (bootIntent == PARTIALLY_DISABLED){
                    //
                }
                else if (bootIntent == ALl_ENABLED){
                    if (mEnable==0){
                        bootIntent = PARTIALLY_DISABLED;
                    }
                    else if (mEnable==1){
                        bootIntent = ALl_ENABLED;
                    }
                }
            }
            else if (DB.autoIntentList.contains(intentName)){
                if (autoIntent== NON_EXSISTS){
                    if (mEnable==0){
                        autoIntent = ALL_DISABLED;
                    }
                    else if (mEnable==1){
                        autoIntent = ALl_ENABLED;
                    }
                }
                else if (autoIntent == ALL_DISABLED){
                    if (mEnable==0){
                        autoIntent = ALL_DISABLED;
                    }
                    else if (mEnable==1){
                        autoIntent = PARTIALLY_DISABLED;
                    }
                }
                else if (autoIntent == PARTIALLY_DISABLED){
                    //
                }
                else if (autoIntent == ALl_ENABLED){
                    if (mEnable==0){
                        autoIntent = PARTIALLY_DISABLED;
                    }
                    else if (mEnable==1){
                        autoIntent = ALl_ENABLED;
                    }
                }
            }

            //////////////////////////////////////////////////////////////////
            intentInfoObject = new IntentInfoObject();
            intentInfoObject.componentName = intentFilterInfo.componentInfo.componentName;
            intentInfoObject.packageName = intentFilterInfo.componentInfo.packageInfo.packageName;
            intentInfoObject.isSystem = intentFilterInfo.componentInfo.packageInfo.isSystem?1:0;
            intentInfoObject.enableState = intentFilterInfo.componentInfo.currentEnabledState;
            intentInfoObject.defaultEnabled = intentFilterInfo.componentInfo.defaultEnabled?1:0;
            //TODO
            intentInfoObject.isEnable = intentFilterInfo.componentInfo.currentEnabledState == 2 ? 0
                    : 1;
            intentInfoObject.intentName = intentName;
            appItemObject.intentInfoObjects.add(intentInfoObject);

            lastAppName = appName;
        }
//        if (appItem!=null){
//            appItem.put("bootIntent",bootIntent);
//            appItem.put("autoIntent",autoIntent);
//        }
        //info.clear();
        //info = null;
        saveHistory();
    }

    public void refresh() {
        // load();
        loadReceiverReader();
        //refreshListDataSource();
    }

    public void changeIncludeSystemFlag() {
        includeSystem = !includeSystem;
        refreshListDataSource();
    }

    public boolean getIncludeSystemFlag(){
        return includeSystem;
    }


    public boolean unBlockAll(ArrayList<IntentInfoObject> info) {
        ArrayList<IntentInfoObject> intentInfoObjects = (ArrayList<IntentInfoObject>)info.clone();
        for (IntentInfoObject intentInfoObject : intentInfoObjects) {
            intentInfoObject.isEnable = 1;
        }
        ToggleAsyncTask  toggleAsyncTask = new ToggleAsyncTask(context);
        toggleAsyncTask.execute(intentInfoObjects);
        saveHistory();
        return true;
    }

    public boolean blockCompelete(ArrayList<IntentInfoObject> info) {
        ArrayList<IntentInfoObject> intentInfoObjects = (ArrayList<IntentInfoObject>)info.clone();
        for (IntentInfoObject intentInfoObject:intentInfoObjects){
            intentInfoObject.isEnable = 0;
        }
        ToggleAsyncTask  toggleAsyncTask = new ToggleAsyncTask(context);
        toggleAsyncTask.execute(intentInfoObjects);
        return true;
    }

    private boolean setADBEnabledState(ContentResolver cr, boolean enable) {
        if (context.checkCallingOrSelfPermission(
                permission.WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Using secure settings API to touch ADB setting");
            return Settings.Secure.putInt(cr, Settings.Secure.ADB_ENABLED,
                    enable ? 1 : 0);
        } else {
            Log.i(TAG, "Using setprop call to touch ADB setting");
            return Utils.runRootCommand(String.format(
                    "setprop persist.service.adb.enable %s", enable ? 1 : 0),
                    null, null);
        }
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class AutostartInfoLoadTask extends AsyncTask<Void, Integer, Void> {
        //TextView textView;
        LinearLayout linearLayout;
        ProgressBar progressBar;
        @Override
        protected void onPreExecute() {
            //textView = (TextView)((Activity)context).findViewById(R.id.loading);
            progressBar = (ProgressBar) ((Activity) context).findViewById(R.id.autostart_loading_progressBar);
            linearLayout = (LinearLayout) ((Activity) context).findViewById(R.id.loadingProgressLayout);
            linearLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
//			if(textView==null){
//				textView = (TextView)((Activity)context).findViewById(R.id.loading);
//			}
            ReceiverReader receiverReader = new ReceiverReader(context, new ReceiverReader.OnLoadProgressListener() {
                @Override
                public void onProgress(
                        ArrayList<IntentFilterInfo> currentState, float progress) {
                    // TODO Auto-generated method stub
                    publishProgress((int) (progress * 100));
                }
            });
            info = receiverReader.load();
            return null;
        }

        protected void onPostExecute(ArrayList<IntentFilterInfo> i) {
            refreshListDataSource();
            linearLayout.setVisibility(View.GONE);
        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
            //textView.setText(String.valueOf(progress[0]));
        }


    }

}
