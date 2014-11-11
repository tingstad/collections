/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.chain;

import com.rictin.util.proxy.Callback;
import com.rictin.util.proxy.Invocation;
import com.rictin.util.proxy.ProxyFactory;

public abstract class Chained<T> {

	protected ProxyFactory<T> proxyFactory;
	protected T proxy;
	private Invocation<T> invocation;

	public Chained() {
	}

	public Chained(ProxyFactory<T> proxyFactory) {
		this.proxyFactory = proxyFactory;
		final Chained<T> t = this;
		this.proxy = proxyFactory.getProxy(new Callback<T>() {

			public Object intercept(Invocation<T> invocation) {
				t.invocation = invocation;
				return null;
			}
			
		});
	}

	protected Object getValueOfElement(T element) {
		return invocation.invoke(element);
	}

	public abstract boolean hasNext();

	public abstract T next();

	protected void validateHasNext() {
		if (!hasNext()) {
			throw new IllegalStateException("No more elements.");
		}
	}

}
