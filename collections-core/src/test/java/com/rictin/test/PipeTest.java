/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rictin.test.data.Person;
import com.rictin.test.data.PersonImpl;
import com.rictin.util.Pipe;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.Order;
import com.rictin.util.pipe.matcher.Num;

public class PipeTest {

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
	public void testFilter() {
		Pipe<Person> pipe = Pipe.from(list);
		List<Person> output = pipe
				.select( Condition.where( pipe.item().getAge() ).is(Num.lessThan(25)) )
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(2, output.get(0).getAge());
		Assert.assertEquals(TORSTEIN, output.get(0).getName());
	}

	@Test
	public void testFilterAndTransform() {
		Pipe<Person> pipe = Pipe.from(list);
		List<String> output = pipe
				.select( Condition.where( pipe.item().getAge()).is(Num.lessThan(25)) )
				.mapTo(pipe.item().getName())
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(TORSTEIN, output.get(0));
	}

	@Test
	public void testFilterMapFilter() {
		Pipe<Person> pipe = Pipe.from(list);
		Pipe<Integer> ages = pipe
				.select(Condition.where( pipe.item().getAge()).is(Num.lessThan(31)) )
				.mapTo(pipe.item().getAge());
		List<Integer> output = ages
				.select(Condition.where( ages.item().intValue()).is(Num.lessThan(25)) )
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(2, output.get(0).intValue());
	}

	@Test
	public void testErr() {
		Pipe<Person> pipe = Pipe.from(list);
		List<String> output = pipe
				.select( Condition.where( pipe.item().getAge() ).is(Num.lessThan(25)) )
				.mapTo(pipe.item().getName())
				.toList();
		
		Pipe<Person> pipe2 = Pipe.from(list);
		Pipe<Integer> ages = pipe2
				.select(Condition.where( pipe2.item().getAge() ).is(Num.lessThan(31)) )
				.mapTo(pipe2.item().getAge());
	}

	@Test
	public void testOneElementListWithMapTo() {
		Pipe<Person> pipe = Pipe.from(asList((Person) new PersonImpl(RICHARD, 30)));
		List<String> output = pipe
				.select(Condition.where( pipe.item().getAge() ).is(Num.lessThan(40)) )
				.mapTo(pipe.item().getName())
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(RICHARD, output.get(0));
	}

	@Test
	public void testOneLinePipe() {
		List<Person> output = Pipe.from(list)
				.select(Condition.where( Pipe.item(list).getAge()).is(Num.lessThan(25)) )
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(TORSTEIN, output.get(0).getName());
	}

	@Test
	public void testEqualTo() {
		List<Person> output = Pipe.from(list)
				.select(Condition.where( Pipe.item(list).getName() ).equalTo(TORSTEIN))
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(TORSTEIN, output.get(0).getName());
	}

	@Test
	public void testNull() {
		list.add(new PersonImpl(null, 0));

		List<Person> output = Pipe.from(list)
				.select(Condition.where( Pipe.item(list).getName() ).isNull())
				.toList();
		
		Assert.assertEquals(1, output.size());
		Assert.assertNull(output.get(0).getName());
	}

	@Test
	public void testSort() {
		List<Person> output = Pipe.from(list)
				.sort(Order.by(Pipe.item(list).getAge()))
				.toList();
		
		Assert.assertEquals(TORSTEIN, output.get(0).getName());
		Assert.assertEquals(RICHARD, output.get(1).getName());
		Assert.assertEquals(KIRSTI, output.get(2).getName());
	}

}
