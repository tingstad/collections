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
	protected Object[] args;
	protected Method method;
	protected Class<?> returnType;

	protected ProxyFactory<T> proxyFactory;

	public ListHandler(Collection<T> list) {
		this.list = list;
		proxyFactory = new ProxyFactory<T>(list);
	}

	protected abstract Object handleList();

	protected Object intercept() {
		Object returnValue = this.handleList();
		if (!returnType.isPrimitive() || returnValue != null) {
			return returnValue;
		}
		return newInstance(returnType);
	}

	public T createProxy() {
		Callback<T> callback = new Callback<T>() {

			public Object intercept(Invocation<T> invocation) {
				return handleList();
			}

		};
		return preserveReturnedNull() 
				? proxyFactory.getProxyWithNulls(callback) 
				: proxyFactory.getProxy(callback);
	}

	protected Object getValueOfElement(T element) {
		return proxyFactory.invoke(element);
	}

	protected Object getValueOfElement2(T element) {
		if (method == null) {
			return element; // identity function
		}
		try {
			return element == null ? null : method.invoke(element);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Object newInstance(Class<?> klass) {
		if (klass.isPrimitive()) {
			if (Boolean.TYPE.equals(klass)) {
				return false;
			} else if (Float.TYPE.equals(klass)) {
				return 0f;
			} else if (Double.TYPE.equals(klass)) {
				return 0d;
			} else if (Character.TYPE.equals(klass)) {
				return (char) 0;
			} else if (Byte.TYPE.equals(klass)) {
				return (byte) 0;
			} else if (Long.TYPE.equals(klass)) {
				return 0l;
			} else if (Short.TYPE.equals(klass)) {
				return (short) 0;
			} else {
				return 0;
			}
		}
		return null;
	}

	protected boolean preserveReturnedNull() {
		return false;
	}

}
