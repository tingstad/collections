/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.chain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.rictin.util.ProxyFactory;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public abstract class Chained<T> {

	protected ProxyFactory<T> proxyFactory;
	protected T proxy;
	protected Method method;
	protected Object[] args;

	public Chained() {
	}

	public Chained(ProxyFactory<T> proxyFactory) {
		this.proxyFactory = proxyFactory;
		this.proxy = proxyFactory.newProxy(new MethodInterceptor() {
			
			public Object intercept(Object arg0, Method arg1, Object[] arg2,
					MethodProxy arg3) throws Throwable {
				method = arg1;
				args = arg2;
				return null;
			}
		});
	}

	protected Object getValueOfElement(T element) {
		if (element == null) {
			return null;
		}
		try {
			return method.invoke(element, args);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public abstract boolean hasNext();

	public abstract T next();

	protected void validateHasNext() {
		if (!hasNext()) {
			throw new IllegalStateException("No more elements.");
		}
	}

}
