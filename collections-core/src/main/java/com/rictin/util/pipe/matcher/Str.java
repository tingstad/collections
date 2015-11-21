/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe.matcher;


public class Str extends Obj {

	public static Matcher<String> startsWith(final String prefix) {
		return new Matcher<String>(prefix) {
			
			public boolean matches(String value, String in) {
				return in.startsWith(value);
			}
		};
	}
	
}
