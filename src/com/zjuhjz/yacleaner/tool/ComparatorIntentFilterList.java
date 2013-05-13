package com.zjuhjz.yacleaner.tool;

import java.util.Comparator;

import com.zjuhjz.yacleaner.db.IntentFilterInfo;

public class ComparatorIntentFilterList implements  Comparator{

	@Override
	public int compare(Object lhs, Object rhs) {
		IntentFilterInfo left = (IntentFilterInfo)lhs;
		IntentFilterInfo right = (IntentFilterInfo)rhs;
		return left.action.compareTo(right.action);
	}
	
}
