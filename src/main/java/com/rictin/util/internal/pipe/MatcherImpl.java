package com.rictin.util.internal.pipe;


public class MatcherImpl<T> {

	private final T value;
	
	protected MatcherImpl(T value) {
		this.value = value;
	}
	
	T getValue() {
		return value;
	}

}
