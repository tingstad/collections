package com.rictin.util.internal.pipe;

import java.util.ArrayList;
import java.util.List;

import com.rictin.util.pipe.Condition;
import com.rictin.util.pipe.WhereNumber;

public class ConditionImpl extends Condition {

	private final List<Predicate> predicates = new ArrayList<Predicate>();

	public ConditionImpl(ConditionImpl condition, Predicate predicate) {
		if (condition != null) {
			predicates.addAll(condition.predicates);
		}
		predicates.add(predicate);
	}

	@Override
	public WhereNumber and(Number number) {
		return new WhereNumberImpl(this, number);
	}

	public List<Predicate> getPredicates() {
		return predicates;
	}

}
