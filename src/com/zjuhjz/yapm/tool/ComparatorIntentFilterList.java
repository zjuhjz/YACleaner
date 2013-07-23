package com.zjuhjz.yapm.tool;

import java.util.Comparator;

import com.zjuhjz.yapm.db.IntentFilterInfo;

public class ComparatorIntentFilterList implements  Comparator<IntentFilterInfo>{

	public int compare(IntentFilterInfo lhs, IntentFilterInfo rhs) {
		// TODO Auto-generated method stub
		return lhs.action.compareTo(rhs.action);
	}
	
}
