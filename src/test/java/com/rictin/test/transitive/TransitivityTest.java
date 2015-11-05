/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.transitive;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.rictin.util.Pipe;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.Num;

public class TransitivityTest {

	@Test
	public void testFilter() {
		List<Human> persons = new ArrayList<Human>(Arrays.asList(
				new Human("Ada", 1815),
				new Human("Charles", 1791)));
		
		Pipe<Human> pipe = Pipe.from(persons);
		
		List<Human> result = pipe
				.select( Condition.where( pipe.item().getDates().getYearOfBirth() ).is(Num.lessThan(1800)) )
				.toList();
		
		assertEquals(1, result.size());
		assertEquals(1791, result.get(0).getDates().getYearOfBirth());
	}
	
}
