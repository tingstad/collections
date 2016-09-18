/* Copyright 2016 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util.internal.proxy;


public class ProxyHolder<T> {

	private final T proxy;
	private final boolean fakeProxy;

	public ProxyHolder(final T proxy, final boolean fakeProxy) {
		this.proxy = proxy;
		this.fakeProxy = fakeProxy;
	}

	public T getProxy() {
		return proxy;
	}

	/**
	 * @return Whether proxy is real proxy or just a mock value for final class (e.g. empty string).
	 */
	public boolean isFakeProxy() {
		return fakeProxy;
	}

}
