/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe.operation;

import com.rictin.util.Pipe;
import com.rictin.util.internal.pipe.PipeAfterWhereImpl;
import com.rictin.util.pipe.Condition;

public interface Where<T> {

	PipeAfterWhereImpl<T> select(Condition condition);

}
