/* Copyright 2013 Richard H. Tingstad
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/**
 * {@link com.rictin.util.Lists} has static methods to simplify common list 
 * operations such as:
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
 * <code>int minScore = Lists.<i>minimum</i>().getScore()</code>
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
 * @author Richard Tingstad
 */
package com.rictin.util;

