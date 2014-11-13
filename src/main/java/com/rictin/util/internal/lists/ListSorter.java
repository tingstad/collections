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

import com.rictin.util.internal.ComparatorUtil;


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
		return ComparatorUtil.createComparator(decending, nullFirst, invocation);
	}

	@Override
	protected Object handleList() {
		Collections.sort(list, createComparator());
		return null;
	}

}
