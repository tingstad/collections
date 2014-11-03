/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rictin.util.Lists;

public class ListAggregatorTest {

	private List<Person> list;

	@Before
	public void setUp() {
		list = new ArrayList<Person>();
		list.add(new Person("Richard", 30));
		list.add(new Person("Kirsti", 31));
		list.add(new Person("Torstein", 1));
	}

	@Test
	public void testSum() {
		int sum = Lists.sum(list).getAge();

		assertEquals(62, sum);
	}

	@Test
	public void testAverage() {
		Integer average = Lists.average(list).getAge();

		assertEquals(20, average.intValue());
	}

	@Test
	public void testNullValue() {
		list.get(0).setPoints(1000L);
		list.get(2).setPoints(2000L);

		Long sum = Lists.sum(list).getPoints();

		assertEquals(Long.valueOf(3000), sum);
		assertEquals(Long.valueOf(1500), Lists.average(list).getPoints());
	}

	@Test
	public void testNullElement() {
		list.add(1, null);

		int sum = Lists.sum(list).getAge();

		assertEquals(62, sum);
	}

	@Test
	public void testDecimalAverage() {
		list.get(0).setScore(0.8);
		list.get(1).setScore(0.8);
		list.get(2).setScore(0.9);

		Double sum = Lists.sum(list).getScore();

		assertEquals(Double.valueOf(2.5), sum);
		assertEquals(Double.valueOf(0.833), Lists.average(list).getScore());
	}

	@Test
	public void testSumOfIntegerList() {
		List<Integer> ints = Arrays.asList(3, 1, 2);

		Integer sum = Lists.sum(ints);

		assertEquals(Integer.valueOf(6), sum);
	}

}
