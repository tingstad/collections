/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.chain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.rictin.util.OptionalArgument;
import com.rictin.util.internal.ComparatorUtil;
import com.rictin.util.internal.proxy.ProxyFactory;

public class ChainSorter<T> extends Chained<T> {

	private boolean decending;
	private boolean nullFirst = false;

	private Chained<T> input;
	private List<T> list;
	private int index = 0;
	private Comparator<T> comparator;
	private Integer limit;
	private OptionalArgument[] options;

	public ChainSorter(Chained<T> input, ProxyFactory<T> proxyFactory, OptionalArgument... options) {
		super(proxyFactory);
		this.input = input;
		this.options = options;
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

	@Override
	protected boolean hasNext() {
		return list == null && input.hasNext() || index < list.size();
	}

	@Override
	protected T getNext() {
		if (list == null) {
			if (limit == null || !optimize()) {
				list = new ArrayList<T>();
				while (input.hasNext()) {
					list.add(input.getNext());
				}
				Collections.sort(list, getComparator());
			} else {
				list = new ArrayList<T>(limit);
				populateSortedListWithLimit();
			}
		}
		return list.get(index++);
	}

	private void populateSortedListWithLimit() {
		while (input.hasNext()) {
			T element = input.getNext();
			if (list.isEmpty()) {
				list.add(element);
				continue;
			}
			int index = Collections.binarySearch(list, element, comparator);
			index = index < 0 ? ~index : index;
			if (index < limit) {
				list.add(index, element);
				if (list.size() > limit) {
					list.remove(limit);
				}
			}
		}
	}

	Comparator<T> getComparator() {
		if (comparator == null) {
			comparator = ComparatorUtil.createComparator(decending, nullFirst, getInvocation());
		}
		return comparator;
	}

	void setComparator(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	void setLimit(Integer limit) {
		this.limit = limit;
	}

	private <T> boolean optimize() {
		for (OptionalArgument option : options)
			if (OptionalArgument.NO_OPTIMIZATION.equals(option))
				return false;
		return true;
	}

}
