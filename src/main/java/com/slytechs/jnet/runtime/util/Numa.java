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
package com.slytechs.jnet.runtime.util;

/**
 * The Interface for describing NUMA Nodes (Non-Uniform Memory Access) on large
 * systems that are NUMA configured.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface Numa {

	/**
	 * Node.
	 *
	 * @param index the index
	 * @return the numa
	 */
	static Numa node(int index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Nodes.
	 *
	 * @param first   the first
	 * @param indexex the indexex
	 * @return the numa[]
	 */
	static Numa[] nodes(int first, int... indexex) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Range.
	 *
	 * @param start  the start
	 * @param length the length
	 * @return the numa[]
	 */
	static Numa[] range(int start, int length) {
		throw new UnsupportedOperationException();
	}

}
