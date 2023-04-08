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
package com.slytechs.jnet.runtime.internal.foreign.struct;

import java.lang.foreign.MemorySegment;
import java.util.function.BiFunction;

import static java.lang.foreign.ValueLayout.ADDRESS;

/**
 * The Class StructUtf8String.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public final class StructUtf8String extends StructMember {

	/**
	 * Read byte sequence.
	 *
	 * @param segment    the segment
	 * @param byteOffset the byte offset
	 * @return the string
	 */
	public static String readByteSequence(MemorySegment segment, long byteOffset) {
		return segment.getUtf8String(byteOffset);
	}

	/**
	 * Read utf 8 string pointer.
	 *
	 * @param segment    the segment
	 * @param byteOffset the byte offset
	 * @return the string
	 */
	public static String readUtf8StringPointer(MemorySegment segment, long byteOffset) {
		return segment.get(ADDRESS, byteOffset)
				.getUtf8String(0);
	}

	/** The native accessor. */
	private final BiFunction<MemorySegment, Long, String> nativeAccessor;

	/**
	 * Instantiates a new struct utf 8 string.
	 *
	 * @param name           the name
	 * @param struct         the struct
	 * @param bitOffset      the bit offset
	 * @param bitSize        the bit size
	 * @param nativeAccessor the native accessor
	 */
	StructUtf8String(String name, StructMembers struct, long bitOffset, long bitSize,
			BiFunction<MemorySegment, Long, String> nativeAccessor) {

		super(name, struct, bitOffset, bitSize);
		this.nativeAccessor = nativeAccessor;

	}

	/**
	 * Gets the utf 8 string.
	 *
	 * @param segment the segment
	 * @return the utf 8 string
	 */
	public String getUtf8String(MemorySegment segment) {
		return nativeAccessor.apply(segment, byteOffset());
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.foreign.struct.StructMember#toString(java.lang.StringBuilder, java.lang.foreign.MemorySegment)
	 */
	@Override
	public StringBuilder toString(StringBuilder b, MemorySegment segment) {
		return b.append(name())
				.append('=')
				.append('"')
				.append(getUtf8String(segment))
				.append('"');
	}
}
