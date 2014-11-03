/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rictin.util.Lists;

public class ListsFilterTest {

	private List<Person> list;

	@Before
	public void setUp() {
		list = new ArrayList<Person>();
		list.add(new Person("Erik", 20));
		list.add(new Person("Erik", 35));
		list.add(new Person("Lars", 27));
	}

	@Test
	public void testAcceptEquals() {
		Lists.filter(list).accept("Erik").getName();
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void testRejectNotEquals() {
		Lists.filter(list).rejectNot("Erik").getName();
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void testRejectEquals() {
		Lists.filter(list).reject("Erik").getName();
		Assert.assertEquals(1, list.size());
		Assert.assertEquals("Lars", list.get(0).getName());
	}

	@Test
	public void testAcceptNotEquals() {
		Lists.filter(list).acceptNot("Erik").getName();
		Assert.assertEquals(1, list.size());
		Assert.assertEquals("Lars", list.get(0).getName());
	}

	@Test
	public void testAcceptGreaterThan() {
		Lists.filter(list).acceptGreaterThan(30).getAge();
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void testAcceptGreaterThanOrEqual() {
		Lists.filter(list).acceptGreaterThanOrEqual(35).getAge();
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(35, list.get(0).getAge());
	}

	@Test
	public void testAcceptLessThan() {
		Lists.filter(list).acceptLessThan(30).getAge();
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void testAcceptLessThanOrEqual() {
		Lists.filter(list).acceptLessThanOrEqual(27).getAge();
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void testRejectLessThan() {
		Lists.filter(list).rejectLessThan(21).getAge();
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void testRejectNull() {
		list.get(1).setScore(0.1);
		list.get(2).setScore(0.2);
		Lists.filter(list).reject(null).getScore();
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void testRejectWithNullElement() {
		list.add(1, null);
		Lists.filter(list).rejectGreaterThan(0).getAge();
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(null, list.get(0));
	}

	@Test
	public void testAcceptWithNullElement() {
		list.add(1, null);
		Lists.filter(list).acceptGreaterThan(0).getAge();
		Assert.assertEquals(3, list.size());
	}

	@Test
	public void testAcceptLessThanDouble() {
		list.get(0).setScore(0.1);
		list.get(1).setScore(0.2);
		list.get(2).setScore(0.3);
		Lists.filter(list).acceptLessThan(0.15).getScore();
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(Double.valueOf(0.1), list.get(0).getScore());
	}

	@Test
	public void testRemoveDuplicates() {
		list.get(0).setPoints(1L);
		list.get(1).setPoints(2L);
		list.get(2).setPoints(1L);
		Lists.filter(list).removeDuplicates().getPoints();
		Assert.assertEquals(2, list.size());
		Assert.assertEquals(3, Lists.sum(list).getPoints().longValue());
	}

	@Test
	public void testAtomicInteger() {
		@SuppressWarnings("unused")
		AtomicInteger a = Lists.filter(list).rejectGreaterThan(2).getAtomicInteger();
		Assert.assertEquals(0, list.size());
	}

	@Test
	public void testFilterString() {
		List<String> list = new ArrayList<String>(
				Arrays.asList("a", "b", "c", "b"));

		Lists.filter(list).reject("b");

		Assert.assertEquals(2, list.size());
	}

	@Test
	public void testFilterLong() {
		List<Long> list = new ArrayList<Long>(
				Arrays.asList(2L, 3L, 4L, 5L, 6L));

		Lists.filter(list).acceptLessThanOrEqual(2).intValue();

		Assert.assertEquals(1, list.size());
		Assert.assertEquals(2, list.get(0).longValue());
	}

}
