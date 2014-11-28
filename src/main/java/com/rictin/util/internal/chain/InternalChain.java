package com.rictin.util.internal.chain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.rictin.util.internal.ComparatorUtil;

public class InternalChain<T> {

	protected List<Chained<T>> operations = new ArrayList<Chained<T>>();
	private boolean planned;

	protected Chained<T> getLastOperation() {
		return operations.get(operations.size() - 1);
	}

	public boolean hasNext() {
		plan();
		return getLastOperation().hasNext();
	}

	public T next() {
		plan();
		return getLastOperation().next();
	}

	private void plan() {
		if (planned) return;
		planned = true;
		planConsecutiveSorting();
		planLimitedSorting();
	}

	/**
	 * Two or more consecutive sorting operations should be merged into 
	 * one sort operation, interpreted as "sort by X, then by Y".
	 */
	private void planConsecutiveSorting() {
		ListIterator<Chained<T>> iterator = operations.listIterator();
		while (iterator.hasNext()) {
			Chained<T> operation = iterator.next();
			if (isSorting(operation)) {
				ChainSorter<T> sorter = (ChainSorter<T>) operation;
				List<Comparator<T>> comparators = new ArrayList<Comparator<T>>();
				comparators.add(sorter.getComparator());
				Iterator<Chained<T>> nextIterator = operations.listIterator(iterator.nextIndex());
				while (nextIterator.hasNext()) {
					Chained<T> next = nextIterator.next();
					if (!isSorting(next)) break;
					comparators.add(((ChainSorter<T>) next).getComparator());
					nextIterator.remove();
				}
				sorter.setComparator(ComparatorUtil.join(comparators));
			}
		}
	}

	private void planLimitedSorting() {
		ListIterator<Chained<T>> iterator = operations.listIterator();
		while (iterator.hasNext()) {
			Chained<T> operation = iterator.next();
			if (isSorting(operation)) {
				ChainSorter<T> sorter = (ChainSorter<T>) operation;
				Iterator<Chained<T>> nextIterator = operations.listIterator(iterator.nextIndex());
				Integer limit = null;
				while (nextIterator.hasNext()) {
					Chained<T> next = nextIterator.next();
					if (!isLimit(next)) continue;
					ChainLimit<T> limiter = (ChainLimit<T>) next;
					if (limit == null || limiter.getLimit() < limit)
						limit = limiter.getLimit();
				}
				sorter.setLimit(limit);
			}
		}
	}

	private static <T> boolean isSorting(Chained<T> operation) {
		return operation instanceof ChainSorter<?>;
	}

	private static <T> boolean isLimit(Chained<T> operation) {
		return operation instanceof ChainLimit<?>;
	}

}
