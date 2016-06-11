package com.rictin.util.internal.pipe;

import java.util.Collection;

import com.rictin.util.internal.proxy.Invocation;
import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.NewConditionCollection;

public class NewConditionCollectionImpl<T extends Collection> extends NewConditionImpl<T> implements NewConditionCollection<T> {

	private final Invocation invocation; 
	private final Condition conditionAnd;
	private final Condition conditionOr;

	public NewConditionCollectionImpl(T value, Invocation<?> invocation, Condition<?> conditionAnd, Condition<?> conditionOr) {
		super(value, invocation, conditionAnd, conditionOr);
		this.invocation = invocation;
		this.conditionAnd = conditionAnd;
		this.conditionOr = conditionOr;
	}

	public Condition<?> hasSize(final int size) {
		return new Condition() {

			@Override
			public boolean satisfies(final Object element) {
				
				if (conditionAnd != null && !conditionAnd.satisfies(element)) return false;
				if (conditionOr != null && conditionOr.satisfies(element)) return true;

				T collection = (T)invocation.invoke(element);

				return collection != null && collection.size() == size;
			}
		};
	}

	public Condition<?> contains(final Object candidate) {
		return new Condition() {

			@Override
			public boolean satisfies(final Object element) {
				
				if (conditionAnd != null && !conditionAnd.satisfies(element)) return false;
				if (conditionOr != null && conditionOr.satisfies(element)) return true;

				T collection = (T)invocation.invoke(element);

				return collection != null && collection.contains(candidate);
			}
		};
	}
	
}
