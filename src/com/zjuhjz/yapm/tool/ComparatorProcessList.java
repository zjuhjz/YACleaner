package com.zjuhjz.yapm.tool;

import com.zjuhjz.yapm.db.YAProcessInfo;

import java.util.Comparator;
import java.util.HashMap;


public class ComparatorProcessList implements  Comparator<Object> {

	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
        YAProcessInfo left = (YAProcessInfo)arg0;
		@SuppressWarnings("unchecked")
        YAProcessInfo right = (YAProcessInfo)arg1;;
		int flag1 = left.isWhiteList?1:0;
		if(flag1==0){
			return left.appName.compareTo(right.appName);
		}
		return flag1;
	}

}
