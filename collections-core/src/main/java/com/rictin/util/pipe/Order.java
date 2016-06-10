/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe;

import com.rictin.util.internal.pipe.OrderImpl;
import com.rictin.util.internal.pipe.OrderParent;


public abstract class Order extends OrderParent {

	protected Order() {}

	public static Order by(Object... itemValue) {
		return new OrderImpl(itemValue.length);
	}

	public abstract Order thenBy(Object... itemValue);

	public abstract Order descending();

	public abstract Order nullsFirst();
}
