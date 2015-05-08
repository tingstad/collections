/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe;

import java.util.Iterator;

import com.rictin.util.pipe.operation.AndOr;
import com.rictin.util.pipe.operation.MapTo;
import com.rictin.util.pipe.operation.ToList;

public interface PipeAfterWhere<T> extends Iterator<T>, Iterable<T>,
		AndOr<T>, MapTo<T>, /*SortBy<T>, GroupBy<T>,*/ ToList<T> {

}
