/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.chain;

import com.rictin.util.internal.proxy.Callback;
import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.internal.proxy.ProxyFactory;

public abstract class Chained<T> {

	protected ProxyFactory<T> proxyFactory;
	protected T proxy;
	private Invocation<T> invocation;

	public Chained() {
	}

	public Chained(ProxyFactory<T> proxyFactory) {
		this.proxyFactory = proxyFactory;
		this.proxy = proxyFactory.getProxy(new Callback<T>() {

			public Object intercept(Invocation<T> i) {
				invocation = i;
				return null;
			}
			
		});
	}
	
	protected Object getValueOfElement(T element) {
		return invocation.invoke(element);
	}

	protected abstract boolean hasNext();

	protected abstract T getNext();

	protected T next() {
		validateHasNext();
		return getNext();
	}
	
	private void validateHasNext() {
		if (!hasNext()) {
			throw new IllegalStateException("No more elements.");
		}
	}

	protected Invocation<T> getInvocation() {
		return invocation;
	}

}
