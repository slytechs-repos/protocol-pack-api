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

/**
 * The Interface IntList.
 */
// @formatter:off
public interface IntList extends IntCollection {
	
	/**
	 * Adds the int.
	 *
	 * @param index the index
	 * @param e     the e
	 */
	void addInt(int index, int e);
	
	/**
	 * Adds the all ints.
	 *
	 * @param index the index
	 * @param c     the c
	 * @return true, if successful
	 */
	boolean addAllInts(int index, IntCollection c);
	
	/**
	 * Index of int.
	 *
	 * @param e the e
	 * @return the int
	 */
	int indexOfInt(int e);
	
	/**
	 * Last index of int.
	 *
	 * @param e the e
	 * @return the int
	 */
	int lastIndexOfInt(int e);
	
	/**
	 * Removes the int.
	 *
	 * @param index the index
	 * @return true, if successful
	 * @see com.slytechs.protocol.runtime.internal.util.collection.IntCollection#removeInt(int)
	 */
	boolean removeInt(int index);
	
	/**
	 * Sets the.
	 *
	 * @param index the index
	 * @param e     the e
	 * @return the int
	 */
	int set(int index, int e);
	
	/**
	 * Sub list.
	 *
	 * @param fromIndex the from index
	 * @param toIndex   the to index
	 * @return the int list
	 */
	IntList subList(int fromIndex, int toIndex);
}
// @formatter:on