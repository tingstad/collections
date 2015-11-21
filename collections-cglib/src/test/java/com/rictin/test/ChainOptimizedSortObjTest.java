/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test;

import static com.rictin.util.OptionalArgument.NO_OPTIMIZATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rictin.util.Chain;
import com.rictin.util.Lists;

public class ChainOptimizedSortObjTest {

	private List<Document> documents;

	@Before
	public void setUp() {
		documents = Arrays.asList(
				new Document("A"),
				new Document("F"),
				new Document("C"),
				new Document("Z"),
				new Document("B"),
				new Document("D"));
	}

	@Test
	public void test() {
		Chain<Document> c = Chain.from(documents);
		c.sort(NO_OPTIMIZATION).ascendingBy().getTitle();
		c.limit(3);
		List<Document> firstThree = c.toList();
		
		assertEquals(3, firstThree.size());
		assertEquals("A", firstThree.get(0).getTitle());
		assertEquals("B", firstThree.get(1).getTitle());
		assertEquals("C", firstThree.get(2).getTitle());

		int comparisons = Lists.sum(documents).getViews();
		
		setUp();
		c = Chain.from(documents);
		c.sort().ascendingBy().getTitle();
		c.limit(3);
		c.toList();
		
		int optimized = Lists.sum(documents).getViews();

		assertTrue(optimized < comparisons);
	}

}
