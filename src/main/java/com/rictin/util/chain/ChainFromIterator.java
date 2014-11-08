/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.chain;

import java.util.Iterator;

import com.rictin.util.proxy.ProxyFactory;

public class ChainFromIterator<T> extends Chained<T> {

	private Iterator<T> input;
	private T next;
	private T first;
	private Boolean hasNext;
	private ProxyFactory<T> proxyFactory;

	public ChainFromIterator(Iterator<T> input) {
		this.input = input;
		if (!input.hasNext()) {
			throw new RuntimeException("Input cannot be empty.");
		}
		first = input.next();
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

}
