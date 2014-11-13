package com.rictin.util.internal.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invocation<T>{

	private Method method;
	private Object[] args;
	private Class<?> clazz;

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
			return method.invoke(object, args);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
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
	
}
