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
package com.slytechs.protocol.runtime.util;

import java.nio.ByteBuffer;

/**
 * Interface which provides various {@code toHexdump} methods which return a
 * hexdump string. Various overrides are provided that can display different
 * detail output or portions of the data buffer.
 * 
 * <p>
 * The only method that must be implemented is the {@link #buffer()} method,
 * which provides the data buffer that all of the {@code toHexdump} methods
 * operate on.
 * </p>
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface ToHexdump {

	/** The default detail. */
	Detail DEFAULT_DETAIL = Detail.LOW;

	/**
	 * Buffer.
	 *
	 * @return the byte buffer
	 */
	ByteBuffer buffer();

	/**
	 * Generates a hex-dump output as a String of the buffer backing this header.
	 *
	 * @return the generated hexdump string
	 */
	default String toHexdump() {
		return toHexdump(DEFAULT_DETAIL);
	}

	/**
	 * Generates a hex-dump output as a String of the buffer backing this header at
	 * a specific detail level.
	 *
	 * @param detail the detail level
	 * @return the generated hexdump string
	 */
	default String toHexdump(Detail detail) {
		return switch (detail) {
		case DEBUG, TRACE, HIGH -> HexStrings.toHexTextDump(buffer());
		case MEDIUM -> HexStrings.toHexDump(buffer());
		case LOW -> HexStrings.toHexString(buffer());
		case OFF -> "";
		};
	}

	/**
	 * Generates a hex-dump output as a String of the buffer backing this header at
	 * a default detail level and offset to the end of the buffer.
	 *
	 * @param offset the offset
	 * @return the generated hexdump string
	 */
	default String toHexdump(int offset) {
		return toHexdump(offset, DEFAULT_DETAIL);
	}

	/**
	 * Generates a hex-dump output as a String of the buffer backing this header at
	 * a specific detail level and offset to the end of the buffer.
	 *
	 * @param offset the offset
	 * @param detail the detail
	 * @return the generated hexdump string
	 */
	default String toHexdump(int offset, Detail detail) {
		return toHexdump(offset, buffer().limit() - offset, detail);
	}

	/**
	 * Generates a hex-dump output as a String of the buffer backing this header at
	 * a default detail level, from starting offset and length.
	 * 
	 * @param offset the offset
	 * @param length the length
	 * @return the string
	 */
	default String toHexdump(int offset, int length) {
		return toHexdump(offset, length, DEFAULT_DETAIL);
	}

	/**
	 * Generates a hex-dump output as a String of the buffer backing this header at
	 * a specific detail level, from starting offset and length.
	 *
	 * @param offset the offset
	 * @param length the length
	 * @param detail the detail
	 * @return the generated hexdump string
	 */
	default String toHexdump(int offset, int length, Detail detail) {
		ByteBuffer slice = buffer().slice(offset, length);

		return switch (detail) {
		case DEBUG, TRACE, HIGH -> HexStrings.toHexTextDump(slice);
		case MEDIUM -> HexStrings.toHexDump(slice);
		case LOW -> HexStrings.toHexString(slice);
		case OFF -> "";
		};
	}
}
