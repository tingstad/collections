/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.transitive;

public class Dates implements HasYearOfBirth {

	private int yearOfBirth;

	public Dates(int yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public int getYearOfBirth() { return yearOfBirth; }

}
