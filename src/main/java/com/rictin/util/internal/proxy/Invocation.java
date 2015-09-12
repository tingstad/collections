/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invocation<T> {

	private Method method;
	private Object[] args;
	private Class<?> clazz;
	private Invocation<Object> transitiveInvocation;

	public Invocation(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Object invoke(T object) {
		if (object == null) {
			return null;
		}
		if (method == null) {
			return object; // identity
		}
		try {
			Object result = method.invoke(object, args);
			if (transitiveInvocation != null) {
				return transitiveInvocation.invoke(result);
			}
			return result;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public void setTransitiveInvocation(Invocation<Object> invocation) {
		transitiveInvocation = invocation;
	}
	
	public Class<?> getReturnType() {
		if (method == null) {
			return clazz;
		}
		return method.getReturnType();
	}
	
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public void setClass(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public String toString() {
		String string = clazz.toString() + "." + method.getName() + "(";
		for (int i = 0; i < args.length; i++)
			string += args[i].toString()
					+ (i < args.length - 1 ? ", " : "");
		return string + ")";
	}
	
}
