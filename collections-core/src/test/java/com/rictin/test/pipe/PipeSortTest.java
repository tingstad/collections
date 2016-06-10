/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.pipe;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.rictin.test.data.Person;
import com.rictin.test.data.PersonImpl;
import com.rictin.util.Pipe;
import com.rictin.util.pipe.Order;

public class PipeSortTest {

	@Test
	public void testSort() {

		List<List> list = Arrays.<List>asList(
				Arrays.asList(1),
				Arrays.asList(),
				Arrays.asList(1, 2, 3, 4),
				Arrays.asList(1, 2, 3),
				Arrays.asList(4, 4, 4, 4),
				Arrays.asList(1, 2));

		List<List> bySize = Pipe.from(list)
				.sort(Order.by(Pipe.item(list).size()))
				.toList();

		for (int i = 0; i < list.size() - 1; i++)
			Assert.assertEquals(i, bySize.get(i).size());
		Assert.assertEquals("Should be stable", 4, bySize.get(list.size()-1).get(0));
	}

	@Test
	public void testSortByTwoFields() {
		List<Person> list = Arrays.<Person>asList(
				new PersonImpl("Ric", 30),
				new PersonImpl("Kir", 31),
				new PersonImpl("Tor", 2),
				new PersonImpl("Anna", 31));

		List<Person> sorted = Pipe.from(list)
				.sort(Order.by(Pipe.item(list).getAge())
						.thenBy(Pipe.item(list).getName()))
				.toList();
		
		Assert.assertEquals("Tor", sorted.get(0).getName());
		Assert.assertEquals("Ric", sorted.get(1).getName());
		Assert.assertEquals("Anna", sorted.get(2).getName());
		Assert.assertEquals("Kir", sorted.get(3).getName());
	}

	@Test
	public void testSortDescending() {
		List<Person> list = Arrays.<Person>asList(
				new PersonImpl("Ric", 32),
				new PersonImpl("Kir", 33),
				new PersonImpl("Tor", 4),
				new PersonImpl("Ada", 33));

		List<Person> sorted = Pipe.from(list)
				.sort(Order.by(Pipe.item(list).getAge())
						.descending()
						.thenBy(Pipe.item(list).getName()))
				.toList();
		
		assertEquals("Ada", sorted.get(0).getName());
		assertEquals("Kir", sorted.get(1).getName());
		assertEquals("Ric", sorted.get(2).getName());
		assertEquals("Tor", sorted.get(3).getName());
	}

	@Test
	public void testSortDescending2() {
		List<Person> list = Arrays.<Person>asList(
				new PersonImpl("Ric", 32),
				new PersonImpl("Kir", 33),
				new PersonImpl("Tor", 4),
				new PersonImpl("Ada", 4),
				new PersonImpl("Ada", 33));

		List<Person> sorted = Pipe.from(list)
				.sort(
						Order.by(Pipe.item(list).getAge())
						.thenBy(Pipe.item(list).getName())
						.descending()
				)
				.toList();
		
		assertEquals("Tor", sorted.get(0).getName());
		assertEquals("Ada", sorted.get(1).getName());
		assertEquals("Ric", sorted.get(2).getName());
		assertEquals("Kir", sorted.get(3).getName());
		assertEquals("Ada", sorted.get(4).getName());
	}

}
