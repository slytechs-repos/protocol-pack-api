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
 * The Interface BitCarrier.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
interface BitCarrier {

	/**
	 * Gets the byte at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the byte at offset
	 */
	byte getByteAtOffset(Object data, long byteOffset);

	/**
	 * Gets the short at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big TODO
	 * @return the short at offset
	 */
	short getShortAtOffset(Object data, long byteOffset, boolean big);

	/**
	 * Gets the int at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big TODO
	 * @return the int at offset
	 */
	int getIntAtOffset(Object data, long byteOffset, boolean big);

	/**
	 * Gets the long at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big TODO
	 * @return the long at offset
	 */
	long getLongAtOffset(Object data, long byteOffset, boolean big);

	/**
	 * Sets the byte at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big TODO
	 * @return the byte
	 */
	byte setByteAtOffset(byte value, Object data, long byteOffset, boolean big);

	/**
	 * Sets the short at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big TODO
	 * @return the short
	 */
	short setShortAtOffset(short value, Object data, long byteOffset, boolean big);

	/**
	 * Sets the int at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big TODO
	 * @return the int
	 */
	int setIntAtOffset(int value, Object data, long byteOffset, boolean big);

	/**
	 * Sets the long at offset.
	 *
	 * @param value      the value
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param big TODO
	 * @return the long
	 */
	long setLongAtOffset(long value, Object data, long byteOffset, boolean big);

}