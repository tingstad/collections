package com.rictin.util.pipe;

import com.rictin.util.internal.pipe.WhereNumberImpl;
import com.rictin.util.internal.pipe.WhereStringImpl;

public abstract class Condition {
	
	protected Condition() { }

	public static WhereNumber where(Number number) {
		return new WhereNumberImpl(number);
	}

	public abstract WhereNumber and(Number number);

	public static WhereString where(String string) {
		return new WhereStringImpl(string);
	}

	public abstract WhereString and(String string);

}
