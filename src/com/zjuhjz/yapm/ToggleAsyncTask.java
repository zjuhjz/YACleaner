package com.zjuhjz.yapm;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.zjuhjz.yapm.db.IntentFilterInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToggleAsyncTask extends AsyncTask<List<HashMap<String, Object>>, Integer, String> {

    private Context context = null;
    public String TAG = ProcessList.TAG;
    LinearLayout linearLayout;
    ProgressBar progressBar;
    @Override
    protected void onPreExecute(){
        progressBar = (ProgressBar) ((Activity) context).findViewById(R.id.autostart_setting_progressBar);
        linearLayout = (LinearLayout) ((Activity) context).findViewById(R.id.settingProgressLayout);
        linearLayout.setVisibility(View.VISIBLE);
        publishProgress(0);
    }

    @Override
    protected void onPostExecute(String i) {
        linearLayout.setVisibility(View.GONE);
    }

    @Override
    protected String doInBackground(List<HashMap<String, Object>>... params) {
        setComponentEnable(params[0]);
        return null;
    }

    public ToggleAsyncTask(Context context) {
        this.context = context;
    }

    protected void onProgressUpdate(Integer... progress) {
        progressBar.setProgress(progress[0]);
    }

    private boolean setADBEnabledState(ContentResolver cr, boolean enable) {
        if (context.checkCallingOrSelfPermission(
                Manifest.permission.WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
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

    private boolean setComponentEnable(List<HashMap<String, Object>> info) {
        ContentResolver cr = context.getContentResolver();
        HashMap<String, Object> item;
        String componentName;
        String packageName;
        boolean enable;
        boolean adbNeedsRedisable = false;
        boolean adbEnabled;
        int length;
        length = info.size();
        for (int i = 0; i < length; ++i) {
            item = info.get(i);
            componentName = (String)item.get("componentName");
            packageName = (String)item.get("packageName");
            enable = (Boolean)item.get("enable");
            if (componentName == null || componentName.isEmpty()
                    || packageName == null || packageName.isEmpty()) {
                return false;
            }
        }

        try {
            adbEnabled = (Settings.Secure.getInt(cr,
                    Settings.Secure.ADB_ENABLED) == 1);
        } catch (Settings.SettingNotFoundException e) {
            // This started to happen at times on the ICS emulator
            // (and possibly one user reported it).
            Log.w(TAG, "Failed to read adb_enabled setting, assuming no", e);
            adbEnabled = false;
        }

        // If adb is disabled, try to enable it, temporarily. This will
        // make our root call go through without hanging.
        // TODO: It seems this might no longer be required under ICS.
        if (!adbEnabled) {
            Log.i(TAG, "Switching ADB on for the root call");
            if (setADBEnabledState(cr, true)) {
                adbEnabled = true;
                adbNeedsRedisable = true;
                // Let's be extra sure we don't run into any timing-related
                // hiccups.
                sleep(1000);
            }
        }

        try {
            // Run the command; we have different invocations we can try, but
            // we'll stop at the first one we succeed with.
            //
            // On ICS, it became necessary to set a library path (which is
            // cleared for suid programs, for obvious reasons). It can't hurt
            // on older versions. See also
            // https://github.com/ChainsDD/su-binary/issues/6
            final String libs = "LD_LIBRARY_PATH=\"$LD_LIBRARY_PATH:/system/lib\" ";
            boolean success = false;
            for (String[] set : new String[][]{
                    {libs + "pm %s '%s/%s'", null},
                    {libs + "sh /system/bin/pm %s '%s/%s'", null},
                    {
                            libs
                                    + "app_process /system/bin com.android.commands.pm.Pm %s '%s/%s'",
                            "CLASSPATH=/system/framework/pm.jar"},
                    {
                            libs
                                    + "/system/bin/app_process /system/bin com.android.commands.pm.Pm %s '%s/%s'",
                            "CLASSPATH=/system/framework/pm.jar"},}) {
                for (int i =0; i<length&&((i==0)^success); ++i){
                    item = info.get(i);
                    componentName = (String)item.get("componentName");
                    packageName = (String)item.get("packageName");
                    enable = (Boolean)item.get("enable");

                    Log.d(TAG,componentName+(enable?" enabled":" disbaled"));
                    //Toast.makeText(context,componentName+(enable?" enabled":" disbaled"),Toast.LENGTH_SHORT).show();
                    if (Utils.runRootCommand(String.format(set[0],
                            (enable ? "enable" : "disable"), packageName,
                            componentName),
                            (set[1] != null) ? new String[]{set[1]} : null,
                            // The timeout shouldn't really be needed ever, since
                            // we now automatically enable ADB, which should work
                            // around any freezing issue. However, in rare, hard
                            // to reproduce cases, it still occurs, and in those
                            // cases the timeout will improve the user experience.
                            25000)) {
                        success = true;
                        publishProgress((int)((i+1)*100/length));
                        //break;
                    }
                }
                if (success){
                    break;
                }
            }

            // We are happy if both the command itself succeed (return code)...
            if (!success)
                return false;

            //context.getPackageManager();
            //new ComponentName(packageName, componentName);

            // success = mComponent.isCurrentlyEnabled() == mDoEnable;
            // if (success)
            Log.i(TAG, "State successfully changed");
            // else
            // Log.i(ListActivity.TAG, "State change failed");
            // return success;
            return true;
        } finally {
            if (adbNeedsRedisable) {
                Log.i(TAG, "Switching ADB off again");
                setADBEnabledState(cr, false);
                // Delay releasing the GUI for a while, there seems to
                // be a mysterious problem of repeating this process multiple
                // times causing it to somehow lock up, no longer work.
                // I'm hoping this might help.
                sleep(5000);
            }
        }
    }
}
