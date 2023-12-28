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
package com.slytechs.jnet.jnetruntime.internal.util.collection;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/**
 * The Interface IntCollection.
 */
// @formatter:off
public interface IntCollection extends Collection<Integer> {
	
	/**
	 * Adds the int.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	boolean addInt(int e);
	
	/**
	 * Adds the all ints.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean addAllInts(IntCollection c);
	
	/**
	 * Contains int.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	boolean containsInt(int e);
	
	/**
	 * Contains all ints.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean containsAllInts(IntCollection c);
	
	/**
	 * Removes the int.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	boolean removeInt(int e);
	
	/**
	 * Removes the all ints.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean removeAllInts(IntCollection c);
	
	/**
	 * Removes the int if.
	 *
	 * @param filter the filter
	 * @return true, if successful
	 */
	boolean removeIntIf(IntPredicate filter);
	
	/**
	 * Retain all ints.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean retainAllInts(IntCollection c);
	
	/**
	 * To int array.
	 *
	 * @return the int[]
	 */
	int[] toIntArray();
	
	/**
	 * To int array.
	 *
	 * @param array the array
	 * @return the int[]
	 */
	int[] toIntArray(int[] array);
	
	/**
	 * To int array.
	 *
	 * @param generator the generator
	 * @return the int[]
	 */
	int[] toIntArray(IntFunction<int[]> generator);
	
	/**
	 * Iterator.
	 *
	 * @return the primitive iterator. of int
	 * @see java.util.Collection#iterator()
	 */
	PrimitiveIterator.OfInt iterator();
	
	/**
	 * Int stream.
	 *
	 * @return the int stream
	 */
	IntStream intStream();
	
	/**
	 * Spliterator.
	 *
	 * @return the spliterator. of int
	 * @see java.util.Collection#spliterator()
	 */
	Spliterator.OfInt spliterator();
}
// @formatter:on