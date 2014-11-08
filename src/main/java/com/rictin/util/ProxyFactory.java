/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class ProxyFactory<T> {

	List<Class<?>> interfaces;
	Class<T> clazz;
	Class<?>[] argumentTypes;
	T identity;

	public static <T> T createProxy(T element, MethodInterceptor interceptor) {
		List<T> list = new ArrayList<T>(1);
		list.add(element);
		return createProxy(list, interceptor);
	}

	public static <T> T createProxy(Collection<T> collection, 
			MethodInterceptor interceptor) {
		return new ProxyFactory<T>(collection).newProxy(interceptor);
	}

	public ProxyFactory(T element) {
		List<T> list = new ArrayList<T>(1);
		list.add(element);
		load(list);
	}

	public ProxyFactory(Collection<T> collection) {
		load(collection);
	}

	private final void load(Collection<T> collection) {
		interfaces = new ArrayList<Class<?>>();
		clazz = (Class<T>) getClassOfElements(collection, interfaces);
		identity = createIdentityProxy(clazz);
		argumentTypes = getArgumentTypesOfShortestConstructor(clazz);
	}
	
	public T newProxy(MethodInterceptor interceptor) {
		if (identity != null) {
			return identity;
		}
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setInterfaces(interfaces.toArray(new Class[0]));
		enhancer.setCallback(interceptor);
		Object proxy = enhancer.create(argumentTypes,
				getArguments(argumentTypes));
		return (T) proxy;
	}
	
/*	protected Object getValueOfElement(T element) {
		if (method == null) {
			return element; // identity function
		}
		try {
			return element == null ? null : method.invoke(element);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}*/

	/**
	 * Final classes cannot be proxied (sub classed), but we support certain
	 * common classes by assuming identity functions.
	 * 
	 * @param c
	 * @return Object to return or null if real proxy should be used
	 */
	@SuppressWarnings("unchecked")
	private static <T> T createIdentityProxy(Class<?> c) {
		T identity = null;
		if (String.class.equals(c)) {
			identity = (T) "";
		} else if (Integer.class.equals(c)) {
			identity = (T) Integer.valueOf(0);
		} else if (Long.class.equals(c)) {
			identity = (T) Long.valueOf(0);
		} else if (Double.class.equals(c)) {
			identity = (T) Double.valueOf(0);
		} else {
			return null;
		}
		return identity;
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

	private static <T> Object newInstance(Class<?> klass) {
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
				return 0l;
			} else if (Short.TYPE.equals(klass)) {
				return (short) 0;
			} else {
				return 0;
			}
		}
		return null;
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
