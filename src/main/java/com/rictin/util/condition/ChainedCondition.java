package com.rictin.util.condition;

import com.rictin.util.Condition;

public interface ChainedCondition<T> extends Condition<T> {

	public abstract NumberCondition and(Number number);

}
