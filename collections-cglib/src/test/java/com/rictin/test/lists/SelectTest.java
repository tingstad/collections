/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.lists;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import com.rictin.test.Person;

import static com.rictin.util.Lists.select;

public class SelectTest {

	private List<Person> list;

	@Before
	public void setUp() {
		list = new ArrayList<Person>();
		list.add(new Person("Erik", 20));
		list.add(new Person("Erik", 35));
		list.add(new Person("Lars", 27));
	}

	@Test
	public void testAccept() {
		List<Person> target = new ArrayList<Person>();
		select(list, target).accept("Lars").getName();

		assertEquals(3, list.size());
		assertEquals(1, target.size());
		assertEquals("Lars", target.get(0).getName());
		assertEquals(27, target.get(0).getAge());
	}

	@Test
	public void testReject() {
		List<Person> target = new ArrayList<Person>();
		select(list, target).rejectGreaterThan(30).getAge();

		assertEquals(3, list.size());
		assertEquals(2, target.size());
	}

}
