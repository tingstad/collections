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

import com.rictin.util.internal.proxy.Invocation;

public class PipeMap<T, U> implements Iterator<U>, Iterable<U> {

	private Iterator<T> input;
	private List<Invocation<T>> invocationList;
	private Invocation<T> invocation;
	private Boolean hasNext;
	private U element;

	public PipeMap(Iterable<T> input, List<Invocation<T>> invocationList, U item) {
		this.input = input.iterator();
		this.invocationList = invocationList;
	}

	public Iterator<U> iterator() {
		return this;
	}

	public boolean hasNext() {
		if (hasNext == null) {
			prepareNext();
		}
		return hasNext;
	}

	public U next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		hasNext = null;
		return element;
	}

	private void prepareNext() {
		Invocation<T> invocation = getInvocation();
		while (input.hasNext()) {
			T in = input.next();
			U out = (U) invocation.invoke(in);
			element = out;
			hasNext = true;
			return;
		}
		hasNext = false;
	}

	private Invocation<T> getInvocation() {
		if (invocation == null) {
			invocation = invocationList.remove(invocationList.size() - 1);
		}
		return invocation;
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
}
