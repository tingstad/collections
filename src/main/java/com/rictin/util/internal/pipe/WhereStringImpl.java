package com.rictin.util.internal.pipe;

import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.WhereString;

public class WhereStringImpl implements WhereString {

	private final Invocation firstArg;
	private final String firstNumber;
	private ConditionImpl condition;
	
	public WhereStringImpl(String string) {
		this.firstNumber = string;
		this.firstArg = PipeParent.takeNextInvocation();
	}

	public WhereStringImpl(ConditionImpl condition, String string) {
		this(string);
		this.condition = condition;
	}

	public Condition startsWith(final String string) {
		final Invocation secondArg = PipeParent.takeNextInvocation();
		Predicate<Object> predicate = new Predicate<Object>() {

			public boolean accept(Object element) {
				String first = firstArg == null ? firstNumber : (String)firstArg.invoke(element);
				String second = secondArg == null ? string : (String)secondArg.invoke(element);
				return first.startsWith(second);
			}
		};
		return new ConditionImpl(condition, predicate);
	}

}
