/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe;


public interface NewConditionString extends NewCondition<String> {

	Condition<?> startsWith(String prefix);

	Condition<?> endsWith(String suffix);

	Condition<?> contains(String string);

	Condition<?> matches(String regex);

}
