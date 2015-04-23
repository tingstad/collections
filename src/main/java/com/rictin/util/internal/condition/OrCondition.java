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
	private Map<Condition<?>, Invocation<?>> map;

	public OrCondition(final Condition<?>... conditions) {
		this.map = new HashMap<Condition<?>, Invocation<?>>();
		this.conditions = new ArrayList<Condition<?>>();
		for (Condition<?> condition : conditions)
			this.conditions.add(condition);
	}

	/*
	 * Assumes that every Condition given to or() uses one invocation.
	 */
	public void setInvocations(List<Invocation<?>> invocations) {
		for (Condition<?> c : conditions)
			if (!map.containsKey(c))
				map.put(c, invocations.remove(0));
	}

	public boolean where(Object element) {
		for (Condition c : conditions) {
			Invocation<Object> invocation = (Invocation<Object>) map.get(c);
			Object v = invocation.invoke(element);
			if (c.where(v)) return true;
		}
		return false;
	}

}
