/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Sorted Iterable using a Comparator.
 * Implemented using a TreeMap<T,List<T>> to guarantee n*log(n) time cost and stable sort.
 * The whole input is sorted on the first call to hasNext().
 */
public class SortedIterable<T> implements Iterator<T>, Iterable<T> {

	private final Iterable<T> input;
	private final TreeMap<T, List<T>> sorted;
	private Iterator<T> keyIterator;
	private Iterator<T> valueIterator;

	public SortedIterable(final Iterable<T> input, final Comparator<T> comparator) {
		this.input = input;
		this.sorted = new TreeMap<T, List<T>>(comparator);
	}

	public boolean hasNext() {
		if (sorted.isEmpty()) {
			for (final T element : input)
				addToCollection(element);
			keyIterator = sorted.keySet().iterator();
		}
		return keyIterator.hasNext() 
				|| valueIterator != null
				&& valueIterator.hasNext();
	}

	public T next() {
		if (valueIterator == null || !valueIterator.hasNext()) {
			final T key = keyIterator.next();
			valueIterator = sorted.get(key).iterator();
		}
		return valueIterator.next();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator<T> iterator() {
		return this;
	}

	protected void addToCollection(final T element) {
		get(element).add(element);
	}

	/**
	 * Equivalent with:
	 * <pre>
	 * if (!sorted.containsKey(key)) {
	 *     sorted.put(key, new ArrayList<T>(1));
	 * }
	 * return sorted.get(key);
	 * </pre>
	 * but with one less log(n) operation (containsKey).
	 */
	private List<T> get(T key) {
		List<T> list = sorted.get(key);
		if (list != null)
			return list;
		List<T> newList = new ArrayList<T>(1);
		sorted.put(key, newList);
		return newList;
	}

	/**
	 * @see SortedLimitedIterable
	 */
	protected void removeLargest() {
		Entry<T, List<T>> entry = sorted.lastEntry();
		List<T> list = entry.getValue();
		list.remove(list.size() - 1);
		if (list.isEmpty()) sorted.remove(entry.getKey());
	}

}