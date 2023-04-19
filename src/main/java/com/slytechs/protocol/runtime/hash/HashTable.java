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
package com.slytechs.protocol.runtime.hash;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A simple hash table where entries are looked up using the hash value either
 * calculated based on key or for faster performance when hash is supplied.
 * <p>
 * In a network application, a hash table can be used to store information about
 * network connections, such as source and destination IP addresses, port
 * numbers, and connection states. For example, a web server can use a hash
 * table to keep track of active TCP connections with clients, allowing it to
 * efficiently handle incoming requests and send responses back.
 * </p>
 * <p>
 * In this scenario, when a client establishes a new connection with the server,
 * the server can create a new entry in the hash table with the connection
 * details, such as the source and destination IP addresses and port numbers. As
 * the connection progresses, the server can update the hash table entry with
 * information about the connection state, such as whether it is established,
 * closed, or in a particular state of the TCP protocol.
 * </p>
 * <p>
 * The hash table can also be used to implement firewall rules and access
 * control policies. For example, the server can use the hash table to keep
 * track of connections that have been denied by a firewall rule, and prevent
 * any further traffic from the same source IP address or port.
 * </p>
 * <p>
 * Another use case for a hash table in a network application is to implement a
 * cache of frequently accessed data, such as DNS lookups or responses to API
 * requests. In this scenario, the hash table can be used to store the results
 * of previous lookups, allowing subsequent requests to be served quickly
 * without having to perform the same expensive computation or network
 * communication again.
 * </p>
 * <p>
 * Overall, a hash table can be a powerful tool in a network application,
 * allowing for efficient storage and lookup of network connection information,
 * as well as supporting firewall rules and caching of frequently accessed data.
 * </p>
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @param <T> the generic type
 */
public class HashTable<T> implements KeyedTable<T> {

	/**
	 * An algorithm which calculates a hash code from buffer data. The data in the
	 * buffer is used between the buffer's position and limit properties.
	 */
	@FunctionalInterface
	public interface HashAlgorithm {

		/**
		 * Calculate hashcode.
		 *
		 * @param buffer the buffer
		 * @return the long
		 */
		long calculateHashcode(ByteBuffer buffer);
	}

	/**
	 * The Class HashEntry.
	 *
	 * @param <T> the generic type
	 */
	public static class HashEntry<T> implements Entry<T> {

		/** The key. */
		private final ByteBuffer key;

		/** The index. */
		private final int index;

		/** The data. */
		private T data;

		/** The is empty. */
		private boolean isEmpty = true;

		/**
		 * Instantiates a new hash entry.
		 *
		 * @param index the index
		 */
		private HashEntry(int index) {
			this(index, MAX_KEY_SIZE_BYTES);
		}

		/**
		 * Instantiates a new hash entry.
		 *
		 * @param index        the index
		 * @param keySizeBytes the key size bytes
		 */
		private HashEntry(int index, int keySizeBytes) {
			this.index = index;
			this.key = ByteBuffer.allocateDirect(keySizeBytes);
		}

		/**
		 * @see com.slytechs.protocol.runtime.hash.KeyedTable.Entry#data()
		 */
		@Override
		public T data() {
			return data;
		}

		/**
		 * Index.
		 *
		 * @return the int
		 * @see com.slytechs.protocol.runtime.hash.KeyedTable.Entry#index()
		 */
		@Override
		public int index() {
			return index;
		}

		/**
		 * Checks if is empty.
		 *
		 * @return true, if is empty
		 */
		@Override
		public boolean isEmpty() {
			return this.isEmpty;
		}

		/**
		 * Key.
		 *
		 * @return the byte buffer
		 */
		public ByteBuffer key() {
			return key;
		}

		/**
		 * Sets the data.
		 *
		 * @param data the new data
		 */
		@Override
		public void setData(T data) {
			this.data = data;
		}

		/**
		 * Sets the empty.
		 *
		 * @param b the new empty
		 */
		@Override
		public void setEmpty(boolean b) {
			this.isEmpty = b;
		}

		/**
		 * Sets the key.
		 *
		 * @param newKey the new key
		 */
		public void setKey(ByteBuffer newKey) {
			key.put(newKey);
			key.flip();
			newKey.flip();
		}

		/**
		 * To string.
		 *
		 * @return the string
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "HashEntry [index=" + index + ", key=" + key + ", data=" + data + "]";
		}
	}

	/** The Constant MAX_KEY_SIZE_BYTES. */
	public static final int MAX_KEY_SIZE_BYTES = 64;

	/** The Constant DEFAULT_TABLE_SIZE. */
	public static final int DEFAULT_TABLE_SIZE = 1024;

	/**
	 * Match keys.
	 *
	 * @param key1 the key 1
	 * @param key2 the key 2
	 * @return true, if successful
	 */
	protected static final boolean matchKeys(ByteBuffer key1, ByteBuffer key2) {
		return key1.compareTo(key2) == 0;
	}

	/** The hash entries. */
	protected final HashEntry<T>[] table;

	/** The entries count. */
	private final int tableSize;

	/** The hash algorithm. */
	private HashAlgorithm hashAlgorithm = Checksums::crc32;

	/** The table mask. */
	private final int tableMask;

	private boolean stickyData;

	/**
	 * Instantiates a new hash table.
	 *
	 * @param entriesCount the total number of hash entries in the table
	 */
	@SuppressWarnings({ "rawtypes",
			"unchecked" })
	public HashTable(int entriesCount) {
		this.tableSize = entriesCount;
		this.table = new HashEntry[entriesCount];
		this.tableMask = entriesCount - 1;
		assert Integer.bitCount(entriesCount) == 1 : "hash table size not a power of 2";

		/* Allocate all the entries */
		IntStream
				.range(0, entriesCount)
				.forEach(i -> table[i] = new HashEntry(i));
	}

	/**
	 * Adds the.
	 *
	 * @param key  the key
	 * @param data the data
	 * @return the int
	 * @see com.slytechs.protocol.runtime.hash.KeyedTable#add(java.nio.ByteBuffer)
	 */
	@Override
	public final int add(ByteBuffer key, T data) {
		return add(key, data, calculateHashcode(key));
	}

	/**
	 * Adds the.
	 *
	 * @param key      the key
	 * @param data     the data
	 * @param hashcode the hashcode
	 * @return the int
	 */
	public int add(ByteBuffer key, T data, long hashcode) {
		int index = index(hashcode);
		HashEntry<T> entry = table[index];
		if (entry.isEmpty) {
			return set(index, key, data);

		} else if (matchKeys(entry.key, key))
			return index;

		return -1;
	}

	/**
	 * Calculate hashcode.
	 *
	 * @param key the key
	 * @return the long
	 */
	protected final long calculateHashcode(ByteBuffer key) {
		long hash = hashAlgorithm.calculateHashcode(key);

		key.flip();

		return hash;
	}

	/**
	 * Enable sticky data mode. Sticky data is persistent across remove calls in
	 * order to enable reuse of previously set data.
	 *
	 * @param b if true, data will be sticky, otherwise it will be cleared when no
	 *          longer used
	 */
	public void enableStickyData(boolean b) {
		this.stickyData = b;
	}

	/**
	 * Gets the.
	 *
	 * @param index the index
	 * @return the hash entry
	 * @see com.slytechs.protocol.runtime.hash.KeyedTable#get(int)
	 */
	@Override
	public final HashEntry<T> get(int index) {
		return this.table[index];
	}

	public long getUnusedEntriesCount() {
		return Arrays.stream(table)
				.filter(e -> e.isEmpty)
				.count();
	}

	/**
	 * Gets the used entries count.
	 *
	 * @return the used entries count
	 */
	public long getUsedEntriesCount() {
		return Arrays.stream(table)
				.filter(e -> !e.isEmpty)
				.count();
	}

	/**
	 * Index.
	 *
	 * @param hashCode the hash code
	 * @return the int
	 */
	private int index(long hashCode) {
		return (int) (hashCode & tableMask);
	}

	/**
	 * Lookup.
	 *
	 * @param key the key
	 * @return the int
	 * @see com.slytechs.protocol.runtime.hash.KeyedTable#lookup(java.nio.ByteBuffer)
	 */
	@Override
	public final int lookup(ByteBuffer key) {
		return lookup(key, calculateHashcode(key));
	}

	/**
	 * Lookup.
	 *
	 * @param key      the key
	 * @param hashcode the hashcode
	 * @return the int
	 */
	public int lookup(ByteBuffer key, long hashcode) {
		int index = index(hashcode);
		HashEntry<T> entry = table[index];

		if (!entry.isEmpty && matchKeys(entry.key, key))
			return index;

		return -1;
	}

	/**
	 * Removes the.
	 *
	 * @param key the key
	 * @return true, if successful
	 * @see com.slytechs.protocol.runtime.hash.KeyedTable#remove(java.nio.ByteBuffer)
	 */
	@Override
	public final boolean remove(ByteBuffer key) {
		return remove(key, calculateHashcode(key));
	}

	/**
	 * Removes the.
	 *
	 * @param key      the key
	 * @param hashcode the hashcode
	 * @return true, if successful
	 */
	public boolean remove(ByteBuffer key, long hashcode) {
		int index = index(hashcode);
		HashEntry<T> entry = table[index];

		if (!entry.isEmpty && matchKeys(entry.key, key)) {
			remove(index);

			return true;
		}

		return false;
	}

	/**
	 * Removes the hash entry at specified index.
	 *
	 * @param index the hash table index
	 */
	protected void remove(int index) {
		table[index].setEmpty(true);
		if (!stickyData)
			table[index].setData(null);
	}

	/**
	 * Sets the.
	 *
	 * @param index the index
	 * @param key   the key
	 * @param data  the data
	 */
	protected final int set(int index, ByteBuffer key, T data) {
		HashEntry<T> hashEntry = table[index];

		hashEntry.setKey(key);
		hashEntry.setEmpty(false);

		if (!stickyData || data != null)
			hashEntry.setData(data);

		return index;
	}

	/**
	 * Sets the hash algorithm.
	 *
	 * @param newAlgorithm the new hash algorithm
	 */
	public final void setHashAlgorithm(HashAlgorithm newAlgorithm) {
		this.hashAlgorithm = Objects.requireNonNull(newAlgorithm, "newAlgorithm");
	}

	public int size() {
		return tableSize;
	}

	/**
	 * Fills every hash table entry with the data value supplied by the factory.
	 * Allows initialization of the the table. All of the filled entries remain
	 * marked as empty but their data is pre-initialized using the factory.
	 *
	 * @param factory the factory supplying data objects based on entry's index
	 *                value
	 */
	public void fill(IntFunction<T> factory) {
		IntStream
				.range(0, tableSize)
				.forEach(i -> table[i].setData(factory.apply(i)));
	}

	/**
	 * Stream all the hash table entries.
	 *
	 * @return the entry stream
	 */
	public Stream<Entry<T>> stream() {
		return IntStream
				.range(0, tableSize)
				.mapToObj(i -> table[i]);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		double used = getUsedEntriesCount();
		double unused = getUnusedEntriesCount();
		double total = size();

		double usedPerc = (used / total) * 100.;
		double unusedPerc = (unused / total) * 100.;

		return getClass().getSimpleName()
				+ " [used=%d (%.0f%%), available=%d (%.0f%%), total=%d, sticky=%s%s]"
						.formatted(
								(long) used, usedPerc,
								(long) unused, unusedPerc,
								(long) total,
								stickyData,
								toStringExtra());
	}

	/**
	 * Allows subclasses to add additional attributes to include in toString()
	 * output.
	 *
	 * @return additional string
	 */
	protected String toStringExtra() {
		return "";
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public final Iterator<Entry<T>> iterator() {
		return new Iterator<>() {
			int index = 0;

			@Override
			public boolean hasNext() {
				return index < tableSize;
			}

			@Override
			public Entry<T> next() {
				return table[index++];
			}

		};
	}
}
