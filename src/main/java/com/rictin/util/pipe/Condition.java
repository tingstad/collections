/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe;

import java.util.Collection;

import com.rictin.util.internal.pipe.ConditionImpl;


public abstract class Condition extends ConditionImpl {

	Condition() {
	}

	public abstract boolean satisfies(Object element);
	
	public static <T> NewCondition<T> where(T itemValue) {
		return new NewCondition<T>(itemValue, fetchInvocation());
	}

	public static NewConditionString where(String itemValue) {
		return new NewConditionString(itemValue, fetchInvocation());
	}

	public static NewCondition<Collection> where(Collection itemValue) {
		return null;
	}

	public <U> NewCondition<U> and(U itemValue) {
		return new NewCondition<U>(itemValue, fetchInvocation(), this);
	}

}
