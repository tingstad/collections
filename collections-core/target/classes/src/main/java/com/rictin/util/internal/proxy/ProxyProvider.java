package com.rictin.util.internal.proxy;

import java.util.List;

public interface ProxyProvider<T> {

	T newProxy(final Callback<T> callback, final boolean preserveReturnedNull, Class<T> clazz, T identity, List<Class<?>> interfaces);

}
