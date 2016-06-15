/* Copyright 2016 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.Iterator;

public class PipeLimit<T> extends PipeImpl<T> implements Iterator<T> {

	private final Iterator<T> input;
	private final int limit;
	private int count;

	public PipeLimit(final PipeImpl<T> source, final int limit) {
		super.init(source);
		this.input = source.iterator();
		this.limit = limit;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	public boolean hasNext() {
		return count < limit && input.hasNext();
	}

	public T next() {
		count++;
		return input.next();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
