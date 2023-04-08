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
package com.slytechs.jnet.runtime.internal.layout;

/**
 * The Class BitOperatorShifter.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class BitOperatorShifter implements BitOperator {

	/**
	 * Gets the 08.
	 *
	 * @param c     the c
	 * @param shift the shift
	 * @param mask  the mask
	 * @return the 08
	 */
	// @formatter:off
	private static byte  get08(byte  c, int shift, byte  mask) { return (byte)  ((c >> shift) & mask); }
	
	/**
	 * Gets the 16.
	 *
	 * @param c     the c
	 * @param shift the shift
	 * @param mask  the mask
	 * @return the 16
	 */
	private static short get16(short c, int shift, short mask) { return (short) ((c >> shift) & mask); }
	
	/**
	 * Gets the 32.
	 *
	 * @param c     the c
	 * @param shift the shift
	 * @param mask  the mask
	 * @return the 32
	 */
	private static int   get32(int   c, int shift, int   mask) { return (int)   ((c >> shift) & mask); }
	
	/**
	 * Gets the 64.
	 *
	 * @param c     the c
	 * @param shift the shift
	 * @param mask  the mask
	 * @return the 64
	 */
	private static long  get64(long  c, int shift, long  mask) { return (long)  ((c >> shift) & mask); }
	// @formatter:on

	/**
	 * Sets the 08.
	 *
	 * @param c     the c
	 * @param shift the shift
	 * @param mask  the mask
	 * @param v     the v
	 * @return the byte
	 */
	// @formatter:off
	private static byte  set08(byte  c, int shift, byte  mask, byte  v) { return (byte)  (c | (v & mask) << shift); }
	
	/**
	 * Sets the 16.
	 *
	 * @param c     the c
	 * @param shift the shift
	 * @param mask  the mask
	 * @param v     the v
	 * @return the short
	 */
	private static short set16(short c, int shift, short mask, short v) { return (short) (c | (v & mask) << shift); }
	
	/**
	 * Sets the 32.
	 *
	 * @param c     the c
	 * @param shift the shift
	 * @param mask  the mask
	 * @param v     the v
	 * @return the int
	 */
	private static int   set32(int   c, int shift, int   mask, int   v) { return (int)   (c | (v & mask) << shift); }
	
	/**
	 * Sets the 64.
	 *
	 * @param c     the c
	 * @param shift the shift
	 * @param mask  the mask
	 * @param v     the v
	 * @return the long
	 */
	private static long  set64(long  c, int shift, long  mask, long  v) { return (long)  (c | (v & mask) << shift); }
	// @formatter:on

	/** The field context. */
	private final BitFieldContext fieldContext;

	/**
	 * Instantiates a new bit operator shifter.
	 *
	 * @param fieldContext the field context
	 */
	BitOperatorShifter(BitFieldContext fieldContext) {
		this.fieldContext = fieldContext;
	}

	/**
	 * Bitmask.
	 *
	 * @return the long
	 * @see com.slytechs.jnet.runtime.internal.layout.BitOperator#bitmask(long, long)
	 */
	@Override
	public long bitmask() {
		return fieldContext.fieldBitmask();
	}

	/**
	 * Bitshift.
	 *
	 * @param strideOffset the stride offset
	 * @return the int
	 * @see com.slytechs.jnet.runtime.internal.layout.BitOperator#bitshift(long, long)
	 */
	@Override
	public int bitshift(long strideOffset) {
		return fieldContext.fieldBitshift(strideOffset);
	}

	/**
	 * Gets the byte.
	 *
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the byte
	 * @see com.slytechs.jnet.runtime.internal.layout.BitOperator#getByteAtOffset(java.lang.Object,
	 *      byte, long, long)
	 */
	@Override
	public byte getByte(byte carrier, long strideOffset) {
		return get08(carrier, bitshift(strideOffset), (byte) bitmask());
	}

	/**
	 * Gets the int.
	 *
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the int
	 * @see com.slytechs.jnet.runtime.internal.layout.BitOperator#getIntAtOffset(java.lang.Object, int,
	 *      long, long)
	 */
	@Override
	public int getInt(int carrier, long strideOffset) {
		return get32(carrier, bitshift(strideOffset), (int) bitmask());
	}

	/**
	 * Gets the long.
	 *
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the long
	 * @see com.slytechs.jnet.runtime.internal.layout.BitOperator#getLongAtOffset(java.lang.Object,
	 *      long, long, long)
	 */
	@Override
	public long getLong(long carrier, long strideOffset) {
		return get64(carrier, bitshift(strideOffset), (long) bitmask());
	}

	/**
	 * Gets the short.
	 *
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the short
	 * @see com.slytechs.jnet.runtime.internal.layout.BitOperator#getShortAtOffset(java.lang.Object,
	 *      short, long, long)
	 */
	@Override
	public short getShort(short carrier, long strideOffset) {
		return get16(carrier, bitshift(strideOffset), (short) bitmask());
	}

	/**
	 * Sets the byte.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the byte
	 * @see com.slytechs.jnet.runtime.internal.layout.BitOperator#setByteAtOffset(byte,
	 *      java.lang.Object, byte, long, long)
	 */
	@Override
	public byte setByte(byte value, byte carrier, long strideOffset) {
		return set08(carrier, bitshift(strideOffset), (byte) bitmask(), value);
	}

	/**
	 * Sets the int.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the int
	 * @see com.slytechs.jnet.runtime.internal.layout.BitOperator#setIntAtOffset(int, java.lang.Object,
	 *      int, long, long)
	 */
	@Override
	public int setInt(int value, int carrier, long strideOffset) {
		return set32(carrier, bitshift(strideOffset), (int) bitmask(), value);
	}

	/**
	 * Sets the long.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the long
	 * @see com.slytechs.jnet.runtime.internal.layout.BitOperator#setLongAtOffset(long,
	 *      java.lang.Object, long, long, long)
	 */
	@Override
	public long setLong(long value, long carrier, long strideOffset) {
		return set64(carrier, bitshift(strideOffset), (long) bitmask(), value);
	}

	/**
	 * Sets the short.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the short
	 * @see com.slytechs.jnet.runtime.internal.layout.BitOperator#setShortAtOffset(short,
	 *      java.lang.Object, short, long, long)
	 */
	@Override
	public short setShort(short value, short carrier, long strideOffset) {
		return set16(carrier, bitshift(strideOffset), (short) bitmask(), value);
	}

}
