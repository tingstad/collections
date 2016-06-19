/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.lists;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ListFilter<T> extends ListHandler<T> {

	private static enum Operation {
		EQUALS,
		NOT_EQUALS,
		GREATER_THAN,
		GREATER_THAN_OR_EQUAL,
		LESS_THAN,
		LESS_THAN_OR_EQUAL,
		DUPLICATE
	}

	private boolean accept;
	private Operation operation;
	private Object testValue;
	private Number number;

	public ListFilter(Collection<T> collection) {
		super(collection);
	}

	public T accept(Object value) {
		accept = true;
		operation = Operation.EQUALS;
		testValue = value;
		return createProxy();
	}

	public T acceptNot(Object value) {
		accept = true;
		operation = Operation.NOT_EQUALS;
		testValue = value;
		return createProxy();
	}

	public T acceptGreaterThan(Number value) {
		accept = true;
		operation = Operation.GREATER_THAN;
		number = value;
		return createProxy();
	}

	public T acceptGreaterThanOrEqual(Number value) {
		accept = true;
		operation = Operation.GREATER_THAN_OR_EQUAL;
		number = value;
		return createProxy();
	}

	public T acceptLessThan(Number value) {
		accept = true;
		operation = Operation.LESS_THAN;
		number = value;
		return createProxy();
	}

	public T acceptLessThanOrEqual(Number value) {
		accept = true;
		operation = Operation.LESS_THAN_OR_EQUAL;
		number = value;
		return createProxy();
	}

	public T removeDuplicates() {
		accept = false;
		operation = Operation.DUPLICATE;
		return createProxy();
	}

	public T reject(Object value) {
		accept = false;
		operation = Operation.EQUALS;
		testValue = value;
		return createProxy();
	}

	public T rejectNot(Object value) {
		accept = false;
		operation = Operation.NOT_EQUALS;
		testValue = value;
		return createProxy();
	}

	public T rejectGreaterThan(Number value) {
		accept = false;
		operation = Operation.GREATER_THAN;
		number = value;
		return createProxy();
	}

	public T rejectGreaterThanOrEqual(Number value) {
		accept = false;
		operation = Operation.GREATER_THAN_OR_EQUAL;
		number = value;
		return createProxy();
	}

	public T rejectLessThan(Number value) {
		accept = false;
		operation = Operation.LESS_THAN;
		number = value;
		return createProxy();
	}

	public T rejectLessThanOrEqual(Number value) {
		accept = false;
		operation = Operation.LESS_THAN_OR_EQUAL;
		number = value;
		return createProxy();
	}

	@Override
	protected Object handleList() {
		Set<Object> exists = new HashSet<Object>();
		for (Iterator<T> iterator = list.iterator(); iterator.hasNext(); ) {
			T element = iterator.next();
			boolean test = false;
			if (element != null) {
				Object value = getValueOfElement(element);
				switch (operation) {
				case EQUALS:
					test = (value == null && testValue == null || value != null && value.equals(testValue));
					break;
				case NOT_EQUALS:
					test = (value == null && testValue != null || value != null && !value.equals(testValue));
					break;
				case GREATER_THAN:
					test = value != null && number.doubleValue() < ((Number) value).doubleValue();
					break;
				case GREATER_THAN_OR_EQUAL:
					test = value != null && number.doubleValue() <= ((Number) value).doubleValue();
					break;
				case LESS_THAN:
					test = value != null && number.doubleValue() > ((Number) value).doubleValue();
					break;
				case LESS_THAN_OR_EQUAL:
					test = value != null && number.doubleValue() >= ((Number) value).doubleValue();
					break;
				case DUPLICATE:
					test = exists.contains(value);
					exists.add(value);
					break;
				default:
					throw new IllegalStateException();
				}
			}
			if (accept != test) {
				iterator.remove();
			}
		}
		return null;
	}
}
