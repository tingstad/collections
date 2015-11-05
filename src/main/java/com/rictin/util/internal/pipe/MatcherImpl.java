package com.rictin.util.internal.pipe;

import com.rictin.util.internal.proxy.Invocation;

public class MatcherImpl<T> {

	private final T value;
	private final Invocation invocation;
	
	protected MatcherImpl(T value) {
		this.value = value;
		this.invocation = PipeParent.takeLastInvocation();
	}
	
	T getValue() {
		return value;
	}

	Invocation getInvocation() {
		return invocation;
	}

}
