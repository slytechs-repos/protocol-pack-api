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

import java.util.function.Supplier;

/**
 * The Class LinkedListSupport.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 * @param <T> the generic type
 * @param <E> the element type
 */
public class LinkedListSupport<T, E extends LinkedListSupport.LinkedEntry<T, E>> {

	/**
	 * The Class LinkedEntry.
	 *
	 * @param <T> the generic type
	 * @param <E> the element type
	 */
	public static abstract class LinkedEntry<T, E extends LinkedEntry<T, E>> {
		
		/** The next. */
		E next;
		
		/** The prev. */
		E prev;
		
		/** The owner. */
		Object owner;

		/** The value. */
		T value;

		/**
		 * Value.
		 *
		 * @return the t
		 */
		public final T value() {
			return value;
		}

		/**
		 * Value.
		 *
		 * @param t the t
		 * @return the t
		 */
		public final T value(T t) {
			return this.value = t;
		}
	}

	/**
	 * Allocate to capacity.
	 *
	 * @param <T>      the generic type
	 * @param <E>      the element type
	 * @param capacity the capacity
	 * @param e        the e
	 * @return the linked list support
	 */
	public static <T, E extends LinkedListSupport.LinkedEntry<T, E>> LinkedListSupport<T, E> allocateToCapacity(
			long capacity,
			Supplier<E> e) {
		LinkedListSupport<T, E> list = new LinkedListSupport<>(capacity);

		for (long i = 0; i < capacity; i++)
			list.addFirst(e.get());

		return list;
	}

	/** The max capacity. */
	private final long maxCapacity;
	
	/** The size. */
	private long size;
	
	/** The head. */
	private E head;
	
	/** The empty list message. */
	private String emptyListMessage = "empty linked list";

	/**
	 * Instantiates a new linked list support.
	 */
	public LinkedListSupport() {
		this.maxCapacity = Integer.MAX_VALUE;
	}

	/**
	 * Instantiates a new linked list support.
	 *
	 * @param maxCapacity the max capacity
	 */
	public LinkedListSupport(long maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	/**
	 * Using empty list message.
	 *
	 * @param newMessage the new message
	 * @return the linked list support
	 */
	public final LinkedListSupport<T, E> usingEmptyListMessage(String newMessage) {
		this.emptyListMessage = newMessage;

		return this;
	}

	/**
	 * Adds the first.
	 *
	 * @param e the e
	 */
	public void addFirst(E e) {
		if (size == maxCapacity)
			throw new IllegalStateException("at max capacity [%d]"
					.formatted(maxCapacity));

		if (head != null)
			head.prev = e;

		e.owner = this;
		e.next = head;
		head = e;

		size++;
	}

	/**
	 * Size.
	 *
	 * @return the long
	 */
	public long size() {
		return size;
	}

	/**
	 * Removes the first.
	 *
	 * @return the e
	 */
	public E removeFirst() {
		if (size == 0)
			throw new IllegalStateException(emptyListMessage);

		E e = head;

		head = e.next;
		if (head != null)
			head.prev = null;

		e.next = null;
		e.prev = null;

		size--;

		return e;
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Find.
	 *
	 * @param e the e
	 * @return the e
	 */
	public E find(E e) {
		return (e != null) && (e.owner == this)
				? e
				: null;
	}

	/**
	 * Find.
	 *
	 * @param t the t
	 * @return the e
	 */
	public E find(T t) {
		E it = head;

		while (it != null) {
			if (it.value == t)
				break;

			it = it.next;
		}

		return it;
	}

	/**
	 * Removes the.
	 *
	 * @param e the e
	 * @return the e
	 */
	public E remove(T e) {
		return remove(find(e));
	}

	/**
	 * Removes the.
	 *
	 * @param e the e
	 * @return the e
	 */
	public E remove(E e) {
		if (e == null || e.owner != this)
			return null;

		if (size == 0)
			throw new IllegalStateException("empty linked list");

		if (e == head)
			return removeFirst();

		E prev = e.prev;
		E next = e.next;

		prev.next = next;
		if (next != null)
			next.prev = prev;

		e.next = null;
		e.prev = null;
		e.owner = null;

		size--;

		return e;
	}

}
