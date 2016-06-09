/* Copyright 2016 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe.sort;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class SortedIterableTest {

	@Test
	public void test() {
		SortedIterable<String> sortedIterable = new SortedIterable<String>(
				asList("a1", "d3", "b7", "d2", "c4", "b2"), 
				new Comparator<String>() {

			public int compare(String a, String b) {
				return a.substring(1).compareTo(b.substring(1));
			}
		});

		List<String> list = new ArrayList<String>();
		for (String s : sortedIterable) list.add(s);

		assertArrayEquals(new String[]{"a1", "d2", "b2", "d3", "c4", "b7"}, list.toArray());
	}

}
