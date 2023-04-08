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
package com.slytechs.protocol.runtime.internal.util;

import com.slytechs.protocol.runtime.NotFound;

/**
 * The Interface HashTable.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 * @param <T> the generic type
 */
public interface HashTable<T> {

	/**
	 * The Interface OfLong.
	 */
	public interface OfLong extends HashTable<Long> {

		/**
		 * @see com.slytechs.protocol.runtime.internal.util.HashTable#get(int)
		 */
		@Override
		default Long get(int key) throws NotFound {
			return getLong(key);
		}

		/**
		 * Gets the long.
		 *
		 * @param key the key
		 * @return the long
		 * @throws NotFound the not found
		 */
		long getLong(int key) throws NotFound;

		/**
		 * @see com.slytechs.protocol.runtime.internal.util.HashTable#peek(int)
		 */
		@Override
		default Long peek(int key) {
			return peekLong(key);
		}

		/**
		 * Peek long.
		 *
		 * @param key the key
		 * @return the long
		 */
		long peekLong(int key);

		/**
		 * Peek long.
		 *
		 * @param key          the key
		 * @param defaultValue the default value
		 * @return the long
		 */
		long peekLong(int key, long defaultValue);

		/**
		 * @see com.slytechs.protocol.runtime.internal.util.HashTable#put(int, java.lang.Object)
		 */
		@Override
		default Long put(int key, Long value) {
			return putLong(key, value);
		}

		/**
		 * Put long.
		 *
		 * @param key   the key
		 * @param value the value
		 * @return the long
		 */
		long putLong(int key, long value);
	}

	/**
	 * Gets the.
	 *
	 * @param key the key
	 * @return the t
	 * @throws NotFound the not found
	 */
	T get(int key) throws NotFound;

	/**
	 * Peek.
	 *
	 * @param key the key
	 * @return the t
	 */
	T peek(int key);

	/**
	 * Put.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the t
	 */
	T put(int key, T value);

	/**
	 * Removes the.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	boolean remove(int key);

	/**
	 * Clear.
	 */
	void clear();
}
