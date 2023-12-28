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

import java.nio.ReadOnlyBufferException;

/**
 * The Class BitCarrierReadonly.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class BitCarrierReadonly implements BitCarrier {

	/** The field. */
	private final BitCarrier field;

	/**
	 * Instantiates a new bit carrier readonly.
	 *
	 * @param field the field
	 */
	BitCarrierReadonly(BitCarrier field) {
		this.field = field;
	}

	/**
	 * Gets the byte at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the byte at offset
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitCarrier#getByteAtOffset(java.lang.Object,
	 *      long)
	 */
	public byte getByteAtOffset(Object data, long byteOffset) {
		return field.getByteAtOffset(data, byteOffset);
	}

	/**
	 * Gets the short at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the short at offset
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitCarrier#getShortAtOffset(java.lang.Object,
	 *      long, boolean)
	 */
	public short getShortAtOffset(Object data, long byteOffset, boolean big) {
		return field.getShortAtOffset(data, byteOffset, big);
	}

	/**
	 * Gets the int at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the int at offset
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitCarrier#getIntAtOffset(java.lang.Object,
	 *      long, boolean)
	 */
	public int getIntAtOffset(Object data, long byteOffset, boolean big) {
		return field.getIntAtOffset(data, byteOffset, big);
	}

	/**
	 * Gets the long at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the long at offset
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitCarrier#getLongAtOffset(java.lang.Object,
	 *      long, boolean)
	 */
	public long getLongAtOffset(Object data, long byteOffset, boolean big) {
		return field.getLongAtOffset(data, byteOffset, big);
	}

	/**
	 * Sets the number at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the number
	 */
	public Number setNumberAtOffset(Number value, Object data, long byteOffset) {
		return throwReadonly();
	}

	/**
	 * Sets the byte at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the byte
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitCarrier#setByteAtOffset(byte,
	 *      java.lang.Object, long, boolean)
	 */
	public byte setByteAtOffset(byte value, Object data, long byteOffset, boolean big) {
		return throwReadonly();
	}

	/**
	 * Sets the short at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the short
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitCarrier#setShortAtOffset(short,
	 *      java.lang.Object, long, boolean)
	 */
	public short setShortAtOffset(short value, Object data, long byteOffset, boolean big) {
		return throwReadonly();
	}

	/**
	 * Sets the int at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the int
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitCarrier#setIntAtOffset(int,
	 *      java.lang.Object, long, boolean)
	 */
	public int setIntAtOffset(int value, Object data, long byteOffset, boolean big) {
		return throwReadonly();
	}

	/**
	 * Sets the long at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big        the big
	 * @return the long
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitCarrier#setLongAtOffset(long,
	 *      java.lang.Object, long, boolean)
	 */
	public long setLongAtOffset(long value, Object data, long byteOffset, boolean big) {
		return throwReadonly();
	}

	/**
	 * Throw readonly.
	 *
	 * @return the byte
	 */
	private byte throwReadonly() {
		throw new ReadOnlyBufferException();
	}
}
