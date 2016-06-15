/* Copyright 2016 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.pipe;

import static com.rictin.util.Pipe.item;
import static com.rictin.util.pipe.Condition.where;
import static java.lang.Integer.valueOf;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.rictin.util.Pipe;
import com.rictin.util.pipe.Order;

public class PipeLimitTest {

	@Test
	public void testLimit() {
		List<Integer> list = Pipe.from(Arrays.<Integer>asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 0))
				.first(3).toList();
		
		assertEquals(Arrays.<Integer>asList(1, 2, 3), list);
	}

	@Test
	public void testLimitWithFilter() {
		List<Integer> input = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);

		List<Integer> list = Pipe.from(input)
				.select(where(valueOf(item(input).intValue())).greaterThan(7))
				.first(3).toList();
		
		assertEquals(Arrays.<Integer>asList(8, 9), list);
	}

	@Test
	public void testLimitWithSort() {
		List<CharSequence> input = Arrays.<CharSequence>asList(
				"a", "d", "f", "e", "i", "h", "g", "c", "b");

		List<CharSequence> list = Pipe.from(input)
				.sort(Order.by(item(input).toString()))
				.first(3).toList();
		
		assertEquals(Arrays.<CharSequence>asList("a", "b", "c"), list);
	}

}
