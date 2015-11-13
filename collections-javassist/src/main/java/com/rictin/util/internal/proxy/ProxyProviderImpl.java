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
import java.util.Iterator;
import java.util.List;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import org.objenesis.ObjenesisStd;

public class ProxyProviderImpl<T> implements ProxyProvider<T> {

	public T newProxy(final Class<T> clazz, T identity, List<Class<?>> interfaces, final Interceptor interceptor) {
		if (identity != null) {
			return identity;
		}
		ProxyFactory factory = new ProxyFactory();

		List<Class<?>> interfaceList = new ArrayList<Class<?>>(interfaces);
		Iterator<Class<?>> iterator = interfaceList.iterator();
		while (iterator.hasNext()) {
			Class<?> c = iterator.next();
			if (!Modifier.isPublic(c.getModifiers())) {
				System.out.println(c);
				iterator.remove();
			}
		}
		if (clazz.isInterface()) {
			if (!interfaceList.contains(clazz))
				interfaceList.add(clazz);
			factory.setInterfaces(interfaceList.toArray(new Class[0]));
		} else {
			factory.setSuperclass(clazz);
			factory.setInterfaces(interfaceList.toArray(new Class[0]));
		}
		MethodHandler methodHandler = new MethodHandler() {
			
			public Object invoke(Object arg0, Method method, Method arg2, Object[] args)
					throws Throwable {
				return interceptor.intercept(clazz, method, args);
			}
		};
		Class<T> proxiedClass = factory.createClass();
		T proxy = new ObjenesisStd().newInstance(proxiedClass);
		((ProxyObject) proxy).setHandler(methodHandler);
		return proxy;
	}

}
