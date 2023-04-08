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

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Stream;

import com.slytechs.protocol.runtime.util.Bits;

/**
 * The Interface BinaryField.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface BinaryField {

	/**
	 * Stream string.
	 *
	 * @param <T>      the generic type
	 * @param enumType the enum type
	 * @param data     the data
	 * @return the stream
	 */
	static <T extends Enum<T> & BinaryField> Stream<String> streamString(Class<T> enumType, Object data) {
		return Arrays.stream(enumType.getEnumConstants())
				.map(f -> f.toString(data));
	}

	/**
	 * Stream enum fields.
	 *
	 * @param <T>      the generic type
	 * @param enumType the enum type
	 * @return the stream
	 */
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
