/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rictin.util.internal.ComparatorUtil;
import com.rictin.util.internal.pipe.PipeFilter;
import com.rictin.util.internal.pipe.PipeFromIterable;
import com.rictin.util.internal.pipe.PipeMap;
import com.rictin.util.internal.proxy.Callback;
import com.rictin.util.internal.proxy.Invocation;

public class Pipe<T> {

	private static Map<String, Pipe> pipes = new ConcurrentHashMap<String, Pipe>();
	private T proxy;
	private Iterable<T> stream;
	private List<Invocation<T>> invocations;

	protected Pipe(Iterable<T> input) {
		addPipe(input);
		invocations = new ArrayList<Invocation<T>>();
		PipeFromIterable<T> adapter = new PipeFromIterable<T>(input);
		proxy = adapter.getProxyFactory().getProxy(new Callback() {

			public Object intercept(Invocation invocation) {
				invocations.add(invocation);
				
				return null;
			}
		});
		stream = adapter;
	}

	public static <T> Pipe<T> from(Iterable<T> input) {
		return new Pipe<T>(input);
	}

	public T item() {
		return proxy;
	}

	public static <T> T item(Iterable<T> source) {
		String key = getPipeId(source);
		if (!pipes.containsKey(key)) {
			throw new IllegalStateException("No pipe exists for source " + source);
		}
		Pipe<T> pipe = pipes.get(key);
		return pipe.item();
	}

	public Pipe<T> filterKeepLessThan(Number value, Object item) {
		stream = new PipeFilter<T>(stream, invocations, value);
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

	public <U> Pipe<U> mapTo(U item) {
		return Pipe.from(new PipeMap<T, U>(stream, invocations, item));
	}

	public <U> Map<U, List<T>> groupBy(U item) {
		Map<U, List<T>> map = new HashMap<U, List<T>>();
		Invocation<T> invocation = invocations.remove(0);
		for (T element : stream) {
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
		List<T> list = new ArrayList<T>();
		for (T element : stream)
			list.add(element);
		return list;
	}

	private void addPipe(Iterable<T> source) {
		pipes.put(getPipeId(source), this);
	}

	private static <T> String getPipeId(Iterable<T> source) {
		return System.identityHashCode(source) + "-" + Thread.currentThread().getId();
	}

}
