package com.rictin.util;

import com.rictin.util.internal.condition.NumberConditionImpl;

import condition.NumberCondition;

public class Conditions {

	private Conditions() {}
	
	public static NumberCondition where(Number number) {
		return new NumberConditionImpl(number);
	}

}
