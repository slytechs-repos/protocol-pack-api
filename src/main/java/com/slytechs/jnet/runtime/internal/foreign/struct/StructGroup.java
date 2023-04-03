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
import java.util.function.Function;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class StructGroup<T> extends StructMember {

	private final Function<MemorySegment, T> mapper;

	StructGroup(String name, StructMembers struct, long bitOffset, long bitSize,
			Function<MemorySegment, T> mapper) {
		super(name, struct, bitOffset, bitSize);
		this.mapper = mapper;
	}

	public MemorySegment mapSegment(MemorySegment segment) {
		long offset = byteOffset();
		long size = byteSize();

		if (offset != 0)
			return segment.asSlice(offset, size);

		return segment;
	}

	public T map(MemorySegment segment) {
		return mapper.apply(mapSegment(segment));
	}

	@Override
	public boolean isPrintable() {
		return mapper != null;
	}

	@Override
	public StringBuilder toString(StringBuilder b, MemorySegment segment) {
		if (!isPrintable())
			return b;

		return b.append(name())
				.append('=')
				.append(map(segment));
	}
}
