/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test;

import static com.rictin.util.Lists.search;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rictin.util.Lists;

public class ListsFinderTest {

	private List<Person> list;

	@Before
	public void setUp() {
		list = new ArrayList<Person>();
		list.add(new Person("Erik", 20));
		list.add(new Person("Lars", 27));
	}

	@Test
	public void testFindString() {
		String name = Lists.search(list).forValue("Erik").getName();
		Assert.assertEquals("Erik", name);
	}

	@Test
	public void testFindInt() {
		Integer age = Lists.search(list).forValue(27).getAge();
		Assert.assertEquals(27, age.intValue());
	}

	@Test
	public void testNotFound() {
		String name = Lists.search(list).forValue("Weird").getName();
		Assert.assertNull(name);
	}

	@Test
	public void testSmallest() {
		Integer smallest = Lists.min(list).getAge();
		Assert.assertEquals(20, smallest.intValue());
	}

	@Test
	public void testLargest() {
		Integer largest = Lists.max(list).getAge();
		Assert.assertEquals(27, largest.intValue());
	}

	@Test
	public void testLargestName() {
		String largest = Lists.max(list).getName();
		Assert.assertEquals("Lars", largest); // L > E
	}

	@Test
	public void testStringList() {
		List<String> list = new ArrayList<String>(
				Arrays.asList("a", "b", "c", "b"));

		String val = search(list).forValue("b");

		assertEquals("b", val);
		assertNull(search(list).forValue("x"));
		assertEquals("c", search(list).forLargest());
		assertEquals("C", search(list).forLargest().toUpperCase());
	}

	@Test(expected = NullPointerException.class)
	public void testDangerousUse() {
		List<String> list = new ArrayList<String>(
				Arrays.asList("a", "b", "c"));

		search(list).forValue("A").toUpperCase();
	}

}
