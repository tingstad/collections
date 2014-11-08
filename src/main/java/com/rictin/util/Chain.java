/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.rictin.util.chain.ChainFilter;
import com.rictin.util.chain.ChainFromIterator;
import com.rictin.util.chain.ChainLimit;
import com.rictin.util.chain.ChainSorter;
import com.rictin.util.chain.ChainSum;
import com.rictin.util.chain.Chained;

public class Chain<T> implements Iterator<T> {

	private ProxyFactory<T> proxyFactory;
	private List<Chained<T>> operations = new ArrayList<Chained<T>>();

	private Chain(Iterator<T> input) {
		ChainFromIterator<T> initial = addInitial(input);
		proxyFactory = initial.getProxyFactory();
	}

	private Chain(Collection<T> collection) {
		addInitial(collection.iterator());
		proxyFactory = new ProxyFactory<T>(collection);
	}
	
	private final ChainFromIterator<T> addInitial(Iterator<T> input) {
		ChainFromIterator<T> initial = new ChainFromIterator<T>(input);
		operations.add(initial);
		return initial;
	}

	public static <T> Chain<T> from(Collection<T> collection) {
		return new Chain<T>(collection);
	}

	public static <T> Chain<T> from(Iterator<T> input) {
		return new Chain<T>(input);
	}

	public ChainFilter<T> filter() {
		return add(new ChainFilter<T>(getLastOperation(), proxyFactory));
	}

	/**
	 * Not lazy!
	 * 
	 * @return
	 */
	public ChainSorter<T> sort() {
		return add(new ChainSorter<T>(getLastOperation(), proxyFactory));
	}

	public void limit(int n) {
		add(new ChainLimit<T>(getLastOperation(), n));
	}

	/**
	 * Terminal.
	 * @return
	 */
	public T sum() {
		add(new ChainSum<T>(getLastOperation(), proxyFactory));
		return next();
	}

	private <O extends Chained<T>> O add(O operation) {
		operations.add(operation);
		return operation;
	}

	/**
	 * Terminal.
	 * @return
	 */
	public int count() {
		int c = 0;
		while (hasNext()) {
			next();
			c++;
		}
		return c;
	}

	/**
	 * Terminal.
	 * @return List
	 */
	public List<T> toList() {
		List<T> list = new ArrayList<T>();
		while (hasNext()) {
			list.add(next());
		}
		return list;
	}

	private Chained<T> getLastOperation() {
		return operations.get(operations.size() - 1);
	}
	
	public boolean hasNext() {
		return getLastOperation().hasNext();
	}

	public T next() {
		return getLastOperation().next();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
