/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.condition;

import java.math.BigDecimal;

import com.rictin.util.Condition;
import com.rictin.util.condition.ChainedConditionImpl;
import com.rictin.util.condition.NumberCondition;


public class NumberConditionImpl extends ChainedConditionImpl<Number> implements NumberCondition {

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

	private static int compare(Number a, Number b) {
		return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
	}

	public boolean where(Number value) {
		// TODO Auto-generated method stub
		return false;
	}

}
