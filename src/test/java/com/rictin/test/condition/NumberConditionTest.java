package com.rictin.test.condition;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.rictin.test.Person;
import com.rictin.util.Conditions;
import com.rictin.util.Pipe;

public class NumberConditionTest {

	@Test
	public void test() {
		List<Person> list = Arrays.asList(
				new Person("RICHARD", 30),
				new Person("KIRSTI", 31),
				new Person("TORSTEIN", 2));
		Pipe<Person> pipe = Pipe.from(list);

		List<Person> adults = pipe.select(
				Conditions.where( pipe.item().getAge() ).isGreaterThan(17)
				).toList();

		Assert.assertEquals(2, adults.size());
		Assert.assertEquals(30, adults.get(0).getAge());
		Assert.assertEquals(31, adults.get(1).getAge());
	}

}
