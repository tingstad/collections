/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public class ProxyProviderImpl<T> implements ProxyProvider<T> {

	public T newProxy(final Class<T> clazz, T identity, List<Class<?>> interfaces, final Interceptor inInterceptor) {
		Enhancer enhancer = new Enhancer();
		if (!Modifier.isPrivate(clazz.getModifiers()) && !Modifier.isFinal(clazz.getModifiers()))
			enhancer.setSuperclass(clazz);
		List<Class<?>> interfaceList = new ArrayList<Class<?>>(interfaces);
		if (clazz.isInterface() && !interfaceList.contains(clazz))
			interfaceList.add(clazz);
		enhancer.setInterfaces(interfaceList.toArray(new Class[0]));
		MethodInterceptor interceptor = new MethodInterceptor() {

			public Object intercept(Object arg0, Method method, Object[] args,
					MethodProxy arg3) throws Throwable {
				return inInterceptor.intercept(clazz, method, args);
			}
		};
		enhancer.setCallbackType(interceptor.getClass());
		Class<T> proxiedClass = enhancer.createClass();
		Enhancer.registerCallbacks(proxiedClass, new net.sf.cglib.proxy.Callback[]{ interceptor });
		Objenesis objenesis = new ObjenesisStd();
		T proxy = objenesis.newInstance(proxiedClass);
		return proxy;
	}

}
