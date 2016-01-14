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
import com.rictin.util.internal.pipe.PipeImpl;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.Order;

public abstract class Pipe<T> implements Iterable<T> {

	protected static final Map<String, Pipe> pipes = new ConcurrentHashMap<String, Pipe>();

	public static <T> Pipe<T> from(final Iterable<T> input) {
		return new PipeImpl<T>(input);
	}

	public abstract T item();

	public static <T> T item(Iterable<T> source) {
		String key = getPipeId(source);
		if (!pipes.containsKey(key)) {
			throw new IllegalArgumentException("No pipe exists for source " + source);
		}
		Pipe<T> pipe = pipes.get(key);
		return pipe.item();
	}

	public abstract Pipe<T> select(final Condition condition);

	public abstract Pipe<T> sort(Order order);

	public Pipe<T> sortBy(Object... item) {
		List<Comparator<T>> comparators = new ArrayList<Comparator<T>>(item.length);
		for (int i = 0; i < item.length; i++) {
//			comparators.add(ComparatorUtil.createComparator(false, false, invocations.remove(0)));
		}
		Comparator<T> comparator = ComparatorUtil.join(comparators);
		
		return this;
	}

	public abstract Pipe<T> first(int limit);

	public <U> Map<U, List<T>> groupBy(U item) {
		Map<U, List<T>> map = new HashMap<U, List<T>>();
/*		Invocation<T> invocation = invocations.remove(0);
		for (T element : stream) {
			@SuppressWarnings("unchecked")
			U key = (U) invocation.invoke(element);
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<T>());
			}
			map.get(key).add(element);
		}*/
		return map;
	}

	public abstract <U> Pipe<U> mapTo(U item);

	public List<T> toList() {
		List<T> list = new ArrayList<T>();
		for (T element : this)
			list.add(element);
		cleanUp();
		return list;
	}

	protected abstract void cleanUp();

	protected static <T> String getPipeId(Iterable<T> source) {
		return System.identityHashCode(source) + "-" + Thread.currentThread().getId();
	}

}
