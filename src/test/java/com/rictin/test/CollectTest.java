/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test;

import static com.rictin.util.Lists.collect;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class CollectTest {

	@Test
	public void test() {
		List<Person> persons = Arrays.asList(
				new Person("A", 20),
				new Person("B", 20),
				new Person("C", 20));

		List<String> names = new ArrayList<String>();
		collect(persons, names).getName();

		assertArrayEquals(new Object[]{ "A", "B", "C" }, names.toArray());
		assertEquals("A", names.get(0));
		assertTrue(names.get(0).equalsIgnoreCase("a"));
	}

	@Test
	public void testSetResult() {
		List<Person> persons = Arrays.asList(
				new Person("A", 20),
				new Person("B", 20),
				new Person("A", 20));

		Set<String> names = new HashSet<String>();
		collect(persons, names).getName();

		assertEquals(2, names.size());
		assertArrayEquals(new Object[]{ "A", "B" }, names.toArray());
	}

	@Test
	public void testSetInput() {
		Set<Person> persons = new HashSet<Person>(asList(
				new Person("A", 20),
				new Person("B", 20),
				new Person("A", 20)));

		Set<String> names = new HashSet<String>();
		collect(persons, names).getName();

		assertEquals(2, names.size());
		assertArrayEquals(new Object[]{ "A", "B" }, names.toArray());
	}

}
