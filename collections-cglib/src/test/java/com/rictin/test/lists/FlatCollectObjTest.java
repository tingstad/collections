/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.lists;

import static com.rictin.util.Lists.flatCollect;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.rictin.test.Person;

public class FlatCollectObjTest {

	@Test
	public void test() {
		@SuppressWarnings("unchecked")
		List<List<Person>> persons = asList(null,
				asList(new Person("Eve", 20), null),
				asList(new Person("Bob", 30), new Person("Mallory", 40)));

		List<String> names = new ArrayList<String>();
		flatCollect(persons, names).getName();

		assertArrayEquals(new Object[] { "Eve", "Bob", "Mallory" },
				names.toArray());
	}

	@Test
	public void testSet() {
		@SuppressWarnings("unchecked")
		Set<List<Person>> persons = new HashSet<List<Person>>(asList(
				null,
				asList(new Person("Bob", 30), new Person("Mallory", 40)),
				asList(new Person("Eve", 20), null)));

		List<Integer> ages = new ArrayList<Integer>();
		flatCollect(persons, ages).getAge();

		Integer[] ints = ages.toArray(new Integer[0]);
		Arrays.sort(ints);
		assertArrayEquals(new Object[] { 20, 30, 40 }, ints);
	}

}
