/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.runtime.internal.foreign.struct;

import java.lang.foreign.MemorySegment;
import java.util.function.BiFunction;

import static java.lang.foreign.ValueLayout.ADDRESS;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class StructUtf8String extends StructMember {

	public static String readByteSequence(MemorySegment segment, long byteOffset) {
		return segment.getUtf8String(byteOffset);
	}

	public static String readUtf8StringPointer(MemorySegment segment, long byteOffset) {
		return segment.get(ADDRESS, byteOffset)
				.getUtf8String(0);
	}

	private final BiFunction<MemorySegment, Long, String> nativeAccessor;

	StructUtf8String(String name, StructMembers struct, long bitOffset, long bitSize,
			BiFunction<MemorySegment, Long, String> nativeAccessor) {

		super(name, struct, bitOffset, bitSize);
		this.nativeAccessor = nativeAccessor;

	}

	public String getUtf8String(MemorySegment segment) {
		return nativeAccessor.apply(segment, byteOffset());
	}

	@Override
	public StringBuilder toString(StringBuilder b, MemorySegment segment) {
		return b.append(name())
				.append('=')
				.append('"')
				.append(getUtf8String(segment))
				.append('"');
	}
}
