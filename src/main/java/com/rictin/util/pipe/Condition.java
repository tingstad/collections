package com.rictin.util.pipe;

import com.rictin.util.internal.pipe.WhereNumberImpl;

public abstract class Condition {
	
	protected Condition() { }

	public static WhereNumber where(Number number) {
		return new WhereNumberImpl(number);
	}

	public abstract WhereNumber and(Number number);

}
