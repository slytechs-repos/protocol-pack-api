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
package com.slytechs.jnet.jnetruntime.internal.util.collection;

import java.util.AbstractList;
import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.PrimitiveIterator.OfInt;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/**
 * The Class IntArrayList.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class IntArrayList extends AbstractList<Integer> implements IntList {

	/** The Constant DEFAULT_INITIAL_CAPACITY. */
	private static final int DEFAULT_INITIAL_CAPACITY = 10;

	/** The offset. */
	private int offset;

	/** The capacity. */
	private int capacity;

	/** The heap. */
	private int[] heap;

	/** The limit. */
	private int limit;

	/**
	 * Instantiates a new int array list.
	 */
	public IntArrayList() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * Instantiates a new int array list.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public IntArrayList(int initialCapacity) {
		this.heap = new int[initialCapacity];
		this.capacity = heap.length;
	}

	/**
	 * Instantiates a new int array list.
	 *
	 * @param c the c
	 */
	public IntArrayList(Collection<Integer> c) {
		this(c.size());

		addAll(c);
	}

	/**
	 * Instantiates a new int array list.
	 *
	 * @param c the c
	 */
	public IntArrayList(IntCollection c) {
		this(c.size());

		addAllInts(c);
	}

	/**
	 * Instantiates a new int array list.
	 *
	 * @param heap   the heap
	 * @param offset the offset
	 * @param end    the end
	 */
	public IntArrayList(int[] heap, int offset, int end) {
		this.heap = heap;
		this.offset = offset;
		this.limit = end;
	}

	/**
	 * Gets the.
	 *
	 * @param index the index
	 * @return the integer
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public Integer get(int index) {
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

		int[] newHeap = new int[capacity + len + DEFAULT_INITIAL_CAPACITY];

		System.arraycopy(heap, offset, newHeap, 0, limit - offset);

		capacity = newHeap.length;
		this.heap = newHeap;
	}

	/**
	 * Adds the int.
	 *
	 * @param e the e
	 * @return true, if successful
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#addInt(int)
	 */
	@Override
	public boolean addInt(int e) {
		assert limit <= capacity;
		if (limit == capacity)
			ensureRemaining(1);

		heap[limit++] = e;

		return true;
	}

	/**
	 * Adds the all ints.
	 *
	 * @param c the c
	 * @return true, if successful
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#addAllInts(com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection)
	 */
	@Override
	public boolean addAllInts(IntCollection c) {
		if (c.isEmpty())
			return false;

		ensureRemaining(c.size());

		if (c instanceof IntArrayList alist)
			System.arraycopy(alist, 0, heap, limit, alist.size());

		else
			for (PrimitiveIterator.OfInt it = c.iterator(); it.hasNext();)
				heap[limit++] = it.nextInt();

		return true;
	}

	/**
	 * Contains int.
	 *
	 * @param e the e
	 * @return true, if successful
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#containsInt(int)
	 */
	@Override
	public boolean containsInt(int e) {
		for (int i = offset; i < limit; i++)
			if (heap[offset + i] == e)
				return true;

		return false;
	}

	/**
	 * Contains all ints.
	 *
	 * @param c the c
	 * @return true, if successful
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#containsAllInts(com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection)
	 */
	@Override
	public boolean containsAllInts(IntCollection c) {
		int match = 0;
		for (PrimitiveIterator.OfInt it = c.iterator(); it.hasNext();) {
			int e = it.nextInt();

			for (int i = offset; i < limit; i++)
				if (heap[i] == e)
					match++;
		}

		return match == c.size();
	}

	/**
	 * Removes the all ints.
	 *
	 * @param c the c
	 * @return true, if successful
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#removeAllInts(com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection)
	 */
	@Override
	public boolean removeAllInts(IntCollection c) {
		return removeAll(c);
	}

	/**
	 * Removes the int if.
	 *
	 * @param filter the filter
	 * @return true, if successful
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#removeIntIf(java.util.function.IntPredicate)
	 */
	@Override
	public boolean removeIntIf(IntPredicate filter) {
		return removeIf(i -> filter.test(i));
	}

	/**
	 * Retain all ints.
	 *
	 * @param c the c
	 * @return true, if successful
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#retainAllInts(com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection)
	 */
	@Override
	public boolean retainAllInts(IntCollection c) {
		return retainAll(c);
	}

	/**
	 * To int array.
	 *
	 * @return the int[]
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#toIntArray()
	 */
	@Override
	public int[] toIntArray() {
		return toIntArray(new int[limit]);
	}

	/**
	 * To int array.
	 *
	 * @param array the array
	 * @return the int[]
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#toIntArray(int[])
	 */
	@Override
	public int[] toIntArray(int[] array) {
		System.arraycopy(heap, offset, array, 0, limit - offset);

		return array;
	}

	/**
	 * To int array.
	 *
	 * @param generator the generator
	 * @return the int[]
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#toIntArray(java.util.function.IntFunction)
	 */
	@Override
	public int[] toIntArray(IntFunction<int[]> generator) {
		return toIntArray(generator.apply(limit));
	}

	/**
	 * Iterator.
	 *
	 * @return the of int
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#iterator()
	 */
	@Override
	public OfInt iterator() {
		return new OfInt() {

			int i = offset;

			@Override
			public boolean hasNext() {
				return i < limit;
			}

			@Override
			public int nextInt() {
				return heap[i++];
			}

			@Override
			public void remove() {
				IntArrayList.this.remove(--i - offset);
			}

		};
	}

	/**
	 * Int stream.
	 *
	 * @return the int stream
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#intStream()
	 */
	@Override
	public IntStream intStream() {
		return IntStream.of(heap).skip(offset).limit(limit);
	}

	/**
	 * Spliterator.
	 *
	 * @return the java.util. spliterator. of int
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection#spliterator()
	 */
	@Override
	public java.util.Spliterator.OfInt spliterator() {
		return intStream().spliterator();
	}

	/**
	 * Sub list.
	 *
	 * @param fromIndex the from index
	 * @param toIndex   the to index
	 * @return the int array list
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public IntArrayList subList(int fromIndex, int toIndex) {
		return new IntArrayList(heap, fromIndex, toIndex);
	}

	/**
	 * Adds the int.
	 *
	 * @param index the index
	 * @param e     the e
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntList#addInt(int,
	 *      int)
	 */
	@Override
	public void addInt(int index, int e) {
		index += offset;

		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Adds the all ints.
	 *
	 * @param index the index
	 * @param c     the c
	 * @return true, if successful
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntList#addAllInts(int,
	 *      com.slytechs.jnet.jnetruntime.internal.util.collection.IntCollection)
	 */
	@Override
	public boolean addAllInts(int index, IntCollection c) {
		index += offset;

		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Index of int.
	 *
	 * @param e the e
	 * @return the int
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntList#indexOfInt(int)
	 */
	@Override
	public int indexOfInt(int e) {
		for (int i = offset; i < limit; i++)
			if (heap[i] == e)
				return i - offset;

		return -1;
	}

	/**
	 * Last index of int.
	 *
	 * @param e the e
	 * @return the int
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntList#lastIndexOfInt(int)
	 */
	@Override
	public int lastIndexOfInt(int e) {
		for (int i = limit - 1; i >= offset; i--)
			if (heap[i] == e)
				return i;

		return -1;
	}

	/**
	 * Removes the int.
	 *
	 * @param index the index
	 * @return true, if successful
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntList#removeInt(int)
	 */
	@Override
	public boolean removeInt(int index) {
		if (isEmpty())
			return false;

		index += offset;

		System.arraycopy(heap, index + 1, heap, index, (limit - 1) - offset);

		return true;
	}

	/**
	 * Sets the.
	 *
	 * @param index the index
	 * @param e     the e
	 * @return the int
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntList#set(int,
	 *      int)
	 */
	@Override
	public int set(int index, int e) {
		index += offset;

		int r0 = heap[index];
		heap[index] = e;
		return r0;
	}

	/**
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see com.slytechs.jnet.jnetruntime.internal.util.collection.IntList#getInt(int)
	 */
	@Override
	public int getInt(int index) {
		index += offset;

		return heap[index];
	}

}
