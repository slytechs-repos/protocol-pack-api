/*
 * Sly Technologies Free License
 * 
 * Copyright 2023 Sly Technologies Inc.
 *
 * Licensed under the Sly Technologies Free License (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.slytechs.com/free-license-text
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.slytechs.jnet.runtime.internal.util.collection;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.function.IntFunction;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;

/**
 * The Interface LongCollection.
 */
// @formatter:off
public interface LongCollection extends Collection<Long> {
	
	/**
	 * Adds the long.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	boolean addLong(long e);
	
	/**
	 * Adds the all longs.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean addAllLongs(LongCollection c);
	
	/**
	 * Contains long.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	boolean containsLong(long e);
	
	/**
	 * Contains all longs.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean containsAllLongs(LongCollection c);
	
	/**
	 * Removes the all longs.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean removeAllLongs(LongCollection c);
	
	/**
	 * Removes the long if.
	 *
	 * @param filter the filter
	 * @return true, if successful
	 */
	boolean removeLongIf(LongPredicate filter);
	
	/**
	 * Retain all longs.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean retainAllLongs(LongCollection c);
	
	/**
	 * To long array.
	 *
	 * @return the long[]
	 */
	long[] toLongArray();
	
	/**
	 * To long array.
	 *
	 * @param array the array
	 * @return the long[]
	 */
	long[] toLongArray(long[] array);
	
	/**
	 * To long array.
	 *
	 * @param generator the generator
	 * @return the long[]
	 */
	long[] toLongArray(IntFunction<long[]> generator);
	
	/**
	 * Iterator.
	 *
	 * @return the primitive iterator. of long
	 * @see java.util.Collection#iterator()
	 */
	PrimitiveIterator.OfLong iterator();
	
	/**
	 * Long stream.
	 *
	 * @return the long stream
	 */
	LongStream longStream();
}
// @formatter:on