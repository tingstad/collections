/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.condition;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rictin.test.Person;
import com.rictin.util.Pipe;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.matcher.Matcher;
import com.rictin.util.pipe.matcher.Num;
import com.rictin.util.pipe.matcher.Str;

public class ConditionTest {

	private List<Person> list;
	
	@Before
	public void setUp() {
		list = Arrays.asList(
				new Person("RICHARD", 30),
				new Person("KIRSTI", 31),
				new Person("TORSTEIN", 2));
	}
	
	@Test
	public void testAnd() {
		Pipe<Person> pipe = Pipe.from(list);

		List<Person> adults = pipe
				.select( Condition.where( pipe.item().getAge() ).is(Num.greaterThan(3))
				.and(5).is(Num.lessThan( pipe.item().getAge() )) )
				.toList();

		Assert.assertEquals(2, adults.size());
		Assert.assertEquals(30, adults.get(0).getAge());
		Assert.assertEquals(31, adults.get(1).getAge());
	}

	@Test
	public void testNumericOnlyCondition() {
		List<Person> persons = Pipe.from(list)
				.select( Condition.where(4).is(Num.greaterThan(3)) )
				.toList();

		Assert.assertEquals(3, persons.size());
	}

	@Test
	public void testFalseNumericOnlyCondition() {
		List<Person> persons = Pipe.from(list)
				.select( Condition.where(2).is(Num.greaterThan(3)) )
				.toList();

		Assert.assertEquals(0, persons.size());
	}

	@Test
	public void testManyConditions() {
		Pipe<Person> pipe = Pipe.from(list);

		List<Person> persons = pipe.select(
				Condition.where( pipe.item().getAge() ).is(Num.greaterThan(3))
				.and(5).is(Num.lessThan(6))
				.and(pipe.item().getAge()).is(Num.lessThan(31))
				)
				.toList();

		Assert.assertEquals(1, persons.size());
		Assert.assertEquals("RICHARD", persons.get(0).getName());
	}

	@Test
	public void testStringCondition() {
		List<Person> persons = Pipe.from(list).select(
				Condition.where( Pipe.item(list).getName() ).is(Str.startsWith("R"))
				).toList();

		Assert.assertEquals(1, persons.size());
		Assert.assertEquals("RICHARD", persons.get(0).getName());
	}

	@Test
	public void testStringConditionWithoutMatcher() {
		List<Person> persons = Pipe.from(list).select(
				Condition.where( Pipe.item(list).getName() ).startsWith("R")
				).toList();

		Assert.assertEquals(1, persons.size());
		Assert.assertEquals("RICHARD", persons.get(0).getName());
	}

	@Test
	public void testAnonymousCondition() {
		List<Person> persons = Pipe.from(list).select(
				new Condition<Person>() {

					public boolean satisfies(Person element) {
						return Integer.valueOf(31).equals(element.getAge());
					}
				}).toList();

		Assert.assertEquals(1, persons.size());
		Assert.assertEquals("KIRSTI", persons.get(0).getName());
	}

	@Test
	public void testAnonymousMatcher() {
		List<Person> persons = Pipe.from(list).select(
				Condition.where( Pipe.item(list).getAge() ).is(new Matcher<Integer>(1) {
					
					// Age is 10*X + _1_
					public boolean matches(Integer value, Integer input) {
						return input % 10 == value;
					}
				})).toList();

		Assert.assertEquals(1, persons.size());
		Assert.assertEquals("KIRSTI", persons.get(0).getName());
	}

}
