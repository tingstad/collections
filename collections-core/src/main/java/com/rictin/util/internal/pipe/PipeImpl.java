/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rictin.util.Pipe;
import com.rictin.util.internal.proxy.Callback;
import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.Order;

public class PipeImpl<T> extends Pipe<T> {

	private final static ThreadLocal<List<Invocation>> invocations = new ThreadLocal<List<Invocation>>();
	private T proxy;
	private Iterable<T> source;

	public PipeImpl(final Iterable<T> input) {
		source = input;
		addPipe(source);
		if (invocations.get() == null) {
			invocations.set(new ArrayList<Invocation>());
		}
		PipeFromIterable<T> adapter = new PipeFromIterable<T>(input);
		proxy = adapter.getProxyFactory().getProxy(new Callback() {

			public Object intercept(Invocation invocation) {
				addInvocation(invocation);
//				pipeInvocations.get().add(new PipeInvocation<T>(pipe, invocation))
				return null;
			}
		});
	}

	protected PipeImpl(final PipeImpl<T> inputPipe) {
		proxy = inputPipe.proxy;
	}

	protected PipeImpl() { }

	public T item() {
		return proxy;
	}

	@Override
	public Pipe<T> select(final Condition condition) {
		return new PipeSelect<T>(this, condition);
	}

	@Override
	public Pipe<T> sort(final Order order) {
		return new PipeSort<T>(this, order);
	}

	@Override
	public Pipe<T> first(final int limit) {
		return new PipeLimit<T>(this, limit);
	}

	public <U> Pipe<U> mapTo(U item) {
		return new PipeMap<T, U>(this, item);
	}

	public Iterator<T> iterator() {
		return source.iterator();
	}

	protected void addPipe(Iterable<T> source) {
		pipes.put(getPipeId(source), this);
		pipes.put(getPipeId(this), this);
	}

	/**
	 * Call from terminal methods.
	 */
	protected void cleanUp() {
		invocations.get().clear();
		pipes.remove(getPipeId(source));
		pipes.remove(getPipeId(this));
	}

	static void addInvocation(Invocation invocation) {
		invocations.get().add(invocation);
	}

	static Invocation takeLastInvocation() {
		final List<Invocation> list = invocations.get();
		return list.isEmpty() ? null : list.remove(invocations.get().size() - 1);
	}

}
