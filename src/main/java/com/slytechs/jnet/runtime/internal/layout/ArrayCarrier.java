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

import com.slytechs.jnet.runtime.util.ByteArray;

/**
 * The Interface ArrayCarrier.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
interface ArrayCarrier {

	/**
	 * Gets the byte array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the byte array at offset
	 */
	byte[] getByteArrayAtOffset(byte[] array, int offset, int length, Object data, long byteOffset);

	/**
	 * Gets the int array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the int array at offset
	 */
	int[] getIntArrayAtOffset(int[] array, int offset, int length, Object data, long byteOffset);

	/**
	 * Gets the long array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the long array at offset
	 */
	long[] getLongArrayAtOffset(long[] array, int offset, int length, Object data, long byteOffset);

	/**
	 * Gets the short array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the short array at offset
	 */
	short[] getShortArrayAtOffset(short[] array, int offset, int length, Object data, long byteOffset);

	/**
	 * Sets the byte array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the byte[]
	 */
	byte[] setByteArrayAtOffset(byte[] array, int offset, int length, Object data, long byteOffset);

	/**
	 * Sets the int array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the int[]
	 */
	int[] setIntArrayAtOffset(int[] array, int offset, int length, Object data, long byteOffset);

	/**
	 * Sets the long array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the long[]
	 */
	long[] setLongArrayAtOffset(long[] array, int offset, int length, Object data, long byteOffset);

	/**
	 * Sets the short array at offset.
	 *
	 * @param array      the array
	 * @param offset     the offset
	 * @param length     the length
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @return the short[]
	 */
	short[] setShortArrayAtOffset(short[] array, int offset, int length, Object data, long byteOffset);

	/**
	 * Wrap at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param byteSize   the byte size
	 * @return the byte array
	 */
	ByteArray wrapAtOffset(byte[] data, long byteOffset, long byteSize);

	/**
	 * Wrap at offset.
	 *
	 * @param data       the data
	 * @param byteOffset the byte offset
	 * @param byteSize   the byte size
	 * @return the byte array
	 */
	ByteArray wrapAtOffset(ByteArray data, long byteOffset, long byteSize);
}  