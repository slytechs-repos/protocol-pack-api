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
 * The Class BitCarrierSync.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class BitCarrierSync implements BitCarrier {

	/** The field. */
	private final BitCarrier field;

	/**
	 * Instantiates a new bit carrier sync.
	 *
	 * @param field the field
	 */
	BitCarrierSync(BitCarrier field) {
		this.field = field;
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getByteAtOffset(java.lang.Object, long)
	 */
	public byte getByteAtOffset(Object data, long byteOffset) {
		synchronized (data) {
			return field.getByteAtOffset(data, byteOffset);
		}
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getShortAtOffset(java.lang.Object, long, boolean)
	 */
	public short getShortAtOffset(Object data, long byteOffset, boolean big) {
		synchronized (data) {
			return field.getShortAtOffset(data, byteOffset, big);
		}
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getIntAtOffset(java.lang.Object, long, boolean)
	 */
	public int getIntAtOffset(Object data, long byteOffset, boolean big) {
		synchronized (data) {
			return field.getIntAtOffset(data, byteOffset, big);
		}
	}

	/**
	 * Gets the long at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the long at offset
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#getLongAtOffset(java.lang.Object,
	 *      long, boolean)
	 */
	public long getLongAtOffset(Object data, long byteOffset, boolean big) {
		synchronized (data) {
			return field.getLongAtOffset(data, byteOffset, big);
		}
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setByteAtOffset(byte, java.lang.Object, long, boolean)
	 */
	public byte setByteAtOffset(byte value, Object data, long byteOffset, boolean big) {
		synchronized (data) {
			return field.setByteAtOffset(value, data, byteOffset, big);
		}
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setShortAtOffset(short, java.lang.Object, long, boolean)
	 */
	public short setShortAtOffset(short value, Object data, long byteOffset, boolean big) {
		synchronized (data) {
			return field.setShortAtOffset(value, data, byteOffset, big);
		}
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setIntAtOffset(int, java.lang.Object, long, boolean)
	 */
	public int setIntAtOffset(int value, Object data, long byteOffset, boolean big) {
		synchronized (data) {
			return field.setIntAtOffset(value, data, byteOffset, big);
		}
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitCarrier#setLongAtOffset(long, java.lang.Object, long, boolean)
	 */
	public long setLongAtOffset(long value, Object data, long byteOffset, boolean big) {
		synchronized (data) {
			return field.setLongAtOffset(value, data, byteOffset, big);
		}
	}

}
