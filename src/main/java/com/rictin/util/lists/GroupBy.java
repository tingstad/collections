/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class GroupBy<T> extends ListHandler<T> {

	private Map<Object, List<T>> map;

	@SuppressWarnings("unchecked")
	public GroupBy(Collection<T> list, Map<?, List<T>> destination) {
		super(list);
		map = (Map<Object, List<T>>) destination;
	}

	@Override
	protected Object handleList() {
		for (T element : list) {
			if (element == null) {
				continue;
			}
			Object value = getValueOfElement(element);
			if (!map.containsKey(value)) {
				map.put(value, new ArrayList<T>());
			}
			map.get(value).add(element);
		}
		return null;
	}

}
