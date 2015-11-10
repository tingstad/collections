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
import org.junit.Test;

import com.rictin.test.data.HasName;
import com.rictin.test.data.Person;
import com.rictin.test.data.PersonImpl;
import com.rictin.util.Lists;

public class ProxyTest {
	
	@Test
	public void testNoConstructor() {
		List<NoConstructor> list = new ArrayList<NoConstructor>();
		list.add(new NoConstructor());

		HasNoConstructor proxy = Lists.min(list);

		Assert.assertNotNull(proxy);
	}

	@Test
	public void testNoConstructorArguments() {
		List<NoConstructorArguments> list = new ArrayList<NoConstructorArguments>();
		list.add(new NoConstructorArguments());

		HasNoConstructorArguments proxy = Lists.min(list);
		
		Assert.assertNotNull(proxy);
	}

	@Test
	public void testPrimitiveArguments() {
		List<PrimitiveArguments> list = new ArrayList<PrimitiveArguments>();
		list.add(new PrimitiveArguments(0, 0l, (char)0, 0f, 0d, false, (byte)0, (short)0));
		
		HasPrimitiveArguments proxy = Lists.min(list);

		Assert.assertNotNull(proxy);
	}

	@Test
	public void testIntegerArgument() {
		List<IntegerArgument> list = new ArrayList<IntegerArgument>();
		list.add(new IntegerArgument(0));

		HasIntegerArgument proxy = Lists.min(list);

		Assert.assertNotNull(proxy);
	}

	@Test
	public void testInterfaces() {
		List<Ba> list = new ArrayList<Ba>();
		list.add(new Ba() { });
		list.add(new Cba() { });
		list.add(new Dba() { });
		list.add(new Cba() { });

		Object proxy = Lists.min(list);

		Assert.assertNotNull(proxy);
	}

	@Test
	public void testTypeDetection() {
		List<HasName> list = new ArrayList<HasName>();
		list.add(new PersonImpl("a", 1));
		list.add(new PersonImpl("b", 1));

		Lists.sort(list).descendingBy().getName();

		Assert.assertEquals("b", list.get(0).getName());
	}

}

interface HasNoConstructor{ }
class NoConstructor implements HasNoConstructor{ }

interface HasNoConstructorArguments {}
class NoConstructorArguments implements HasNoConstructorArguments {
	public NoConstructorArguments() {}
}

interface HasPrimitiveArguments {}
class PrimitiveArguments implements HasPrimitiveArguments {
	public PrimitiveArguments(int i, long l, char c, float f, double d, boolean b, byte y, short s) {}
}

interface HasIntegerArgument {}
class IntegerArgument implements HasIntegerArgument {
	public IntegerArgument(Integer i) {}
}

interface A { }

interface Ba extends A { }

interface Cba extends Ba { }

interface Dba extends Ba { }

interface Ea extends A { }
