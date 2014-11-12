/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.lists;

import java.lang.reflect.Method;
import java.util.Collection;

import com.rictin.util.proxy.Callback;
import com.rictin.util.proxy.Invocation;
import com.rictin.util.proxy.ProxyFactory;

abstract class ListHandler<T> {

	protected Collection<T> list;
	protected Invocation<T> invocation;

	protected ProxyFactory<T> proxyFactory;

	public ListHandler(Collection<T> list) {
		this.list = list;
		proxyFactory = new ProxyFactory<T>(list);
	}

	protected abstract Object handleList();

	public T createProxy() {
		Callback<T> callback = new Callback<T>() {

			public Object intercept(Invocation<T> i) {
				invocation = i;
				return handleList();
			}

		};
		return preserveReturnedNull() 
				? proxyFactory.getProxyWithNulls(callback) 
				: proxyFactory.getProxy(callback);
	}

	protected Object getValueOfElement(T element) {
		return invocation.invoke(element);
	}

	protected boolean preserveReturnedNull() {
		return false;
	}

}
