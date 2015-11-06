/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe;

import com.rictin.util.internal.proxy.Invocation;

public class NewConditionString extends NewCondition<String> {

	NewConditionString(String value, Invocation invocation) {
		super(value, invocation);
	}

	public Condition startsWith(final String prefix) {
		
		final String firstValue = this.firstValue;
		final Invocation firstInvocation = this.firstInvocation;
		final String secondValue = prefix;
		final Invocation secondInvocation = fetchInvocation();

		return new Condition() {

			@Override
			public boolean satisfies(final Object element) {
				
				if (condition != null && !condition.satisfies(element)) return false;
				
				String value = firstInvocation == null ? firstValue : (String)firstInvocation.invoke(element);
				String in = secondInvocation == null ? secondValue : (String)secondInvocation.invoke(element);
				
				return Str.startsWith(prefix).matches(in, value);
			}
		};
	}
	
	
}
