/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.NewCondition;
import com.rictin.util.pipe.matcher.Matcher;

public class NewConditionImpl<T> implements NewCondition<T> {

	private final T firstValue;
	private final Invocation firstInvocation;
	private final Condition conditionAnd;
	private final Condition conditionOr;

	public NewConditionImpl(T value, Invocation<?> invocation, Condition<?> conditionAnd, Condition<?> conditionOr) {
		this.conditionAnd = conditionAnd;
		this.conditionOr = conditionOr;
		this.firstValue = value;
		this.firstInvocation = invocation;
	}

	protected T getValue(MatcherImpl<T> matcher) {
		return matcher.getValue();
	}

	public Condition<?> is(final Matcher<T> matcher) {
		
		final T secondValue = getValue(matcher);
		final Invocation secondInvocation = PipeImpl.takeLastInvocation();

		return new Condition() {

			@Override
			public boolean satisfies(final Object element) {
				
				if (conditionAnd != null && !conditionAnd.satisfies(element)) return false;
				if (conditionOr != null && conditionOr.satisfies(element)) return true;
				
				T value = firstInvocation == null ? firstValue : (T)firstInvocation.invoke(element);
				T in = secondInvocation == null ? secondValue : (T)secondInvocation.invoke(element);
				
				return matcher.matches(in, value);
			}
		};
	}

}
