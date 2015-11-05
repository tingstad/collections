/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rictin.util.ComparatorBuilder;

public class ComparatorBuilderTest {

	private final static String RICHARD = "Richard";
	private final static String KIRSTI = "Kirsti";
	private final static String TORSTEIN = "Torstein";
	private List<Person> list;

	@Before
	public void setUp() {
		list = new ArrayList<Person>();
		list.add(new Person(RICHARD, 30));
		list.add(new Person(RICHARD, 31));
		list.add(new Person(KIRSTI, 31));
	}

	@Test
	public void test() {
		
		ComparatorBuilder<Person> builder = ComparatorBuilder.from(list);
		builder.ascendingBy().getName();
		builder.ascendingBy().getAge();
		Comparator<Person> comparator = builder.build();
		
		Assert.assertEquals(RICHARD, Collections.max(list, comparator).getName());
		Assert.assertEquals(31, Collections.max(list, comparator).getAge());
		Assert.assertEquals(KIRSTI, Collections.min(list, comparator).getName());
	}

}
