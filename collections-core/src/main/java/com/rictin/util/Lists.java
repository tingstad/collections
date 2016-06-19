/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.rictin.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.rictin.util.internal.lists.Collect;
import com.rictin.util.internal.lists.FlatCollect;
import com.rictin.util.internal.lists.GroupBy;
import com.rictin.util.internal.lists.ListAggregator;
import com.rictin.util.internal.lists.ListFilter;
import com.rictin.util.internal.lists.ListFinder;
import com.rictin.util.internal.lists.ListSorter;
import com.rictin.util.internal.lists.ListUpdater;

/**
 * {@link com.rictin.util.Lists} has static methods to simplify common
 * collection operations such as:
 * <p/>
 * <code>Lists.<i>sort</i>(list).descendingBy().getFirstName()</code>
 * <p/>
 * <code>Lists.<i>sort</i>(list).nullFirstDescendingBy().getFirstName()</code>
 * <p/>
 * <code>Lists.<i>filter</i>(list).accept("Erik").getFirstName()</code>
 * <p/>
 * <code>Lists.<i>filter</i>(list).rejectLessThan(18).getAge()</code>
 * <p/>
 * <code>int total = Lists.<i>sum</i>(list).getCredit()</code>
 * <p/>
 * <code>Lists.<i>search</i>(list).forValue("Erik").getFirstName() != null</code>
 * <p/>
 * <code>int minScore = Lists.<i>min</i>().getScore()</code>
 * <p/>
 * <code>Lists.<i>forEach</i>(list).setScore(0)</code>
 * <p/>
 * <code>Lists.<i>select</i>(fromList, toList).acceptGreaterThan(30).getAge()</code>
 * <p/>
 * <code>List&lt;String> names = new ArrayList&lt;String>();</br>
 * Lists.<i>collect</i>(persons, names).getName()</code>
 * <p/>
 * <code>Lists.<i>groupBy</i>(inputList, resultMap).getAge()</code>
 * 
 * @author Richard H. Tingstad
 */
public class Lists {

	private Lists() {}
	
	/**
	 * Sorts list by specified value. Example:
	 * <p/>
	 * <code>Lists.sort(list).descendingBy().getFirstName()</code>.
	 * 
	 * @param list
	 *            Non-empty list
	 */
	public static <T> ListSorter<T> sort(List<T> list) {
		return new ListSorter<T>(list);
	}

	/**
	 * Filters collection by keeping only accepted elements, or by rejecting
	 * elements. Example:
	 * <p/>
	 * <code>Lists.filter(list).accept("Lars").getFirstName()</code>.
	 * 
	 * @param collection
	 *            Non-empty collection to filter
	 */
	public static <T> ListFilter<T> filter(Collection<T> collection) {
		return new ListFilter<T>(collection);
	}

	/**
	 * Checks if specified value exists in collection. Example:
	 * <p/>
	 * <code>Lists.search(list).forValue("Lars").getFirstName()</code>.
	 * 
	 * @param collection
	 *            Collection to search
	 * @return null if not found, otherwise the value
	 */
	public static <T> ListFinder<T> search(Collection<T> collection) {
		return new ListFinder<T>(collection);
	}

	/**
	 * Finds smallest (non-null) value of specified type in collection. Synonym
	 * for {@link Lists#search(Collection)}.forSmallest().
	 * 
	 * @param collection
	 *            Collection to search
	 * @return May return null if collection element are
	 *         java.lang.String/Integer/...
	 */
	public static <T> T min(Collection<T> collection) {
		return new ListFinder<T>(collection).forSmallest();
	}

	/**
	 * Finds largest value of specified type in collection. Synonym for
	 * {@link Lists#search(Collection)}.forLargest().
	 * 
	 * @return May return null if collection element are
	 *         java.lang.String/Integer/...
	 */
	public static <T> T max(Collection<T> collection) {
		return new ListFinder<T>(collection).forLargest();
	}

	/**
	 * Adds all specified numeric values. Example:
	 * <code>int total = Lists.sum(list).getCredit();</code> If collection
	 * elements are java.lang.Integer/Long/... no method should be called after
	 * sum(Collection).
	 * 
	 * @param collection
	 *            Non-empty collection
	 */
	public static <T> T sum(Collection<T> collection) {
		return new ListAggregator<T>(collection).sum();
	}

	/**
	 * Calculates average of specified values. Note that precision depends on
	 * return type, i.e. the average of Integers can only be Integer. Example:
	 * <code>int avg = Lists.sum(list).getAge();</code> If collection elements
	 * are java.lang.Integer/Long/... no method should be called after
	 * average(Collection).
	 * 
	 * @param collection
	 *            Non-empty collection
	 */
	public static <T> T average(Collection<T> collection) {
		return new ListAggregator<T>(collection).average();
	}

	/**
	 * Deletes elements where the specified value is a duplicate. Synonym for
	 * {@link Lists#filter(Collection)}.removeDuplicates().getSomething().
	 * Example: <code>Lists.unique(list).getFullName()</code>
	 * 
	 * @param collection
	 *            Non-empty collection
	 */
	public static <T> T unique(Collection<T> collection) {
		return new ListFilter<T>(collection).removeDuplicates();
	}

	/**
	 * Call specified method on all elements, usually to set a property value.
	 * Example:
	 * <p/>
	 * <code>Lists.forEach(list).setSomething(0)</code>
	 * 
	 * @param collection
	 *            Non-empty collection
	 */
	public static <T> T forEach(Collection<T> collection) {
		return new ListUpdater<T>(collection).createProxy();
	}

	public static <T> T groupBy(Collection<T> input, Map<?, List<T>> result) {
		return new GroupBy<T>(input, result).createProxy();
	}

	/**
	 * Example:<br/>
	 * List&lt;String> names = new ArrayList&lt;String>();<br/>
	 * Lists.<i>collect</i>(persons, names).getName();
	 * 
	 * @param input
	 *            Input collection to collect from
	 * @param result
	 *            Initialized collection where results will be added
	 */
	public static <T> T collect(Collection<T> input, Collection<?> result) {
		return new Collect<T>(input, result).createProxy();
	}

	/**
	 * Similar to the filter method, but populates a target collection with the
	 * results instead of manipulating the original one.
	 * 
	 * @param input
	 * @param result
	 * @see Lists#filter(Collection)
	 */
	public static <T> ListFilter<T> select(Collection<T> input,
			Collection<T> result) {
		return new ListFilter<T>(input, result);
	}

	/**
	 * Similar to Scala's flatMap.
	 * 
	 * @param input
	 * @param result
	 */
	public static <T> T flatCollect(Collection<? extends Collection<T>> input,
			Collection<?> result) {
		return new FlatCollect<T>(input, result).createProxy();
	}

}
