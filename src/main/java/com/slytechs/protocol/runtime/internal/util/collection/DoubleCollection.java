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
package com.slytechs.protocol.runtime.internal.util.collection;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/**
 * The Interface DoubleCollection.
 */
// @formatter:off
public interface DoubleCollection extends Collection<Double> {
	
	/**
	 * Adds the double.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	boolean addDouble(double e);
	
	/**
	 * Adds the all doubles.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean addAllDoubles(DoubleCollection c);
	
	/**
	 * Contains double.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	boolean containsDouble(double e);
	
	/**
	 * Contains all doubles.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean containsAllDoubles(DoubleCollection c);
	
	/**
	 * Removes the double.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	boolean removeDouble(double e);
	
	/**
	 * Removes the all doubles.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean removeAllDoubles(DoubleCollection c);
	
	/**
	 * Removes the double if.
	 *
	 * @param filter the filter
	 * @return true, if successful
	 */
	boolean removeDoubleIf(IntPredicate filter);
	
	/**
	 * Retain all doubles.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	boolean retainAllDoubles(DoubleCollection c);
	
	/**
	 * To double array.
	 *
	 * @return the double[]
	 */
	double[] toDoubleArray();
	
	/**
	 * To double array.
	 *
	 * @param array the array
	 * @return the double[]
	 */
	double[] toDoubleArray(double[] array);
	
	/**
	 * To double array.
	 *
	 * @param generator the generator
	 * @return the double[]
	 */
	double[] toDoubleArray(IntFunction<double[]> generator);
	
	/**
	 * Iterator.
	 *
	 * @return the primitive iterator. of double
	 * @see java.util.Collection#iterator()
	 */
	PrimitiveIterator.OfDouble iterator();
	
	/**
	 * Double stream.
	 *
	 * @return the int stream
	 */
	IntStream doubleStream();
	
	/**
	 * Spliterator.
	 *
	 * @return the spliterator. of double
	 * @see java.util.Collection#spliterator()
	 */
	Spliterator.OfDouble spliterator();
}
// @formatter:on