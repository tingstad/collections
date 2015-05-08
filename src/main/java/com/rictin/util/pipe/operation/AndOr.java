/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe.operation;

import com.rictin.util.internal.pipe.WhereNumberImpl;

/**
 * 
 * @see Where
 */
public interface AndOr<T> {

	WhereNumberImpl<T> and(Number number);
	//TODO: PipeWhereNumber<T> and(String string);

	// PipeWhereNumber<T> or(Number number);
	// PipeWhereNumber<T> or(String string);

}
