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

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.slytechs.jnet.runtime.internal.util.collection.LinkedListSupport;

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
 * 	          reader page
 * 		           |
 * 		           v
 * 		         +---+
 * 		         |   |------+
 * 	   	         +---+      |
 * 	 		                |
 * 			                v
 * 	    +---+    +---+    +---+    +---+
 *     <---|   |--->|   |-H->|   |--->|   |--->
 *     --->|   |<---|   |<---|   |<---|   |<---
 * 	    +---+    +---+    +---+    +---+
 * </pre>
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 * @param <T> the generic type
 */
public class RingExecutor<T> {

	/**
	 * The Class Entry.
	 *
	 * @param <T> the generic type
	 */
	static class Entry<T>
			extends LinkedListSupport.LinkedEntry<T, Entry<T>>
			implements ResourceStatus<T> {

		/** The exchange. */
		private final Exchanger<T> exchange = new Exchanger<>();

		/**
		 * Done.
		 *
		 * @throws InterruptedException the interrupted exception
		 */
		void done() throws InterruptedException {
			/* Called from the slot worker thread */
			exchange.exchange(value());
		}

		/**
		 * Await.
		 *
		 * @return the t
		 * @throws InterruptedException the interrupted exception
		 * @see com.slytechs.jnet.runtime.internal.concurrent.RingExecutor.ResourceStatus#await()
		 */
		@Override
		public T await() throws InterruptedException {
			return exchange.exchange(null);
		}

		/**
		 * Await.
		 *
		 * @param timeout the timeout
		 * @param unit    the unit
		 * @return the t
		 * @throws InterruptedException the interrupted exception
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

	/**
	 * The Interface ResourceStatus.
	 *
	 * @param <T> the generic type
	 */
	public interface ResourceStatus<T> {
		
		/**
		 * Await.
		 *
		 * @return the t
		 * @throws InterruptedException the interrupted exception
		 */
		T await() throws InterruptedException;

		/**
		 * Await.
		 *
		 * @param timeout the timeout
		 * @param unit    the unit
		 * @return the t
		 * @throws InterruptedException the interrupted exception
		 */
		T await(long timeout, TimeUnit unit) throws InterruptedException;
	}

	/**
	 * The Class Slot.
	 *
	 * @param <T> the generic type
	 */
	public static abstract class Slot<T> {

		/** The ring. */
		protected RingExecutor<T> ring;
		
		/** The next slot. */
		protected Slot<T> nextSlot;

		/** The accepted counter. */
		protected long acceptedCounter = 0;
		
		/** The dropped counter. */
		protected long droppedCounter = 0;
		
		/** The processed counter. */
		protected long processedCounter = 0;

		/** The used entries. */
		private final LinkedListSupport<T, Entry<T>> usedEntries = new LinkedListSupport<>();

		/**
		 * Accept.
		 *
		 * @param entry the entry
		 * @return true, if successful
		 */
		public abstract boolean accept(Entry<T> entry);

		/**
		 * Adds the resource.
		 *
		 * @param entry the entry
		 */
		protected final void addResource(Entry<T> entry) {
			usedEntries.addFirst(entry);
		}

		/**
		 * Next.
		 *
		 * @param entry the entry
		 * @return true, if successful
		 * @throws InterruptedException the interrupted exception
		 */
		public boolean next(Entry<T> entry) throws InterruptedException {
			if (nextSlot == null)
				ring.release(entry);

			return nextSlot.accept(entry);
		}

		/**
		 * Release.
		 *
		 * @param resource the resource
		 * @throws IllegalStateException the illegal state exception
		 * @throws InterruptedException  the interrupted exception
		 */
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
		 * Removes the resource.
		 *
		 * @param resource the resource
		 * @return the entry
		 */
		private Entry<T> removeResource(T resource) {
			return usedEntries.remove(resource);
		}

	}

	/** The slots. */
	protected final Slot<T>[] slots;
	
	/** The free list. */
	private final LinkedListSupport<T, Entry<T>> freeList;
	
	/** The name. */
	private final String name;

	/**
	 * Instantiates a new ring executor.
	 *
	 * @param name     the name
	 * @param capacity the capacity
	 * @param slots    the slots
	 */
	@SafeVarargs
	public RingExecutor(String name, long capacity, Slot<T>... slots) {
		this.name = name;
		this.slots = initializeSlots(slots);

		this.freeList = LinkedListSupport
				.allocateToCapacity(capacity, Entry<T>::new)
				.usingEmptyListMessage("out of space");
	}

	/**
	 * Accept.
	 *
	 * @param resource the resource
	 * @return the resource status
	 */
	public ResourceStatus<T> accept(T resource) {
		Entry<T> r = wrap(resource);

		slots[0].accept(r);

		return r;
	}

	/**
	 * Initialize slots.
	 *
	 * @param slots the slots
	 * @return the slot[]
	 */
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

	/**
	 * Release.
	 *
	 * @param resource the resource
	 * @throws InterruptedException the interrupted exception
	 */
	protected void release(Entry<T> resource) throws InterruptedException {
		resource.done();

		unwrap(resource);
	}

	/**
	 * Unwrap.
	 *
	 * @param entry the entry
	 * @return the t
	 */
	protected T unwrap(Entry<T> entry) {
		T t = entry.value();

		entry.value(null);

		freeList.addFirst(entry);

		return t;
	}

	/**
	 * Wrap.
	 *
	 * @param resource the resource
	 * @return the entry
	 */
	protected Entry<T> wrap(T resource) {
		Entry<T> entry = freeList.removeFirst();
		entry.value(resource);

		return entry;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "%s []".formatted(name);
	}
}
