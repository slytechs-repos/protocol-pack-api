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

import java.util.List;

/**
 * The Interface ListLong.
 */
// @formatter:off
public interface ListLong extends LongCollection, List<Long>, Cloneable {
	
	/**
	 * Adds the long.
	 *
	 * @param index the index
	 * @param e     the e
	 */
	void     addLong(int index, long e);
	
	/**
	 * Adds the all longs.
	 *
	 * @param index the index
	 * @param c     the c
	 * @return true, if successful
	 */
	boolean  addAllLongs(int index, LongCollection c);
	
	/**
	 * Index of long.
	 *
	 * @param e the e
	 * @return the int
	 */
	int      indexOfLong(long e);
	
	/**
	 * Last index of long.
	 *
	 * @param e the e
	 * @return the int
	 */
	int      lastIndexOfLong(long e);
	
	/**
	 * Sets the.
	 *
	 * @param index the index
	 * @param e     the e
	 * @return the long
	 */
	long     set(int index, long e);
	
	/**
	 * @see java.util.List#subList(int, int)
	 */
	ListLong subList(int fromIndex, int toIndex);
	
	/**
	 * Removes the long.
	 *
	 * @param index the index
	 * @return the long
	 */
	long     removeLong(int index);
	
	/**
	 * Clone.
	 *
	 * @return the list long
	 */
	ListLong clone();
}
// @formatter:on