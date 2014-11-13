package com.rictin.util.internal.proxy;

public interface Callback<T> {

	Object intercept(Invocation<T> invocation);

}
