/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public class ProxyProviderImpl<T> implements ProxyProvider<T> {

	public T newProxy(final Callback<T> callback, final boolean preserveReturnedNull, final Class<T> clazz, T identity, List<Class<?>> interfaces) {
		if (identity != null) {
			return identity;
		}
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		List<Class<?>> interfaceList = new ArrayList<Class<?>>(interfaces);
		if (clazz.isInterface() && !interfaceList.contains(clazz))
			interfaceList.add(clazz);
		enhancer.setInterfaces(interfaceList.toArray(new Class[0]));
		MethodInterceptor interceptor = new MethodInterceptor() {

			public Object intercept(Object arg0, Method arg1, Object[] arg2,
					MethodProxy arg3) throws Throwable {
				Invocation<T> invocation = new Invocation<T>(clazz);
				invocation.setMethod(arg1);
				invocation.setArgs(arg2);
				Object a = transitiveInterception(invocation);
				Object b = callback.intercept(invocation);
				return b == null && !preserveReturnedNull ? a : b;
			}
		};
		enhancer.setCallbackType(interceptor.getClass());
		Class<T> proxiedClass = enhancer.createClass();
		Enhancer.registerCallbacks(proxiedClass, new net.sf.cglib.proxy.Callback[]{ interceptor });
		Objenesis objenesis = new ObjenesisStd();
		T proxy = objenesis.newInstance(proxiedClass);
		return proxy;
	}

	private Object transitiveInterception(final Invocation<T> invocation) {

		Class returnType = invocation.getReturnType();
		final ProxyFactory subFactory = new ProxyFactory(returnType);
		Object subProxy = subFactory.getProxy(new Callback() {
			public Object intercept(Invocation subInvocation) {
				invocation.setTransitiveInvocation(subInvocation);
				if (subFactory.identity != null)
					return null;
				return transitiveInterception(subInvocation);
			}
		});
		
		return subProxy;
	}

}

class CountAndDepth {
	public long count = 0;
	public long depth = 0;
}
