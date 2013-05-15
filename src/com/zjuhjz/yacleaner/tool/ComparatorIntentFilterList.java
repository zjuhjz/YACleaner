package com.zjuhjz.yacleaner.tool;

import java.util.Comparator;

import com.zjuhjz.yacleaner.db.IntentFilterInfo;

public class ComparatorIntentFilterList implements  Comparator<IntentFilterInfo>{

	public int compare(IntentFilterInfo lhs, IntentFilterInfo rhs) {
		// TODO Auto-generated method stub
		return lhs.action.compareTo(rhs.action);
	}
	
}
