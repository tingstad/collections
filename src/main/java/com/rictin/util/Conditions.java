/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import com.rictin.util.condition.NumberCondition;
import com.rictin.util.internal.condition.NumberConditionImpl;
import com.rictin.util.internal.condition.OrCondition;


public class Conditions {

	private Conditions() {}
	
	public static NumberCondition where(Number number) {
		return new NumberConditionImpl(number);
	}

	public static Condition or(final Condition<?>... conditions) {
		return new OrCondition(conditions);
	}

	public static Condition and(final Condition<?>... conditions) {
		return new Condition<Object>() {
			public boolean where(Object value) {
				for (Condition condition : conditions)
					if (!condition.where(value))
						return false;
				return true;
			}
		};
	}

}
