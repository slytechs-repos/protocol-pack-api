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
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public interface HashTable<T> {

	public interface OfLong extends HashTable<Long> {

		@Override
		default Long get(int key) throws NotFound {
			return getLong(key);
		}

		long getLong(int key) throws NotFound;

		@Override
		default Long peek(int key) {
			return peekLong(key);
		}

		long peekLong(int key);

		long peekLong(int key, long defaultValue);

		@Override
		default Long put(int key, Long value) {
			return putLong(key, value);
		}

		long putLong(int key, long value);
	}

	T get(int key) throws NotFound;

	T peek(int key);

	T put(int key, T value);

	boolean remove(int key);

	void clear();
}
