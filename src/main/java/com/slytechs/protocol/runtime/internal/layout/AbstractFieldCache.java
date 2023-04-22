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
package com.slytechs.protocol.runtime.internal.layout;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.slytechs.protocol.runtime.util.MemoryUnit;

/**
 * The Class AbstractFieldCache.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class AbstractFieldCache {

	/** The Constant DEFAULT_CACHE_SIZE. */
	private static final int DEFAULT_CACHE_SIZE = MemoryUnit.KILOBYTES.toBytesAsInt(1);

	/**
	 * The Class Entry.
	 */
	public static class Entry {
		
		/** The source. */
		Reference<Object> source;
		
		/** The offset. */
		long offset;
		
		/** The cached value. */
		protected Object cachedValue;

		/**
		 * Instantiates a new entry.
		 */
		public Entry() {
		}

		/**
		 * Checks if is valid.
		 *
		 * @return true, if is valid
		 */
		public boolean isValid() {
			return (source != null);
		}

		/**
		 * Save.
		 *
		 * @param <T>    the generic type
		 * @param source the source
		 * @param offset the offset
		 * @param value  the value
		 * @return the t
		 */
		public <T> T save(Object source, long offset, T value) {
			this.source = new WeakReference<>(source);
			this.offset = offset;
			this.cachedValue = value;

			return value;
		}
	}

	/** The cache. */
	private final Entry[] cache;

	/**
	 * Instantiates a new abstract field cache.
	 */
	public AbstractFieldCache() {
		this(DEFAULT_CACHE_SIZE);
	}

	/**
	 * Instantiates a new abstract field cache.
	 *
	 * @param cacheSize the cache size
	 */
	public AbstractFieldCache(int cacheSize) {
		this(cacheSize, Entry::new);
	}

	/**
	 * Instantiates a new abstract field cache.
	 *
	 * @param cacheSize the cache size
	 * @param supplier  the supplier
	 */
	public AbstractFieldCache(int cacheSize, Supplier<? extends Entry> supplier) {
		this.cache = new Entry[cacheSize];
		IntStream.range(0, cacheSize)
				.forEach(i -> cache[i] = new Entry());
	}

	/**
	 * Lookup.
	 *
	 * @param source    the source
	 * @param bitOffset the bit offset
	 * @return the entry
	 */
	protected Entry lookup(Object source, long bitOffset) {
		int hash = System.identityHashCode(source) ^ Long.hashCode(bitOffset);

		Entry entry = cache[hash % cache.length];
		if ((entry.source != null) && (entry.source.get() != source) && (entry.offset != bitOffset))
			entry.source = null;

		return entry;
	}

}
