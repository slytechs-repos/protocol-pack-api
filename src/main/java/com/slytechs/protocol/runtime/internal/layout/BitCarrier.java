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