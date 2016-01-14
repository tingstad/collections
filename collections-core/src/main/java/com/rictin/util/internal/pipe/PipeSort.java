/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.rictin.util.Pipe;
import com.rictin.util.pipe.Order;

public class PipeSort<T> extends PipeImpl<T> {

	private final Order order;
	private final Iterator<T> input;
	private int limit;

	public PipeSort(PipeImpl<T> source, Order order) {
		super.init(source);
		this.input = source.iterator();
		this.order = order;
	}

	@Override
	public Iterator<T> iterator() {
		List<T> list = new ArrayList<T>();
		while (input.hasNext())
			list.add(input.next());
		OrderParent order = this.order;
		Collections.sort(list, order.getComparator());
		return list.iterator();
	}

	@Override
	public Pipe<T> first(final int limit) {
		this.limit = limit;
		return super.first(limit);
	}

}
