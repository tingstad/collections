/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.lists;

import java.util.Collection;


public class Collect<T> extends ListHandler<T> {

	private Collection<Object> result;

	@SuppressWarnings("unchecked")
	public Collect(Collection<T> input, Collection<?> result) {
		super(input);
		this.result = (Collection<Object>) result;
	}

	@Override
	protected Object handleList() {
		for (T element : list) {
			if (element == null) {
				continue;
			}
			Object value = getValueOfElement(element);
			result.add(value);
		}
		return null;
	}

}
