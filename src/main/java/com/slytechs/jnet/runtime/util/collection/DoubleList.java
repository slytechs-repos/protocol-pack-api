/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.runtime.util.collection;

/**
 * The Interface DoubleList.
 */
// @formatter:off
public interface DoubleList extends DoubleCollection {
	
	/**
	 * Adds the double.
	 *
	 * @param index the index
	 * @param e     the e
	 */
	void addDouble(int index, double e);
	
	/**
	 * Adds the all doubles.
	 *
	 * @param index the index
	 * @param c     the c
	 * @return true, if successful
	 */
	boolean addAllDoubles(int index, DoubleCollection c);
	
	/**
	 * Index of double.
	 *
	 * @param e the e
	 * @return the int
	 */
	int indexOfDouble(double e);
	
	/**
	 * Last index of double.
	 *
	 * @param e the e
	 * @return the int
	 */
	int lastIndexOfDouble(double e);
	
	/**
	 * Removes the double.
	 *
	 * @param index the index
	 * @return true, if successful
	 */
	boolean removeDouble(int index);
	
	/**
	 * Sets the.
	 *
	 * @param index the index
	 * @param e     the e
	 * @return the double
	 */
	double set(int index, double e);
	
	/**
	 * Sub list.
	 *
	 * @param fromIndex the from index
	 * @param toIndex   the to index
	 * @return the double list
	 */
	DoubleList subList(int fromIndex, int toIndex);
}
// @formatter:on