/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.rictin.util.Condition;
import com.rictin.util.internal.proxy.Invocation;

public class PipeFilter<T> implements Iterator<T>, Iterable<T> {

	private Iterator<T> input;
	private List<Invocation<T>> invocationList;
	private Invocation<T> invocation;
	private Boolean hasNext;
	private T element;
	private Number value;
	private Condition<T> condition;

	public PipeFilter(Iterable<T> input, List<Invocation<T>> invocationList, Condition<T> condition) {
		this.input = input.iterator();
		this.invocationList = invocationList;
		this.condition = condition;
	}

	public PipeFilter(Iterable<T> input, List<Invocation<T>> invocationList, Number value) {
		this.input = input.iterator();
		this.invocationList = invocationList;
		this.value = value;
	}

	public Iterator<T> iterator() {
		return this;
	}

	public boolean hasNext() {
		if (hasNext == null) {
			prepareNext();
		}
		return hasNext;
	}

	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		hasNext = null;
		return element;
	}

	private boolean isAccepted(T element, Invocation<T> invocation) {
		if (condition != null) {
			return condition.where(element);
		}
		Object v = invocation.invoke(element);
		if (v != null && ((Number)v).doubleValue() < value.doubleValue()) {
			return true;
		}
		return false;
	}
	
	private void prepareNext() {
		Invocation<T> invocation = getInvocation();
		while (input.hasNext()) {
			T in = input.next();
			if (isAccepted(in, invocation)) {
				element = in;
				hasNext = true;
				return;
			}
		}
		hasNext = false;
	}

	private Invocation<T> getInvocation() {
		if (invocation == null && condition == null) {
			invocation = invocationList.remove(invocationList.size() - 1);
		}
		return invocation;
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
}
