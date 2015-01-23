package com.rictin.test.transitive;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.rictin.util.Pipe;

public class TransitivityTest {

	@Test
	public void testFilter() {
		List<Human> persons = new ArrayList<Human>(Arrays.asList(
				new Human("Ada", 1815),
				new Human("Charles", 1791)));
		
		Pipe<Human> pipe = Pipe.from(persons);
		pipe.filterKeepLessThan(1800, pipe.item().getDates().getYearOfBirth());
		List<Human> result = pipe.toList();
		
		assertEquals(1, result.size());
		assertEquals(1791, result.get(0).getDates().getYearOfBirth());
	}
	
}
