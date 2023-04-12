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

import java.util.AbstractList;
import java.util.Collection;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.PrimitiveIterator.OfLong;
import java.util.function.IntFunction;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;

/**
 * The Class ArrayListLong.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class ArrayListLong extends AbstractList<Long> implements ListLong {

	/** The Constant DEFAULT_INITIAL_CAPACITY. */
	private static final int DEFAULT_INITIAL_CAPACITY = 10;

	/** The offset. */
	private int offset;
	
	/** The capacity. */
	private int capacity;
	
	/** The heap. */
	private long[] heap;
	
	/** The limit. */
	private int limit;

	/**
	 * Instantiates a new array list long.
	 */
	public ArrayListLong() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * Instantiates a new array list long.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public ArrayListLong(int initialCapacity) {
		this.heap = new long[initialCapacity];
		this.capacity = heap.length;
	}

	/**
	 * Instantiates a new array list long.
	 *
	 * @param c the c
	 */
	public ArrayListLong(Collection<Long> c) {
		this(c.size());

		addAll(c);
	}

	/**
	 * Instantiates a new array list long.
	 *
	 * @param c the c
	 */
	public ArrayListLong(LongCollection c) {
		this(c.size());

		addAllLongs(c);
	}

	/**
	 * Instantiates a new array list long.
	 *
	 * @param heap   the heap
	 * @param offset the offset
	 * @param end    the end
	 */
	public ArrayListLong(long[] heap, int offset, int end) {
		this.heap = heap;
		this.offset = offset;
		this.limit = end;
	}

	/**
	 * Gets the.
	 *
	 * @param index the index
	 * @return the long
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public Long get(int index) {
		index += offset;
		if (index < offset || index > limit)
			throw new IndexOutOfBoundsException();

		return heap[index];
	}

	/**
	 * Size.
	 *
	 * @return the int
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return limit - offset;
	}

	/**
	 * Ensure remaining.
	 *
	 * @param len the len
	 */
	private void ensureRemaining(int len) {
		if (limit + len < capacity)
			return;

		long[] newHeap = new long[capacity + len + DEFAULT_INITIAL_CAPACITY];

		System.arraycopy(heap, offset, newHeap, 0, limit - offset);

		capacity = newHeap.length;
		this.heap = newHeap;
	}

	/**
	 * Adds the long.
	 *
	 * @param e the e
	 * @return true, if successful
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#addLong(int)
	 */
	@Override
	public boolean addLong(long e) {
		assert limit <= capacity;
		ensureRemaining(1);

		heap[limit++] = e;

		return true;
	}

	/**
	 * Adds the all longs.
	 *
	 * @param c the c
	 * @return true, if successful
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#addAllLongs(com.slytechs.protocol.runtime.internal.util.collection.LongCollection)
	 */
	@Override
	public boolean addAllLongs(LongCollection c) {
		if (c.isEmpty())
			return false;

		ensureRemaining(c.size());

		if (c instanceof ArrayListLong alist)
			System.arraycopy(alist, 0, heap, limit, alist.size());

		else
			for (PrimitiveIterator.OfLong it = c.iterator(); it.hasNext();)
				heap[limit++] = it.nextLong();

		return true;
	}

	/**
	 * Contains long.
	 *
	 * @param e the e
	 * @return true, if successful
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#containsLong(int)
	 */
	@Override
	public boolean containsLong(long e) {
		for (int i = offset; i < limit; i++)
			if (heap[offset + i] == e)
				return true;

		return false;
	}

	/**
	 * Contains all longs.
	 *
	 * @param c the c
	 * @return true, if successful
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#containsAllLongs(com.slytechs.protocol.runtime.internal.util.collection.LongCollection)
	 */
	@Override
	public boolean containsAllLongs(LongCollection c) {
		int match = 0;
		for (PrimitiveIterator.OfLong it = c.iterator(); it.hasNext();) {
			long e = it.nextLong();

			for (int i = offset; i < limit; i++)
				if (heap[i] == e)
					match++;
		}

		return match == c.size();
	}

	/**
	 * Removes the all longs.
	 *
	 * @param c the c
	 * @return true, if successful
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#removeAllLongs(com.slytechs.protocol.runtime.internal.util.collection.LongCollection)
	 */
	@Override
	public boolean removeAllLongs(LongCollection c) {
		return removeAll(c);
	}

	/**
	 * Removes the long if.
	 *
	 * @param filter the filter
	 * @return true, if successful
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#removeLongIf(java.util.function.LongPredicate)
	 */
	@Override
	public boolean removeLongIf(LongPredicate filter) {
		return removeIf(i -> filter.test(i));
	}

	/**
	 * Retain all longs.
	 *
	 * @param c the c
	 * @return true, if successful
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#retainAllLongs(com.slytechs.protocol.runtime.internal.util.collection.LongCollection)
	 */
	@Override
	public boolean retainAllLongs(LongCollection c) {
		return retainAll(c);
	}

	/**
	 * To long array.
	 *
	 * @return the long[]
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#toLongArray()
	 */
	@Override
	public long[] toLongArray() {
		return toLongArray(new long[limit]);
	}

	/**
	 * To long array.
	 *
	 * @param array the array
	 * @return the long[]
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#toLongArray(int[])
	 */
	@Override
	public long[] toLongArray(long[] array) {
		System.arraycopy(heap, offset, array, 0, limit - offset);

		return array;
	}

	/**
	 * To long array.
	 *
	 * @param generator the generator
	 * @return the long[]
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#toLongArray(java.util.function.LongFunction)
	 */
	@Override
	public long[] toLongArray(IntFunction<long[]> generator) {
		return toLongArray(generator.apply(limit));
	}

	/**
	 * Iterator.
	 *
	 * @return the of long
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#iterator()
	 */
	@Override
	public OfLong iterator() {
		return new OfLong() {

			int i = offset;

			@Override
			public boolean hasNext() {
				return i < limit;
			}

			@Override
			public long nextLong() {
				return heap[i++];
			}

			@Override
			public void remove() {
				ArrayListLong.this.remove(--i - offset);
			}

		};
	}

	/**
	 * Long stream.
	 *
	 * @return the long stream
	 * @see com.slytechs.protocol.runtime.internal.util.collection.LongCollection#intStream()
	 */
	@Override
	public LongStream longStream() {
		return LongStream.of(heap).skip(offset).limit(limit);
	}

	/**
	 * Sub list.
	 *
	 * @param fromIndex the from index
	 * @param toIndex   the to index
	 * @return the array list long
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public ArrayListLong subList(int fromIndex, int toIndex) {
		return new ArrayListLong(heap, fromIndex, toIndex);
	}

	/**
	 * Adds the long.
	 *
	 * @param index the index
	 * @param e     the e
	 * @see com.slytechs.protocol.runtime.internal.util.collection.ListLong#addLong(int, int)
	 */
	@Override
	public void addLong(int index, long e) {
		ensureRemaining(1);
		index += offset;

		System.arraycopy(heap, index, heap, index + 1, limit - index);
		heap[index] = e;
		limit ++;
	}

	/**
	 * Space insert.
	 *
	 * @param index the index
	 * @param len   the len
	 */
	private void spaceInsert(int index, int len) {

		spaceInsert(index, 1);

		limit += len;
	}

	/**
	 * Space remove.
	 *
	 * @param index the index
	 * @param len   the len
	 */
	private void spaceRemove(int index, int len) {
		System.arraycopy(heap, index + len, heap, index, (limit - index) - len);
		limit -= len;
	}

	/**
	 * Adds the all longs.
	 *
	 * @param index the index
	 * @param c     the c
	 * @return true, if successful
	 * @see com.slytechs.protocol.runtime.internal.util.collection.ListLong#addAllLongs(int,
	 *      com.slytechs.protocol.runtime.internal.util.collection.LongCollection)
	 */
	@Override
	public boolean addAllLongs(int index, LongCollection c) {
		if (c.isEmpty())
			return false;

		index += offset;

		spaceInsert(index, c.size());

		if (c instanceof ArrayListLong list)
			for (int i = 0; i < c.size(); i++)
				heap[index++] = list.heap[i];

		else
			for (long e : c)
				heap[index++] = e;

		return true;
	}

	/**
	 * Index of long.
	 *
	 * @param e the e
	 * @return the int
	 * @see com.slytechs.protocol.runtime.internal.util.collection.ListLong#indexOfLong(int)
	 */
	@Override
	public int indexOfLong(long e) {
		for (int i = offset; i < limit; i++)
			if (heap[i] == e)
				return i - offset;

		return -1;
	}

	/**
	 * Last index of long.
	 *
	 * @param e the e
	 * @return the int
	 * @see com.slytechs.protocol.runtime.internal.util.collection.ListLong#lastIndexOfLong(int)
	 */
	@Override
	public int lastIndexOfLong(long e) {
		for (int i = limit - 1; i >= offset; i--)
			if (heap[i] == e)
				return i;

		return -1;
	}

	/**
	 * Removes the long.
	 *
	 * @param index the index
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.util.collection.ListLong#removeLong(int)
	 */
	@Override
	public long removeLong(int index) {
		Objects.checkIndex(offset, limit);

		index += offset;

		long r0 = heap[offset + index];

		spaceRemove(index, 1);

		return r0;
	}

	/**
	 * Sets the.
	 *
	 * @param index the index
	 * @param e     the e
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.util.collection.ListLong#set(int, int)
	 */
	@Override
	public long set(int index, long e) {
		index += offset;

		long r0 = heap[index];
		heap[index] = e;
		return r0;
	}

	/**
	 * Clone.
	 *
	 * @return the array list long
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ArrayListLong clone() {
		try {
			ArrayListLong clone = (ArrayListLong) super.clone();

			clone.heap = this.heap.clone();

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

}
