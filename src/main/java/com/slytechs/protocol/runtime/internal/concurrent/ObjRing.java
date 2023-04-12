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
package com.slytechs.protocol.runtime.internal.concurrent;

import java.util.function.IntFunction;

/**
 * The Class ObjRing.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 * @param <T> the generic type
 */
public class ObjRing<T> extends Ring {

	/** The table. */
	private final T[] table;
	
	/** The allocator. */
	private final IntFunction<T[]> allocator;

	/**
	 * Instantiates a new obj ring.
	 *
	 * @param size      the size
	 * @param allocator the allocator
	 */
	public ObjRing(int size, IntFunction<T[]> allocator) {
		super(size);
		this.table = allocator.apply(size);
		this.allocator = allocator;
	}

	/**
	 * Enqueue.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean enqueue(T value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Bulk enqueue.
	 *
	 * @param values the values
	 * @return true, if successful
	 */
	@SuppressWarnings("unchecked")
	public boolean bulkEnqueue(T... values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Burst enqueue.
	 *
	 * @param values the values
	 * @return the int
	 */
	@SuppressWarnings("unchecked")
	public int burstEnqueue(T... values) {
		throw new UnsupportedOperationException();
	}
}
