/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.rictin.util.compare.JoinedComparator;
import com.rictin.util.internal.ComparatorUtil;
import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.pipe.Order;

public class OrderImpl extends Order {

	private final List<Invocation> invocations;
	private final OrderImpl origin;
	private boolean descending;
	private boolean nullsFirst;
	private Comparator comparator;

	public OrderImpl(final int values) {
		this(values, null);
	}

	private OrderImpl(final int values, final OrderImpl origin) {
		invocations = new ArrayList<Invocation>(values);
		for (final byte nil : new byte[values]) // for (int i = 0; i < values; i++)
			invocations.add(PipeImpl.takeLastInvocation());
		this.origin = origin;
	}

	@Override
	public OrderImpl descending() {
		if (this.comparator != null)
			throw new IllegalStateException();
		this.descending = true;
		return this;
	}

	@Override
	public OrderImpl nullsFirst() {
		if (this.comparator != null)
			throw new IllegalStateException();
		this.nullsFirst = true;
		return this;
	}

	@Override
	public OrderImpl thenBy(Object... itemValue) {
		return new OrderImpl(itemValue.length, this);
	}

	private Comparator buildComparator(final Invocation invocation) {
		return ComparatorUtil.createComparator(descending, nullsFirst, invocation);
	}

	@Override
	Comparator getComparator() {
		if (this.comparator == null) {
			final int n = invocations.size();
			final List<Comparator> comparators = new ArrayList(n);
			for (final byte nil : new byte[n])
				comparators.add(buildComparator(invocations.remove(0)));
			final Comparator comparator = new JoinedComparator(comparators);
			this.comparator = 
					origin == null 
					? comparator 
					: new JoinedComparator(origin.getComparator(), comparator);
		}
		return this.comparator;
	}

}
