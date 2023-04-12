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
package com.slytechs.protocol.runtime.internal.foreign.struct;

import java.lang.foreign.MemorySegment;
import java.util.function.Function;

/**
 * The Class StructGroup.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 * @param <T> the generic type
 */
public final class StructGroup<T> extends StructMember {

	/** The mapper. */
	private final Function<MemorySegment, T> mapper;

	/**
	 * Instantiates a new struct group.
	 *
	 * @param name      the name
	 * @param struct    the struct
	 * @param bitOffset the bit offset
	 * @param bitSize   the bit size
	 * @param mapper    the mapper
	 */
	StructGroup(String name, StructMembers struct, long bitOffset, long bitSize,
			Function<MemorySegment, T> mapper) {
		super(name, struct, bitOffset, bitSize);
		this.mapper = mapper;
	}

	/**
	 * Map segment.
	 *
	 * @param segment the segment
	 * @return the memory segment
	 */
	public MemorySegment mapSegment(MemorySegment segment) {
		long offset = byteOffset();
		long size = byteSize();

		if (offset != 0)
			return segment.asSlice(offset, size);

		return segment;
	}

	/**
	 * Map.
	 *
	 * @param segment the segment
	 * @return the t
	 */
	public T map(MemorySegment segment) {
		return mapper.apply(mapSegment(segment));
	}

	/**
	 * Checks if is printable.
	 *
	 * @return true, if is printable
	 * @see com.slytechs.protocol.runtime.internal.foreign.struct.StructMember#isPrintable()
	 */
	@Override
	public boolean isPrintable() {
		return mapper != null;
	}

	/**
	 * To string.
	 *
	 * @param b       the b
	 * @param segment the segment
	 * @return the string builder
	 * @see com.slytechs.protocol.runtime.internal.foreign.struct.StructMember#toString(java.lang.StringBuilder,
	 *      java.lang.foreign.MemorySegment)
	 */
	@Override
	public StringBuilder toString(StringBuilder b, MemorySegment segment) {
		if (!isPrintable())
			return b;

		return b.append(name())
				.append('=')
				.append(map(segment));
	}
}
