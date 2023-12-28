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
package com.slytechs.jnet.jnetruntime.internal.foreign.struct;

import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * The Class StructArray.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 * @param <T> the generic type
 */
public final class StructArray<T> extends StructMember {

	/** The mapper. */
	private final Function<MemorySegment, T> mapper;
	
	/** The count. */
	private final Function<MemorySegment, Integer> count;

	/**
	 * Instantiates a new struct array.
	 *
	 * @param name    the name
	 * @param struct  the struct
	 * @param offset  the offset
	 * @param size    the size
	 * @param count   the count
	 * @param factory the factory
	 */
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

	/**
	 * Gets the at index.
	 *
	 * @param index   the index
	 * @param segment the segment
	 * @return the at index
	 */
	public T getAtIndex(int index, MemorySegment segment) {
		MemorySegment mappedSegment = segment.asSlice(byteOffset() + (byteSize() * index), byteSize());

		return mapper.apply(mappedSegment);
	}

	/**
	 * To array getter.
	 *
	 * @param segment the segment
	 * @return the int function
	 */
	public IntFunction<T> toArrayGetter(MemorySegment segment) {
		return index -> mapper.apply(segment);
	}

	/**
	 * To list.
	 *
	 * @param segment the segment
	 * @return the list
	 */
	public List<T> toList(MemorySegment segment) {
		int count = this.count.apply(segment);
		List<T> list = new ArrayList<>(count);

		for (int i = 0; i < count; i++)
			list.add(getAtIndex(i, segment));

		return list;
	}

	/**
	 * To array.
	 *
	 * @param arrayFactory the array factory
	 * @param segment      the segment
	 * @return the t[]
	 */
	public T[] toArray(IntFunction<T[]> arrayFactory, MemorySegment segment) {
		int count = this.count.apply(segment);
		T[] array = arrayFactory.apply(count);

		for (int i = 0; i < count; i++)
			array[i] = getAtIndex(i, segment);

		return array;
	}

	/**
	 * Gets the utf 8 string.
	 *
	 * @param segment the segment
	 * @return the utf 8 string
	 */
	public String getUtf8String(MemorySegment segment) {
		return segment.getUtf8String(byteOffset());
	}

	/**
	 * To string.
	 *
	 * @param b       the b
	 * @param segment the segment
	 * @return the string builder
	 * @see com.slytechs.jnet.jnetruntime.internal.foreign.struct.StructMember#toString(java.lang.StringBuilder,
	 *      java.lang.foreign.MemorySegment)
	 */
	@Override
	public StringBuilder toString(StringBuilder b, MemorySegment segment) {
		int count = this.count.apply(segment);

		return b.append(name())
				.append('[').append(count).append(']')
				.append('=')
				.append(toList(segment));
	}
}
