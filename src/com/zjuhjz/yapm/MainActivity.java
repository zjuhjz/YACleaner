package com.zjuhjz.yapm;

import com.actionbarsherlock.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.ads.*;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener, AdListener {
    public final static int FREE_VERSION = 0;
    public final static int CHARGED_VERSION = 1;
    public static int version = FREE_VERSION;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if (version == FREE_VERSION){
            setContentView(R.layout.activity_main_free);
        }else {
            setContentView(R.layout.activity_main);
        }

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        final String[] dropDownValues = new String[]{"Autostart Disabler","Process  Manager"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(actionBar.getThemedContext(),
                android.R.layout.simple_spinner_item, android.R.id.text1,
                dropDownValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(adapter, this);
        AdView adView = (AdView) findViewById(R.id.adView);
        adView.setAdListener(this);

        ImageView imageView = (ImageView)findViewById(R.id.closeImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.adViewLayout);
                frameLayout.setVisibility(View.GONE);
            }
        });
	}

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        if (i == 0){
            AutoStartAppList autoStartAppList = new AutoStartAppList();
            getSupportFragmentManager().beginTransaction()
                    .replace(version==FREE_VERSION? R.id.main_container_free:R.id.main_container, autoStartAppList).commit();
        }
        else if (i==1){
            ProcessList processList = new ProcessList();
            getSupportFragmentManager().beginTransaction()
                    .replace(version==FREE_VERSION? R.id.main_container_free:R.id.main_container, processList).commit();
        }
        return false;
    }

    @Override
    public void onReceiveAd(Ad ad) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.adViewLayout);
        frameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode errorCode) {

    }

    @Override
    public void onPresentScreen(Ad ad) {

    }

    @Override
    public void onDismissScreen(Ad ad) {

    }

    @Override
    public void onLeaveApplication(Ad ad) {

    }
}
