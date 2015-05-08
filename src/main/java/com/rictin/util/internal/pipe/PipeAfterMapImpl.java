/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.List;

import com.rictin.util.pipe.PipeAfterMap;
import com.rictin.util.pipe.WhereNumber;

public class PipeAfterMapImpl<T> extends PipeParent<T> implements PipeAfterMap<T> {

	public PipeAfterMapImpl(Iterable<T> source) {
		super(source);
	}

	public WhereNumber<T> where(Number number) {
		return new WhereNumberImpl<T>(this, number);
	}

	public List<T> toList() {
		return doToList();
	}

}
