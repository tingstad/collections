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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rictin.util.Pipe;
import com.rictin.util.internal.proxy.Callback;
import com.rictin.util.internal.proxy.Invocation;

public abstract class PipeParent<T> implements Iterable<T> {

	static Map<String, PipeParent> pipes = new ConcurrentHashMap<String, PipeParent>();

	private static ThreadLocal<List<PipeInvocation>> pipeInvocations = new ThreadLocal<List<PipeInvocation>>();

	private T proxy;
	private Iterable<T> source;
	private Iterable<T> stream;
	private static ThreadLocal<List<Invocation>> invocations = new ThreadLocal<List<Invocation>>();
	Class<T> itemClass;

	protected PipeParent() { }

	protected PipeParent(PipeParent<T> inputPipe) {
		proxy = inputPipe.proxy;
		stream = inputPipe.stream;
	}

	protected PipeParent(Iterable<T> input) {
		init(input);
	}

	protected void init(Iterable<T> input) {
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
		stream = adapter;
	}

	public T item() {
		return proxy;
	}

	public Iterator<T> iterator() {
		return stream.iterator();
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
	private void cleanUp() {
		invocations.get().clear();
		pipes.remove(getPipeId(source));
		pipes.remove(getPipeId(this));
	}

	protected List<T> doToList() {
		List<T> list = new ArrayList<T>();
		for (T element : this)
			list.add(element);
		cleanUp();
		return list;
	}

	protected <U> Pipe<U> doMapTo(U item) {
		return new PipeMap<T, U>(this, item);
	}

}

class PipeInvocation<T> {
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