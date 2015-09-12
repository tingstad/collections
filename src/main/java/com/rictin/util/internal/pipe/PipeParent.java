/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rictin.util.Pipe;
import com.rictin.util.internal.proxy.Callback;
import com.rictin.util.internal.proxy.Invocation;

public abstract class PipeParent<T> implements Iterable<T> {

	private static final Map<String, PipeParent> pipes = new ConcurrentHashMap<String, PipeParent>();

	private static final ThreadLocal<List<PipeInvocation>> pipeInvocations = new ThreadLocal<List<PipeInvocation>>();

	private T proxy;
	private Iterable<T> source;
	private static final ThreadLocal<List<Invocation>> invocations = new ThreadLocal<List<Invocation>>();
	private Class<T> itemClass; //TODO: Will we need this?

	protected PipeParent() { }

	protected PipeParent(final PipeParent<T> inputPipe) {
		init(inputPipe);
	}

	protected PipeParent(final Iterable<T> input) {
		init(input);
	}

	protected void init(final PipeParent<T> inputPipe) {
		proxy = inputPipe.proxy;
	}

	protected void init(final Iterable<T> input) {
		pipeInvocations.set(new ArrayList<PipeInvocation>());
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
		itemClass = (Class<T>) proxy.getClass();
	}

	public T item() {
		return proxy;
	}

	public static <T> T item(Iterable<T> source) {
		String key = getPipeId(source);
		if (!pipes.containsKey(key)) {
			throw new IllegalArgumentException("No pipe exists for source " + source);
		}
		PipeParent<T> pipe = pipes.get(key);
		return pipe.item();
	}

	static void addInvocation(Invocation invocation) {
		invocations.get().add(invocation);
	}

	static Invocation takeNextInvocation() {
		final List<Invocation> list = invocations.get();
		return list.isEmpty() ? null : list.remove(0);
	}

	static Invocation takeLastInvocation() {
		final List<Invocation> list = invocations.get();
		return list.isEmpty() ? null : list.remove(invocations.get().size() - 1);
	}

	protected void addPipe(Iterable<T> source) {
		pipes.put(getPipeId(source), this);
		pipes.put(getPipeId(this), this);
	}

	private static <T> String getPipeId(Iterable<T> source) {
		return System.identityHashCode(source) + "-" + Thread.currentThread().getId();
	}

	/**
	 * Call from terminal methods.
	 */
	protected void cleanUp() {
		invocations.get().clear();
		pipes.remove(getPipeId(source));
		pipes.remove(getPipeId(this));
	}


}

class PipeInvocation<T> { //TODO: Will this be useful?
	private final Pipe<T> pipe;
	private final Invocation<T> invocation;
	PipeInvocation(Pipe<T> pipe, Invocation<T> invocation) {
		this.pipe = pipe;
		this.invocation = invocation;
	}
	public Pipe<T> getPipe() {
		return pipe;
	}
	public Invocation<T> getInvocation() {
		return invocation;
	}
}