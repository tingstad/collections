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
import com.rictin.util.internal.pipe.NewConditionNumberImpl;
import com.rictin.util.internal.pipe.NewConditionStringImpl;


public abstract class Condition<T> extends ConditionImpl {

	public abstract boolean satisfies(T element);

	public static <U> Condition<U> or(final Condition... conditions) {
		return new Condition<U>() {

			public boolean satisfies(U element) {
				for (Condition<U> condition : conditions)
					if (condition.satisfies(element))
						return true;
				return false;
			}
		};
	}

	public static <U> NewCondition<U> where(U itemValue) {
		return new NewConditionImpl<U>(itemValue, fetchInvocation(), null, null);
	}
	public <U> NewCondition<U> and(U itemValue) {
		return new NewConditionImpl<U>(itemValue, fetchInvocation(), this, null);
	}
	public <U> NewCondition<U> or(U itemValue) {
		return new NewConditionImpl<U>(itemValue, fetchInvocation(), null, this);
	}

	public static NewConditionString where(String itemValue) {
		return new NewConditionStringImpl(itemValue, fetchInvocation(), null, null);
	}
	public NewConditionString and(String itemValue) {
		return new NewConditionStringImpl(itemValue, fetchInvocation(), this, null);
	}
	public NewConditionString or(String itemValue) {
		return new NewConditionStringImpl(itemValue, fetchInvocation(), null, this);
	}

	public static <T extends Number> NewConditionNumber<T> where(T itemValue) {
		return new NewConditionNumberImpl<T>(itemValue, fetchInvocation(), null, null);
	}
	public <N extends Number> NewConditionNumber<N> and(N itemValue) {
		return new NewConditionNumberImpl<N>(itemValue, fetchInvocation(), this, null);
	}
	public <N extends Number> NewConditionNumber<N> or(N itemValue) {
		return new NewConditionNumberImpl<N>(itemValue, fetchInvocation(), null, this);
	}

	public static NewCondition<Collection<?>> where(Collection<?> itemValue) {
		throw new RuntimeException("Not yes implemented"); //TODO: where(collection)
	}


}
