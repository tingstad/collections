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
import com.rictin.util.Conditions;
import com.rictin.util.Pipe;

public class ConditionTest {

	@Test
	public void testOr() {
		List<Person> list = Arrays.asList(
				new Person("RICHARD", 30),
				new Person("KIRSTI", 31),
				new Person("TORSTEIN", 2));
		Pipe<Person> pipe = Pipe.from(list);

		List<Person> adults = pipe.select(
				Conditions.or(
						Conditions.where( pipe.item().getAge() ).isGreaterThan(30),
						Conditions.where( pipe.item().getAge() ).isLessThan(30))
				).toList();

		Assert.assertEquals(2, adults.size());
		Assert.assertEquals(31, adults.get(0).getAge());
		Assert.assertEquals(2, adults.get(1).getAge());
	}
	
}
