/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.Comparator;
import java.util.Iterator;

import com.rictin.util.Pipe;
import com.rictin.util.internal.pipe.sort.SortedIterable;
import com.rictin.util.internal.pipe.sort.SortedLimitedIterable;
import com.rictin.util.pipe.Order;

public class PipeSort<T> extends PipeImpl<T> {

	private final Order order;
	private final Iterable<T> input;
	private Integer limit;

	public PipeSort(final PipeImpl<T> source, final Order order) {
		super.init(source);
		this.input = source;
		this.order = order;
	}

	@Override
	public Iterator<T> iterator() {
		final OrderParent order = this.order;
		final Comparator<T> comparator = order.getComparator();
		final SortedIterable<T> sortedIterable = 
				limit == null 
						? new SortedIterable<T>(this.input, comparator) 
						: new SortedLimitedIterable<T>(input, comparator, limit);
		return sortedIterable.iterator();
	}

	@Override
	public Pipe<T> first(final int limit) {
		this.limit = limit;
		return super.first(limit);
	}

}
