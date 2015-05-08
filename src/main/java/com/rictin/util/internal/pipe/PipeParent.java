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

	private static Map<String, PipeParent> pipes = new ConcurrentHashMap<String, PipeParent>();

	private T proxy;
	private Iterable<T> source;
	private Iterable<T> stream;
	protected List<Invocation<T>> invocations = new ArrayList<Invocation<T>>();

	protected PipeParent() { }

	protected PipeParent(PipeParent<T> input) {
		invocations = input.invocations;
		proxy = input.proxy;
		stream = input.stream;
	}

	protected PipeParent(Iterable<T> input) {
		init(input);
	}

	protected void init(Iterable<T> input) {
		source = input;
		addPipe(source);
		PipeFromIterable<T> adapter = new PipeFromIterable<T>(input);
		proxy = adapter.getProxyFactory().getProxy(new Callback() {

			public Object intercept(Invocation invocation) {
				invocations.add(invocation);
				
				return null;
			}
		});
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
		pipes.remove(getPipeId(source));
		pipes.remove(getPipeId(this));
	}

	protected List<T> doToList() {
		cleanUp();
		List<T> list = new ArrayList<T>();
		for (T element : this)
			list.add(element);
		return list;
	}

	protected <U> Pipe<U> doMapTo(U item) {
		return Pipe.from(new PipeMap<T, U>(this, item));
	}

}
