/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe.sort;

import java.util.Comparator;

/**
 * Sorts an Iterable but keeping only the first X elements.
 * This saves memory and comparisons.
 * 
 * @see SortedIterable
 */
public class SortedLimitedIterable<T> extends SortedIterable<T> {

	private final Comparator<T> comparator;
	private final int limit;
	private int size;
	private T max;

	public SortedLimitedIterable(final Iterable<T> input, final Comparator<T> comparator, final int limit) {
		super(input, comparator);
		this.comparator = comparator;
		this.limit = limit;
		this.size = 0;
	}

	@Override
	protected void addToCollection(final T element) {
		boolean small = max != null && comparator.compare(element, max) < 0;
		if (size < limit || small) {
			super.addToCollection(element);
			size++;
			if (!small) max = element;
			if (size > limit) super.removeLargest();
		}
	}

}
