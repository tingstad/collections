/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.rictin.util.Pipe;
import com.rictin.util.internal.condition.Condition;
import com.rictin.util.pipe.PipeAfterWhere;

public class PipeAfterWhereImpl<T> extends PipeParent<T> implements PipeAfterWhere<T> {

	private Iterator<T> input;
	private Boolean hasNext;
	private T element;
	private Condition<T> condition;

	public PipeAfterWhereImpl(PipeParent<T> source, Condition<T> condition) {
		super(source);
		this.input = source.iterator();
		this.condition = condition;
	}

	private void prepareNext() {
		while (input.hasNext()) {
			T in = input.next();
			if (condition.accept(in)) {
				element = in;
				hasNext = true;
				return;
			}
		}
		hasNext = false;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	public List<T> toList() {
		return doToList();
	}

	public WhereNumberImpl<T> and(Number number) {
		return new WhereNumberImpl<T>(this, number);
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
		// TODO Auto-generated method stub
	}

	public <U> Pipe<U> mapTo(U item) {
		return doMapTo(item);
	}

}