/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe.matcher;

public class Obj {

	public static Matcher isNull() {
		return new Matcher<Object>(null) {
			
			public boolean matches(Object value, Object in) {
				return in == null;
			}
		};
	}

	public static Matcher equalTo(Object value) {
		return new Matcher<Object>(value) {
			
			public boolean matches(Object value, Object in) {
				return value == null ? in == null : value.equals(in);
			}
		};
	}

}
