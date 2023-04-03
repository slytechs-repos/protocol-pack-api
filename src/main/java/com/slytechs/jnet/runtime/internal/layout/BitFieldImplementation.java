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

import static com.slytechs.jnet.runtime.util.Bits.*;

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

	private final boolean big;

	/**
	 * Instantiates a new bit field implementation.
	 *
	 * @param context the context
	 */
	public BitFieldImplementation(BitFieldContext context) {
		this.context = context;

		this.carrierSize = (int) context.carrierSize();
		this.big = (context.order() == ByteOrder.BIG_ENDIAN);

		this.carrier = new BitCarrierImplementation(context);

		if (context.fieldSize() == context.carrierSize())
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

		this.carrier = carrier;
		if (context.fieldSize() == context.carrierSize())
			this.op = new BitOperatorNop(context);
		else
			this.op = new BitOperatorShifter(context);
	}

	/**
	 * Bit offset.
	 *
	 * @return the long
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#bitOffset()
	 */
	@Override
	public long bitOffset() {
		return context.fieldOffset(0);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BinaryField#bitOffset(long[])
	 */
	@Override
	public long bitOffset(long... sequences) {
		return context.fieldOffset(context.strideOffset(sequences));
	}

	/**
	 * Cache.
	 *
	 * @return the bit field
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#cache()
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getBit(java.lang.Object)
	 */
	@Override
	public boolean getBit(Object data) {
		return getByte(data) != 0;
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getBit(java.lang.Object,
	 *      long[])
	 */
	@Override
	public boolean getBit(Object data, long... sequences) {
		return getByte(data, sequences) != 0;
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getByte(java.lang.Object)
	 */
	@Override
	public byte getByte(Object data) {
		return (carrierSize == 8)
				? op.getByte(carrier.getByteAtOffset(data, carrierByteOffset(0)), 0)
				: routeNumberGetterAtOfset(data, 0, true).byteValue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getByte(java.lang.Object,
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getByteAt(long,
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getByteAt(long,
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getInt(java.lang.Object)
	 */
	@Override
	public int getInt(Object data) {
		return (carrierSize == 32)
				? op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(0), big), 0)
				: routeNumberGetterAtOfset(data, 0, true).intValue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getInt(java.lang.Object,
	 *      long[])
	 */
	@Override
	public int getInt(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);

		return (carrierSize == 32)
				? op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).intValue();
	}

	@Override
	public int getIntAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 32)
				? op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(offset), big), offset)
				: routeNumberGetterAtOfset(data, offset, true).intValue();
	}

	@Override
	public int getIntAt(long offset, Object data, long... sequences) {
		offset <<= SHIFT_BYTES_TO_BITS;
		long strideOffset = context.strideOffset(sequences) + offset;

		return (carrierSize == 32)
				? op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).intValue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getLong(java.lang.Object)
	 */
	@Override
	public long getLong(Object data) {
		return (carrierSize == 64)
				? op.getLong(carrier.getLongAtOffset(data, carrierByteOffset(0), big), 0)
				: routeNumberGetterAtOfset(data, 0, true).longValue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getLong(java.lang.Object,
	 *      long[])
	 */
	@Override
	public long getLong(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);

		return (carrierSize == 64)
				? op.getLong(carrier.getLongAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).longValue();
	}

	@Override
	public long getLongAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 64)
				? op.getLong(carrier.getLongAtOffset(data, carrierByteOffset(offset), big), offset)
				: routeNumberGetterAtOfset(data, offset, true).longValue();
	}

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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getNumber(java.lang.Object)
	 */
	@Override
	public Number getNumber(Object data) {
		return routeNumberGetterAtOfset(data, 0, true);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getNumber(java.lang.Object,
	 *      long[])
	 */
	@Override
	public Number getNumber(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);
		return routeNumberGetterAtOfset(data, strideOffset, true);
	}

	@Override
	public Number getNumberAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return routeNumberGetterAtOfset(data, offset, true);
	}

	@Override
	public Number getNumberAt(long offset, Object data, long... sequences) {
		offset <<= SHIFT_BYTES_TO_BITS;
		long strideOffset = context.strideOffset(sequences) + offset;

		return routeNumberGetterAtOfset(data, strideOffset, true);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getShort(java.lang.Object)
	 */
	@Override
	public short getShort(Object data) {
		return (carrierSize == 16)
				? op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(0), big), 0)
				: routeNumberGetterAtOfset(data, 0, true).shortValue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getShort(java.lang.Object,
	 *      long[])
	 */
	@Override
	public short getShort(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);

		return (carrierSize == 16)
				? op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(strideOffset), big), strideOffset)
				: routeNumberGetterAtOfset(data, strideOffset, true).shortValue();
	}

	@Override
	public short getShortAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 16)
				? op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(offset), big), offset)
				: routeNumberGetterAtOfset(data, offset, true).shortValue();
	}

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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedByte(java.lang.Object)
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedByte(java.lang.Object,
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

	@Override
	public int getUnsignedByteAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 8)
				? Byte.toUnsignedInt(op.getByte(carrier.getByteAtOffset(data, carrierByteOffset(offset)), offset))
				: routeNumberGetterAtOfset(data, offset, false).intValue();
	}

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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedInt(java.lang.Object)
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedInt(java.lang.Object,
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

	@Override
	public long getUnsignedIntAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 32)
				? Integer
						.toUnsignedLong(op.getInt(carrier.getIntAtOffset(data, carrierByteOffset(offset), big), offset))
				: routeNumberGetterAtOfset(data, offset, false).longValue();
	}

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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedNumber(java.lang.Object)
	 */
	@Override
	public Number getUnsignedNumber(Object data) {
		return routeNumberGetterAtOfset(data, 0, false);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedNumber(java.lang.Object,
	 *      long[])
	 */
	@Override
	public Number getUnsignedNumber(Object data, long... sequences) {
		long strideOffset = context.strideOffset(sequences);
		return routeNumberGetterAtOfset(data, strideOffset, false);
	}

	@Override
	public Number getUnsignedNumberAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return routeNumberGetterAtOfset(data, offset, false);
	}

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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedShort(java.lang.Object)
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#getUnsignedShort(java.lang.Object,
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

	@Override
	public int getUnsignedShortAt(long offset, Object data) {
		offset <<= SHIFT_BYTES_TO_BITS;

		return (carrierSize == 16)
				? Short.toUnsignedInt(
						op.getShort(carrier.getShortAtOffset(data, carrierByteOffset(offset), big), offset))
				: routeNumberGetterAtOfset(data, offset, false).intValue();
	}

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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#layout()
	 */
	@Override
	public BinaryLayout layout() {
		return context.binaryLayout();
	}

	/**
	 * Order.
	 *
	 * @return the byte order
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#order()
	 */
	@Override
	public ByteOrder order() {
		return context.order();
	}

	/**
	 * Order.
	 *
	 * @param newOrder the new order
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#order(java.nio.ByteOrder)
	 */
	@Override
	public void order(ByteOrder newOrder) {
		context.order(newOrder);
	}

	/**
	 * Readonly.
	 *
	 * @return the bit field
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#readonly()
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setByte(byte,
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setByte(byte,
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setInt(int,
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setInt(int,
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setLong(long,
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setLong(long,
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setShort(short,
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#setShort(short,
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#synchronize()
	 */
	@Override
	public BitField synchronize() {
		return new BitFieldImplementation(context, new BitCarrierSync(carrier));
	}

	/**
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#toString(java.lang.Object,
	 *      long[])
	 */
	@Override
	public String toString(Object data, long... sequences) {
		return FormattedBitField.toString(this, data, sequences);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField#nonProxy()
	 */
	@Override
	public BitField nonProxy() {
		return this;
	}

}
