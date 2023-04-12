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
 * The Class BitOperatorShifter.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class BitOperatorNop implements BitOperator {

	/** The field context. */
	private final BitFieldContext fieldContext;

	/**
	 * Instantiates a new bit operator shifter.
	 *
	 * @param fieldContext the field context
	 */
	BitOperatorNop(BitFieldContext fieldContext) {
		this.fieldContext = fieldContext;
	}

	/**
	 * Bitmask.
	 *
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.layout.BitOperator#bitmask(long, long)
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
	 * @see com.slytechs.protocol.runtime.internal.layout.BitOperator#bitshift(long, long)
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
	 * @see com.slytechs.protocol.runtime.internal.layout.BitOperator#getByteAtOffset(java.lang.Object,
	 *      byte, long, long)
	 */
	@Override
	public byte getByte(byte carrier, long strideOffset) {
		return carrier;
	}

	/**
	 * Gets the int.
	 *
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the int
	 * @see com.slytechs.protocol.runtime.internal.layout.BitOperator#getIntAtOffset(java.lang.Object, int,
	 *      long, long)
	 */
	@Override
	public int getInt(int carrier, long strideOffset) {
		return carrier;
	}

	/**
	 * Gets the long.
	 *
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.layout.BitOperator#getLongAtOffset(java.lang.Object,
	 *      long, long, long)
	 */
	@Override
	public long getLong(long carrier, long strideOffset) {
		return carrier;
	}

	/**
	 * Gets the short.
	 *
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the short
	 * @see com.slytechs.protocol.runtime.internal.layout.BitOperator#getShortAtOffset(java.lang.Object,
	 *      short, long, long)
	 */
	@Override
	public short getShort(short carrier, long strideOffset) {
		return carrier;
	}

	/**
	 * Sets the byte.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the byte
	 * @see com.slytechs.protocol.runtime.internal.layout.BitOperator#setByteAtOffset(byte,
	 *      java.lang.Object, byte, long, long)
	 */
	@Override
	public byte setByte(byte value, byte carrier, long strideOffset) {
		return value;
	}

	/**
	 * Sets the int.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the int
	 * @see com.slytechs.protocol.runtime.internal.layout.BitOperator#setIntAtOffset(int, java.lang.Object,
	 *      int, long, long)
	 */
	@Override
	public int setInt(int value, int carrier, long strideOffset) {
		return value;
	}

	/**
	 * Sets the long.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.layout.BitOperator#setLongAtOffset(long,
	 *      java.lang.Object, long, long, long)
	 */
	@Override
	public long setLong(long value, long carrier, long strideOffset) {
		return value;
	}

	/**
	 * Sets the short.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the short
	 * @see com.slytechs.protocol.runtime.internal.layout.BitOperator#setShortAtOffset(short,
	 *      java.lang.Object, short, long, long)
	 */
	@Override
	public short setShort(short value, short carrier, long strideOffset) {
		return value;
	}

}
