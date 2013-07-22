package com.zjuhjz.yapm;

import com.actionbarsherlock.app.ActionBar;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        final String[] dropDownValues = new String[]{"Autostart Manager","Process  Manager"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(actionBar.getThemedContext(),
                android.R.layout.simple_spinner_item, android.R.id.text1,
                dropDownValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(adapter, this);
	}

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        if (i == 0){
            AutoStartAppList autoStartAppList = new AutoStartAppList();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, autoStartAppList).commit();
        }
        else if (i==1){
            ProcessList processList = new ProcessList();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, processList).commit();
        }
        return false;
    }
}
