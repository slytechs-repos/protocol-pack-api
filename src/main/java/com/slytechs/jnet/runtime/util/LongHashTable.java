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

import com.slytechs.jnet.runtime.util.HashTable.OfLong;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public abstract class LongHashTable implements OfLong {

	public static class LongHash24Table extends LongHashTable {
		private final Entry[][][] table = new Entry[256][][];

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

	public static class LongHash16Table extends LongHashTable {
		private final Entry[][] table = new Entry[256][];

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

	public static class LongHash8Table extends LongHashTable {
		private final Entry[] table = new Entry[256];

		@Override
		protected Entry entry(int key) {
			int idx0 = idx32(key);

			Entry e = table[idx0];
			if (e == null)
				e = table[idx0] = new Entry();

			return e;
		}
	}

	protected abstract Entry entry(int key);

	private static class Entry {
		long token;

		int key;
		long value;
	}

	private static int idx32(int v) {
		return ((v >> 24) ^ (v >> 16) ^ (v >> 8) ^ (v >> 0)) & 0xFF;
	}

	private static int idx16(int key, int depth) {
		int v = (key >> (depth << 3)) & 0xFFFF;

		return ((v >> 8) ^ v) & 0xFF;
	}

	private static int idx8(int key, int depth) {
		return (key >> (depth << 3)) & 0xFF;
	}

	private long token = 1;

	public LongHashTable() {
		clear();
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.HashTable.OfLong#getLong(int)
	 */
	@Override
	public long getLong(int key) throws NotFound {
		Entry e = entry(key);
		if ((e.key == key) && e.token == token)
			throw new NotFound(Integer.toString(key));

		return e.value;
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.HashTable.OfLong#peekLong(int)
	 */
	@Override
	public long peekLong(int key) {
		Entry e = entry(key);
		if (e.key == key && e.token == token)
			return 0;

		return e.value;
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.HashTable.OfLong#putLong(int, long)
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
	 * @see com.slytechs.jnet.runtime.util.HashTable#remove(int)
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
	 * @see com.slytechs.jnet.runtime.util.HashTable.OfLong#peekLong(int, long)
	 */
	@Override
	public long peekLong(int key, long defaultValue) {
		Entry e = entry(key);
		if (e.key == key && e.token == token)
			return e.value;

		return defaultValue;
	}

	/**
	 * @see com.slytechs.jnet.runtime.util.HashTable#clear()
	 */
	@Override
	public void clear() {
		this.token++;
	}

}
