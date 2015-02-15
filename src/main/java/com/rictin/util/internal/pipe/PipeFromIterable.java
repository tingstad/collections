/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.Iterator;

import com.rictin.util.internal.proxy.ProxyFactory;

public class PipeFromIterable<T> implements Iterator<T>, Iterable<T> {

	private Iterator<T> input;
	private T next;
	private T first;
	private Boolean hasNext;
	private ProxyFactory<T> proxyFactory;

	public PipeFromIterable(Iterable<T> input) {
		this.input = input.iterator();
		if (!input.iterator().hasNext()) {
			throw new RuntimeException("Input cannot be empty.");
		}
		first = input.iterator().next();
		proxyFactory = new ProxyFactory<T>(first);
	}

	public boolean hasNext() {
		if (hasNext == null) {
			prepareNext();
		}
		return hasNext;
	}

	public T next() {
		if (!hasNext()) {
			throw new IllegalStateException("No more elements.");
		}
		hasNext = null;
		return next;
	}

	private boolean inputHasNext() {
		if (first != null) {
			return true;
		}
		return input.hasNext();
	}

	private T nextInput() {
		if (first != null) {
			T ret = first;
			first = null;
			return ret;
		}
		return input.next();
	}

	private void prepareNext() {
		while (inputHasNext()) {
			T in = nextInput();
			next = in;
			hasNext = true;
			return;
		}
		hasNext = false;
	}

	public ProxyFactory<T> getProxyFactory() {
		return proxyFactory;
	}

	public Iterator<T> iterator() {
		return this;
	}

	public void remove() {
		throw new RuntimeException("Unsuported");
	}

}
