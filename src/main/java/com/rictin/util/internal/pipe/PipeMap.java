/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.rictin.util.Pipe;
import com.rictin.util.internal.proxy.Callback;
import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.internal.proxy.ProxyFactory;
import com.rictin.util.pipe.Condition;

public class PipeMap<T, U> extends Pipe<U> implements Iterator<U>, Iterable<U> {

	private Iterator<T> input;
	private Invocation<T> invocation;
	private Boolean hasNext;
	private U element;
	private Class<U> itemClass;

	private U proxy;
	
	public PipeMap(PipeParent<T> input, U item) {
		this.input = input.iterator();
		itemClass = (Class<U>) item.getClass();
		invocation = PipeParent.takeLastInvocation();
	}

	public Iterator<U> iterator() {
		return this;
	}

	public boolean hasNext() {
		if (hasNext == null) {
			prepareNext();
		}
		return hasNext;
	}

	public U next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		hasNext = null;
		return element;
	}

	private void prepareNext() {
		while (input.hasNext()) {
			T in = input.next();
			U out = (U) invocation.invoke(in);
			element = out;
			hasNext = true;
			return;
		}
		hasNext = false;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public U item() {
		if (proxy == null) {
			ProxyFactory<U> proxyFactory = new ProxyFactory<U>(itemClass);
			proxy = proxyFactory.getProxy(new Callback<U>() {

				public Object intercept(Invocation<U> invocation) {
					PipeParent.addInvocation(invocation);
					return null;
				}
			});
		}
		return proxy;
	}
	
	public PipeAfterWhereImpl<U> select(Condition condition) {
		final ConditionImpl conditionImpl = (ConditionImpl) condition;
		Predicate<T> predicate = new Predicate<T>() {

			public boolean accept(T element) {
				for (Predicate<T> predicate : conditionImpl.getPredicates())
					if (!predicate.accept(element))
						return false;
				return true;
			}
		};
		return new PipeAfterWhereImpl(this, predicate);
	}

	public <V> Pipe<V> mapTo(V item) {
		return super.doMapTo(item);
	}

	public Pipe<U> sortBy(Object... item) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<U> toList() {
		return super.doToList();
	}

	public <V> Map<V, List<U>> groupBy(V item) {
		return null;
	}
}
