/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.Iterator;

import com.rictin.util.Pipe;

public class PipeImpl<T> extends Pipe<T> {

	private final Iterable<T> input;
	
	public PipeImpl(final Iterable<T> input) {
		super.init(input);
		this.input = input;
	}

	public Iterator<T> iterator() {
		return input.iterator();
	}

}
