/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe;

import java.util.Comparator;

import com.rictin.util.internal.pipe.OrderImpl;
import com.rictin.util.internal.proxy.Invocation;


public class Order extends OrderImpl {

	private Order(Comparator comparator) {
		this.comparator = comparator;
	}

	public static <X> Order by(X itemValue) {
		final Invocation invocation = fetchInvocation();
		final Comparator comparator = new Comparator() {

			public int compare(Object o1, Object o2) {
				Object v1 = invocation.invoke(o1);
				Object v2 = invocation.invoke(o2);
				if (v1 == null)
					return v2 == null ? 0 : -1;
				if (v2 == null)
					return 1;
				return ((Comparable) v1).compareTo(v2);
			}
		};
		return new Order(comparator);
	}

}
