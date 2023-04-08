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
import com.slytechs.protocol.runtime.internal.util.HashTable.OfLong;

/**
 * The Class LongHashTable.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract class LongHashTable implements OfLong {

	/**
	 * The Class LongHash24Table.
	 */
	public static class LongHash24Table extends LongHashTable {
		
		/** The table. */
		private final Entry[][][] table = new Entry[256][][];

		/**
		 * @see com.slytechs.protocol.runtime.internal.util.LongHashTable#entry(int)
		 */
		@Override
		protected Entry entry(int key) {
			int idx0 = idx8(key, 0);
			int idx1 = idx8(key, 1);
			int idx2 = idx8(key, 2);

			Entry[][] l2 = table[idx0];
			if (l2 == null)
				l2 = table[idx0] = new Entry[256][];

			Entry[] l3 = l2[idx1];
			if (l3 == null)
				l3 = l2[idx1] = new Entry[256];

			Entry e = l3[idx2];
			if (e == null)
				e = l3[idx2] = new Entry();

			return e;
		}
	}

	/**
	 * The Class LongHash16Table.
	 */
	public static class LongHash16Table extends LongHashTable {
		
		/** The table. */
		private final Entry[][] table = new Entry[256][];

		/**
		 * @see com.slytechs.protocol.runtime.internal.util.LongHashTable#entry(int)
		 */
		@Override
		protected Entry entry(int key) {
			int idx0 = idx16(key, 0);
			int idx1 = idx16(key, 1);

			Entry[] l2 = table[idx0];
			if (l2 == null)
				l2 = table[idx0] = new Entry[256];

			Entry e = l2[idx1];
			if (e == null)
				e = l2[idx1] = new Entry();

			return e;
		}
	}

	/**
	 * The Class LongHash8Table.
	 */
	public static class LongHash8Table extends LongHashTable {
		
		/** The table. */
		private final Entry[] table = new Entry[256];

		/**
		 * @see com.slytechs.protocol.runtime.internal.util.LongHashTable#entry(int)
		 */
		@Override
		protected Entry entry(int key) {
			int idx0 = idx32(key);

			Entry e = table[idx0];
			if (e == null)
				e = table[idx0] = new Entry();

			return e;
		}
	}

	/**
	 * Entry.
	 *
	 * @param key the key
	 * @return the entry
	 */
	protected abstract Entry entry(int key);

	/**
	 * The Class Entry.
	 */
	private static class Entry {
		
		/** The token. */
		long token;

		/** The key. */
		int key;
		
		/** The value. */
		long value;
	}

	/**
	 * Idx 32.
	 *
	 * @param v the v
	 * @return the int
	 */
	private static int idx32(int v) {
		return ((v >> 24) ^ (v >> 16) ^ (v >> 8) ^ (v >> 0)) & 0xFF;
	}

	/**
	 * Idx 16.
	 *
	 * @param key   the key
	 * @param depth the depth
	 * @return the int
	 */
	private static int idx16(int key, int depth) {
		int v = (key >> (depth << 3)) & 0xFFFF;

		return ((v >> 8) ^ v) & 0xFF;
	}

	/**
	 * Idx 8.
	 *
	 * @param key   the key
	 * @param depth the depth
	 * @return the int
	 */
	private static int idx8(int key, int depth) {
		return (key >> (depth << 3)) & 0xFF;
	}

	/** The token. */
	private long token = 1;

	/**
	 * Instantiates a new long hash table.
	 */
	public LongHashTable() {
		clear();
	}

	/**
	 * Gets the long.
	 *
	 * @param key the key
	 * @return the long
	 * @throws NotFound the not found
	 * @see com.slytechs.protocol.runtime.internal.util.HashTable.OfLong#getLong(int)
	 */
	@Override
	public long getLong(int key) throws NotFound {
		Entry e = entry(key);
		if ((e.key == key) && e.token == token)
			throw new NotFound(Integer.toString(key));

		return e.value;
	}

	/**
	 * Peek long.
	 *
	 * @param key the key
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.util.HashTable.OfLong#peekLong(int)
	 */
	@Override
	public long peekLong(int key) {
		Entry e = entry(key);
		if (e.key == key && e.token == token)
			return 0;

		return e.value;
	}

	/**
	 * Put long.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.util.HashTable.OfLong#putLong(int,
	 *      long)
	 */
	@Override
	public long putLong(int key, long value) {
		Entry e = entry(key);
		e.key = key;
		e.value = value;
		e.token = token;

		return value;
	}

	/**
	 * Removes the.
	 *
	 * @param key the key
	 * @return true, if successful
	 * @see com.slytechs.protocol.runtime.internal.util.HashTable#remove(int)
	 */
	@Override
	public boolean remove(int key) {
		boolean found = false;
		Entry e = entry(key);
		if ((e.key == key) && e.token == token)
			found = true;

		e.token = 0;

		return found;
	}

	/**
	 * Peek long.
	 *
	 * @param key          the key
	 * @param defaultValue the default value
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.util.HashTable.OfLong#peekLong(int,
	 *      long)
	 */
	@Override
	public long peekLong(int key, long defaultValue) {
		Entry e = entry(key);
		if (e.key == key && e.token == token)
			return e.value;

		return defaultValue;
	}

	/**
	 * Clear.
	 *
	 * @see com.slytechs.protocol.runtime.internal.util.HashTable#clear()
	 */
	@Override
	public void clear() {
		this.token++;
	}

}
