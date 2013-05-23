package com.zjuhjz.yacleaner.tool;

import java.util.Comparator;
import java.util.HashMap;


public class ComparatorProcessList implements  Comparator<Object> {

	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		HashMap<String, String> left = (HashMap<String, String>)arg0;
		@SuppressWarnings("unchecked")
		HashMap<String, String> right = (HashMap<String, String>)arg1;;
		int flag1 = left.get("whitelist").compareTo(right.get("whitelist"));
		if(flag1==0){
			return left.get("app_name").compareTo(right.get("app_name"));
		}
		return flag1;
	}

}
