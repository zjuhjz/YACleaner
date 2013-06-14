package com.zjuhjz.yapm;

import java.util.HashMap;
import com.zjuhjz.yapm.R;
import com.zjuhjz.yapm.adapter.AutoStartAppListAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class AutoStartAppList extends ListFragment implements
		OnItemClickListener {
	AutoStartAppListAdapter autoStartAppListAdapter;
	public static final String TAG = "yacleanerlog";
	AutoStartInfo autoStartInfo;
	static final int INCLUDE_SYSTEM_ID = Menu.FIRST;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().getPackageManager();
		autoStartInfo = new AutoStartInfo(getActivity());
		this.getListView().setOnItemClickListener(this);
		showAppInfo();
		registerForContextMenu(getListView());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.activity_autostart_app_list,
				container, false);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem populateItem = menu.add(Menu.NONE, INCLUDE_SYSTEM_ID, 0,
				"All App");
		populateItem.setIcon(R.drawable.ic_include_system_package);
		MenuItemCompat.setShowAsAction(populateItem,
				MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case INCLUDE_SYSTEM_ID:
			if (autoStartInfo != null && autoStartAppListAdapter != null) {
				autoStartInfo.changeIncludeSystemFlag();
				autoStartAppListAdapter.notifyDataSetChanged();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.autostartapp_contextmenu, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	private void showAppInfo() {
		Context context = getActivity();
		autoStartAppListAdapter = new AutoStartAppListAdapter(context,
				autoStartInfo.appInfoList, R.layout.autostart_app_list_item,
				new String[] { "appName", "historyStatus" }, new int[] {
						R.id.autostart_app_name, R.id.autostart_status });
		setListAdapter(autoStartAppListAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		arg1.showContextMenu();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int id = item.getItemId();
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		HashMap<String, Object> mAppitem = autoStartInfo.appInfoList
				.get(info.position);
		if (id == R.id.block_gentle) {
			autoStartInfo.blockGentle(mAppitem);
			// List<HashMap<String,Object>> mIntentsInfoList =
			// (List<HashMap<String,Object>>)mAppitem.get("intentInfoList");
		} else if (id == R.id.block_strong) {
			autoStartInfo.blockStrong(mAppitem);
		} else if (id == R.id.block_complete) {
			autoStartInfo.blockCompelete(mAppitem);
		} else if (id == R.id.unblock) {
			autoStartInfo.unBlockAll(mAppitem);
		}
		autoStartAppListAdapter.notifyDataSetChanged();
		return super.onContextItemSelected(item);
	}
	//
	// private boolean unBlockAll(HashMap<String, Object> list) {
	// @SuppressWarnings("unchecked")
	// List<HashMap<String, Object>> mIntentsInfoList = (List<HashMap<String,
	// Object>>) list
	// .get("intentInfoList");
	// List<String[]> unBlockComponentList = new ArrayList<String[]>();
	// for (HashMap<String, Object> mIntentsInfo : mIntentsInfoList) {
	// String[] unBlockComponent = new String[] {
	// (String) mIntentsInfo.get("component_name"),
	// (String) mIntentsInfo.get("package_name") };
	// unBlockComponentList.add(unBlockComponent);
	// }
	// for (String[] i : unBlockComponentList) {
	// setComponentEnable(true, i[0], i[1]);
	// }
	// Toast.makeText(getActivity(), "unblock succesfully", Toast.LENGTH_SHORT)
	// .show();
	// return true;
	// }
	//
	// private boolean blockCompelete(HashMap<String, Object> list) {
	// @SuppressWarnings("unchecked")
	// List<HashMap<String, Object>> mIntentsInfoList = (List<HashMap<String,
	// Object>>) list
	// .get("intentInfoList");
	// List<String[]> blockComponentList = new ArrayList<String[]>();
	// for (HashMap<String, Object> mIntentsInfo : mIntentsInfoList) {
	// String[] blockComponent = new String[] {
	// (String) mIntentsInfo.get("component_name"),
	// (String) mIntentsInfo.get("package_name") };
	// blockComponentList.add(blockComponent);
	// }
	// for (String[] i : blockComponentList) {
	// setComponentEnable(false, i[0], i[1]);
	// }
	//
	// Toast.makeText(getActivity(), "block succesfully", Toast.LENGTH_SHORT)
	// .show();
	// return true;
	// }
	//
	// private boolean blockStrong(HashMap<String, Object> list) {
	// @SuppressWarnings("unchecked")
	// List<HashMap<String, Object>> mIntentsInfoList = (List<HashMap<String,
	// Object>>) list
	// .get("intentInfoList");
	// List<String> blockIntentsNameList = new ArrayList<String>();
	// for (String[] i : Constants.broadcastActions) {
	// blockIntentsNameList.add(i[0]);
	// }
	// List<String[]> blockComponentList = new ArrayList<String[]>();
	// for (HashMap<String, Object> mIntentsInfo : mIntentsInfoList) {
	// if (blockIntentsNameList.contains((String) mIntentsInfo
	// .get("intentName"))) {
	// String[] blockComponent = new String[] {
	// (String) mIntentsInfo.get("component_name"),
	// (String) mIntentsInfo.get("package_name") };
	// blockComponentList.add(blockComponent);
	// }
	// }
	// for (String[] i : blockComponentList) {
	// setComponentEnable(false, i[0], i[1]);
	// }
	// Toast.makeText(getActivity(), "block succesfully", Toast.LENGTH_SHORT)
	// .show();
	// return true;
	// }
	//
	// private boolean blockGentle(HashMap<String, Object> list) {
	// @SuppressWarnings("unchecked")
	// List<HashMap<String, Object>> mIntentsInfoList = (List<HashMap<String,
	// Object>>) list
	// .get("intentInfoList");
	// List<String> blockIntentsNameList = new ArrayList<String>();
	// for (String i : Constants.gentleIntentsList) {
	// blockIntentsNameList.add(i);
	// }
	// List<String[]> blockComponentList = new ArrayList<String[]>();
	// for (HashMap<String, Object> mIntentsInfo : mIntentsInfoList) {
	// if (blockIntentsNameList.contains((String) mIntentsInfo
	// .get("intentName"))) {
	// String[] blockComponent = new String[] {
	// (String) mIntentsInfo.get("component_name"),
	// (String) mIntentsInfo.get("package_name") };
	// blockComponentList.add(blockComponent);
	// }
	// }
	// for (String[] i : blockComponentList) {
	// setComponentEnable(false, i[0], i[1]);
	// }
	// Toast.makeText(getActivity(), "block succesfully", Toast.LENGTH_SHORT)
	// .show();
	// return true;
	// }
	//
	// private boolean setComponentEnable(boolean enable, String componentName,
	// String packageName) {
	// ContentResolver cr = getActivity().getContentResolver();
	// boolean adbNeedsRedisable = false;
	// boolean adbEnabled;
	//
	// if (componentName == null || componentName.isEmpty()
	// || packageName == null || packageName.isEmpty()) {
	// return false;
	// }
	// try {
	// adbEnabled = (Settings.Secure.getInt(cr,
	// Settings.Secure.ADB_ENABLED) == 1);
	// } catch (SettingNotFoundException e) {
	// // This started to happen at times on the ICS emulator
	// // (and possibly one user reported it).
	// Log.w(TAG, "Failed to read adb_enabled setting, assuming no", e);
	// adbEnabled = false;
	// }
	//
	// // If adb is disabled, try to enable it, temporarily. This will
	// // make our root call go through without hanging.
	// // TODO: It seems this might no longer be required under ICS.
	// if (!adbEnabled) {
	// Log.i(TAG, "Switching ADB on for the root call");
	// if (setADBEnabledState(cr, true)) {
	// adbEnabled = true;
	// adbNeedsRedisable = true;
	// // Let's be extra sure we don't run into any timing-related
	// // hiccups.
	// sleep(1000);
	// }
	// }
	//
	// try {
	// // Run the command; we have different invocations we can try, but
	// // we'll stop at the first one we succeed with.
	// //
	// // On ICS, it became necessary to set a library path (which is
	// // cleared for suid programs, for obvious reasons). It can't hurt
	// // on older versions. See also
	// // https://github.com/ChainsDD/su-binary/issues/6
	// final String libs = "LD_LIBRARY_PATH=\"$LD_LIBRARY_PATH:/system/lib\" ";
	// boolean success = false;
	// for (String[] set : new String[][] {
	// { libs + "pm %s '%s/%s'", null },
	// { libs + "sh /system/bin/pm %s '%s/%s'", null },
	// {
	// libs
	// + "app_process /system/bin com.android.commands.pm.Pm %s '%s/%s'",
	// "CLASSPATH=/system/framework/pm.jar" },
	// {
	// libs
	// +
	// "/system/bin/app_process /system/bin com.android.commands.pm.Pm %s '%s/%s'",
	// "CLASSPATH=/system/framework/pm.jar" }, }) {
	// if (Utils.runRootCommand(String.format(set[0],
	// (enable ? "enable" : "disable"), packageName,
	// componentName),
	// (set[1] != null) ? new String[] { set[1] } : null,
	// // The timeout shouldn't really be needed ever, since
	// // we now automatically enable ADB, which should work
	// // around any freezing issue. However, in rare, hard
	// // to reproduce cases, it still occurs, and in those
	// // cases the timeout will improve the user experience.
	// 25000)) {
	// success = true;
	// break;
	// }
	// }
	//
	// // We are happy if both the command itself succeed (return code)...
	// if (!success)
	// return false;
	//
	// getActivity().getPackageManager();
	// new ComponentName(packageName, componentName);
	//
	// // success = mComponent.isCurrentlyEnabled() == mDoEnable;
	// // if (success)
	// Log.i(TAG, "State successfully changed");
	// // else
	// // Log.i(ListActivity.TAG, "State change failed");
	// // return success;
	// return true;
	// } finally {
	// if (adbNeedsRedisable) {
	// Log.i(TAG, "Switching ADB off again");
	// setADBEnabledState(cr, false);
	// // Delay releasing the GUI for a while, there seems to
	// // be a mysterious problem of repeating this process multiple
	// // times causing it to somehow lock up, no longer work.
	// // I'm hoping this might help.
	// sleep(5000);
	// }
	// }
	// }
	//
	// private boolean setADBEnabledState(ContentResolver cr, boolean enable) {
	// if (getActivity().checkCallingOrSelfPermission(
	// permission.WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
	// Log.i(TAG, "Using secure settings API to touch ADB setting");
	// return Settings.Secure.putInt(cr, Settings.Secure.ADB_ENABLED,
	// enable ? 1 : 0);
	// } else {
	// Log.i(TAG, "Using setprop call to touch ADB setting");
	// return Utils.runRootCommand(String.format(
	// "setprop persist.service.adb.enable %s", enable ? 1 : 0),
	// null, null);
	// }
	// }

	// public static void sleep(long time) {
	// try {
	// Thread.sleep(time);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }

}
