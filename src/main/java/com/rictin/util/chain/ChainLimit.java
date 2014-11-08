/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.chain;


public class ChainLimit<T> extends Chained<T> {

	private Chained<T> input;
	private int limit;
	private int count;

	public ChainLimit(Chained<T> input, int limit) {
		this.input = input;
		this.limit = limit;
	}

	@Override
	public boolean hasNext() {
		return input.hasNext() && count < limit;
	}

	@Override
	public T next() {
		validateHasNext();
		count++;
		return input.next();
	}

}
