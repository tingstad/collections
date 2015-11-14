/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.data;

import java.util.concurrent.atomic.AtomicInteger;


public interface Person extends HasName {

	int getAge();

	Long getPoints();

	Double getScore();

	AtomicInteger getAtomicInteger();

	void setName(String name);

	void setAge(int age);

	IDocument getDocument();
}
