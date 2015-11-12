/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.lists;

import static com.rictin.util.Lists.groupBy;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.rictin.test.data.Person;
import com.rictin.test.data.PersonImpl;

public class GroupByTest {

	@Test
	public void test() {
		List<Person> persons = asList(
				(Person) new PersonImpl("Erik", 4),
				(Person) new PersonImpl("Erik", 30),
				(Person) new PersonImpl("Thor", 30));

		Map<Integer, List<Person>> personsByAge = new HashMap<Integer, List<Person>>();
		groupBy(persons, personsByAge).getAge();

		assertEquals(2, personsByAge.size());
		assertEquals(2, personsByAge.get(30).size());
		assertEquals(1, personsByAge.get(4).size());
		assertEquals("Erik", personsByAge.get(4).get(0).getName());
	}

	@Test
	public void testNulls() {
		List<Person> persons = asList(
				(Person) new PersonImpl("Erik", 4),
				null,
				(Person) new PersonImpl("Erik", 30),
				(Person) new PersonImpl(null, 20),
				(Person) new PersonImpl("Thor", 30));

		Map<String, List<Person>> personsByName = new HashMap<String, List<Person>>();
		groupBy(persons, personsByName).getName();

		assertEquals(3, personsByName.size());
		assertEquals(2, personsByName.get("Erik").size());
		assertEquals(1, personsByName.get(null).size());
		assertEquals(1, personsByName.get("Thor").size());
	}

}
