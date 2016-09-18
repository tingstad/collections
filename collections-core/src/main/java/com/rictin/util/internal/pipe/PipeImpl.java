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
import com.rictin.util.internal.proxy.ProxyHolder;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.Order;

public class PipeImpl<T> extends Pipe<T> {

	protected final static ThreadLocal<List<PipeInvocation>> invocations = new ThreadLocal<List<PipeInvocation>>();
	private T proxy;
	private Iterable<T> source;
	private ProxyHolder<T> proxyHolder;
	protected PipeInvocation last;

	public PipeImpl(final Iterable<T> input) {
		source = input;
		addPipe(source);
		invocations.set(new ArrayList<PipeInvocation>());
		PipeFromIterable<T> adapter = new PipeFromIterable<T>(input);
		final Pipe pipe = this;
		proxyHolder = adapter.getProxyFactory().getProxyHolder(new Callback<T>() {

			public Object intercept(Invocation<T> invocation) {
				addInvocation(invocation);
				return null;
			}
		});
		proxy = proxyHolder.getProxy();
	}

	protected PipeImpl(final PipeImpl<T> inputPipe) {
		proxy = inputPipe.proxy;
	}

	protected PipeImpl() { }

	public T item() {
		last = new PipeInvocation(this);
		invocations.get().add(last);

		if (proxyHolder.isFakeProxy()) {
			// See intercept method further up
			addInvocation(new Invocation<T>(proxyHolder.getProxy().getClass()));
		}
		
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

	void addInvocation(Invocation invocation) {
		last.addInvocation(invocation);
	}

	static Invocation takeLastInvocation() {
		final List<PipeInvocation> list = invocations.get();
		if (list.isEmpty())
			return null;
		final PipeInvocation pipeInvocation = list.remove(list.size() - 1);
		if (pipeInvocation.getInvocation() != null) {
			return pipeInvocation.getInvocation();
		}
		return new Invocation(
				pipeInvocation.getPipe().proxyHolder.getProxy().getClass());
	}

}

class PipeInvocation {
	private Pipe pipe;
	private Invocation invocation;
	PipeInvocation(Pipe pipe) {
		this.pipe = pipe;
	}
	void addInvocation(Invocation invocation) {
		this.invocation = invocation;
	}
	Invocation getInvocation() {
		return invocation;
	}
	PipeImpl getPipe() {
		return (PipeImpl)pipe;
	}
}
