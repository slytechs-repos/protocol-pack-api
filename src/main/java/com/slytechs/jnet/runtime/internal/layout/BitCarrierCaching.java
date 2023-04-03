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

/**
 * The Class BitCarrierCaching.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class BitCarrierCaching implements BitCarrier {

	/**
	 * The Class NumberCache.
	 */
	static class NumberCache extends AbstractFieldCache {

		/**
		 * The Class NumberEntry.
		 */
		public static class NumberEntry extends Entry {

			/**
			 * Value.
			 *
			 * @return the number
			 */
			public Number value() {
				return (Number) cachedValue;
			}
		}

		/**
		 * @see com.slytechs.jnet.runtime.internal.layout.AbstractFieldCache#lookup(java.lang.Object, long)
		 */
		public NumberEntry lookup(Object data, long offset) {
			return (NumberEntry) super.lookup(data, offset);
		}

	}

	/** The cache. */
	private final NumberCache cache;
	
	/** The carrier. */
	private final BitCarrier carrier;

	/**
	 * Instantiates a new bit carrier caching.
	 *
	 * @param carrier the carrier
	 */
	BitCarrierCaching(BitCarrier carrier) {
		this.carrier = carrier;
		this.cache = new NumberCache();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getByteAtOffset(java.lang.Object, long)
	 */
	@Override
	public byte getByteAtOffset(Object data, long byteOffset) {
		NumberCache.NumberEntry entry = cache.lookup(data, byteOffset);

		return entry.isValid()
				? entry.value().byteValue()
				: entry.save(data, byteOffset, carrier.getByteAtOffset(data, byteOffset));
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getIntAtOffset(java.lang.Object, long, boolean)
	 */
	@Override
	public int getIntAtOffset(Object data, long byteOffset, boolean big) {
		NumberCache.NumberEntry entry = cache.lookup(data, byteOffset);

		return entry.isValid()
				? entry.value().intValue()
				: entry.save(data, byteOffset, carrier.getIntAtOffset(data, byteOffset, big));
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getLongAtOffset(java.lang.Object, long, boolean)
	 */
	@Override
	public long getLongAtOffset(Object data, long byteOffset, boolean big) {
		NumberCache.NumberEntry entry = cache.lookup(data, byteOffset);

		return entry.isValid()
				? entry.value().longValue()
				: entry.save(data, byteOffset, carrier.getLongAtOffset(data, byteOffset, big));
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getShortAtOffset(java.lang.Object, long, boolean)
	 */
	@Override
	public short getShortAtOffset(Object data, long byteOffset, boolean big) {
		NumberCache.NumberEntry entry = cache.lookup(data, byteOffset);

		return entry.isValid()
				? entry.value().shortValue()
				: entry.save(data, byteOffset, carrier.getShortAtOffset(data, byteOffset, big));
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setByteAtOffset(byte, java.lang.Object, long, boolean)
	 */
	@Override
	public byte setByteAtOffset(byte value, Object data, long byteOffset, boolean big) {
		return cache.lookup(data, byteOffset)
				.save(data, byteOffset, carrier.setByteAtOffset(value, data, byteOffset, big));
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setIntAtOffset(int, java.lang.Object, long, boolean)
	 */
	@Override
	public int setIntAtOffset(int value, Object data, long byteOffset, boolean big) {
		return cache.lookup(data, byteOffset)
				.save(data, byteOffset, carrier.setIntAtOffset(value, data, byteOffset, big));
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setLongAtOffset(long, java.lang.Object, long, boolean)
	 */
	@Override
	public long setLongAtOffset(long value, Object data, long byteOffset, boolean big) {
		return cache.lookup(data, byteOffset)
				.save(data, byteOffset, carrier.setLongAtOffset(value, data, byteOffset, big));
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setShortAtOffset(short, java.lang.Object, long, boolean)
	 */
	@Override
	public short setShortAtOffset(short value, Object data, long byteOffset, boolean big) {
		return cache.lookup(data, byteOffset)
				.save(data, byteOffset, carrier.setShortAtOffset(value, data, byteOffset, big));
	}

}