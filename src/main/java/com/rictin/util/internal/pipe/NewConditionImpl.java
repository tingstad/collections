package com.rictin.util.internal.pipe;

import com.rictin.util.internal.proxy.Invocation;

public class NewConditionImpl<T> {

	protected static Invocation fetchInvocation() {
		return PipeParent.takeLastInvocation();
	}

	protected Invocation getInvocation(MatcherImpl matcher) {
		return matcher.getInvocation();
	}

	protected T getValue(MatcherImpl<T> matcher) {
		return matcher.getValue();
	}

}
