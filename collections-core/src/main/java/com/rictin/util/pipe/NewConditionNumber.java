/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe;


public interface NewConditionNumber<T extends Number> extends NewCondition<T> {

	Condition<?> greaterThan(T number);

	Condition<?> lessThan(T number);

	Condition<?> noGreaterThan(T number);

	Condition<?> noLessThan(T number);

}
