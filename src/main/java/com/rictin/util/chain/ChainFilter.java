/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.chain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import com.rictin.util.proxy.Callback;
import com.rictin.util.proxy.Invocation;
import com.rictin.util.proxy.ProxyFactory;

public class ChainFilter<T> extends Chained<T> {

	private static enum Operation {
		EQUALS,
		NOT_EQUALS,
		GREATER_THAN,
		GREATER_THAN_OR_EQUAL,
		LESS_THAN,
		LESS_THAN_OR_EQUAL,
		DUPLICATE
	};

	private boolean accept;
	private Operation operation;
	private Object testValue;
	private Number number;
	private T proxy;
	private Method method;
	private Object[] args;

	private Chained<T> input;
	private Boolean hasNext;
	private T element;

	public ChainFilter(Chained<T> input, ProxyFactory<T> proxyFactory) {
		this.input = input;
		this.proxy = proxyFactory.getProxy(new Callback<T>() {

			public Object intercept(Invocation<T> invocation) {
				method = invocation.getMethod();
				args = invocation.getArgs();
				return null;
			}
			
		});
	}

	public T accept(Object value) {
		accept = true;
		operation = Operation.EQUALS;
		testValue = value;
		return proxy;
	}

	public T acceptNot(Object value) {
		accept = true;
		operation = Operation.NOT_EQUALS;
		testValue = value;
		return proxy;
	}

	public T acceptGreaterThan(Number value) {
		accept = true;
		operation = Operation.GREATER_THAN;
		number = value;
		return proxy;
	}

	public T acceptGreaterThanOrEqual(Number value) {
		accept = true;
		operation = Operation.GREATER_THAN_OR_EQUAL;
		number = value;
		return proxy;
	}

	public T acceptLessThan(Number value) {
		accept = true;
		operation = Operation.LESS_THAN;
		number = value;
		return proxy;
	}

	public T acceptLessThanOrEqual(Number value) {
		accept = true;
		operation = Operation.LESS_THAN_OR_EQUAL;
		number = value;
		return proxy;
	}

	public T removeDuplicates() {
		accept = false;
		operation = Operation.DUPLICATE;
		return proxy;
	}

	public T reject(Object value) {
		accept = false;
		operation = Operation.EQUALS;
		testValue = value;
		return proxy;
	}

	public T rejectNot(Object value) {
		accept = false;
		operation = Operation.NOT_EQUALS;
		testValue = value;
		return proxy;
	}

	public T rejectGreaterThan(Number value) {
		accept = false;
		operation = Operation.GREATER_THAN;
		number = value;
		return proxy;
	}

	public T rejectGreaterThanOrEqual(Number value) {
		accept = false;
		operation = Operation.GREATER_THAN_OR_EQUAL;
		number = value;
		return proxy;
	}

	public T rejectLessThan(Number value) {
		accept = false;
		operation = Operation.LESS_THAN;
		number = value;
		return proxy;
	}

	public T rejectLessThanOrEqual(Number value) {
		accept = false;
		operation = Operation.LESS_THAN_OR_EQUAL;
		number = value;
		return proxy;
	}

	private boolean isAccepted(T element) {
		Object value = null;
		if (element != null) {
			try {
				value = method.invoke(element, args);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		Set<Object> exists = new HashSet<Object>();
		boolean test = false;
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
		}
		return accept == test;
	}

	public boolean hasNext() {
		if (hasNext == null) {
			prepareNext();
		}
		return hasNext;
	}

	public T next() {
		if (!hasNext()) {
			throw new IllegalStateException("No more elements.");
		}
		hasNext = null;
		return element;
	}

	private void prepareNext() {
		while (input.hasNext()) {
			T in = input.next();
			if (isAccepted(in)) {
				element = in;
				hasNext = true;
				return;
			}
		}
		hasNext = false;
	}

}
