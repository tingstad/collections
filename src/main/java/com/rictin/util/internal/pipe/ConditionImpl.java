/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import com.rictin.util.internal.proxy.Invocation;


public class ConditionImpl {

	private final Invocation invocation;
	
	protected ConditionImpl(final Invocation invocation) {
		this.invocation = invocation;
	}

	protected Invocation getInvocation() {
		return invocation;
	}

	protected static Invocation fetchInvocation() {
		return PipeParent.takeLastInvocation();
	}

}
