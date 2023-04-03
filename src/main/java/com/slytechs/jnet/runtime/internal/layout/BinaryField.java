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

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Stream;

import com.slytechs.jnet.runtime.util.Bits;

/**
 * The Interface BinaryField.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface BinaryField {

	static <T extends Enum<T> & BinaryField> Stream<String> streamString(Class<T> enumType, Object data) {
		return Arrays.stream(enumType.getEnumConstants())
				.map(f -> f.toString(data));
	}

	static <T extends Enum<T> & BinaryField> Stream<BinaryField> streamEnumFields(Class<T> enumType) {
		return Arrays.stream(enumType.getEnumConstants());
	}

	/**
	 * Layout.
	 *
	 * @return the binary layout
	 */
	BinaryLayout layout();

	/**
	 * Field name.
	 *
	 * @return the optional
	 */
	default Optional<String> fieldName() {
		return layout().layoutName();
	}

	/**
	 * Size.
	 *
	 * @return the optional long
	 */
	default OptionalLong size() {
		return layout().size();
	}

	/**
	 * Bit size.
	 *
	 * @return the long
	 */
	default long bitSize() {
		return layout().bitSize();
	}

	/**
	 * Byte size.
	 *
	 * @return the long
	 */
	default long byteSize() {
		return Bits.bitsToBytes(bitSize());
	}

	/**
	 * Bit offset.
	 *
	 * @return the long
	 */
	long bitOffset();

	/**
	 * Bit offset.
	 *
	 * @param sequences the sequences
	 * @return the long
	 */
	long bitOffset(long... sequences);

	/**
	 * Byte offset.
	 *
	 * @return the long
	 */
	default long byteOffset() {
		return Bits.bitsToBytes(bitOffset());
	}

	/**
	 * Byte offset.
	 *
	 * @param sequences the sequences
	 * @return the long
	 */
	default long byteOffset(long... sequences) {
		return Bits.bitsToBytes(bitOffset(sequences));
	}

	/**
	 * To string.
	 *
	 * @param data      the data
	 * @param sequences the sequences
	 * @return the string
	 */
	String toString(Object data, long... sequences);
}
