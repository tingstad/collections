/* Copyright 2014 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.pipe;

import java.util.List;

import com.rictin.util.internal.condition.Condition;
import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.pipe.PipeAfterWhere;
import com.rictin.util.pipe.WhereNumber;

public class WhereNumberImpl<T> implements WhereNumber<T> {

	private final PipeParent<T> origin;
	private final List<Invocation<T>> invocations;
	private final Invocation<T> firstArg;
	private final Number firstNumber;
	
	public WhereNumberImpl(PipeParent<T> origin, Number number) {
		this.firstNumber = number;
		this.origin = origin;
		this.invocations = origin.invocations;
		this.firstArg = invocations.isEmpty() ? null : invocations.remove(0);
	}

	public PipeAfterWhere<T> isGreaterThan(final Number number) {
		final Invocation<T> secondArg = invocations.isEmpty() ? null : invocations.remove(0);
		return new PipeAfterWhereImpl<T>(origin, new Condition<T>() {

			public boolean accept(T element) {
				Number first = firstArg == null ? firstNumber : (Number)firstArg.invoke(element);
				Number second = secondArg == null ? number : (Number)secondArg.invoke(element);
				return first.doubleValue() > second.doubleValue();
			}
		});
	}

	public PipeAfterWhere<T> isLessThan(final Number number) {
		final Invocation<T> secondArg = invocations.isEmpty() ? null : invocations.remove(0);
		Condition<T> condition = new Condition<T>() {

			public boolean accept(T element) {
				Number first = firstArg == null ? firstNumber : (Number)firstArg.invoke(element);
				Number second = secondArg == null ? number : (Number)secondArg.invoke(element);
				return first.doubleValue() < second.doubleValue();
			}
		};
		return new PipeAfterWhereImpl<T>(origin, condition);
	}

}
