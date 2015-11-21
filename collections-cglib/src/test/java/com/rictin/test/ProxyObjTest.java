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

import com.rictin.util.Lists;

public class ProxyObjTest {
	
	@Test
	public void testNoConstructor() {
		List<NoConstructorObj> list = new ArrayList<NoConstructorObj>();
		list.add(new NoConstructorObj());

		NoConstructorObj proxy = Lists.min(list);

		Assert.assertNotNull(proxy);
	}

	@Test
	public void testNoConstructorArguments() {
		List<NoConstructorArgumentsObj> list = new ArrayList<NoConstructorArgumentsObj>();
		list.add(new NoConstructorArgumentsObj());

		NoConstructorArgumentsObj proxy = Lists.min(list);
		
		Assert.assertNotNull(proxy);
	}

	@Test
	public void testPrimitiveArguments() {
		List<PrimitiveArgumentsObj> list = new ArrayList<PrimitiveArgumentsObj>();
		list.add(new PrimitiveArgumentsObj(0, 0l, (char)0, 0f, 0d, false, (byte)0, (short)0));
		
		PrimitiveArgumentsObj proxy = Lists.min(list);

		Assert.assertNotNull(proxy);
	}

	@Test
	public void testIntegerArgument() {
		List<IntegerArgumentObj> list = new ArrayList<IntegerArgumentObj>();
		list.add(new IntegerArgumentObj(0));

		IntegerArgumentObj proxy = Lists.min(list);

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
		list.add(new Person("a", 1));
		list.add(new Person("b", 1));

		Lists.sort(list).descendingBy().getName();

		Assert.assertEquals("b", list.get(0).getName());
	}

}

class NoConstructorObj{ }

class NoConstructorArgumentsObj {
	public NoConstructorArgumentsObj() {}
}

class PrimitiveArgumentsObj {
	public PrimitiveArgumentsObj(int i, long l, char c, float f, double d, boolean b, byte y, short s) {}
}

class IntegerArgumentObj {
	public IntegerArgumentObj(Integer i) {}
}

interface A { }

interface Ba extends A { }

interface Cba extends Ba { }

interface Dba extends Ba { }

interface Ea extends A { }
