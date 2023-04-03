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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getByteAtOffset(java.lang.Object, long)
	 */
	public byte getByteAtOffset(Object data, long byteOffset) {
		return field.getByteAtOffset(data, byteOffset);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getShortAtOffset(java.lang.Object, long, boolean)
	 */
	public short getShortAtOffset(Object data, long byteOffset, boolean big) {
		return field.getShortAtOffset(data, byteOffset, big);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getIntAtOffset(java.lang.Object, long, boolean)
	 */
	public int getIntAtOffset(Object data, long byteOffset, boolean big) {
		return field.getIntAtOffset(data, byteOffset, big);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getLongAtOffset(java.lang.Object, long, boolean)
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
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setByteAtOffset(byte, java.lang.Object, long, boolean)
	 */
	public byte setByteAtOffset(byte value, Object data, long byteOffset, boolean big) {
		return throwReadonly();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setShortAtOffset(short, java.lang.Object, long, boolean)
	 */
	public short setShortAtOffset(short value, Object data, long byteOffset, boolean big) {
		return throwReadonly();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setIntAtOffset(int, java.lang.Object, long, boolean)
	 */
	public int setIntAtOffset(int value, Object data, long byteOffset, boolean big) {
		return throwReadonly();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setLongAtOffset(long, java.lang.Object, long, boolean)
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
