/* Copyright 2016 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe;

import java.util.Collection;


public interface NewConditionCollection<T extends Collection> extends NewCondition<T> {

	Condition<?> hasSize(int size);

	Condition<?> contains(Object element);

}
