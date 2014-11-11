package com.rictin.util.proxy;

public interface Callback<T> {

	Object intercept(Invocation<T> invocation);

}
