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
package com.slytechs.jnet.jnetruntime.internal.layout;

import static com.slytechs.jnet.jnetruntime.util.Bits.*;

import java.lang.constant.ConstantDesc;
import java.nio.ByteOrder;
import java.util.Optional;

/**
 * The Class BitFieldImplementation.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class BitFieldImplementation implements BitField {

	/** The context. */
	private final BitFieldContext context;

	/** The op. */
	private final BitOperator op;

	/** The carrier. */
	private final BitCarrier carrier;

	/** The carrier size. */
	private final int carrierSize;

	/** The big. */
	private final boolean big;

	private final boolean isCarrierSize;

	/**
	 * Instantiates a new bit field implementation.
	 *
	 * @param context the context
	 */
	public BitFieldImplementation(BitFieldContext context) {
		this.context = context;

		this.carrierSize = (int) context.carrierSize();
		this.big = (context.order() == ByteOrder.BIG_ENDIAN);
		this.isCarrierSize = true
				&& (context.fieldSize() == context.carrierSize())
				&& (context.carrierOffset(0) == 0);

		this.carrier = new BitCarrierImplementation(context);

		if (isCarrierSize)
			this.op = new BitOperatorNop(context);
		else
			this.op = new BitOperatorShifter(context);
	}

	/**
	 * Instantiates a new bit field implementation.
	 *
	 * @param context the context
	 * @param carrier the carrier
	 */
	public BitFieldImplementation(BitFieldContext context, BitCarrier carrier) {
		this.context = context;

		this.carrierSize = (int) context.carrierSize();
		this.big = (context.order() == ByteOrder.BIG_ENDIAN);
		this.isCarrierSize = true
				&& (context.fieldSize() == context.carrierSize())
				&& (context.carrierOffset(0) == 0);

		this.carrier = carrier;
		if (isCarrierSize)
			this.op = new BitOperatorNop(context);
		else
			this.op = new BitOperatorShifter(context);
	}

	/**
	 * Bit offset.
	 *
	 * @return the long
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#bitOffset()
	 */
	@Override
	public long bitOffset() {
		return context.fieldOffset(0);
	}

	/**
	 * Bit offset.
	 *
	 * @param sequences the sequences
	 * @return the long
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BinaryField#bitOffset(long[])
	 */
	@Override
	public long bitOffset(long... sequences) {
		return context.fieldOffset(context.strideOffset(sequences));
	}

	/**
	 * Cache.
	 *
	 * @return the bit field
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#cache()
	 */
	@Override
	public BitField cache() {
		if (carrier instanceof BitCarrierCaching)
			return this;

		return new BitFieldImplementation(context, new BitCarrierCaching(carrier));
	}

	/**
	 * Carrier byte offset.
	 *
	 * @param strideOffset the stride offset
	 * @return the long
	 */
	private final long carrierByteOffset(long strideOffset) {
		return context.carrierByteOffset(strideOffset);
	}

	/**
	 * Describe constable.
	 *
	 * @return the optional<? extends constant desc>
	 * @see java.lang.constant.Constable#describeConstable()
	 */
	@Override
	public Optional<? extends ConstantDesc> describeConstable() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the bit.
	 *
	 * @param data the data
	 * @return the bit
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getBit(java.lang.Object)
	 */
	@Override
	public boolean getBit(Object data) {
		return getByte(data) != 0;
	}

	/**
	 * Gets the bit.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the bit
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getBit(java.lang.Object,
	 *      long[])
	 */
	@Override
	public boolean getBit(Object data, long... sequences) {
		return getByte(data, sequences) != 0;
	}

	/**
	 * Gets the byte.
	 *
	 * @param data the data
	 * @return the byte
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getByte(java.lang.Object)
	 */
	@Override
	public byte getByte(Object data) {
		return (carrierSize == 8)
				? op.getByte(carrier.getByteAtOffset(data, carrierByteOffset(0)), 0)
				: routeNumberGetterAtOfset(data, 0, true).byteValue();
	}

	/**
	 * Gets the byte.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getByte(java.lang.Object,
	 *      long[])
	 */
	@Override
	public byte getByte(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);

		return (carrierSize == 8)
				? op.getByte(carrier.getByteAtOffset(data, carrierByteOffset(strideOffset)), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).byteValue();
	}

	/**
	 * Gets the byte at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the byte at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getByteAt(long,
	 *      java.lang.Object)
	 */
	@Override
	public byte getByteAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 8)
				? op.getByte(carrier.getByteAtOffset(data, carrierByteOffset(offset)), offset)
				: routeNumberGetterAtOfset(data, offset, true).byteValue();
	}

	/**
	 * Gets the byte at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getByteAt(long,
	 *      java.lang.Object, long[])
	 */
	@Override
	public byte getByteAt(long offset, Object data, long... sequences) {
		offset <<= SHIFT_BYTES_TO_BITS;
		long strideOffset = context.strideOffset(sequences) + offset;

		return (carrierSize == 8)
				? op.getByte(carrier.getByteAtOffset(data, carrierByteOffset(strideOffset)), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).byteValue();
	}

	/**
	 * Gets the int.
	 *
	 * @param data the data
	 * @return the int
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getInt(java.lang.Object)
	 */
	@Override
	public int getInt(Object data) {
		return (carrierSize == 32)
				? op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(0), big), 0)
				: routeNumberGetterAtOfset(data, 0, true).intValue();
	}

	/**
	 * Gets the int.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the int
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getInt(java.lang.Object,
	 *      long[])
	 */
	@Override
	public int getInt(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);

		return (carrierSize == 32)
				? op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).intValue();
	}

	/**
	 * Gets the int at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the int at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getIntAt(long,
	 *      java.lang.Object)
	 */
	@Override
	public int getIntAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 32)
				? op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(offset), big), offset)
				: routeNumberGetterAtOfset(data, offset, true).intValue();
	}

	/**
	 * Gets the int at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the int at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getIntAt(long,
	 *      java.lang.Object, long[])
	 */
	@Override
	public int getIntAt(long offset, Object data, long... sequences) {
		offset <<= SHIFT_BYTES_TO_BITS;
		long strideOffset = context.strideOffset(sequences) + offset;

		return (carrierSize == 32)
				? op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).intValue();
	}

	/**
	 * Gets the long.
	 *
	 * @param data the data
	 * @return the long
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getLong(java.lang.Object)
	 */
	@Override
	public long getLong(Object data) {
		return (carrierSize == 64)
				? op.getLong(carrier.getLongAtOffset(data, carrierByteOffset(0), big), 0)
				: routeNumberGetterAtOfset(data, 0, true).longValue();
	}

	/**
	 * Gets the long.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the long
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getLong(java.lang.Object,
	 *      long[])
	 */
	@Override
	public long getLong(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);

		return (carrierSize == 64)
				? op.getLong(carrier.getLongAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).longValue();
	}

	/**
	 * Gets the long at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the long at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getLongAt(long,
	 *      java.lang.Object)
	 */
	@Override
	public long getLongAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 64)
				? op.getLong(carrier.getLongAtOffset(data, carrierByteOffset(offset), big), offset)
				: routeNumberGetterAtOfset(data, offset, true).longValue();
	}

	/**
	 * Gets the long at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the long at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getLongAt(long,
	 *      java.lang.Object, long[])
	 */
	@Override
	public long getLongAt(long offset, Object data, long... sequences) {
		offset <<= SHIFT_BYTES_TO_BITS;
		long strideOffset = context.strideOffset(sequences) + offset;

		return (carrierSize == 64)
				? op.getLong(carrier.getLongAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).longValue();
	}

	/**
	 * Gets the number.
	 *
	 * @param data the data
	 * @return the number
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getNumber(java.lang.Object)
	 */
	@Override
	public Number getNumber(Object data) {
		return routeNumberGetterAtOfset(data, 0, true);
	}

	/**
	 * Gets the number.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the number
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getNumber(java.lang.Object,
	 *      long[])
	 */
	@Override
	public Number getNumber(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);
		return routeNumberGetterAtOfset(data, strideOffset, true);
	}

	/**
	 * Gets the number at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the number at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getNumberAt(long,
	 *      java.lang.Object)
	 */
	@Override
	public Number getNumberAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return routeNumberGetterAtOfset(data, offset, true);
	}

	/**
	 * Gets the number at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the number at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getNumberAt(long,
	 *      java.lang.Object, long[])
	 */
	@Override
	public Number getNumberAt(long offset, Object data, long... sequences) {
		offset <<= SHIFT_BYTES_TO_BITS;
		long strideOffset = context.strideOffset(sequences) + offset;

		return routeNumberGetterAtOfset(data, strideOffset, true);
	}

	/**
	 * Gets the short.
	 *
	 * @param data the data
	 * @return the short
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getShort(java.lang.Object)
	 */
	@Override
	public short getShort(Object data) {
		return (carrierSize == 16)
				? op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(0), big), 0)
				: routeNumberGetterAtOfset(data, 0, true).shortValue();
	}

	/**
	 * Gets the short.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the short
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getShort(java.lang.Object,
	 *      long[])
	 */
	@Override
	public short getShort(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);

		return (carrierSize == 16)
				? op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).shortValue();
	}

	/**
	 * Gets the short at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the short at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getShortAt(long,
	 *      java.lang.Object)
	 */
	@Override
	public short getShortAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 16)
				? op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(offset), big), offset)
				: routeNumberGetterAtOfset(data, offset, true).shortValue();
	}

	/**
	 * Gets the short at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the short at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getShortAt(long,
	 *      java.lang.Object, long[])
	 */
	@Override
	public short getShortAt(long offset, Object data, long... sequences) {
		offset <<= SHIFT_BYTES_TO_BITS;
		long strideOffset = context.strideOffset(sequences) + offset;

		return (carrierSize == 16)
				? op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).shortValue();
	}

	/**
	 * Gets the unsigned byte.
	 *
	 * @param data the data
	 * @return the unsigned byte
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedByte(java.lang.Object)
	 */
	@Override
	public int getUnsignedByte(Object data) {
		return (carrierSize == 8)
				? Byte.toUnsignedInt(op.getByte(carrier.getByteAtOffset(data, carrierByteOffset(0)), 0))
				: routeNumberGetterAtOfset(data, 0, false).intValue();
	}

	/**
	 * Gets the unsigned byte.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned byte
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedByte(java.lang.Object,
	 *      long[])
	 */
	@Override
	public int getUnsignedByte(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);

		return (carrierSize == 8)
				? Byte.toUnsignedInt(
						op.getByte(carrier.getByteAtOffset(data, carrierByteOffset(strideOffset)), strideOffset))
				: routeNumberGetterAtOfset(data, strideOffset, false).intValue();
	}

	/**
	 * Gets the unsigned byte at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the unsigned byte at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedByteAt(long,
	 *      java.lang.Object)
	 */
	@Override
	public int getUnsignedByteAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 8)
				? Byte.toUnsignedInt(op.getByte(carrier.getByteAtOffset(data, carrierByteOffset(offset)), offset))
				: routeNumberGetterAtOfset(data, offset, false).intValue();
	}

	/**
	 * Gets the unsigned byte at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned byte at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedByteAt(long,
	 *      java.lang.Object, long[])
	 */
	@Override
	public int getUnsignedByteAt(long offset, Object data, long... sequences) {
		offset <<= SHIFT_BYTES_TO_BITS;
		long strideOffset = context.strideOffset(sequences) + offset;

		return (carrierSize == 8)
				? Byte.toUnsignedInt(
						op.getByte(carrier.getByteAtOffset(data, carrierByteOffset(strideOffset)), strideOffset))
				: routeNumberGetterAtOfset(data, strideOffset, false).intValue();
	}

	/**
	 * Gets the unsigned int.
	 *
	 * @param data the data
	 * @return the unsigned int
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedInt(java.lang.Object)
	 */
	@Override
	public long getUnsignedInt(Object data) {
		return (carrierSize == 32)
				? Integer.toUnsignedLong(op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(0), big), 0))
				: routeNumberGetterAtOfset(data, 0, false).longValue();
	}

	/**
	 * Gets the unsigned int.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned int
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedInt(java.lang.Object,
	 *      long[])
	 */
	@Override
	public long getUnsignedInt(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);

		return (carrierSize == 32)
				? Integer.toUnsignedLong(
						op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset))
				: routeNumberGetterAtOfset(data, strideOffset, false).longValue();

	}

	/**
	 * Gets the unsigned int at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the unsigned int at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedIntAt(long,
	 *      java.lang.Object)
	 */
	@Override
	public long getUnsignedIntAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 32)
				? Integer
						.toUnsignedLong(op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(offset), big), offset))
				: routeNumberGetterAtOfset(data, offset, false).longValue();
	}

	/**
	 * Gets the unsigned int at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned int at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedIntAt(long,
	 *      java.lang.Object, long[])
	 */
	@Override
	public long getUnsignedIntAt(long offset, Object data, long... sequences) {
		offset <<= SHIFT_BYTES_TO_BITS;
		long strideOffset = context.strideOffset(sequences) + offset;

		return (carrierSize == 32)
				? Integer.toUnsignedLong(
						op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset))
				: routeNumberGetterAtOfset(data, strideOffset, false).longValue();
	}

	/**
	 * Gets the unsigned number.
	 *
	 * @param data the data
	 * @return the unsigned number
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedNumber(java.lang.Object)
	 */
	@Override
	public Number getUnsignedNumber(Object data) {
		return routeNumberGetterAtOfset(data, 0, false);
	}

	/**
	 * Gets the unsigned number.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned number
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedNumber(java.lang.Object,
	 *      long[])
	 */
	@Override
	public Number getUnsignedNumber(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);
		return routeNumberGetterAtOfset(data, strideOffset, false);
	}

	/**
	 * Gets the unsigned number at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the unsigned number at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedNumberAt(long,
	 *      java.lang.Object)
	 */
	@Override
	public Number getUnsignedNumberAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return routeNumberGetterAtOfset(data, offset, false);
	}

	/**
	 * Gets the unsigned number at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned number at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedNumberAt(long,
	 *      java.lang.Object, long[])
	 */
	@Override
	public Number getUnsignedNumberAt(long offset, Object data, long... sequences) {
		offset <<= SHIFT_BYTES_TO_BITS;
		long strideOffset = context.strideOffset(sequences) + offset;
		return routeNumberGetterAtOfset(data, strideOffset, false);
	}

	/**
	 * Gets the unsigned short.
	 *
	 * @param data the data
	 * @return the unsigned short
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedShort(java.lang.Object)
	 */
	@Override
	public int getUnsignedShort(Object data) {
		return (carrierSize == 16)
				? Short.toUnsignedInt(op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(0), big), 0))
				: routeNumberGetterAtOfset(data, 0, false).intValue();
	}

	/**
	 * Gets the unsigned short.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned short
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedShort(java.lang.Object,
	 *      long[])
	 */
	@Override
	public int getUnsignedShort(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);

		return (carrierSize == 16)
				? Short.toUnsignedInt(
						op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset))
				: routeNumberGetterAtOfset(data, strideOffset, false).intValue();
	}

	/**
	 * Gets the unsigned short at.
	 *
	 * @param offset the offset
	 * @param data   the data
	 * @return the unsigned short at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedShortAt(long,
	 *      java.lang.Object)
	 */
	@Override
	public int getUnsignedShortAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 16)
				? Short.toUnsignedInt(
						op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(offset), big), offset))
				: routeNumberGetterAtOfset(data, offset, false).intValue();
	}

	/**
	 * Gets the unsigned short at.
	 *
	 * @param offset    the offset
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the unsigned short at
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#getUnsignedShortAt(long,
	 *      java.lang.Object, long[])
	 */
	@Override
	public int getUnsignedShortAt(long offset, Object data, long... sequences) {
		offset <<= SHIFT_BYTES_TO_BITS;
		long strideOffset = context.strideOffset(sequences) + offset;

		return (carrierSize == 16)
				? Short.toUnsignedInt(
						op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset))
				: routeNumberGetterAtOfset(data, strideOffset, false).intValue();
	}

	/**
	 * Layout.
	 *
	 * @return the binary layout
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#layout()
	 */
	@Override
	public BinaryLayout layout() {
		return context.binaryLayout();
	}

	/**
	 * Order.
	 *
	 * @return the byte order
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#order()
	 */
	@Override
	public ByteOrder order() {
		return context.order();
	}

	/**
	 * Order.
	 *
	 * @param newOrder the new order
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#order(java.nio.ByteOrder)
	 */
	@Override
	public void order(ByteOrder newOrder) {
		context.order(newOrder);
	}

	/**
	 * Readonly.
	 *
	 * @return the bit field
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#readonly()
	 */
	@Override
	public BitField readonly() {
		return new BitFieldImplementation(context, new BitCarrierReadonly(carrier));
	}

	/**
	 * Route number getter at ofset.
	 *
	 * @param data         the data
	 * @param strideOffset the stride offset
	 * @param signed       the signed
	 * @return the number
	 */
	public Number routeNumberGetterAtOfset(Object data, long strideOffset, boolean signed) {
		Number r0 = switch (carrierSize) {
		case 8 -> carrier.getByteAtOffset(data, context.carrierByteOffset(strideOffset));
		case 16 -> carrier.getShortAtOffset(data, context.carrierByteOffset(strideOffset), big);
		case 32 -> carrier.getIntAtOffset(data, context.carrierByteOffset(strideOffset), big);
		case 64 -> carrier.getLongAtOffset(data, context.carrierByteOffset(strideOffset), big);

		default -> throw new IllegalStateException();
		};

		if (signed)
			return switch (carrierSize) {
			case 8 -> op.getByte(r0.byteValue(), strideOffset);
			case 16 -> op.getShort(r0.shortValue(), strideOffset);
			case 32 -> op.getInt(r0.intValue(), strideOffset);
			case 64 -> op.getLong(r0.longValue(), strideOffset);

			default -> throw new IllegalStateException();
			};
		else
			return switch (carrierSize) {
			case 8 -> Byte.toUnsignedInt(op.getByte(r0.byteValue(), strideOffset));
			case 16 -> Short.toUnsignedInt(op.getShort(r0.shortValue(), strideOffset));
			case 32 -> Integer.toUnsignedLong(op.getInt(r0.intValue(), strideOffset));
			case 64 -> op.getLong(r0.longValue(), strideOffset);

			default -> throw new IllegalStateException();
			};
	}

	/**
	 * Route number setter at offset.
	 *
	 * @param value        the value
	 * @param data         the data
	 * @param strideOffset the stride offset
	 * @return the number
	 */
	public Number routeNumberSetterAtOffset(Number value, Object data, long strideOffset) {
		final long byteOffset = context.carrierByteOffset(strideOffset);

		if (isCarrierSize) {
			return switch (carrierSize) {

			case 8 -> {
				yield carrier.setByteAtOffset(value.byteValue(), data, byteOffset, big);
			}

			case 16 -> {
				yield carrier.setShortAtOffset(value.shortValue(), data, byteOffset, big);
			}
			case 32 -> {
				yield carrier.setIntAtOffset(value.intValue(), data, byteOffset, big);
			}
			case 64 -> {
				yield carrier.setLongAtOffset(value.longValue(), data, byteOffset, big);
			}

			default -> throw new IllegalStateException();
			};
		}

		return switch (carrierSize) {

		case 8 -> {
			byte r0 = carrier.getByteAtOffset(data, byteOffset);
			r0 = op.setByte(value.byteValue(), r0, strideOffset);
			yield carrier.setByteAtOffset(r0, data, byteOffset, big);
		}

		case 16 -> {
			short r0 = carrier.getShortAtOffset(data, byteOffset, big);
			r0 = op.setShort(value.shortValue(), r0, strideOffset);
			yield carrier.setShortAtOffset(r0, data, byteOffset, big);
		}
		case 32 -> {
			int r0 = carrier.getIntAtOffset(data, byteOffset, big);
			r0 = op.setInt(value.intValue(), r0, strideOffset);
			yield carrier.setIntAtOffset(r0, data, byteOffset, big);
		}
		case 64 -> {
			long r0 = carrier.getLongAtOffset(data, byteOffset, big);
			r0 = op.setLong(value.longValue(), r0, strideOffset);
			yield carrier.setLongAtOffset(r0, data, byteOffset, big);
		}

		default -> throw new IllegalStateException();
		};
	}

	/**
	 * Route number setter at offset.
	 *
	 * @param value        the value
	 * @param data         the data
	 * @param strideOffset the stride offset
	 * @return the number
	 */
	public Number routeNumberSetterAtOffset2(Number value, Object data, long strideOffset) {
		final long byteOffset = context.carrierByteOffset(strideOffset);

		Number r0 = switch (carrierSize) {
		case 8 -> carrier.getByteAtOffset(data, byteOffset);
		case 16 -> carrier.getShortAtOffset(data, byteOffset, big);
		case 32 -> carrier.getIntAtOffset(data, byteOffset, big);
		case 64 -> carrier.getLongAtOffset(data, byteOffset, big);

		default -> throw new IllegalStateException();
		};

		r0 = switch (carrierSize) {
		case 8 -> op.getByte(r0.byteValue(), strideOffset);
		case 16 -> op.getShort(r0.shortValue(), strideOffset);
		case 32 -> op.getInt(r0.intValue(), strideOffset);
		case 64 -> op.getLong(r0.longValue(), strideOffset);

		default -> throw new IllegalStateException();
		};

		switch (carrierSize) {
		case 8 -> carrier.setByteAtOffset(r0.byteValue(), data, byteOffset, big);
		case 16 -> carrier.setShortAtOffset(r0.shortValue(), data, byteOffset, big);
		case 32 -> carrier.setIntAtOffset(r0.intValue(), data, byteOffset, big);
		case 64 -> carrier.setLongAtOffset(r0.longValue(), data, byteOffset, big);

		default -> throw new IllegalStateException();
		}

		return value;
	}

	/**
	 * Sets the byte.
	 *
	 * @param value the value
	 * @param data  the data
	 * @return the byte
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#setByte(byte,
	 *      java.lang.Object)
	 */
	@Override
	public byte setByte(byte value, Object data) {
		long byteOffset = carrierByteOffset(0);
		return (carrierSize == 8)
				? carrier
						.setByteAtOffset(op
								.setByte(value, carrier
										.getByteAtOffset(data, byteOffset), 0),
								data, byteOffset, big)
				: routeNumberSetterAtOffset(value, data, 0).byteValue();
	}

	/**
	 * Sets the byte.
	 *
	 * @param value     the value
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the byte
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#setByte(byte,
	 *      java.lang.Object, long[])
	 */
	@Override
	public byte setByte(byte value, Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);
		long byteOffset = carrierByteOffset(strideOffset);
		return (carrierSize == 8)
				? carrier
						.setByteAtOffset(op
								.setByte(value, carrier
										.getByteAtOffset(data, byteOffset), strideOffset),
								data, byteOffset, big)
				: routeNumberSetterAtOffset(value, data, strideOffset).byteValue();
	}

	/**
	 * Sets the int.
	 *
	 * @param value the value
	 * @param data  the data
	 * @return the int
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#setInt(int,
	 *      java.lang.Object)
	 */
	@Override
	public int setInt(int value, Object data) {
		long byteOffset = carrierByteOffset(0);
		return (carrierSize == 32)
				? carrier
						.setIntAtOffset(op
								.setInt(value, carrier
										.getIntAtOffset(data, byteOffset, big), 0),
								data, byteOffset, big)
				: routeNumberSetterAtOffset(value, data, 0).intValue();
	}

	/**
	 * Sets the int.
	 *
	 * @param value     the value
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the int
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#setInt(int,
	 *      java.lang.Object, long[])
	 */
	@Override
	public int setInt(int value, Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);
		long byteOffset = carrierByteOffset(strideOffset);
		return (carrierSize == 32)
				? carrier
						.setIntAtOffset(op
								.setInt(value, carrier
										.getIntAtOffset(data, byteOffset, big), strideOffset),
								data, byteOffset, big)
				: routeNumberSetterAtOffset(value, data, strideOffset).intValue();
	}

	/**
	 * Sets the long.
	 *
	 * @param value the value
	 * @param data  the data
	 * @return the long
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#setLong(long,
	 *      java.lang.Object)
	 */
	@Override
	public long setLong(long value, Object data) {
		long byteOffset = carrierByteOffset(0);
		return (carrierSize == 64)
				? carrier
						.setLongAtOffset(op
								.setLong(value, carrier
										.getLongAtOffset(data, byteOffset, big), 0),
								data, byteOffset, big)
				: routeNumberSetterAtOffset(value, data, 0).longValue();
	}

	/**
	 * Sets the long.
	 *
	 * @param value     the value
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the long
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#setLong(long,
	 *      java.lang.Object, long[])
	 */
	@Override
	public long setLong(long value, Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);
		long byteOffset = carrierByteOffset(strideOffset);
		return (carrierSize == 64)
				? carrier
						.setLongAtOffset(op
								.setLong(value, carrier
										.getLongAtOffset(data, byteOffset, big), strideOffset),
								data, byteOffset, big)
				: routeNumberSetterAtOffset(value, data, strideOffset).longValue();
	}

	/**
	 * Sets the short.
	 *
	 * @param value the value
	 * @param data  the data
	 * @return the short
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#setShort(short,
	 *      java.lang.Object)
	 */
	@Override
	public short setShort(short value, Object data) {
		long byteOffset = carrierByteOffset(0);
		return (carrierSize == 16)
				? carrier
						.setShortAtOffset(op
								.setShort(value, carrier
										.getShortAtOffset(data, byteOffset, big), 0),
								data, byteOffset, big)
				: routeNumberSetterAtOffset(value, data, 0).shortValue();
	}

	/**
	 * Sets the short.
	 *
	 * @param value     the value
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the short
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#setShort(short,
	 *      java.lang.Object, long[])
	 */
	@Override
	public short setShort(short value, Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);
		long byteOffset = carrierByteOffset(strideOffset);
		return (carrierSize == 16)
				? carrier
						.setShortAtOffset(op
								.setShort(value, carrier
										.getShortAtOffset(data, byteOffset, big), strideOffset),
								data, byteOffset, big)
				: routeNumberSetterAtOffset(value, data, strideOffset).shortValue();
	}

	/**
	 * Synchronize.
	 *
	 * @return the bit field
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#synchronize()
	 */
	@Override
	public BitField synchronize() {
		return new BitFieldImplementation(context, new BitCarrierSync(carrier));
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BitField [" + context + "]";
	}

	/**
	 * To string.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the string
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#toString(java.lang.Object,
	 *      long[])
	 */
	@Override
	public String toString(Object data, long... sequences) {
		return FormattedBitField.toString(this, data, sequences);
	}

	/**
	 * Non proxy.
	 *
	 * @return the bit field
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField#nonProxy()
	 */
	@Override
	public BitField nonProxy() {
		return this;
	}

}
