/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.pipe.matcher;


public class Num extends Obj {

	public static <T extends Number> Matcher<T> greaterThan(T value) {
		return new Matcher<T>(value) {
			
			public boolean matches(T value, T in) {
				return compare(in, value) > 0;
			}
		};
	}

	public static <T extends Number> Matcher<T> lessThan(T value) {
		return new Matcher<T>(value) {
			
			public boolean matches(T value, T in) {
				return compare(in, value) < 0;
			}
		};
	}

	private static int compare(Number a, Number b) {
		if (a == null && b == null)
			return 0;
		if (a == null)
			return -1;
		if (b == null)
			return 1;
		return ((Comparable) a).compareTo(b);
	}

}
