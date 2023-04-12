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

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemoryLayout.PathElement;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SequenceLayout;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.stream.Collectors;

import com.slytechs.protocol.runtime.internal.foreign.MemoryUtils;

import static java.lang.foreign.ValueLayout.*;

/**
 * The Class StructMembers.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public final class StructMembers {

	/** The map. */
	private final Map<String, StructMember> map = new HashMap<>();
	
	/** The list. */
	private final List<StructMember> list = new ArrayList<>();
	
	/** The layout. */
	private final MemoryLayout layout;

	/**
	 * Instantiates a new struct members.
	 *
	 * @param layout the layout
	 */
	public StructMembers(MemoryLayout layout) {
		this.layout = layout;
	}

	/**
	 * Instantiates a new struct members.
	 *
	 * @param layout the layout
	 * @param path   the path
	 */
	public StructMembers(MemoryLayout layout, String path) {
		this.layout = layout.select(MemoryUtils.path(path));
	}

	/**
	 * Using array.
	 *
	 * @param <T>    the generic type
	 * @param path   the path
	 * @param mapper the mapper
	 * @return the struct members
	 */
	public <T> StructMembers usingArray(String path, Function<MemorySegment, T> mapper) {
		array(path, mapper);

		return this;
	}

	/**
	 * Fetch from map.
	 *
	 * @param <T>  the generic type
	 * @param name the name
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	private <T> T fetchFromMap(String name) {
		return (T) map.get(name);
	}

	/**
	 * Adds the member.
	 *
	 * @param name   the name
	 * @param member the member
	 */
	private void addMember(String name, StructMember member) {
		map.put(name, member);
		list.add(member);
	}

	/**
	 * Array.
	 *
	 * @param <T>    the generic type
	 * @param path   the path
	 * @param mapper the mapper
	 * @return the struct array
	 */
	public <T> StructArray<T> array(String path, Function<MemorySegment, T> mapper) {
		String name = path;
		if (map.containsKey(name))
			return fetchFromMap(name);

		PathElement[] p = MemoryUtils.path(path);

		if (!(layout.select(p) instanceof SequenceLayout sequence))
			throw new IllegalArgumentException("only sequence layouts are mappable %s".formatted(path));

		long bitOffset = layout.bitOffset(p);
		long bitSize = sequence.elementLayout().bitSize();
		int count = (int) sequence.elementCount();

		var handle = new StructArray<>(name, this, bitOffset, bitSize, t -> count, mapper);

		addMember(name, handle);

		return handle;
	}

	/**
	 * Using array.
	 *
	 * @param <T>                        the generic type
	 * @param path                       the path
	 * @param mapper                     the mapper
	 * @param arrayLengthValueLayoutPath the array length value layout path
	 * @return the struct members
	 */
	public <T> StructMembers usingArray(String path, Function<MemorySegment, T> mapper,
			String arrayLengthValueLayoutPath) {

		final StructValue arrayLength = value(arrayLengthValueLayoutPath);

		array(path, mapper, arrayLength::getInt);

		return this;
	}

	/**
	 * Array.
	 *
	 * @param <T>    the generic type
	 * @param path   the path
	 * @param mapper the mapper
	 * @param count  the count
	 * @return the struct array
	 */
	public <T> StructArray<T> array(String path, Function<MemorySegment, T> mapper,
			Function<MemorySegment, Integer> count) {
		String name = path;
		if (map.containsKey(name))
			return fetchFromMap(name);

		PathElement[] p = MemoryUtils.path(path);

		if (!(layout.select(p) instanceof SequenceLayout sequence))
			throw new IllegalArgumentException("only sequence layouts are mappable %s".formatted(path));

		long bitOffset = layout.bitOffset(p);
		long bitSize = sequence.elementLayout().bitSize();

		var handle = new StructArray<>(name, this, bitOffset, bitSize, count, mapper);

		addMember(name, handle);

		return handle;
	}

	/**
	 * Using group.
	 *
	 * @param path the path
	 * @return the struct members
	 */
	public StructMembers usingGroup(String path) {
		group(path);

		return this;
	}

	/**
	 * Group.
	 *
	 * @param path the path
	 * @return the struct group
	 */
	public StructGroup<?> group(String path) {
		String name = path;
		if (map.containsKey(name))
			return fetchFromMap(name);

		PathElement[] p = MemoryUtils.path(path);
		if (!(layout.select(p) instanceof GroupLayout group))
			throw new IllegalArgumentException("only group layouts are allowed %s".formatted(path));

		long bitOffset = layout.bitOffset(p);
		long bitSize = group.bitSize();

		var handle = new StructGroup<>(name, this, bitOffset, bitSize, null);

		addMember(name, handle);

		return handle;
	}

	/**
	 * Using group.
	 *
	 * @param <T>    the generic type
	 * @param path   the path
	 * @param mapper the mapper
	 * @return the struct members
	 */
	public <T> StructMembers usingGroup(String path, Function<MemorySegment, T> mapper) {
		group(path, mapper);

		return this;
	}

	/**
	 * Group.
	 *
	 * @param <T>    the generic type
	 * @param path   the path
	 * @param mapper the mapper
	 * @return the struct group
	 */
	public <T> StructGroup<T> group(String path, Function<MemorySegment, T> mapper) {
		String name = path;
		if (map.containsKey(name))
			return fetchFromMap(name);

		PathElement[] p = MemoryUtils.path(path);

		if (!(layout.select(p) instanceof GroupLayout group))
			throw new IllegalArgumentException("only group layouts are allowed %s".formatted(path));

		long bitOffset = layout.bitOffset(p);
		long bitSize = group.bitSize();

		var handle = new StructGroup<>(name, this, bitOffset, bitSize, mapper);

		addMember(name, handle);

		return handle;
	}

	/**
	 * Using members.
	 *
	 * @param <E>      the element type
	 * @param enumType the enum type
	 * @return the struct members
	 */
	public <E extends Enum<E> & StructMemberName> StructMembers usingMembers(Class<E> enumType) {

		return this;
	}

	/**
	 * Using value.
	 *
	 * @param path the path
	 * @return the struct members
	 */
	public StructMembers usingValue(String path) {
		value(path);

		return this;
	}

	/**
	 * Using int value.
	 *
	 * @param path        the path
	 * @param bitOperator the bit operator
	 * @return the struct members
	 */
	public StructMembers usingIntValue(String path, IntUnaryOperator bitOperator) {
		valueLong(path, lv -> (long) bitOperator.applyAsInt((int) lv));

		return this;
	}

	/**
	 * Using long value.
	 *
	 * @param path        the path
	 * @param bitOperator the bit operator
	 * @return the struct members
	 */
	public StructMembers usingLongValue(String path, LongUnaryOperator bitOperator) {
		valueLong(path, bitOperator);

		return this;
	}

	/**
	 * Value.
	 *
	 * @param path the path
	 * @return the struct value
	 */
	public StructValue value(String path) {
		return valueLong(path, null);
	}

	/**
	 * Value long.
	 *
	 * @param path        the path
	 * @param bitOperator the bit operator
	 * @return the struct value
	 */
	private StructValue valueLong(String path, LongUnaryOperator bitOperator) {
		String name = path;
		if (map.containsKey(name))
			return fetchFromMap(name);

		PathElement[] p = MemoryUtils.path(path);

		if (!(layout.select(p) instanceof ValueLayout value))
			throw new IllegalArgumentException("only value layouts are allowed %s".formatted(path));

		VarHandle varHandle = layout.varHandle(p);
		long bitOffset = layout.bitOffset(p);
		long bitSize = value.bitSize();

		var handle = new StructValue(name, this, varHandle, bitOffset, bitSize, bitOperator);

		addMember(name, handle);

		return handle;
	}

	/**
	 * Value.
	 *
	 * @param <T>     the generic type
	 * @param path    the path
	 * @param factory the factory
	 * @return the struct value
	 */
	public <T> StructValue value(String path, Function<MemorySegment, T> factory) {
		String name = path;
		if (map.containsKey(name))
			return fetchFromMap(name);

		PathElement[] p = MemoryUtils.path(path);

		if (!(layout.select(p) instanceof ValueLayout value))
			throw new IllegalArgumentException("only value layouts are allowed %s".formatted(path));

		VarHandle varHandle = layout.varHandle(p);
		long bitOffset = layout.bitOffset(p);
		long bitSize = value.bitSize();

		var handle = new StructValue(name, this, varHandle, bitOffset, bitSize, null);

		addMember(name, handle);

		return handle;
	}

	/**
	 * Using string.
	 *
	 * @param path the path
	 * @return the struct members
	 */
	public StructMembers usingString(String path) {
		string(path);

		return this;
	}

	/**
	 * String.
	 *
	 * @param path the path
	 * @return the struct utf 8 string
	 */
	public StructUtf8String string(String path) {
		String name = path;
		if (map.containsKey(name))
			return fetchFromMap(name);

		PathElement[] p = MemoryUtils.path(path);
		MemoryLayout selected = layout.select(p);

		if (selected instanceof SequenceLayout seq && seq.elementLayout().equals(JAVA_BYTE)) {

			long bitOffset = layout.bitOffset(p);
			long bitSize = seq.bitSize();

			var handle = new StructUtf8String(name, this, bitOffset, bitSize, StructUtf8String::readByteSequence);

			addMember(name, handle);

			return handle;

		} else if (selected.equals(ADDRESS)) {
			long bitOffset = layout.bitOffset(p);
			long bitSize = -1;

			var handle = new StructUtf8String(name, this, bitOffset, bitSize, StructUtf8String::readUtf8StringPointer);

			addMember(name, handle);

			return handle;

		} else {
			throw new IllegalArgumentException(
					"only UTF8 string layouts (sequence/JAVA_BYTE or ADDRESS) are allowed %s [%s]"
							.formatted(path, layout.select(p)));
		}
	}

	/**
	 * To collection.
	 *
	 * @return the collection
	 */
	private Collection<StructMember> toCollection() {
		return list;
	}

	/**
	 * To string.
	 *
	 * @param b the b
	 * @return the string builder
	 */
	public final StringBuilder toString(StringBuilder b) {
		toCollection().stream()
				.filter(StructMember::isPrintable)
				.forEach(h -> h.toString(b).append(", "));

		return b;
	}

	/**
	 * To string.
	 *
	 * @param b       the b
	 * @param segment the segment
	 * @return the string builder
	 */
	public final StringBuilder toString(StringBuilder b, MemorySegment segment) {
		String temp = list.stream()
				.filter(StructMember::isPrintable)
				.map(h -> h.toString(new StringBuilder(), segment).toString())
				.collect(Collectors.joining(", "));

		return b.append(temp);
	}

	/**
	 * Gets the member.
	 *
	 * @param <T>  the generic type
	 * @param name the name
	 * @return the member
	 */
	@SuppressWarnings("unchecked")
	<T extends StructMember> T getMember(String name) {
		if (!map.containsKey(name))
			throw new IllegalStateException("named '%s' struct member does not exist".formatted(name));

		return (T) map.get(name);
	}

	/**
	 * Bit size.
	 *
	 * @return the long
	 */
	public long bitSize() {
		return layout.bitSize();
	}

	/**
	 * Byte size.
	 *
	 * @return the long
	 */
	public long byteSize() {
		return bitSize() >> 3;
	}

	/**
	 * Byte offset.
	 *
	 * @param path the path
	 * @return the long
	 */
	public long byteOffset(String path) {
		return layout.byteOffset(MemoryUtils.path(path));
	}

	/**
	 * Layout.
	 *
	 * @return the memory layout
	 */
	public MemoryLayout layout() {
		return layout;
	}
}
