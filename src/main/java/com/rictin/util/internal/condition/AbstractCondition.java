/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.condition;

import com.rictin.util.Condition;

public abstract class AbstractCondition<T> implements Condition<T>{

	protected AbstractCondition() {
		
	}
	
	public boolean where(T element) {
		// TODO Auto-generated method stub
		return false;
	}

}
