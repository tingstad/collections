/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rictin.util.internal.ComparatorUtil;
import com.rictin.util.internal.pipe.PipeImpl;
import com.rictin.util.internal.pipe.PipeMap;
import com.rictin.util.internal.pipe.PipeParent;
import com.rictin.util.internal.pipe.PipeSelect;
import com.rictin.util.pipe.Condition;

public abstract class Pipe<T> extends PipeParent<T> implements Iterable<T> {

	public static <T> Pipe<T> from(final Iterable<T> input) {
		return new PipeImpl<T>(input);
	}

	public Pipe<T> select(final Condition condition) {
		return new PipeSelect<T>(this, condition);
	}

	public Pipe<T> sortBy(Object... item) {
		List<Comparator<T>> comparators = new ArrayList<Comparator<T>>(item.length);
		for (int i = 0; i < item.length; i++) {
//			comparators.add(ComparatorUtil.createComparator(false, false, invocations.remove(0)));
		}
		Comparator<T> comparator = ComparatorUtil.join(comparators);
		
		return this;
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

	public <U> Pipe<U> mapTo(U item) {
		return new PipeMap<T, U>(this, item);
	}

	public List<T> toList() {
		List<T> list = new ArrayList<T>();
		for (T element : this)
			list.add(element);
		cleanUp();
		return list;
	}

}
