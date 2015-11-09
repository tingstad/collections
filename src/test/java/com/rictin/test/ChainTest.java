/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rictin.test.data.Person;
import com.rictin.test.data.PersonImpl;
import com.rictin.util.Chain;

public class ChainTest {

	private List<Person> list;

	@Before
	public void setUp() {
		list = new ArrayList<Person>();
		list.add(new PersonImpl("RICHARD", 30));
		list.add(new PersonImpl("KIRSTI", 31));
		list.add(new PersonImpl("TORSTEIN", 2));
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
	public void testChainSortAndLimitAndSum() {
		Chain<Person> c = Chain.from(list.iterator());
		c.filter().acceptLessThan(40).getAge();
		c.sort().ascendingBy().getName();
		c.limit(2);
		int sum = c.sum().getAge();

		Assert.assertFalse(c.hasNext());
		Assert.assertEquals(61, sum);
	}

	@Test
	public void testSortByTwoFields() {
		Chain<Person> chain = Chain.from(Arrays.<Person>asList(
				new PersonImpl("Richard", 31),
				new PersonImpl("Richard", 30),
				new PersonImpl("Kirsti", 31)));
		chain.sort().descendingBy().getAge();
		chain.sort().ascendingBy().getName();
		
		Person person = chain.next();
		Assert.assertEquals(31, person.getAge());
		Assert.assertEquals("Kirsti", person.getName());
		person = chain.next();
		Assert.assertEquals(31, person.getAge());
		Assert.assertEquals("Richard", person.getName());
		person = chain.next();
		Assert.assertEquals(30, person.getAge());
		Assert.assertEquals("Richard", person.getName());		
		Assert.assertFalse(chain.hasNext());
	}
	
}
