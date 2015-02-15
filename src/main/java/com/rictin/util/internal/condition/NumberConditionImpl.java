package com.rictin.util.internal.condition;

import java.math.BigDecimal;

import com.rictin.util.Condition;

import condition.NumberCondition;

public class NumberConditionImpl implements NumberCondition {

	public NumberConditionImpl(Number number) {
	}

	public Condition<Number> isGreaterThan(final Number number) {
		return new Condition<Number>() {
			
			public boolean where(Number value) {
				return compare(value, number) > 0;
			}
		};
	}

	public Condition<Number> isLessThan(final Number number) {
		return new Condition<Number>() {
			
			public boolean where(Number value) {
				return compare(value, number) < 0;
			}
		};
	}

	private int compare(Number a, Number b) {
		return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
	}
	
}
