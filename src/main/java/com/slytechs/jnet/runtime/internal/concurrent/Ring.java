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
package com.slytechs.jnet.runtime.internal.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class Ring {

	interface Guardian {
		boolean bulk(int count);

		int burst(int count);
	}

	public class SyncGuardian implements Guardian {

		/**
		 * @see com.slytechs.jnet.runtime.internal.concurrent.Ring.Guardian#bulk()
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
		 * @see com.slytechs.jnet.runtime.internal.concurrent.Ring.Guardian#burst(int)
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

	private final AtomicInteger consumerTail = new AtomicInteger();
	private final AtomicInteger consumerHead = new AtomicInteger();
	private final AtomicInteger producerTail = new AtomicInteger();
	private final AtomicInteger producerHead = new AtomicInteger();
	private final AtomicInteger size = new AtomicInteger();
	private final Guardian consumerGuard;
	private final Guardian producerGuard;
	private final int capacity;

	protected Ring(int capacity) {
		this(capacity, null, null);
	}

	protected Ring(int capacity, Guardian consumerGuard, Guardian producerGuard) {
		this.capacity = capacity;
		this.consumerGuard = consumerGuard;
		this.producerGuard = producerGuard;

	}

	protected int consumer() {
		throw new UnsupportedOperationException();
	}

	protected int consumer(int count) {
		throw new UnsupportedOperationException();
	}

	protected int producer() {
		throw new UnsupportedOperationException();
	}

	protected int producer(int count) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return size.get();
	}

	public boolean isEmpty() {
		return size.get() == 0;
	}
}
