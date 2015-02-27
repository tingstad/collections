/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.rictin.util.internal.proxy.ProxyFactory;

public class PipeFromIterable<T> implements Iterator<T>, Iterable<T> {

	private Iterator<T> input;
	private T next;
	private Boolean hasNext;
	private ProxyFactory<T> proxyFactory;

	public PipeFromIterable(Iterable<T> input) {
		this.input = input.iterator();
		if (!this.input.hasNext()) {
			throw new RuntimeException("Input cannot be empty.");
		}
		hasNext = true;
		next = this.input.next();
		proxyFactory = new ProxyFactory<T>(next);
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
		return next;
	}

	private void prepareNext() {
		if (input.hasNext()) {
			next = input.next();
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
