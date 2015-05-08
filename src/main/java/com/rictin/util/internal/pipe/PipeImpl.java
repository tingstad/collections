/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rictin.util.Pipe;
import com.rictin.util.internal.ComparatorUtil;
import com.rictin.util.pipe.WhereNumber;

public class PipeImpl<T> extends Pipe<T> {

	public PipeImpl(Iterable<T> input) {
		super.init(input);
	}

	public WhereNumber<T> where(Number number) {
		return new WhereNumberImpl<T>(this, number);
	}

	public Pipe<T> sortBy(Object... item) {
		List<Comparator<T>> comparators = new ArrayList<Comparator<T>>(item.length);
		for (int i = 0; i < item.length; i++) {
//			comparators.add(ComparatorUtil.createComparator(false, false, invocations.remove(0)));
		}
		Comparator<T> comparator = ComparatorUtil.join(comparators);
		
		return this;
	}

	public <U> Pipe<U> mapTo(U item) {
		return doMapTo(item);
	}

	public <U> Map<U, List<T>> groupBy(U item) {
		Map<U, List<T>> map = new HashMap<U, List<T>>();
/*		Invocation<T> invocation = invocations.remove(0);
		for (T element : stream) {
			@SuppressWarnings("unchecked")
			U key = (U) invocation.invoke(element);
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<T>());
			}
			map.get(key).add(element);
		}*/
		return map;
	}

	public List<T> toList() {
		return doToList();
	}

}
