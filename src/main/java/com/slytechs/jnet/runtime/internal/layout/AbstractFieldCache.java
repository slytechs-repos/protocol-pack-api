/*
 * MIT License
 * 
 * Copyright (c) 2020 Sly Technologies Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.slytechs.jnet.runtime.internal.layout;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.slytechs.jnet.runtime.util.MemoryUnit;

/**
 * The Class AbstractFieldCache.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class AbstractFieldCache {

	/** The Constant DEFAULT_CACHE_SIZE. */
	private static final int DEFAULT_CACHE_SIZE = MemoryUnit.KILOBYTES.toIntBytes(1);

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
