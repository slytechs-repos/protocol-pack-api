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
import java.util.stream.IntStream;

/**
 * Hash table based on Cuckoo hashing algorithm.
 * 
 * <p>
 * A hash table based on the cuckoo algorithm is a data structure that uses two
 * or more hash functions to map keys to indices in an array. The cuckoo hashing
 * algorithm is a form of open addressing that resolves collisions by rehashing
 * items to alternative hash tables until a vacancy is found.
 * </p>
 * <p>
 * In this algorithm, each key is associated with two or more hash values, and
 * the table contains multiple arrays or buckets. If a collision occurs in one
 * of the buckets, the algorithm looks for an empty slot in the alternate hash
 * table by checking if the slot at the hash value given by the next hash
 * function is empty. If there is an empty slot, the item is moved to the new
 * location, and the original slot is vacated. If there is no empty slot, the
 * item that is already in the new slot is moved to its alternate location, and
 * the algorithm tries to find a new vacant slot for the item being inserted.
 * </p>
 * <p>
 * This process of rehashing and relocating items continues until either the
 * item is successfully inserted, or a pre-determined maximum number of rehashes
 * has been reached. If the maximum number of rehashes is exceeded, the table is
 * rehashed with a larger size to accommodate the items, and the insertion
 * process is restarted.
 * </p>
 * <p>
 * Cuckoo hashing has a worst-case time complexity of O(1) for insert, delete,
 * and lookup operations, making it very efficient for large datasets. However,
 * the algorithm requires a significant amount of memory overhead to maintain
 * the multiple hash tables and can be complex to implement.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public final class CuckooHashTable<T> extends HashTable<T> {

	public static final int DEFAULT_ENTRIES_PER_BUCKET_COUNT = 16;

	private static class Bucket {

		private final BucketEntry[] bucketEntries;

		private Bucket(int indexCount, int bucketIndex, HashEntry<?>[] hashEntries) {
			this.bucketEntries = new BucketEntry[indexCount];

			int indexOffset = bucketIndex * indexCount;

			IntStream
					.range(0, indexCount)
					.forEach(i -> bucketEntries[i] = new BucketEntry(indexOffset + i, hashEntries));
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Bucket [entries=" + Arrays.toString(bucketEntries) + "]";
		}

	}

	private static class BucketEntry {

		/** The signature of a key. */
		private int signature;
		private final ByteBuffer key; // Shared reference with hash entry, for convenience

		/** The index into the hash entry table */
		private final int index;

		BucketEntry(int index, HashEntry<?>[] entries) {
			this.index = index;
			this.key = entries[index].key();
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "BucketEntry [signature=" + signature + ", index=" + index + "]";
		}
	}

	private final Bucket[] buckets;

	private final int bucketCount;

	private final int bucketBitmask;

	private final int indexesPerBucket;

	public CuckooHashTable() {
		this(DEFAULT_TABLE_SIZE, DEFAULT_ENTRIES_PER_BUCKET_COUNT);
	}

	public CuckooHashTable(int tableSize) {
		this(tableSize, DEFAULT_ENTRIES_PER_BUCKET_COUNT);
	}

	public CuckooHashTable(int tableSize, int indexesPerBucket) {
		super(tableSize);

		this.indexesPerBucket = indexesPerBucket;
		assert Integer.bitCount(indexesPerBucket) == 1 : "indexes per bucket must be a power of 2";

		this.bucketCount = tableSize / indexesPerBucket;
		assert (bucketCount & 1) == 0 : ""
				+ "table size not a power of 2 [%d]".formatted(bucketCount);

		// Turn into bitmask since we're a power 2 size/count
		this.bucketBitmask = bucketCount - 1;

		this.buckets = new Bucket[bucketCount];

		IntStream
				.range(0, bucketCount)
				.forEach(i -> buckets[i] = new Bucket(indexesPerBucket, i, super.table));
	}

	/**
	 * @see com.slytechs.protocol.runtime.hash.HashTable#add(java.nio.ByteBuffer,
	 *      long)
	 */
	@Override
	public int add(ByteBuffer key, T data, long hashcode) {
		int index = lookup(key, hashcode);
		if (index != -1)
			return index;
		
		index = getPrimaryBucketIndex(hashcode);

		BucketEntry empty = findEmptyAndEvictIfNeccessary(index);
		if (empty == null)
			return -1; // No more room in either bucket

		empty.signature = getShortSignature(hashcode);
		super.set(empty.index, key, data);

		return empty.index;
	}

	private BucketEntry findEmptyAndEvictIfNeccessary(int bucketIndex1) {
		Bucket bucket1 = buckets[bucketIndex1];

		int entryIndex1 = findEmptyIndex(bucket1);
		if (entryIndex1 != -1) // Found an empty
			return bucket1.bucketEntries[entryIndex1];

		// Evict first entry
		entryIndex1 = 0;
		BucketEntry evictee = bucket1.bucketEntries[entryIndex1];

		// Swap with empty entry in bucket2
		int bucketIndex2 = getAlternativeBucketIndex(bucketIndex1, evictee.signature);
		Bucket bucket2 = buckets[bucketIndex2];
		int entryIndex2 = findEmptyIndex(bucket2);
		if (entryIndex2 == -1)
			return null; // No more entries

		BucketEntry empty = bucket2.bucketEntries[entryIndex2];

		/* Swap empty with evictee entries between the 2 buckets */
		bucket1.bucketEntries[entryIndex1] = empty;
		bucket2.bucketEntries[entryIndex2] = evictee;

		return empty;
	}

	private int findEmptyIndex(Bucket bucket) {
		BucketEntry[] bucketEntries = bucket.bucketEntries;
		HashEntry<?>[] hashEntries = super.table;

		for (int i = 0; i < bucketEntries.length; i++) {
			if (hashEntries[bucketEntries[i].index].isEmpty())
				return i;
		}

		return -1;
	}

	private int getAlternativeBucketIndex(int index, int signature) {
		return (index ^ signature) & bucketBitmask;
	}

	private int getPrimaryBucketIndex(long hashcode) {
		return (int) (hashcode & bucketBitmask);
	}

	private int getShortSignature(long hashcode) {
		return (int) ((hashcode >> 16) & 0xFFFF);
	}

	/**
	 * @see com.slytechs.protocol.runtime.hash.HashTable#lookup(java.nio.ByteBuffer,
	 *      long)
	 */
	@Override
	public int lookup(ByteBuffer key, long hashcode) {
		int signature = getShortSignature(hashcode);
		int bucketIndex = getPrimaryBucketIndex(hashcode);

		int index = findEntryIndex(bucketIndex, key, signature);
		if (index != -1)
			return buckets[bucketIndex].bucketEntries[index].index;

		index = getAlternativeBucketIndex(index, signature);
		index = findEntryIndex(index, key, signature);
		if (index != -1)
			return buckets[bucketIndex].bucketEntries[index].index;

		return -1;
	}

	private int findEntryIndex(int index, ByteBuffer key, int signature) {
		Bucket bucket = buckets[index];

		for (int i = 0; i < indexesPerBucket; i++) {
			BucketEntry be = bucket.bucketEntries[i];
			HashEntry<?> te = table[be.index];
			if (!te.isEmpty() && (be.signature == signature) && matchKeys(be.key, key))
				return i;
		}

		return -1;
	}

	/**
	 * @see com.slytechs.protocol.runtime.hash.HashTable#toStringExtra()
	 */
	@Override
	protected String toStringExtra() {
		return ", buckets=%d/%d".formatted(bucketCount, indexesPerBucket);
	}
}
