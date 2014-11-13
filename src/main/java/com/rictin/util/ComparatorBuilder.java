/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.rictin.util.internal.lists.ListSorter;


public class ComparatorBuilder<T> extends ListSorter<T> {

	private List<Comparator<T>> comparators = new ArrayList<Comparator<T>>();
	private List<Method> methods = new ArrayList<Method>();
	
	private ComparatorBuilder(List<T> collection) {
		super(collection);
	}

	public static <T> ComparatorBuilder<T> from(T object) {
		return new ComparatorBuilder<T>(Arrays.asList(object));
	}

	public static <T> ComparatorBuilder<T> from(List<T> objects) {
		return new ComparatorBuilder<T>(objects);
	}

	@Override
	protected Object handleList() {
		comparators.add(createComparator());
		methods.add(invocation.getMethod());
		return null;
	}

	public Comparator<T> build() {
		if (comparators.isEmpty()) {
			throw new RuntimeException("Must specify what to compare.");
		}
		if (comparators.size() == 1) {
			return comparators.get(0);
		}
		return new Comparator<T>() {

			public int compare(T o1, T o2) {
				int i = 0;
				int c = 0;
				while (c == 0 && i < comparators.size()) {
//					method = methods.get(i);
					c = comparators.get(i++).compare(o1, o2);
				}
				return c;
			}
			
		};
	}
	
}
