/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe;

import com.rictin.util.internal.pipe.NewConditionImpl;
import com.rictin.util.internal.proxy.Invocation;

public class NewCondition<T> extends NewConditionImpl<T> {

	private T firstValue;
	private Invocation firstInvocation;
	private final Condition condition;
	
	NewCondition(final T value, final Invocation invocation, final Condition condition) {
		this.condition = condition;
		this.firstValue = value;
		this.firstInvocation = invocation;
	}

	NewCondition(final T value, final Invocation invocation) {
		this(value, invocation, null);
	}

	public Condition is(final Matcher<T> matcher) {
		
		final T firstValue = this.firstValue;
		final Invocation firstInvocation = this.firstInvocation;
		final T secondValue = getValue(matcher);
		final Invocation secondInvocation = fetchInvocation();

		return new Condition() {

			@Override
			public boolean satisfies(final Object element) {
				
				if (condition != null && !condition.satisfies(element)) return false;
				
				T value = firstInvocation == null ? firstValue : (T)firstInvocation.invoke(element);
				T in = secondInvocation == null ? secondValue : (T)secondInvocation.invoke(element);
				
				return matcher.matches(in, value);
			}
		};
	}

}
