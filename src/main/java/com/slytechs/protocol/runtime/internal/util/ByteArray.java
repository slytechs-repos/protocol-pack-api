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
package com.slytechs.protocol.runtime.internal.util;

import java.nio.BufferUnderflowException;

/**
 * The Interface ByteArray.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface ByteArray {

	/**
	 * Allocate.
	 *
	 * @param length the length
	 * @return the byte array
	 */
	static ByteArray allocate(int length) {
		return wrap(new byte[length]);
	}

	/**
	 * Wrap.
	 *
	 * @param array the array
	 * @return the byte array
	 */
	static ByteArray wrap(byte[] array) {
		return wrap(array, 0, array.length);
	}

	/**
	 * Wrap.
	 *
	 * @param array  the array
	 * @param offset the offset
	 * @param length the length
	 * @return the byte array
	 */
	static ByteArray wrap(byte[] array, long offset, long length) {
		return wrap(array, (int) offset, (int) length);
	}

	/**
	 * Wrap the array in a ByteBuffer.
	 *
	 * @param array  the array
	 * @param offset the offset into the array
	 * @param length the length length of data within the array
	 * @return a new ByteArray wrapper
	 * @throws IllegalArgumentException if offset or length are negative
	 * @throws BufferUnderflowException if offset plus length are out of bounds for
	 *                                  the array
	 */
	static ByteArray wrap(byte[] array, int offset, int length)
			throws IllegalArgumentException, BufferUnderflowException {

		if (offset < 0 || length < 0)
			throw new IllegalArgumentException("nagagive offset or length");

		if (offset + length > array.length)
			throw new BufferUnderflowException();

		return new ByteArray() {

			@Override
			public byte[] array() {
				return array;
			}

			@Override
			public int arrayLength() {
				return length;
			}

			@Override
			public int arrayOffset() {
				return offset;
			}

			
			@Override
			public String toString() {
				return "ByteArray [off=" + offset + " len=" + length + "]";
			}
		};

	}

	/**
	 * Array.
	 *
	 * @return the byte[]
	 */
	default byte[] array() {
		return null;
	}

	/**
	 * Array length.
	 *
	 * @return the int
	 */
	default int arrayLength() {
		return array().length;
	}

	/**
	 * Array offset.
	 *
	 * @return the int
	 */
	default int arrayOffset() {
		return 0;
	}
}
