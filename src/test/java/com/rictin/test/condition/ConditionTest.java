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
import org.junit.Test;

import com.rictin.test.Person;
import com.rictin.util.Pipe;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.matcher.Num;
import com.rictin.util.pipe.matcher.Str;

public class ConditionTest {

	@Test
	public void testAnd() {
		List<Person> list = Arrays.asList(
				new Person("RICHARD", 30),
				new Person("KIRSTI", 31),
				new Person("TORSTEIN", 2));
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
		List<Person> list = Arrays.asList(
				new Person("RICHARD", 30),
				new Person("KIRSTI", 31),
				new Person("TORSTEIN", 2));

		List<Person> persons = Pipe.from(list)
				.select( Condition.where(4).is(Num.greaterThan(3)) )
				.toList();

		Assert.assertEquals(3, persons.size());
	}

	@Test
	public void testFalseNumericOnlyCondition() {
		List<Person> list = Arrays.asList(
				new Person("RICHARD", 30),
				new Person("KIRSTI", 31),
				new Person("TORSTEIN", 2));

		List<Person> persons = Pipe.from(list)
				.select( Condition.where(2).is(Num.greaterThan(3)) )
				.toList();

		Assert.assertEquals(0, persons.size());
	}

	@Test
	public void testManyConditions() {
		List<Person> list = Arrays.asList(
				new Person("RICHARD", 30),
				new Person("KIRSTI", 31),
				new Person("TORSTEIN", 2));
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
		List<Person> list = Arrays.asList(
				new Person("RICHARD", 30),
				new Person("KIRSTI", 31),
				new Person("TORSTEIN", 2));

		List<Person> persons = Pipe.from(list).select(
				Condition.where( Pipe.item(list).getName() ).is(Str.startsWith("R"))
				).toList();

		Assert.assertEquals(1, persons.size());
		Assert.assertEquals("RICHARD", persons.get(0).getName());
	}

	@Test
	public void testStringConditionWithoutMatcher() {
		List<Person> list = Arrays.asList(
				new Person("RICHARD", 30),
				new Person("KIRSTI", 31),
				new Person("TORSTEIN", 2));

		List<Person> persons = Pipe.from(list).select(
				Condition.where( Pipe.item(list).getName() ).startsWith("R")
				).toList();

		Assert.assertEquals(1, persons.size());
		Assert.assertEquals("RICHARD", persons.get(0).getName());
	}

}
