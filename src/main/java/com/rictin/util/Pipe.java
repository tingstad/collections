/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import com.rictin.util.internal.pipe.PipeImpl;
import com.rictin.util.pipe.SuperPipe;
import com.rictin.util.pipe.operation.GroupBy;
import com.rictin.util.pipe.operation.MapTo;
import com.rictin.util.pipe.operation.SortBy;
import com.rictin.util.pipe.operation.ToList;
import com.rictin.util.pipe.operation.Where;

public abstract class Pipe<T> extends SuperPipe<T> implements Where<T>, MapTo<T>, SortBy<T>, GroupBy<T>, ToList<T> {

	public static <T> Pipe<T> from(Iterable<T> input) {
		return new PipeImpl<T>(input);
	}

}
