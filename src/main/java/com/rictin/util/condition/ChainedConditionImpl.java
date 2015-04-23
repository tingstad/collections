package com.rictin.util.condition;

import com.rictin.util.internal.condition.NumberConditionImpl;

public class ChainedConditionImpl<T> implements ChainedCondition<T> {

	public boolean where(T value) {
		// TODO Auto-generated method stub
		return false;
	}

	public NumberCondition and(Number number) {
		return new NumberConditionImpl(number);
	}	
	
}
