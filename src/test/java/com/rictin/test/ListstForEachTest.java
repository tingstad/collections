/* Copyright 2013 Richard H. Tingstad
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

import com.rictin.util.Lists;

public class ListstForEachTest {

	private List<Person> list;
	
	@Before
	public void setUp() {
		list = new ArrayList<Person>();
		list.add(new Person("Erik", 20));
		list.add(new Person("Erik", 35));
		list.add(new Person("Lars", 27));
	}

	@Test
	public void test() {
		Lists.forEach(list).setAge(18);

		Assert.assertEquals(18, list.get(0).getAge());
		Assert.assertEquals(18, list.get(1).getAge());
		Assert.assertEquals(18, list.get(2).getAge());
	}
	
}
