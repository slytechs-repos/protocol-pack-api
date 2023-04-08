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
		 * Lookup.
		 *
		 * @param data   the data
		 * @param offset the offset
		 * @return the number entry
		 * @see com.slytechs.protocol.runtime.internal.layout.AbstractFieldCache#lookup(java.lang.Object,
		 *      long)
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
	 * Gets the byte at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the byte at offset
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#getByteAtOffset(java.lang.Object,
	 *      long)
	 */
	@Override
	public byte getByteAtOffset(Object data, long byteOffset) {
		NumberCache.NumberEntry entry = cache.lookup(data, byteOffset);

		return entry.isValid()
				? entry.value().byteValue()
				: entry.save(data, byteOffset, carrier.getByteAtOffset(data, byteOffset));
	}

	/**
	 * Gets the int at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the int at offset
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#getIntAtOffset(java.lang.Object,
	 *      long, boolean)
	 */
	@Override
	public int getIntAtOffset(Object data, long byteOffset, boolean big) {
		NumberCache.NumberEntry entry = cache.lookup(data, byteOffset);

		return entry.isValid()
				? entry.value().intValue()
				: entry.save(data, byteOffset, carrier.getIntAtOffset(data, byteOffset, big));
	}

	/**
	 * Gets the long at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the long at offset
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#getLongAtOffset(java.lang.Object,
	 *      long, boolean)
	 */
	@Override
	public long getLongAtOffset(Object data, long byteOffset, boolean big) {
		NumberCache.NumberEntry entry = cache.lookup(data, byteOffset);

		return entry.isValid()
				? entry.value().longValue()
				: entry.save(data, byteOffset, carrier.getLongAtOffset(data, byteOffset, big));
	}

	/**
	 * Gets the short at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the short at offset
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#getShortAtOffset(java.lang.Object,
	 *      long, boolean)
	 */
	@Override
	public short getShortAtOffset(Object data, long byteOffset, boolean big) {
		NumberCache.NumberEntry entry = cache.lookup(data, byteOffset);

		return entry.isValid()
				? entry.value().shortValue()
				: entry.save(data, byteOffset, carrier.getShortAtOffset(data, byteOffset, big));
	}

	/**
	 * Sets the byte at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the byte
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#setByteAtOffset(byte,
	 *      java.lang.Object, long, boolean)
	 */
	@Override
	public byte setByteAtOffset(byte value, Object data, long byteOffset, boolean big) {
		return cache.lookup(data, byteOffset)
				.save(data, byteOffset, carrier.setByteAtOffset(value, data, byteOffset, big));
	}

	/**
	 * Sets the int at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the int
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#setIntAtOffset(int,
	 *      java.lang.Object, long, boolean)
	 */
	@Override
	public int setIntAtOffset(int value, Object data, long byteOffset, boolean big) {
		return cache.lookup(data, byteOffset)
				.save(data, byteOffset, carrier.setIntAtOffset(value, data, byteOffset, big));
	}

	/**
	 * Sets the long at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#setLongAtOffset(long,
	 *      java.lang.Object, long, boolean)
	 */
	@Override
	public long setLongAtOffset(long value, Object data, long byteOffset, boolean big) {
		return cache.lookup(data, byteOffset)
				.save(data, byteOffset, carrier.setLongAtOffset(value, data, byteOffset, big));
	}

	/**
	 * Sets the short at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the short
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#setShortAtOffset(short,
	 *      java.lang.Object, long, boolean)
	 */
	@Override
	public short setShortAtOffset(short value, Object data, long byteOffset, boolean big) {
		return cache.lookup(data, byteOffset)
				.save(data, byteOffset, carrier.setShortAtOffset(value, data, byteOffset, big));
	}

}