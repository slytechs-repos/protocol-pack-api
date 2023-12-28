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
package com.slytechs.jnet.jnetruntime.hash;

import java.nio.ByteBuffer;

/**
 * A table which uses keys to hold state and data.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @param <T> the generic type
 */
public interface KeyedTable<T> extends Iterable<KeyedTable.Entry<T>> {

	/**
	 * The Interface Entry.
	 *
	 * @param <T> the generic type
	 */
	public interface Entry<T> {

		/**
		 * Data.
		 *
		 * @return the t
		 */
		T data();

		/**
		 * Index.
		 *
		 * @return the int
		 */
		int index();

		/**
		 * Checks if is empty.
		 *
		 * @return true, if is empty
		 */
		boolean isEmpty();

		/**
		 * Sets the data.
		 *
		 * @param data the new data
		 */
		void setData(T data);

		/**
		 * Sets the empty.
		 *
		 * @param b the new empty
		 */
		void setEmpty(boolean b);
	}

	/**
	 * Adds a new entry or retrieves an existing entry if one exists.
	 *
	 * @param key  the key
	 * @param data the data
	 * @return index of the table entry is returned if added successfully or an
	 *         existing entry already found, otherwise -1 on failure
	 */
	int add(ByteBuffer key, T data);

	/**
	 * Looks up an entry using key and returns null if not found.
	 *
	 * @param key the key
	 * @return index of the table entry is returned if found, otherwise -1 on
	 *         failure
	 */
	int lookup(ByteBuffer key);

	/**
	 * Removes the entry for ke.
	 *
	 * @param key the key
	 * @return true, if entry was found and removed, otherwise false
	 */
	boolean remove(ByteBuffer key);

	/**
	 * Gets a table entry at the specified index.
	 *
	 * @param index the entry index
	 * @return the entry
	 */
	public Entry<T> get(int index);
}
