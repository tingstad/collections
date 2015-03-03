/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rictin.util.Condition;
import com.rictin.util.internal.proxy.Invocation;

public class OrCondition implements Condition<Object> {

	private Collection<Condition<?>> conditions;
	private List<Invocation<?>> invocations;
	private Map<Condition<?>, Invocation<?>> map;

	public OrCondition(final Condition<?>... conditions) {
		this.map = new HashMap<Condition<?>, Invocation<?>>();
		this.conditions = new ArrayList<Condition<?>>();
		for (Condition<?> condition : conditions)
			this.conditions.add(condition);
	}

	public void setInvocations(List<Invocation<?>> invocations) {
		this.invocations = invocations;
	}

	public boolean where(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean compute(Object element) {
		for (Condition c : conditions) {
			if (!map.containsKey(c))
				map.put(c, invocations.remove(0));
			final Invocation<Object> invocation = (Invocation<Object>) map.get(c);
			Object v = invocation.invoke(element);
			if (c.where(v)) return true;
		}
		return false;
	}

}
