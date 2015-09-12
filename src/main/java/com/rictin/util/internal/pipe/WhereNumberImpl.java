/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.WhereNumber;

public class WhereNumberImpl implements WhereNumber {

	private final Invocation firstArg;
	private final Number firstNumber;
	private ConditionImpl condition;
	
	public WhereNumberImpl(Number number) {
		this.firstNumber = number;
		this.firstArg = PipeParent.takeNextInvocation();
	}

	public WhereNumberImpl(ConditionImpl condition, Number number) {
		this(number);
		this.condition = condition;
	}

	public Condition isGreaterThan(final Number number) {
		final Invocation secondArg = PipeParent.takeNextInvocation();
		Predicate<Object> predicate = new Predicate<Object>() {

			public boolean accept(Object element) {
				Number first = firstArg == null ? firstNumber : (Number)firstArg.invoke(element);
				Number second = secondArg == null ? number : (Number)secondArg.invoke(element);
				return first.doubleValue() > second.doubleValue();
			}
		};
		return new ConditionImpl(condition, predicate);
	}

	public Condition isLessThan(final Number number) {
		final Invocation secondArg = PipeParent.takeNextInvocation();
		Predicate predicate = new Predicate() {

			public boolean accept(Object element) {
				Number first = firstArg == null ? firstNumber : (Number)firstArg.invoke(element);
				Number second = secondArg == null ? number : (Number)secondArg.invoke(element);
				return first.doubleValue() < second.doubleValue();
			}
		};
		return new ConditionImpl(condition, predicate);
	}

}
