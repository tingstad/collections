/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.rictin.util.Pipe;
import com.rictin.util.pipe.Condition;

public class PipeSelect<T> extends Pipe<T> implements Iterator<T> {

	private Iterator<T> input;
	private Boolean hasNext;
	private T element;
	private Condition<T> condition;

	public PipeSelect(PipeParent<T> source, Condition<T> condition) {
		super.init(source);
		this.input = source.iterator();
		this.condition = condition;
	}

	private boolean accept(final T element) {
		return condition.satisfies(element);
	}
	
	private void prepareNext() {
		while (input.hasNext()) {
			T in = input.next();
			if (accept(in)) {
				element = in;
				hasNext = true;
				return;
			}
		}
		hasNext = false;
	}

	public boolean hasNext() {
		if (hasNext == null) {
			prepareNext();
		}
		return hasNext;
	}

	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		hasNext = null;
		return element;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator<T> iterator() {
		return this;
	}

}