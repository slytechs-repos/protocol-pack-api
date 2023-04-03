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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class StructArray<T> extends StructMember {

	private final Function<MemorySegment, T> mapper;
	private final Function<MemorySegment, Integer> count;

	StructArray(
			String name,
			StructMembers struct,
			long offset,
			long size,
			Function<MemorySegment, Integer> count,
			Function<MemorySegment, T> factory) {

		super(name, struct, offset, size);
		this.count = count;

		this.mapper = factory;
	}

	public T getAtIndex(int index, MemorySegment segment) {
		MemorySegment mappedSegment = segment.asSlice(byteOffset() + (byteSize() * index), byteSize());

		return mapper.apply(mappedSegment);
	}

	public IntFunction<T> toArrayGetter(MemorySegment segment) {
		return index -> mapper.apply(segment);
	}

	public List<T> toList(MemorySegment segment) {
		int count = this.count.apply(segment);
		List<T> list = new ArrayList<>(count);

		for (int i = 0; i < count; i++)
			list.add(getAtIndex(i, segment));

		return list;
	}

	public T[] toArray(IntFunction<T[]> arrayFactory, MemorySegment segment) {
		int count = this.count.apply(segment);
		T[] array = arrayFactory.apply(count);

		for (int i = 0; i < count; i++)
			array[i] = getAtIndex(i, segment);

		return array;
	}

	public String getUtf8String(MemorySegment segment) {
		return segment.getUtf8String(byteOffset());
	}

	@Override
	public StringBuilder toString(StringBuilder b, MemorySegment segment) {
		int count = this.count.apply(segment);

		return b.append(name())
				.append('[').append(count).append(']')
				.append('=')
				.append(toList(segment));
	}
}
