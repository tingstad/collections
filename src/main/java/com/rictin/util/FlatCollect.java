/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import java.util.Collection;

public class FlatCollect<T> extends ListHandler<T> {

	private Collection<? extends Collection<T>> source;
	private Collection<Object> result;

	@SuppressWarnings("unchecked")
	public FlatCollect(Collection<? extends Collection<T>> input, Collection<?> result) {
		super(getFirstElement(input));
		this.source = input;
		this.result = (Collection<Object>) result;
	}

	private static <T> Collection<T> getFirstElement(Collection<? extends Collection<T>> collection) {
		for (Collection<T> list : collection) {
			if (list != null && !list.isEmpty()) {
				return list;
			}
		}
		return null;
	}

	@Override
	protected Object handleList() {
		for (Collection<T> nested : source) {
			if (nested == null) {
				continue;
			}
			for (T element : nested) {
				if (element == null) {
					continue;
				}
				Object value = getValueOfElement(element);
				result.add(value);
			}
		}
		return null;
	}

}
