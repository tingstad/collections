/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.lists;

import java.util.Collection;

import com.rictin.util.Lists;

public class ListFinder<T> extends ListHandler<T> {

	private Object value;
	private boolean smallest;
	private boolean largest;

	public ListFinder(Collection<T> list) {
		super(list);
	}

	/**
	 * Checks if value exists in collection. Example usage: {@link Lists#search(Collection)}
	 * .forValue("Sven").getFirstName()
	 * 
	 * @param value
	 *            Value to detect
	 * @return May return null if collection elements are java.lang.String/Integer/...
	 */
	public T forValue(Object value) {
		this.value = value;
		return createProxy();
	}

	/**
	 * Finds smallest (non-null) value of specified type in collection.
	 * 
	 * @return May return null if collection elements are java.lang.String/Integer/...
	 * @see Lists#min(Collection)
	 */
	public T forSmallest() {
		smallest = true;
		return createProxy();
	}

	/**
	 * Finds largest value of specified type in collection.
	 * 
	 * @return May return null if collection elements are java.lang.String/Integer/...
	 * @see Lists#max(Collection)
	 */
	public T forLargest() {
		largest = true;
		return createProxy();
	}

	@Override
	protected Object handleList() {
		Object best = null;
		for (T element : list) {
			if (element == null) {
				continue;
			}
			Object test = getValueOfElement(element);
			if (!smallest && !largest
					&& (value == null && test == null 
					|| value != null && value.equals(test))) {
				return test;
			} else if (smallest || largest) {
				if (best == null) {
					best = test;
				} else {
					@SuppressWarnings({ "unchecked", "rawtypes" })
					int comparison = ((Comparable) best).compareTo(test);
					if (smallest && comparison > 0) {
						best = test;
					}
					else if (largest && comparison < 0) {
						best = test;
					}
				}
			}
		}
		return best;
	}

	@Override
	protected boolean preserveReturnedNull() {
		return true;
	}

}
