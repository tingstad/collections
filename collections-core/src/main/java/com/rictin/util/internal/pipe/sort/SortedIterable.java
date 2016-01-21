package com.rictin.util.internal.pipe.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class SortedIterable<T> implements Iterator<T>, Iterable<T> {

	private Iterable<T> input;
	private final Comparator<T> comparator;
	private final TreeMap<T, List<T>> sorted;
	private Iterator<T> keyIterator;
	private Iterator<T> valueIterator;

	public SortedIterable(final Iterable<T> input, final Comparator<T> comparator) {
		this.input = input;
		this.comparator = comparator;
		this.sorted = new TreeMap<T, List<T>>(comparator);
	}

	public boolean hasNext() {
		if (sorted.isEmpty())
			for (final T element : input)
				addToCollection(element);
		return keyIterator.hasNext() 
				|| valueIterator != null
				&& valueIterator.hasNext();
	}

	public T next() {
		if (valueIterator == null || !valueIterator.hasNext()) {
			final T key = keyIterator.next();
			valueIterator = sorted.get(key).iterator();
		}
		return valueIterator.next();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator<T> iterator() {
		return this;
	}

	private void addToCollection(final T element) {
		if (!sorted.containsKey(element)) {
			sorted.put(element, new ArrayList<T>(1));
		}
		sorted.get(element).add(element);
		keyIterator = sorted.keySet().iterator();
	}
}