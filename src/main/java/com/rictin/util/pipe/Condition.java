/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe;

import java.util.Collection;

import com.rictin.util.internal.pipe.ConditionImpl;
import com.rictin.util.internal.pipe.NewConditionImpl;
import com.rictin.util.internal.pipe.NewConditionStringImpl;


public abstract class Condition<T> extends ConditionImpl {

	public abstract boolean satisfies(T element);
	
	public static <V> NewCondition<V> where(V itemValue) {
		return new NewConditionImpl<V>(itemValue, fetchInvocation());
	}
	public <U> NewCondition<U> and(U itemValue) {
		return new NewConditionImpl<U>(itemValue, fetchInvocation(), this);
	}

	public static NewConditionString where(String itemValue) {
		return new NewConditionStringImpl(itemValue, fetchInvocation());
	}
	public NewConditionString and(String itemValue) {
		return new NewConditionStringImpl(itemValue, fetchInvocation(), this);
	}

	public static NewCondition<Collection<?>> where(Collection<?> itemValue) {
		throw new RuntimeException("Not yes implemented"); //TODO: where(collection)
	}


}
