/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.lists;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.rictin.test.data.Person;
import com.rictin.test.data.PersonImpl;
import com.rictin.util.Lists;

public class ListsSortTest {

	private final static String RICHARD = "Richard";
	private final static String KIRSTI = "Kirsti";
	private final static String TORSTEIN = "Torstein";
	private List<Person> list;

	@Before
	public void setUp() {
		list = new ArrayList<Person>();
		list.add(new PersonImpl(RICHARD, 30));
		list.add(new PersonImpl(KIRSTI, 31));
		list.add(new PersonImpl(TORSTEIN, 2));
	}

	@Test
	public void testOne() {
		Lists.sort(list).ascendingBy().getName();

		assertEquals(KIRSTI, list.get(0).getName());
		assertEquals(RICHARD, list.get(1).getName());
		assertEquals(TORSTEIN, list.get(2).getName());
	}

	@Test
	public void testTwo() {
		Lists.sort(list).nullsFirstDescendingBy().getAge();

		assertEquals(true, list.get(0).getAge() > list.get(1).getAge());
		assertEquals(true, list.get(1).getAge() > list.get(2).getAge());
	}

	@Test(expected = Exception.class)
	public void testThatWrongUsageGivesError() {
		Lists.sort(list).descendingBy().setName("foo");
	}

	@SuppressWarnings("unused")
	@Ignore("TODO: Not yet resolved")
	@Test(expected = Exception.class)
	public void testThatTryingToUseElementGivesError() {
		Person person = Lists.sort(list).descendingBy();
		String name = person.getName();
		int age = person.getAge();
	}

	@Test
	public void testSortNullLast() {
		list.add(null);
		list.add(new PersonImpl(null, 1));

		Lists.sort(list).ascendingBy().getName();

		assertEquals(KIRSTI, list.get(0).getName());
		assertEquals(RICHARD, list.get(1).getName());
		assertEquals(TORSTEIN, list.get(2).getName());
		assertEquals(null, list.get(3).getName());
		assertEquals(null, list.get(4));
	}

	@Test
	public void testSortNullLastDescending() {
		list.add(null);
		list.add(new PersonImpl(null, 1));

		Lists.sort(list).descendingBy().getName();

		assertEquals(TORSTEIN, list.get(0).getName());
		assertEquals(RICHARD, list.get(1).getName());
		assertEquals(KIRSTI, list.get(2).getName());
		assertEquals(null, list.get(3).getName());
		assertEquals(null, list.get(4));
	}

	@Test
	public void testSortNullFirst() {
		list.add(new PersonImpl(null, 1));
		list.add(null);

		Lists.sort(list).nullsFirstAscendingBy().getName();

		assertEquals(null, list.get(0));
		assertEquals(null, list.get(1).getName());
		assertEquals(KIRSTI, list.get(2).getName());
		assertEquals(RICHARD, list.get(3).getName());
		assertEquals(TORSTEIN, list.get(4).getName());
	}

	@Test
	public void testSortNullFirstDescending() {
		list.add(new PersonImpl(null, 1));
		list.add(null);

		Lists.sort(list).nullsFirstDescendingBy().getName();

		assertEquals(null, list.get(0));
		assertEquals(null, list.get(1).getName());
		assertEquals(TORSTEIN, list.get(2).getName());
		assertEquals(RICHARD, list.get(3).getName());
		assertEquals(KIRSTI, list.get(4).getName());
	}

	@Test
	public void testNullElementsAndNullValues() {
		list.add(null);
		list.add(new PersonImpl(null, 1));
		Lists.sort(list).ascendingBy().getName();

		assertEquals(KIRSTI, list.get(0).getName());
		assertEquals(RICHARD, list.get(1).getName());
		assertEquals(TORSTEIN, list.get(2).getName());
		assertEquals(null, list.get(3).getName());
		assertEquals(null, list.get(4));
	}

	@Test
	public void testNullElementsAndNullValuesNullsFirst() {
		list.add(null);
		list.add(new PersonImpl(null, 1));
		Lists.sort(list).nullsFirstDescendingBy().getName();

		assertEquals(null, list.get(0));
		assertEquals(null, list.get(1).getName());
		assertEquals(TORSTEIN, list.get(2).getName());
		assertEquals(RICHARD, list.get(3).getName());
		assertEquals(KIRSTI, list.get(4).getName());
	}

	@Test(expected = Exception.class)
	public void testEmptyListFailsBecauseWeNeedAnElementToKnowType() {
		list.clear();
		Lists.sort(list).ascendingBy().getName();

		assertEquals(0, list.size());
	}

	@Test
	public void testInteger() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(20);
		list.add(30);
		list.add(10);
		Lists.sort(list).ascendingBy().intValue();

		assertEquals(3, list.size());
		assertEquals(10, list.get(0).intValue());
		assertEquals(20, list.get(1).intValue());
		assertEquals(30, list.get(2).intValue());
	}

	@Test
	public void testString() {
		List<String> list = Arrays.asList("a", "d", null, "c", "b", null);

		Lists.sort(list).nullsFirstDescendingBy();

		assertEquals(null, list.get(0));
		assertEquals(null, list.get(1));
		assertEquals("d", list.get(2));
		assertEquals("c", list.get(3));
		assertEquals("b", list.get(4));
		assertEquals("a", list.get(5));

		Lists.sort(list).ascendingBy().toUpperCase();

		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
		assertEquals("d", list.get(3));
		assertEquals(null, list.get(4));
		assertEquals(null, list.get(5));
	}

}
