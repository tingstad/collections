package com.rictin.util.internal.condition;

import java.math.BigDecimal;

import com.rictin.util.Condition;

import condition.NumberCondition;

public class NumberConditionImpl implements NumberCondition {

	private Number number;

	public NumberConditionImpl(Number number) {
		this.number = number;
	}

	public Condition<Number> isGreaterThan(final Number number) {
		return new Condition<Number>() {
			
			public boolean where(Number value) {
				return new BigDecimal(value.toString()).compareTo(new BigDecimal(number.toString())) > 0;
			}
		};
	}

}
