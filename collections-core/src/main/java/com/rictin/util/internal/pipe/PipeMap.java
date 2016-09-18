/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.rictin.util.internal.proxy.Callback;
import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.internal.proxy.ProxyFactory;

public class PipeMap<T, U> extends PipeImpl<U> implements Iterator<U>, Iterable<U> {

	private Iterator<T> input;
	private Invocation<T> invocation;
	private Boolean hasNext;
	private U element;
	private Class<U> itemClass;

	private U proxy;
	
	public PipeMap(PipeImpl<T> input, U item) {
		this.input = input.iterator();
		itemClass = (Class<U>) item.getClass();
		invocation = PipeImpl.takeLastInvocation();
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
		while (input.hasNext()) {
			T in = input.next();
			U out = (U) invocation.invoke(in);
			element = out;
			hasNext = true;
			return;
		}
		hasNext = false;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public U item() {
		last = new PipeInvocation(this);
		invocations.get().add(last);
		if (proxy == null) {
			ProxyFactory<U> proxyFactory = new ProxyFactory<U>(itemClass);
			proxy = proxyFactory.getProxy(new Callback<U>() {

				public Object intercept(Invocation<U> invocation) {
					addInvocation(invocation);
					return null;
				}
			});
		}
		return proxy;
	}

}
