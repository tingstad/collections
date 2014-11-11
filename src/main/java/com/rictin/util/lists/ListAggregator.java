/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.lists;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;


public class ListAggregator<T> extends ListHandler<T> {

	private boolean average;

	public ListAggregator(Collection<T> list) {
		super(list);
	}

	public T sum() {
		return createProxy();
	}

	public T average() {
		average = true;
		return createProxy();
	}

	@Override
	protected Object handleList() {
		long count = 0;
		BigDecimal d = BigDecimal.valueOf(0);
		for (T element : list) {
			String value =  "" + proxyFactory.invoke(element);
			if (!"null".equals(value)) {
				count++;
				d = d.add(new BigDecimal(value));
			}
		}
		if (average) {
			d = d.divide(new BigDecimal(count), d.scale() + 2,
					BigDecimal.ROUND_HALF_UP);
		}
		Class<?> returnType = proxyFactory.getReturnType();
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

}
