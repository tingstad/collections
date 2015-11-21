/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.lists;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.rictin.util.Lists;

public class ListsSortCoreOnlyTest {

	@Test(expected = Exception.class)
	public void testNumberFailsBecauseNumberIsNotAnInterface() {
		List<Number> list = new ArrayList<Number>();
		list.add(20);
		list.add(30);
		list.add(10.5);
		Lists.sort(list).ascendingBy().intValue();

		assertEquals(3, list.size());
		assertEquals(10, list.get(0).intValue());
		assertEquals(20, list.get(1).intValue());
		assertEquals(30, list.get(2).intValue());
	}

}
