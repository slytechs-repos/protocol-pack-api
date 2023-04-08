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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class Ring.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class Ring {

	/**
	 * The Interface Guardian.
	 */
	interface Guardian {
		
		/**
		 * Bulk.
		 *
		 * @param count the count
		 * @return true, if successful
		 */
		boolean bulk(int count);

		/**
		 * Burst.
		 *
		 * @param count the count
		 * @return the int
		 */
		int burst(int count);
	}

	/**
	 * The Class SyncGuardian.
	 */
	public class SyncGuardian implements Guardian {

		/**
		 * Bulk.
		 *
		 * @param count the count
		 * @return true, if successful
		 * @see com.slytechs.protocol.runtime.internal.concurrent.Ring.Guardian#bulk()
		 */
		@Override
		public synchronized boolean bulk(int count) {
			int consTail = consumerTail.get();
			int prodHead = producerHead.get();
			int prodNext = producerHead.get();
			int size = prodHead - consTail;

			return true;
		}

		/**
		 * Burst.
		 *
		 * @param count the count
		 * @return the int
		 * @see com.slytechs.protocol.runtime.internal.concurrent.Ring.Guardian#burst(int)
		 */
		@Override
		public synchronized int burst(int count) {
			int consTail = consumerTail.get();
			int prodHead = producerHead.get();
			int prodNext = producerHead.get();
			int size = (prodHead - consTail) % capacity;

			return size > count ? count : size;
		}

	}

	/** The consumer tail. */
	private final AtomicInteger consumerTail = new AtomicInteger();
	
	/** The consumer head. */
	private final AtomicInteger consumerHead = new AtomicInteger();
	
	/** The producer tail. */
	private final AtomicInteger producerTail = new AtomicInteger();
	
	/** The producer head. */
	private final AtomicInteger producerHead = new AtomicInteger();
	
	/** The size. */
	private final AtomicInteger size = new AtomicInteger();
	
	/** The consumer guard. */
	private final Guardian consumerGuard;
	
	/** The producer guard. */
	private final Guardian producerGuard;
	
	/** The capacity. */
	private final int capacity;

	/**
	 * Instantiates a new ring.
	 *
	 * @param capacity the capacity
	 */
	protected Ring(int capacity) {
		this(capacity, null, null);
	}

	/**
	 * Instantiates a new ring.
	 *
	 * @param capacity      the capacity
	 * @param consumerGuard the consumer guard
	 * @param producerGuard the producer guard
	 */
	protected Ring(int capacity, Guardian consumerGuard, Guardian producerGuard) {
		this.capacity = capacity;
		this.consumerGuard = consumerGuard;
		this.producerGuard = producerGuard;

	}

	/**
	 * Consumer.
	 *
	 * @return the int
	 */
	protected int consumer() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Consumer.
	 *
	 * @param count the count
	 * @return the int
	 */
	protected int consumer(int count) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Producer.
	 *
	 * @return the int
	 */
	protected int producer() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Producer.
	 *
	 * @param count the count
	 * @return the int
	 */
	protected int producer(int count) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return size.get();
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return size.get() == 0;
	}
}
