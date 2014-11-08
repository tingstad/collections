/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rictin.util.proxy.ProxyFactory;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class Pipe<T> implements MethodInterceptor {

	private T proxy;
	private Collection<T> input;
	private Object object;
	private Method method;
	private Object[] args;
	private MethodProxy methodProxy;

	private Pipe(Collection<T> input) {
		proxy = ProxyFactory.createProxy(input, this);
		this.input = input;
	}

	public static <T> Pipe<T> from(Collection<T> input) {
		return new Pipe<T>(input);
	}

	public T item() {
		return proxy;
	}

	public Pipe<T> filterKeepLessThan(Number value, Object item) {
		List<T> list = new ArrayList<T>();
		for (T element : input) {
			Object v = invoke(method, element, args);
			if (v != null && ((Number)v).doubleValue() < value.doubleValue()) {
				list.add(element);
			}
		}
		input.clear();
		input.addAll(list);
		return this;
	}

	public Pipe<T> sortBy(Object... item) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public <U> Pipe<U> mapTo(U item) {
		List<U> list = new ArrayList<U>();
		for (T element : input) {
			list.add((U) invoke(method, element, args));
		}
		Pipe<U> pipe = new Pipe<U>(list);
		return pipe;
	}

	public <U> Map<U, List<T>> groupBy(U item) {
		Map<U, List<T>> map = new HashMap<U, List<T>>();
		for (T element : input) {
			@SuppressWarnings("unchecked")
			U key = (U) invoke(method, element, args);
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<T>());
			}
			map.get(key).add(element);
		}
		return map;
	}

	private Object invoke(Method method, Object object, Object[] args) {
		if (Modifier.isFinal(object.getClass().getModifiers())) {
			return object;
		}
		try {
			return method.invoke(object, args);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public List<T> toList() {
		return new ArrayList<T>(this.input);
	}

	public Object intercept(Object object, Method method, Object[] args,
			MethodProxy methodProxy) throws Throwable {
		this.object = object;
		this.method = method;
		this.args = args;
		this.methodProxy = methodProxy;
		return null;
	}

}
