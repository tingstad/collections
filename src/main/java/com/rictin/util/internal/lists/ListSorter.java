/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ListSorter<T> extends ListHandler<T> {

	private boolean decending;
	private boolean nullFirst = false;
	protected List<T> list;

	public ListSorter(List<T> list) {
		super(list);
		this.list = list;
	}

	/**
	 * 1, 2, 3, ... Nulls last. Sort-by values must be Comparable.
	 */
	public T ascendingBy() {
		decending = false;
		return createProxy();
	}

	/**
	 * 3, 2, 1, ... Nulls last. Sort-by values must be Comparable.
	 */
	public T descendingBy() {
		decending = true;
		return createProxy();
	}

	/**
	 * (Nulls,) 1, 2, 3, ... Sort-by values must be Comparable.
	 */
	public T nullsFirstAscendingBy() {
		decending = false;
		nullFirst = true;
		return createProxy();
	}

	/**
	 * (Nulls,) 3, 2, 1, ... Sort-by values must be Comparable.
	 */
	public T nullsFirstDescendingBy() {
		decending = true;
		nullFirst = true;
		return createProxy();
	}

	protected Comparator<T> createComparator() {
		if (Void.TYPE.equals(invocation.getReturnType())) {
			throw new RuntimeException("Method must return a value!");
		}
/*		if (!returnType.isPrimitive()
				&& !Arrays.asList(returnType.getInterfaces()).contains(
						Comparable.class)) {
			throw new RuntimeException("Must be comparable!");
		}*/
		final boolean descending = this.decending;
		return new Comparator<T>() {
			public int compare(T o1, T o2) {
				Object v1 = null, v2 = null;
				v1 = invocation.invoke(o1);
				v2 = invocation.invoke(o2);
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
	protected Object handleList() {
		Collections.sort(list, createComparator());
		return null;
	}

}
