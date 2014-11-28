/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rictin.util.internal.ComparatorUtil;
import com.rictin.util.internal.proxy.Callback;
import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.internal.proxy.ProxyFactory;

public class Pipe<T> {

	private T proxy;
	private Collection<T> input;
	private List<Invocation<T>> invocations;

	private Pipe(Collection<T> input) {
		invocations = new ArrayList<Invocation<T>>();
		proxy = (T) new ProxyFactory(input).getProxy(new Callback() {

			public Object intercept(Invocation invocation) {
				invocations.add(invocation);
				return null;
			}
		});
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
		Invocation<T> invocation = invocations.remove(0);
		for (T element : input) {
			Object v = invocation.invoke(element);
			if (v != null && ((Number)v).doubleValue() < value.doubleValue()) {
				list.add(element);
			}
		}
		input.clear();
		input.addAll(list);
		return this;
	}

	public Pipe<T> sortBy(Object... item) {
		List<Comparator<T>> comparators = new ArrayList<Comparator<T>>(item.length);
		for (int i = 0; i < item.length; i++) {
			comparators.add(ComparatorUtil.createComparator(false, false, invocations.remove(0)));
		}
		Comparator<T> comparator = ComparatorUtil.join(comparators);
		
		return this;
	}

	@SuppressWarnings("unchecked")
	public <U> Pipe<U> mapTo(U item) {
		List<U> list = new ArrayList<U>();
		Invocation<T> invocation = invocations.remove(0);
		for (T element : input) {
			list.add((U) invocation.invoke(element));
		}
		Pipe<U> pipe = new Pipe<U>(list);
		return pipe;
	}

	public <U> Map<U, List<T>> groupBy(U item) {
		Map<U, List<T>> map = new HashMap<U, List<T>>();
		Invocation<T> invocation = invocations.remove(0);
		for (T element : input) {
			@SuppressWarnings("unchecked")
			U key = (U) invocation.invoke(element);
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<T>());
			}
			map.get(key).add(element);
		}
		return map;
	}

	public List<T> toList() {
		return new ArrayList<T>(this.input);
	}

}
