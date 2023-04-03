/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.runtime.util.collection;

import java.util.function.Supplier;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class LinkedListSupport<T, E extends LinkedListSupport.LinkedEntry<T, E>> {

	public static abstract class LinkedEntry<T, E extends LinkedEntry<T, E>> {
		E next;
		E prev;
		Object owner;

		T value;

		public final T value() {
			return value;
		}

		public final T value(T t) {
			return this.value = t;
		}
	}

	public static <T, E extends LinkedListSupport.LinkedEntry<T, E>> LinkedListSupport<T, E> allocateToCapacity(
			long capacity,
			Supplier<E> e) {
		LinkedListSupport<T, E> list = new LinkedListSupport<>(capacity);

		for (long i = 0; i < capacity; i++)
			list.addFirst(e.get());

		return list;
	}

	private final long maxCapacity;
	private long size;
	private E head;
	private String emptyListMessage = "empty linked list";

	public LinkedListSupport() {
		this.maxCapacity = Integer.MAX_VALUE;
	}

	public LinkedListSupport(long maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public final LinkedListSupport<T, E> usingEmptyListMessage(String newMessage) {
		this.emptyListMessage = newMessage;

		return this;
	}

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

	public long size() {
		return size;
	}

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

	public boolean isEmpty() {
		return size == 0;
	}

	public E find(E e) {
		return (e != null) && (e.owner == this)
				? e
				: null;
	}

	public E find(T t) {
		E it = head;

		while (it != null) {
			if (it.value == t)
				break;

			it = it.next;
		}

		return it;
	}

	public E remove(T e) {
		return remove(find(e));
	}

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
