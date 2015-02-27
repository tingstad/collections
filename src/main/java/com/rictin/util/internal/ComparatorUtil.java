package com.rictin.util.internal;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.rictin.util.internal.proxy.Invocation;

public class ComparatorUtil {

	private ComparatorUtil() { }

	public static <T> Comparator<T> createComparator(
			final boolean descending,
			final boolean nullFirst,
			final Invocation<T> invocation) {
		final Class<?> returnType = invocation.getReturnType();
		if (Void.TYPE.equals(returnType)) {
			throw new RuntimeException("Method must return a value!");
		}
		if (!returnType.isPrimitive()
				&& !Arrays.asList(returnType.getInterfaces()).contains(
						Comparable.class)) {
			throw new RuntimeException("Must be comparable!");
		}
		return new Comparator<T>() {
			public int compare(T o1, T o2) {
				if (o1 == null && o2 == null) {
					return 0;
				} else if (o1 == null) {
					return nullFirst ? -1 : 1;
				} else if (o2 == null) {
					return nullFirst ? 1 : -1;
				}
				Object v1 = invocation.invoke(o1);
				Object v2 = invocation.invoke(o2);
				if (v1 == null && v1 == v2) {
					return 0;
				} else if (v1 == null) {
					return nullFirst ? -1 : 1;
				} else if (v2 == null) {
					return nullFirst ? 1 : -1;
				}
				if (descending) {
					Object t = v1;
					v1 = v2;
					v2 = t;
				}
				return ((Comparable) v1).compareTo(v2);
			}
		};
	}

	public static <T> Comparator<T> join(final List<Comparator<T>> comparators) {
		if (comparators.size() == 1) {
			return comparators.get(0);
		}
		return new Comparator<T>() {

			public int compare(T o1, T o2) {
				int i = 0;
				int c = 0;
				while (c == 0 && i < comparators.size()) {
					c = comparators.get(i++).compare(o1, o2);
				}
				return c;
			}
			
		};
	}

}
