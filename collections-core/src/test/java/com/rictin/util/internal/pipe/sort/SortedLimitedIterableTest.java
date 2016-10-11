/* Copyright 2016 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe.sort;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SortedLimitedIterableTest {

	@Test
	public void test() {
		SortedLimitedIterable<String> sortedIterable = new SortedLimitedIterable<String>(
				asList("a1", "d3", "b7", "d2", "c4", "b2"), 
				new Comparator<String>() {
					public int compare(String a, String b) {
						return a.substring(1).compareTo(b.substring(1));
					}
				},
				3);

		List<String> list = new ArrayList<String>();
		for (String s : sortedIterable) list.add(s);

		assertArrayEquals(new String[]{"a1", "d2", "b2"}, list.toArray());
	}

	@Test
	public void testLimit0() {
		SortedLimitedIterable<String> sortedIterable = new SortedLimitedIterable<String>(
				asList("a1", "d3", "b7", "d2", "c4", "b2"), 
				new Comparator<String>() {
					public int compare(String a, String b) {
						return a.substring(1).compareTo(b.substring(1));
					}
				},
				0);

		assertFalse(sortedIterable.hasNext());
	}

	@Test
	public void testLimit1() {
		final int[] comparisons = new int[1];
		SortedLimitedIterable<String> sortedIterable = new SortedLimitedIterable<String>(
				asList("a1", "d3", "b7", "d2", "c4", "b2"), 
				new Comparator<String>() {
					public int compare(String a, String b) {
						comparisons[0]++;
						return a.substring(1).compareTo(b.substring(1));
					}
				},
				1);

		List<String> list = new ArrayList<String>();
		for (String s : sortedIterable) list.add(s);

		assertArrayEquals(new String[]{ "a1" }, list.toArray());

		//TODO check Assert.assertEquals("put a1 + 5 compare + get a1 = 7", 7, comparisons[0]);
		Assert.assertEquals(6, comparisons[0]);
	}

}
