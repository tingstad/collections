/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rictin.util.Chain;

public class ChainTest {

	private List<Person> list;

	@Before
	public void setUp() {
		list = new ArrayList<Person>();
		list.add(new Person("RICHARD", 30));
		list.add(new Person("KIRSTI", 31));
		list.add(new Person("TORSTEIN", 2));
	}

	@Test
	public void testChainFromList() {
		Chain<Person> c = Chain.from(list);
		c.filter().acceptGreaterThan(18).getAge();

		Person p1 = c.next();
		Person p2 = c.next();

		Assert.assertFalse(c.hasNext());
		Assert.assertEquals(30, p1.getAge());
		Assert.assertEquals(31, p2.getAge());
	}

	@Test
	public void testChainFromIterator() {
		Chain<Person> c = Chain.from(list.iterator());
		c.filter().acceptGreaterThan(18).getAge();

		Person p1 = c.next();
		Person p2 = c.next();

		Assert.assertFalse(c.hasNext());
		Assert.assertEquals(30, p1.getAge());
		Assert.assertEquals(31, p2.getAge());
	}

	@Test
	public void testLimit() {
		Chain<Person> c = Chain.from(list);
		c.limit(2);
		Assert.assertEquals(2, c.toList().size());
		Assert.assertFalse(c.hasNext());
	}

	@Test
	public void testChainSortAndSum() {
		Chain<Person> c = Chain.from(list.iterator());
		c.filter().acceptLessThan(40).getAge();
		c.sort().ascendingBy().getName();
		c.limit(2);
		int sum = c.sum().getAge();

		Assert.assertFalse(c.hasNext());
		Assert.assertEquals(61, sum);
	}

}
