/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.NewConditionString;
import com.rictin.util.pipe.matcher.Str;

public class NewConditionStringImpl extends NewConditionImpl<String> implements NewConditionString {

	public NewConditionStringImpl(String value, Invocation<?> invocation, Condition<?> conditionAnd, Condition<?> conditionOr) {
		super(value, invocation, conditionAnd, conditionOr);
	}
	
	public Condition<?> startsWith(String prefix) {
		return is(Str.startsWith(prefix));
	}

}
