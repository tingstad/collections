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

import com.rictin.util.proxy.Callback;
import com.rictin.util.proxy.Invocation;
import com.rictin.util.proxy.ProxyFactory;

public class ProxyFactoryTest {

	private final static String RICHARD = "Richard";
	private List<Person> list;

	@Before
	public void setUp() {
		list = new ArrayList<Person>();
		list.add(new Person(RICHARD, 30));
	}

	@Test
	public void test() {
		final String testString = "Hello";
		Person proxy = (Person) new ProxyFactory(list).getProxy(new Callback<Person>() {

			public Object intercept(Invocation<Person> invocation) {
				Assert.assertEquals("getName", invocation.getMethod().getName());
				return testString;
			}
		});
		String string = proxy.getName();
		Assert.assertEquals(testString, string);
	}

}
