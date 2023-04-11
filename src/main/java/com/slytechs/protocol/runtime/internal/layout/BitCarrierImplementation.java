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

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.slytechs.protocol.runtime.internal.ArrayUtils;
import com.slytechs.protocol.runtime.internal.util.ByteArray;

/**
 * The Class BitCarrierImplementation.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@SuppressWarnings("preview")
class BitCarrierImplementation implements BitCarrier {

	/** The Constant MASK_08. */
	private static final long MASK_08 = 0xFFl;

	/** The Constant MASK_16. */
	private static final long MASK_16 = 0xFFFFl;

	/** The Constant MASK_32. */
	private static final long MASK_32 = 0xFFFFFFFFl;

	/** The Constant MASK_64. */
	private static final long MASK_64 = 0xFFFFFFFF_FFFFFFFFl;

	/**
	 * Gets the primitive.
	 *
	 * @param byteOffset the byte offset
	 * @param maxBytes   the max bytes
	 * @param data       the data
	 * @param mask       the mask
	 * @param big        TODO
	 * @return the primitive
	 */
	private static Number getPrimitive(long byteOffset, long maxBytes, Number data, long mask, boolean big) {
		if (byteOffset >= maxBytes)
			throw new IndexOutOfBoundsException();

		long r0 = data.longValue();
		r0 >>= (byteOffset * 8);
		r0 &= mask;

		return r0;
	}

	/**
	 * Sets the primitive.
	 *
	 * @param byteOffset the byte offset
	 * @param maxBytes   the max bytes
	 * @param data       the data
	 * @param mask       the mask
	 * @param value      the value
	 * @param big        TODO
	 * @return the number
	 */
	private static Number setPrimitive(long byteOffset,
			long maxBytes,
			Number data,
			long mask,
			Number value,
			boolean big) {
		if (byteOffset >= maxBytes)
			throw new IndexOutOfBoundsException();

		return (data.longValue() | (value.longValue() & mask) << (byteOffset * 8));
	}

	/** The Constant JAVA_SHORT_BIG. */
	// @formatter:off
	private final static ValueLayout.OfShort JAVA_SHORT_BIG    = ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withBitAlignment(8);
	
	/** The Constant JAVA_INT_BIG. */
	private final static ValueLayout.OfInt   JAVA_INT_BIG      = ValueLayout.JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN).withBitAlignment(8);
	
	/** The Constant JAVA_LONG_BIG. */
	private final static ValueLayout.OfLong  JAVA_LONG_BIG     = ValueLayout.JAVA_LONG.withOrder(ByteOrder.BIG_ENDIAN).withBitAlignment(8);
	
	/** The Constant JAVA_SHORT_LITTLE. */
	private final static ValueLayout.OfShort JAVA_SHORT_LITTLE = ValueLayout.JAVA_SHORT.withOrder(ByteOrder.LITTLE_ENDIAN).withBitAlignment(8);
	
	/** The Constant JAVA_INT_LITTLE. */
	private final static ValueLayout.OfInt   JAVA_INT_LITTLE   = ValueLayout.JAVA_INT.withOrder(ByteOrder.LITTLE_ENDIAN).withBitAlignment(8);
	
	/** The Constant JAVA_LONG_LITTLE. */
	private final static ValueLayout.OfLong  JAVA_LONG_LITTLE  = ValueLayout.JAVA_LONG.withOrder(ByteOrder.LITTLE_ENDIAN).withBitAlignment(8);

	/**
	 * Java byte.
	 *
	 * @return the value layout. of byte
	 */
	private ValueLayout.OfByte  JAVA_BYTE ()            { return ValueLayout.JAVA_BYTE; }
	
	/**
	 * Java short.
	 *
	 * @param big the big
	 * @return the value layout. of short
	 */
	private ValueLayout.OfShort JAVA_SHORT(boolean big) { return (big) ? JAVA_SHORT_BIG : JAVA_SHORT_LITTLE; }
	
	/**
	 * Java int.
	 *
	 * @param big the big
	 * @return the value layout. of int
	 */
	private ValueLayout.OfInt   JAVA_INT  (boolean big) { return (big) ? JAVA_INT_BIG   : JAVA_INT_LITTLE;	 }
	
	/**
	 * Java long.
	 *
	 * @param big the big
	 * @return the value layout. of long
	 */
	private ValueLayout.OfLong  JAVA_LONG (boolean big) { return (big) ? JAVA_LONG_BIG  : JAVA_LONG_LITTLE;	 }
	// @formatter:on

	/** The carrier size. */
	private final int carrierSize;

	/**
	 * Instantiates a new bit carrier implementation.
	 *
	 * @param context the context
	 */
	BitCarrierImplementation(BitFieldContext context) {
		this.carrierSize = (int) context.carrierSize();

		assert (carrierSize == 8) || (carrierSize == 16) || (carrierSize == 32) || (carrierSize == 64) : ""
				+ "Carrier size " + carrierSize;
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
		assert carrierSize == 8 : "wrong carrier bit size";

		return switch (data) {
		// @formatter:off
		case byte[] d        -> ArrayUtils.getByte(d, (int) byteOffset);
		case ByteArray d     -> ArrayUtils.getByte(d.array(), (int) byteOffset + d.arrayOffset());
		case ByteBuffer d    -> d.get((int) byteOffset);
		case MemorySegment d -> d.get(JAVA_BYTE(), byteOffset);
		case Byte d          -> getPrimitive(byteOffset, 1, d, MASK_08, false).byteValue();
		case Short d         -> getPrimitive(byteOffset, 2, d, MASK_08, false).byteValue();
		case Integer d       -> getPrimitive(byteOffset, 4, d, MASK_08, false).byteValue();
		case Long d          -> getPrimitive(byteOffset, 8, d, MASK_08, false).byteValue();
//		case short[] d       -> ArrayUtils.getByte(d, (int) byteOffset);
//		case int[] d         -> ArrayUtils.getByte(d, (int) byteOffset);
//		case long[] d        -> ArrayUtils.getByte(d, (int) byteOffset);
		// @formatter:on

		default -> throwDataTypeUnsupported(data);
		};
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
		assert carrierSize == 32 : "wrong carrier bit size";
		ByteOrder bo = big ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;

		return switch (data) {
		// @formatter:off
		case byte[] d        -> ArrayUtils.getInt(d, (int) byteOffset, big);
		case ByteArray d     -> ArrayUtils.getInt(d.array(), (int) byteOffset + d.arrayOffset(), big);
		case ByteBuffer d    -> d.order(bo).getInt((int) byteOffset);
		case MemorySegment d -> d.get(JAVA_INT(big), byteOffset);
		case Byte d          -> throwDataTypeTooShort(data);
		case Short d         -> throwDataTypeTooShort(data);
		case Integer d       -> getPrimitive(byteOffset, 1, d, MASK_32, big).intValue();
		case Long d          -> getPrimitive(byteOffset, 2, d, MASK_32, big).intValue();
//		case short[] d       -> ArrayUtils.getInt(d, (int) byteOffset, context.order());
//		case int[] d         -> ArrayUtils.getInt(d, (int) byteOffset, context.order());
//		case long[] d        -> ArrayUtils.getInt(d, (int) byteOffset, context.order());
		// @formatter:on

		default -> throwDataTypeUnsupported(data);
		};
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
		assert carrierSize == 64 : "wrong carrier bit size";
		ByteOrder bo = big ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;

		return switch (data) {
		// @formatter:off
		case byte[] d        -> ArrayUtils.getLong(d, (int) byteOffset, big);
		case ByteArray d     -> ArrayUtils.getLong(d.array(), (int) byteOffset + d.arrayOffset(), big);
		case ByteBuffer d    -> d.order(bo).getLong((int) byteOffset);
		case MemorySegment d -> d.get(JAVA_LONG(big), byteOffset);
		case Byte d          -> throwDataTypeTooShort(data);
		case Short d         -> throwDataTypeTooShort(data);
		case Integer d       -> throwDataTypeTooShort(data);
		case Long d          -> getPrimitive(byteOffset, 1, d, MASK_64, big).longValue();
//		case short[] d       -> ArrayUtils.getLong(d, (int) byteOffset, context.order());
//		case int[] d         -> ArrayUtils.getLong(d, (int) byteOffset, context.order());
//		case long[] d        -> ArrayUtils.getLong(d, (int) byteOffset, context.order());
		// @formatter:on

		default -> throwDataTypeUnsupported(data);
		};
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
		assert carrierSize == 16 : "wrong carrier bit size";

		ByteOrder bo = big ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;

		return switch (data) {
		// @formatter:off
		case byte[] d        -> ArrayUtils.getShort(d, (int) byteOffset, big);
		case ByteArray d     -> ArrayUtils.getShort(d.array(), (int) byteOffset + d.arrayOffset(), big);
		case ByteBuffer d    -> d.order(bo).getShort((int) byteOffset);
		case MemorySegment d -> d.get(JAVA_SHORT(big), byteOffset);
		case Byte d          -> throwDataTypeTooShort(data);
		case Short d         -> getPrimitive(byteOffset, 1, d, MASK_16, big).shortValue();
		case Integer d       -> getPrimitive(byteOffset, 2, d, MASK_16, big).shortValue();
		case Long d          -> getPrimitive(byteOffset, 4, d, MASK_16, big).shortValue();
//		case short[] d       -> ArrayUtils.getShort(d, (int) byteOffset, context.order());
//		case int[] d         -> ArrayUtils.getShort(d, (int) byteOffset, context.order());
//		case long[] d        -> ArrayUtils.getShort(d, (int) byteOffset, context.order());
		// @formatter:on

		default -> throwDataTypeUnsupported(data);
		};
	}

	/**
	 * Sets the byte at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the byte
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#setByte(byte,
	 *      java.lang.Object, long)
	 */
	@Override
	public byte setByteAtOffset(byte value, Object data, long byteOffset, boolean big) {
		assert carrierSize == 8 : "wrong carrier bit size";

		switch (data) {
		// @formatter:off
		case byte[] d        -> ArrayUtils.putByte(d, (int) byteOffset, value);
		case ByteArray d     -> ArrayUtils.putByte(d.array(), (int) byteOffset + d.arrayOffset(), value);
		case ByteBuffer d    -> d.put((int) byteOffset, value);
		case MemorySegment d -> d.set(JAVA_BYTE(), byteOffset, value);
		case Byte d          -> setPrimitive(byteOffset, 1, d, MASK_08, value, big);
		case Short d         -> setPrimitive(byteOffset, 2, d, MASK_08, value, big);
		case Integer d       -> setPrimitive(byteOffset, 4, d, MASK_08, value, big);
		case Long d          -> setPrimitive(byteOffset, 8, d, MASK_08, value, big);
//		case short[] d       -> ArrayUtils.putByte(d, (int) byteOffset, value);
//		case int[] d         -> ArrayUtils.putByte(d, (int) byteOffset, value);
//		case long[] d        -> ArrayUtils.putByte(d, (int) byteOffset, value);

		// @formatter:on
		default -> throwDataTypeUnsupported(data);
		}

		return value;
	}

	/**
	 * Sets the int at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the int
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#setInt(int,
	 *      java.lang.Object, long)
	 */
	@Override
	public int setIntAtOffset(int value, Object data, long byteOffset, boolean big) {
		assert carrierSize == 32 : "wrong carrier bit size";

		switch (data) {
		// @formatter:off
		case byte[] d        -> ArrayUtils.putInt(d, (int) byteOffset, value, big);
		case ByteArray d     -> ArrayUtils.putInt(d.array(), (int) byteOffset + d.arrayOffset(), value, big);
		case ByteBuffer d    -> d.putInt((int) byteOffset, value);
		case MemorySegment d -> d.set(JAVA_INT(big), byteOffset, value);
		case Byte d          -> throwDataTypeTooShort(data);
		case Short d         -> throwDataTypeTooShort(data);
		case Integer d       -> setPrimitive(byteOffset, 1, d, MASK_32, value, big);
		case Long d          -> setPrimitive(byteOffset, 2, d, MASK_32, value, big);
//		case short[] d       -> ArrayUtils.putInt(d, (int) byteOffset, value, context.order());
//		case int[] d         -> ArrayUtils.putInt(d, (int) byteOffset, value, context.order());
//		case long[] d        -> ArrayUtils.putInt(d, (int) byteOffset, value, context.order());

		// @formatter:on
		default -> throwDataTypeUnsupported(data);
		}

		return value;
	}

	/**
	 * Sets the long at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#setLong(long,
	 *      java.lang.Object, long)
	 */
	@Override
	public long setLongAtOffset(long value, Object data, long byteOffset, boolean big) {
		assert carrierSize == 64 : "wrong carrier bit size";

		switch (data) {
		// @formatter:off
		case byte[] d        -> ArrayUtils.putLong(d, (int) byteOffset, value, big);
		case ByteArray d     -> ArrayUtils.putLong(d.array(), (int) byteOffset + d.arrayOffset(), value, big);
		case ByteBuffer d    -> d.putLong((int) byteOffset, value);
		case MemorySegment d -> d.set(JAVA_LONG(big), byteOffset, value);
		case Byte d          -> throwDataTypeTooShort(data);
		case Short d         -> throwDataTypeTooShort(data);
		case Integer d       -> throwDataTypeTooShort(data);
		case Long d          -> setPrimitive(byteOffset, 1, d, MASK_64, value, big);
//		case short[] d       -> ArrayUtils.putLong(d, (int) byteOffset, value, context.order());
//		case int[] d         -> ArrayUtils.putLong(d, (int) byteOffset, value, context.order());
//		case long[] d        -> ArrayUtils.putLong(d, (int) byteOffset, value, context.order());
		// @formatter:on

		default -> throwDataTypeUnsupported(data);
		}

		return value;
	}

	/**
	 * Sets the short at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the short
	 * @see com.slytechs.protocol.runtime.internal.layout.BitCarrier#setShortAtOffset(int,
	 *      java.lang.Object, long, boolean)
	 */
	@Override
	public short setShortAtOffset(short value, Object data, long byteOffset, boolean big) {
		assert carrierSize == 16 : "wrong carrier bit size";

		// @formatter:off
		switch (data) {
		case byte[] d        -> ArrayUtils.putShort(d, (int) byteOffset, value, big);
		case ByteArray d     -> ArrayUtils.putShort(d.array(), (int) byteOffset + d.arrayOffset(), value, big);
		case ByteBuffer d    -> d.putShort((int) byteOffset, value);
		case MemorySegment d -> d.set(JAVA_SHORT(big), byteOffset, value);
		case Byte d          -> throwDataTypeTooShort(data);
		case Short d         -> setPrimitive(byteOffset, 1, d, MASK_16, value, big);
		case Integer d       -> setPrimitive(byteOffset, 2, d, MASK_16, value, big);
		case Long d          -> setPrimitive(byteOffset, 4, d, MASK_16, value, big);
//		case short[] d       -> ArrayUtils.putShort(d, (int) byteOffset, value, context.order());
//		case int[] d         -> ArrayUtils.putShort(d, (int) byteOffset, value, context.order());
//		case long[] d        -> ArrayUtils.putShort(d, (int) byteOffset, value, context.order());
		// @formatter:on

		default -> throwDataTypeUnsupported(data);
		}

		return value;
	}

	/**
	 * Throw data type too short.
	 *
	 * @param data the data
	 * @return the byte
	 */
	private byte throwDataTypeTooShort(Object data) {
		throw new IndexOutOfBoundsException("carrier data type too short: " + data.getClass().getSimpleName());
	}

	/**
	 * Throw data type unsupported.
	 *
	 * @param data the data
	 * @return the byte
	 */
	private byte throwDataTypeUnsupported(Object data) {
		throw new IllegalArgumentException("Unsupported data type: " + data.getClass().getSimpleName());
	}

}
