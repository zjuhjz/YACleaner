package com.zjuhjz.yacleaner.db;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

public class YAProcessInfo{
	public String appName;
	public String processName;
	public String packageName;
	
	public boolean isWhiteList;
	public boolean isSystemApp;
	public long totalMemoryUsage;
	public List<String> processNameList;
	public List<Integer> pid;
	public int iconResourceId;
	
	public YAProcessInfo(){
		totalMemoryUsage = 0;
		processNameList = new ArrayList<String>();
		pid = new ArrayList<Integer>();
	}
}