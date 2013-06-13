package com.zjuhjz.yapm;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.zjuhjz.yapm.ProcessList;
import com.zjuhjz.yapm.R;


public class MainActivity extends FragmentActivity  {
	private FragmentTabHost mTabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Process Management"),ProcessList.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("stat").setIndicator("Autostart Management"),AutoStartAppList.class, null);
	}

}
