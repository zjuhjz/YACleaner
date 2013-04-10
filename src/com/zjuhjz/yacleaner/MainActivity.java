package com.zjuhjz.yacleaner;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import com.zjuhjz.yacleaner.ProcessList;


public class MainActivity extends FragmentActivity  {
	private FragmentTabHost mTabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"),ProcessList.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("stat").setIndicator("Simple"),ProcessList.class, null);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}

}
