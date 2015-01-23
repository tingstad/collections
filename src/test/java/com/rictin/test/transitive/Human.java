package com.rictin.test.transitive;

public class Human {

	private Dates dates;

	public Human(String name, int yearOfBirth) {
		dates = new Dates(yearOfBirth);
	}

	public Dates getDates() { return dates; }

}
