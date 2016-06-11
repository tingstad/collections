/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe;

import java.util.Collection;

import com.rictin.util.internal.pipe.ConditionImpl;
import com.rictin.util.internal.pipe.NewConditionCollectionImpl;
import com.rictin.util.internal.pipe.NewConditionImpl;
import com.rictin.util.internal.pipe.NewConditionNumberImpl;
import com.rictin.util.internal.pipe.NewConditionStringImpl;


public abstract class Condition<T> extends ConditionImpl {

	public abstract boolean satisfies(T element);

	public static <U> Condition<U> either(final Condition... conditions) {
		return new Condition<U>() {

			public boolean satisfies(U element) {
				for (Condition<U> condition : conditions)
					if (condition.satisfies(element))
						return true;
				return false;
			}
		};
	}

	public static <U> Condition<U> not(final Condition condition) {
		return new Condition<U>() {

			public boolean satisfies(U element) {
				return !condition.satisfies(element);
			}
		};
	}

	public static <X> NewCondition<X> where(X itemValue) {
		return new NewConditionImpl<X>(itemValue, fetchInvocation(), null, null);
	}
	public <X> NewCondition<X> and(X itemValue) {
		return new NewConditionImpl<X>(itemValue, fetchInvocation(), this, null);
	}
	public <X> NewCondition<X> or(X itemValue) {
		return new NewConditionImpl<X>(itemValue, fetchInvocation(), null, this);
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

	public static <N extends Number> NewConditionNumber<N> where(N itemValueNumber) {
		return new NewConditionNumberImpl<N>(itemValueNumber, fetchInvocation(), null, null);
	}
	public <N extends Number> NewConditionNumber<N> and(N itemValueNumber) {
		return new NewConditionNumberImpl<N>(itemValueNumber, fetchInvocation(), this, null);
	}
	public <N extends Number> NewConditionNumber<N> or(N itemValueNumber) {
		return new NewConditionNumberImpl<N>(itemValueNumber, fetchInvocation(), null, this);
	}

	public static <C extends Collection> NewConditionCollection<C> where(C itemValueCollection) {
		return new NewConditionCollectionImpl<C>(itemValueCollection, fetchInvocation(), null, null);
	}
	public <C extends Collection> NewConditionCollection<C> and(C itemValueCollection) {
		return new NewConditionCollectionImpl<C>(itemValueCollection, fetchInvocation(), this, null);
	}
	public <C extends Collection> NewConditionCollection<C> or(C itemValueCollection) {
		return new NewConditionCollectionImpl<C>(itemValueCollection, fetchInvocation(), null, this);
	}

}
