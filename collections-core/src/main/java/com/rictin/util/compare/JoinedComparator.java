/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.compare;

import java.util.Collection;
import java.util.Comparator;

public class JoinedComparator<T> implements Comparator<T> {

	private final Comparator[] comparators;

	public JoinedComparator(final Collection<Comparator<T>> comparators) {
		this(comparators.toArray(new Comparator[0]));
	}

	public JoinedComparator(final Comparator... comparators) {
		this.comparators = comparators;
	}

	public int compare(T o1, T o2) {
		int i = 0;
		int c = 0;
		while (c == 0 && i < comparators.length)
			c = comparators[i++].compare(o1, o2);
		return c;
	}

}
