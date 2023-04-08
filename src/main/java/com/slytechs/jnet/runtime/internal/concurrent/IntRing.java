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
package com.slytechs.jnet.runtime.internal.concurrent;

/**
 * The Class IntRing.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class IntRing extends Ring {

	/** The table. */
	private final int[] table;

	/**
	 * Instantiates a new int ring.
	 *
	 * @param size the size
	 */
	public IntRing(int size) {
		super(size);
		this.table = new int[size];
	}

	/**
	 * Enqueue.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean enqueue(int value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Bulk enqueue.
	 *
	 * @param values the values
	 * @return true, if successful
	 */
	public boolean bulkEnqueue(int... values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Burst enqueue.
	 *
	 * @param values the values
	 * @return the int
	 */
	public int burstEnqueue(int... values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Dequeue.
	 *
	 * @param dstOfSizeOne the dst of size one
	 * @return true, if successful
	 */
	public boolean dequeue(int[] dstOfSizeOne) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Bulk dequeue.
	 *
	 * @param count the count
	 * @param dst   the dst
	 * @return true, if successful
	 */
	public boolean bulkDequeue(int count, int[] dst) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Burst dequeue.
	 *
	 * @param count the count
	 * @param dst   the dst
	 * @return the int
	 */
	public int burstDequeue(int count, int[] dst) {
		throw new UnsupportedOperationException();
	}
}
