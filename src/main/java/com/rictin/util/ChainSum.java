/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

class ChainSum<T> extends Chained<T> {

	private Chained<T> input;
	private boolean done;

	public ChainSum(Chained<T> input, ProxyFactory<T> proxyFactory) {
		super(proxyFactory);
		this.input = input;
	}

	@Override
	boolean hasNext() {
		return !done;
	}

	@Override
	T next() {
		done = true;
		return proxyFactory.newProxy(new MethodInterceptor() {
			
			public Object intercept(Object arg0, Method arg1, Object[] arg2,
					MethodProxy arg3) throws Throwable {
				method = arg1;
				BigDecimal d = BigDecimal.valueOf(0);
				while (input.hasNext()) {
					T element = input.next();
					String value =  "" + getValueOfElement(element);
					if (!"null".equals(value)) {
						d = d.add(new BigDecimal(value));
					}
				}
				Class<?> returnType = method.getReturnType();
				if (BigDecimal.class.equals(returnType)) {
					return d;
				}
				if (BigInteger.class.equals(returnType)) {
					return d.toBigInteger();
				}
				if (Double.TYPE.equals(returnType) || Double.class.equals(returnType)) {
					return d.doubleValue();
				}
				if (Float.TYPE.equals(returnType) || Float.class.equals(returnType)) {
					return d.floatValue();
				}
				if (Integer.TYPE.equals(returnType) || Integer.class.equals(returnType)) {
					return d.intValue();
				}
				return d.longValue();
			}
		});
	}

}
