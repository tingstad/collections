/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.chain;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.rictin.util.internal.proxy.Callback;
import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.internal.proxy.ProxyFactory;

public class ChainSum<T> extends Chained<T> {

	private Chained<T> input;
	private boolean done;

	public ChainSum(Chained<T> input, ProxyFactory<T> proxyFactory) {
		super(proxyFactory);
		this.input = input;
	}

	@Override
	public boolean hasNext() {
		return !done;
	}

	@Override
	protected T getNext() {
		done = true;
		return proxyFactory.getProxy(new Callback<T>() {

			public Object intercept(Invocation<T> invocation) {
				Method method = invocation.getMethod();
				BigDecimal d = BigDecimal.valueOf(0);
				while (input.hasNext()) {
					T element = input.getNext();
					String value =  "" + invocation.invoke(element);
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
