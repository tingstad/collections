/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyFactory<T> implements ProxyProvider<T> {

	private List<Class<?>> interfaces = new ArrayList<Class<?>>();

	private Class<T> clazz;
	private Class<?>[] argumentTypes;
	private T identity;
	
	public ProxyFactory(T element) {
		List<T> list = new ArrayList<T>(1);
		list.add(element);
		load(list);
	}

	public ProxyFactory(Collection<T> collection) {
		load(collection);
	}

	public ProxyFactory(Class<T> c) {
		interfaces = new ArrayList<Class<?>>();
		clazz = c;
		identity = createIdentityProxy(clazz);
		argumentTypes = getArgumentTypesOfShortestConstructor(clazz);
	}

	private final void load(Collection<T> collection) {
		Class<T> c = (Class<T>) getClassOfElements(collection, interfaces);
		load(c);
	}
	
	private final void load(Class<T> c) {
		clazz = c;
		identity = createIdentityProxy(clazz);
		argumentTypes = getArgumentTypesOfShortestConstructor(clazz);
	}

	public T getProxyWithNulls(final Callback<T> callback) {
		return getProxy(callback, true);
	}

	public T getProxy(final Callback<T> callback) {
		return getProxy(callback, false);
	}
	
	private T getProxy(final Callback<T> callback, final boolean preserveReturnedNull) {
		if (Modifier.isFinal(clazz.getModifiers())) {
			T returnValue = (T) callback.intercept(new Invocation<T>(clazz));
			return (T) (preserveReturnedNull || returnValue != null ? returnValue : identity);
		}
		T proxy = callNewProxy(callback, preserveReturnedNull);
		return proxy;
	}

	private ProxyProvider<T> getProxyProvider() {
		ProxyProvider<T> proxyProvider = null;
		Class<?> c = null;
		try {
			c = Class.forName("com.rictin.util.internal.proxy.ProxyProviderImpl");
		} catch (ClassNotFoundException e) {
			proxyProvider = this;
		}
		if (proxyProvider == null) {
			try {
				proxyProvider = (ProxyProvider<T>) c.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return proxyProvider;
	}

	private T callNewProxy(final Callback<T> callback, final boolean preserveReturnedNull) {
		ProxyProvider<T> proxyProvider = getProxyProvider();
		Interceptor interceptor = new Interceptor() {
			
			public Object intercept(Class clazz, Method method, Object[] args) {
				Invocation<T> invocation = new Invocation<T>(clazz);
				invocation.setMethod(method);
				invocation.setArgs(args);
				Object a = transitiveInterception(invocation);
				Object b = callback.intercept(invocation);
				return b == null && !preserveReturnedNull ? a : b;
			}
		};
		return (T) proxyProvider.newProxy(clazz, identity, interfaces, interceptor);
	}

	public T newProxy(final Class<T> clazz, T identity, List<Class<?>> interfaces, final Interceptor interceptor) {
		Object prox = Proxy.newProxyInstance(clazz.getClassLoader(),
		        clazz.isInterface() ? new Class[]{ clazz } : clazz.getInterfaces(),
		        new InvocationHandler() {
					
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						return interceptor.intercept(clazz, method, args);
					}
				});
		return (T) prox;
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

	/**
	 * Final classes cannot be proxied (sub classed), but we support certain
	 * common classes by assuming identity functions.
	 * 
	 * @param c
	 * @return Object to return or null if real proxy should be used
	 */
	@SuppressWarnings("unchecked")
	private static <T> T createIdentityProxy(final Class<?> c) {
		if (String.class.equals(c)) {
			return (T) "";
		} else if (Integer.class.equals(c)) {
			return (T) Integer.valueOf(0);
		} else if (Long.class.equals(c)) {
			return (T) Long.valueOf(0);
		} else if (Double.class.equals(c)) {
			return (T) Double.valueOf(0);
		} else if (c.isPrimitive()) {
			return (T) Integer.valueOf(0);
		}
		return null;
	}

	private static <T> Class<?>[] getArgumentTypesOfShortestConstructor(
			Class<T> klass) {
		Class<?>[] argumentTypes = new Class[0];
		Constructor<?>[] constructors = klass.getConstructors();
		int min = -1;
		for (Constructor<?> constructor : constructors) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			if (min == -1 || parameterTypes.length < min) {
				min = parameterTypes.length;
				argumentTypes = parameterTypes;
			}
		}
		return argumentTypes;
	}

	private static <T> Object[] getArguments(Class<?>[] argumentTypes) {
		Object[] arguments = new Object[argumentTypes.length];
		for (int i = argumentTypes.length - 1; i >= 0; i--) {
			try {
				Class<?> argumentType = argumentTypes[i];
				arguments[i] = newInstance(argumentType);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return arguments;
	}

	private static <T> Object newInstance(final Class<?> klass) {
		if (klass.isPrimitive()) {
			if (Boolean.TYPE.equals(klass)) {
				return false;
			} else if (Float.TYPE.equals(klass)) {
				return 0f;
			} else if (Double.TYPE.equals(klass)) {
				return 0d;
			} else if (Character.TYPE.equals(klass)) {
				return (char) 0;
			} else if (Byte.TYPE.equals(klass)) {
				return (byte) 0;
			} else if (Long.TYPE.equals(klass)) {
				return 0L;
			} else if (Short.TYPE.equals(klass)) {
				return (short) 0;
			} else {
				return 0;
			}
		}
		return createIdentityProxy(klass);
	}

	/**
	 * Returns the Class that all elements are or extend, prioritizing parents
	 * over grand parents. Example: If all elements are Integer, return Integer.
	 * Else if all elements are Number, return Number. Else return Object.
	 * 
	 * @param interfaces
	 *            Returns the interfaces that all non-null elements implement
	 * @return Class of hopefully all elements
	 */
	private static <T> Class<? extends Object> getClassOfElements(
			final Collection<T> list,
			final List<Class<?>> interfaces) {
		Map<String, Integer> interfaceMap = new HashMap<String, Integer>();
		Map<String, CountAndDepth> classMap = new HashMap<String, CountAndDepth>();
		Integer count = 0;
		for (T element : list) {
			if (element == null) {
				continue;
			}
			count++;
			Class<? extends Object> klass = element.getClass();
			int depth = 0;
			do {
				addClass(classMap, klass.getName(), depth++);
				addInterfaces(interfaceMap, klass);
				klass = klass.getSuperclass();
			} while (klass != null);
		}
		for (String key : interfaceMap.keySet()) {
			if (count.equals(interfaceMap.get(key))) {
				try {
					interfaces.add(Class.forName(key));
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		}
		long bestCount = 0;
		long bestDepth = 0;
		String klass = null;
		for (String key : classMap.keySet()) {
			CountAndDepth value = classMap.get(key);
			if (klass == null || value.count > bestCount
					|| value.count == bestCount && value.depth < bestDepth) {
				klass = key;
				bestCount = value.count;
				bestDepth = value.depth;
			}
		}
		try {
			return Class.forName(klass);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Increments map.get(klass)'s count and depth
	 */
	private static void addClass(Map<String, CountAndDepth> map, String klass,
			int depth) {
		CountAndDepth countAndDepth = map.get(klass);
		if (countAndDepth == null) {
			countAndDepth = new CountAndDepth();
			map.put(klass, countAndDepth);
		}
		countAndDepth.count += 1;
		countAndDepth.depth += depth;
	}

	/**
	 * map.get(iclass)++ for iclass and all its super classes
	 */
	private static void addInterfaces(Map<String, Integer> map,
			Class<? extends Object> klass) {
		for (Class<?> iclass : klass.getInterfaces()) {
			addInterface(map, iclass.getName());
			addInterfaces(map, iclass);
		}
	}

	/**
	 * map.get(klass)++
	 */
	private static void addInterface(Map<String, Integer> map, String klass) {
		Integer i = map.get(klass);
		if (i == null) {
			i = Integer.valueOf(0);
		}
		map.put(klass, i + 1);
	}

}

class CountAndDepth {
	public long count = 0;
	public long depth = 0;
}
