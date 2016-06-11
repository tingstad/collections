/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.NewConditionNumber;
import com.rictin.util.pipe.matcher.Num;

public class NewConditionNumberImpl<T extends Number> extends NewConditionImpl<T> implements NewConditionNumber<T> {

	public NewConditionNumberImpl(T value, Invocation<?> invocation, Condition<?> conditionAnd, Condition<?> conditionOr) {
		super(value, invocation, conditionAnd, conditionOr);
	}
	
	public Condition<?> greaterThan(T number) {
		return is(Num.greaterThan(number));
	}

	public Condition<?> lessThan(T number) {
		return is(Num.lessThan(number));
	}

	public Condition<?> noGreaterThan(T number) {
		return is(Num.noGreaterThan(number));
	}

	public Condition<?> noLessThan(T number) {
		return is(Num.noLessThan(number));
	}

}
