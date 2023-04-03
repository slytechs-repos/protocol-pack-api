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

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.slytechs.jnet.runtime.util.collection.LinkedListSupport;

/**
 * A ring executor with multiple slots, where a 'resource' is passed around the
 * ring. Each slot, is accessed by 2 threads. The resource object is passed from
 * an outside thread (usually another slot's thread). The resource is then
 * exchanged with a slot, worker thread or user thread that is using the
 * get/poll methods to perform processing on the data. The resource is exchanged
 * via a concurrency mechanism (ie. Exchanger, BlockingQueue, etc). Once the
 * slot's thread is done processing the resource, the resource must be
 * 'released' back to the slot, so that the resource can be passed on to the
 * next slot in the ring executor.
 * 
 * <p>
 * The ring executor allows each slot to have a dedicated threaded processor get
 * access to a resource, process it, then release it. Lastly the resource is
 * then passed on to the next slot in the ring executor until some slot, stops
 * forwarding the resource. Even though this is a ring, there is a first slot
 * and a last slot. First slot is where the resource is injected into the ring
 * and the last slot is where the processing of the resource stops.
 * </p>
 * <p>
 * <code>
 * [Pcap: read packet]
 * [slot-0: user process packet]
 * [slot-1: transmit packet]
 * </code>
 * </p>
 * or
 * <p>
 * <code>
 * [Pcap-en0: read packet]
 * [Pcap-en1: read packet]
 * [slot-0: add packet to buffer segment]
 * [slot-1: 
 * </code>
 * </p>
 * 
 * <pre>
 *
	          reader page
		           |
		           v
		         +---+
		         |   |------+
	   	         +---+      |
	 		                |
			                v
	    +---+    +---+    +---+    +---+
    <---|   |--->|   |-H->|   |--->|   |--->
    --->|   |<---|   |<---|   |<---|   |<---
	    +---+    +---+    +---+    +---+
 * </pre>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class RingExecutor<T> {

	static class Entry<T>
			extends LinkedListSupport.LinkedEntry<T, Entry<T>>
			implements ResourceStatus<T> {

		private final Exchanger<T> exchange = new Exchanger<>();

		void done() throws InterruptedException {
			/* Called from the slot worker thread */
			exchange.exchange(value());
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.concurrent.RingExecutor.ResourceStatus#await()
		 */
		@Override
		public T await() throws InterruptedException {
			return exchange.exchange(null);
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.concurrent.RingExecutor.ResourceStatus#await(long,
		 *      java.util.concurrent.TimeUnit)
		 */
		@Override
		public T await(long timeout, TimeUnit unit) throws InterruptedException {
			try {
				return exchange.exchange(null, timeout, unit);
			} catch (TimeoutException e) {
				return null;
			}
		}

	}

	public interface ResourceStatus<T> {
		T await() throws InterruptedException;

		T await(long timeout, TimeUnit unit) throws InterruptedException;
	}

	public static abstract class Slot<T> {

		protected RingExecutor<T> ring;
		protected Slot<T> nextSlot;

		protected long acceptedCounter = 0;
		protected long droppedCounter = 0;
		protected long processedCounter = 0;

		private final LinkedListSupport<T, Entry<T>> usedEntries = new LinkedListSupport<>();

		public abstract boolean accept(Entry<T> entry);

		protected final void addResource(Entry<T> entry) {
			usedEntries.addFirst(entry);
		}

		public boolean next(Entry<T> entry) throws InterruptedException {
			if (nextSlot == null)
				ring.release(entry);

			return nextSlot.accept(entry);
		}

		public final void release(T resource) throws IllegalStateException, InterruptedException {
			Entry<T> entry = removeResource(resource);
			if (entry == null)
				throw new IllegalStateException("invalid resource [%s]"
						.formatted(resource));

			if (nextSlot == null) {
				ring.release(entry);
				return;
			}

			if (nextSlot.accept(entry)) {
				acceptedCounter++;
				if (acceptedCounter < 0)
					acceptedCounter = 1;
			} else {
				droppedCounter++;
				if (droppedCounter < 0)
					droppedCounter = 1;
			}
		}

		/**
		 * @param resource
		 * @return
		 */
		private Entry<T> removeResource(T resource) {
			return usedEntries.remove(resource);
		}

	}

	protected final Slot<T>[] slots;
	private final LinkedListSupport<T, Entry<T>> freeList;
	private final String name;

	@SafeVarargs
	public RingExecutor(String name, long capacity, Slot<T>... slots) {
		this.name = name;
		this.slots = initializeSlots(slots);

		this.freeList = LinkedListSupport
				.allocateToCapacity(capacity, Entry<T>::new)
				.usingEmptyListMessage("out of space");
	}

	public ResourceStatus<T> accept(T resource) {
		Entry<T> r = wrap(resource);

		slots[0].accept(r);

		return r;
	}

	protected Slot<T>[] initializeSlots(Slot<T>[] slots) {
		if (slots.length == 0)
			return slots;

		slots[0].ring = this;
		slots[0].nextSlot = null;

		for (int i = 0; i < slots.length - 1; i++) {
			Slot<T> slot0 = slots[i + 0];
			Slot<T> slot1 = slots[i + 1];

			slot0.nextSlot = slot1;
			slot1.ring = this;
			slot1.nextSlot = null;
		}

		return slots;
	}

	protected void release(Entry<T> resource) throws InterruptedException {
		resource.done();

		unwrap(resource);
	}

	protected T unwrap(Entry<T> entry) {
		T t = entry.value();

		entry.value(null);

		freeList.addFirst(entry);

		return t;
	}

	protected Entry<T> wrap(T resource) {
		Entry<T> entry = freeList.removeFirst();
		entry.value(resource);

		return entry;
	}

	@Override
	public String toString() {
		return "%s []".formatted(name);
	}
}
