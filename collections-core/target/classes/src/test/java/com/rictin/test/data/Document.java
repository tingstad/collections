/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.test.data;

public class Document implements IDocument {

	private String title;	
	private int views;
	
	public Document(String title) {
		this.title = title;
	}

	public String getTitle() {
		views++;
		return title;
	}

	public int getViews() {
		return views;
	}

	@Override
	public String toString() {
		return title;
	}
	
}
