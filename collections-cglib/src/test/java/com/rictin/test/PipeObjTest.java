/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test;

import static java.math.BigInteger.valueOf;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rictin.util.Pipe;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.Order;
import com.rictin.util.pipe.matcher.Num;

public class PipeObjTest {

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
		Pipe<Person> pipe = Pipe.from(asList(new Person(RICHARD, 30)));
		List<String> output = pipe
				.select(Condition.where( pipe.item().getAge()).is(Num.lessThan(40)) )
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
	public void testBigIntegerMod() {
		List<BigInteger> numbers = asList(valueOf(1), valueOf(2), valueOf(3), valueOf(4), valueOf(5));
		
		List<BigInteger> odd = Pipe.from(numbers).select(
				Condition.where(
						Pipe.item(numbers).mod(valueOf(2))
				).greaterThan(valueOf(0)))
				.toList();

		Assert.assertEquals(3, odd.size());
		Assert.assertEquals(1, odd.get(0).intValue());
		Assert.assertEquals(3, odd.get(1).intValue());
		Assert.assertEquals(5, odd.get(2).intValue());
	}

	@Test
	public void testChainedCallsAndNoMethodCalls() {
		List<BigInteger> numbers = asList(valueOf(1), valueOf(2), valueOf(5), valueOf(4), valueOf(3));
		
		List<BigInteger> list = Pipe.from(numbers)
				.select(
						Condition.where(Pipe.item(numbers)).equalTo(valueOf(1))
						.or(Pipe.item(numbers).add(valueOf(0))).equalTo(valueOf(2))
						.or(Pipe.item(numbers).add(valueOf(1)).subtract(valueOf(1))).noLessThan(valueOf(2))
						)
				.sort(
						Order.by(Pipe.item(numbers).mod(valueOf(2)))
						.thenBy(Pipe.item(numbers))
						.descending()
						)
				.toList();

		assertEquals(4, list.get(0).intValue());
		assertEquals(2, list.get(1).intValue());
		assertEquals(5, list.get(2).intValue());
		assertEquals(3, list.get(3).intValue());
		assertEquals(1, list.get(4).intValue());
	}

}
