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

import com.slytechs.protocol.runtime.internal.util.ByteArray;

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