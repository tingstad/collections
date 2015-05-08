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

import com.rictin.util.Pipe;

public class PipeTest {

	private final static String RICHARD = "Richard";
	private final static String KIRSTI = "Kirsti";
	private final static String TORSTEIN = "Torstein";
	private List<Person> list;

	@Before
	public void setUp() {
		list = new ArrayList<Person>();
		list.add(new Person(RICHARD, 30));
		list.add(new Person(KIRSTI, 31));
		list.add(new Person(TORSTEIN, 2));
	}

	@Test
	public void test() {
		Pipe<Person> pipe = Pipe.from(list);
		List<Person> output = pipe
				.where(pipe.item().getAge()).isLessThan(25)
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(2, output.get(0).getAge());
		Assert.assertEquals(TORSTEIN, output.get(0).getName());
	}

	@Test
	public void test2() {
		Pipe<Person> pipe = Pipe.from(list);
		List<String> output = pipe
				.where(pipe.item().getAge()).isLessThan(25)
				.mapTo(pipe.item().getName())
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(TORSTEIN, output.get(0));
	}

	@Test
	public void test3() {
		Pipe<Person> pipe = Pipe.from(list);
		Pipe<Integer> ages = pipe
				.where(pipe.item().getAge()).isLessThan(31)
				.mapTo(pipe.item().getAge());
		List<Integer> output = ages
				.where(ages.item().intValue()).isLessThan(25)
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(2, output.get(0).intValue());
	}

	@Test
	public void test4() {
//		List<HasName> 
		Pipe<Person> pipe = Pipe.from(list);
		Pipe<Integer> ages = pipe
				.where(pipe.item().getAge()).isLessThan(31)
				.mapTo(pipe.item().getAge());
		List<Integer> output = ages
				.where(ages.item().intValue()).isLessThan(25)
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(2, output.get(0).intValue());
	}

	@Test
	public void testOneLinePipe() {
		List<Person> output = Pipe.from(list)
				.where(Pipe.item(list).getAge()).isLessThan(25)
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(TORSTEIN, output.get(0).getName());
	}


}
