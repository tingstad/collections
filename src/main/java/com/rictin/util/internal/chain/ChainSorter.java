/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.chain;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.rictin.util.internal.proxy.ProxyFactory;

public class ChainSorter<T> extends Chained<T> {//Iterator<T> {

	private boolean decending;
	private boolean nullFirst = false;

	private Chained<T> input;
	private List<T> list;
	private int index = 0;

	public ChainSorter(Chained<T> input, ProxyFactory<T> proxyFactory) {
		super(proxyFactory);
		this.input = input;
	}

	/**
	 * 1, 2, 3, ... Nulls last. Sort-by values must be Comparable.
	 */
	public T ascendingBy() {
		decending = false;
		return proxy;
	}

	/**
	 * 3, 2, 1, ... Nulls last. Sort-by values must be Comparable.
	 */
	public T descendingBy() {
		decending = true;
		return proxy;
	}

	/**
	 * (Nulls,) 1, 2, 3, ... Sort-by values must be Comparable.
	 */
	public T nullsFirstAscendingBy() {
		decending = false;
		nullFirst = true;
		return proxy;
	}

	/**
	 * (Nulls,) 3, 2, 1, ... Sort-by values must be Comparable.
	 */
	public T nullsFirstDescendingBy() {
		decending = true;
		nullFirst = true;
		return proxy;
	}

	private Comparator<T> createComparator() {
		Method method = getInvocation().getMethod();
		if (Void.TYPE.equals(method.getReturnType())) {
			throw new RuntimeException("Method must return a value!");
		}
		if (!method.getReturnType().isPrimitive()
				&& !Arrays.asList(method.getReturnType().getInterfaces()).contains(
						Comparable.class)) {
			throw new RuntimeException("Must be comparable!");
		}
		final boolean descending = this.decending;
		return new Comparator<T>() {
			public int compare(T o1, T o2) {
				Object v1 = null, v2 = null;
				try {
					v1 = (o1 == null ? null : getValueOfElement(o1));
					v2 = (o2 == null ? null : getValueOfElement(o2));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if (o1 == null && o2 == null) {
					return 0;
				} else if (o1 == null) {
					return nullFirst ? -1 : 1;
				} else if (o2 == null) {
					return nullFirst ? 1 : -1;
				} else if (v1 == null && v1 == v2) {
					return 0;
				} else if (v1 == null) {
					return nullFirst ? -1 : 1;
				} else if (v2 == null) {
					return nullFirst ? 1 : -1;
				}
				if (descending) {
					Object t = v1;
					v1 = v2;
					v2 = t;
				}
				return ((Comparable) v1).compareTo(v2);
			}
		};
	}

	@Override
	public boolean hasNext() {
		return list == null && input.hasNext() || index < list.size();
	}

	@Override
	protected T getNext() {
		if (list == null) {
			list = new ArrayList<T>();
			while (input.hasNext()) {
				list.add(input.getNext());
			}
			Collections.sort(list, createComparator());
		}
		return list.get(index++);
	}

}