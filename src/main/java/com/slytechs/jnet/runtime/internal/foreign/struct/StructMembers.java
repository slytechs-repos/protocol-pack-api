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

import com.slytechs.jnet.runtime.internal.foreign.MemoryUtils;

import static java.lang.foreign.ValueLayout.*;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class StructMembers {

	private final Map<String, StructMember> map = new HashMap<>();
	private final List<StructMember> list = new ArrayList<>();
	private final MemoryLayout layout;

	public StructMembers(MemoryLayout layout) {
		this.layout = layout;
	}

	public StructMembers(MemoryLayout layout, String path) {
		this.layout = layout.select(MemoryUtils.path(path));
	}

	public <T> StructMembers usingArray(String path, Function<MemorySegment, T> mapper) {
		array(path, mapper);

		return this;
	}

	@SuppressWarnings("unchecked")
	private <T> T fetchFromMap(String name) {
		return (T) map.get(name);
	}

	private void addMember(String name, StructMember member) {
		map.put(name, member);
		list.add(member);
	}

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

	public <T> StructMembers usingArray(String path, Function<MemorySegment, T> mapper,
			String arrayLengthValueLayoutPath) {

		final StructValue arrayLength = value(arrayLengthValueLayoutPath);

		array(path, mapper, arrayLength::getInt);

		return this;
	}

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

	public StructMembers usingGroup(String path) {
		group(path);

		return this;
	}

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

	public <T> StructMembers usingGroup(String path, Function<MemorySegment, T> mapper) {
		group(path, mapper);

		return this;
	}

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

	public <E extends Enum<E> & StructMemberName> StructMembers usingMembers(Class<E> enumType) {

		return this;
	}

	public StructMembers usingValue(String path) {
		value(path);

		return this;
	}

	public StructMembers usingIntValue(String path, IntUnaryOperator bitOperator) {
		valueLong(path, lv -> (long) bitOperator.applyAsInt((int) lv));

		return this;
	}

	public StructMembers usingLongValue(String path, LongUnaryOperator bitOperator) {
		valueLong(path, bitOperator);

		return this;
	}

	public StructValue value(String path) {
		return valueLong(path, null);
	}

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

	public StructMembers usingString(String path) {
		string(path);

		return this;
	}

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

	private Collection<StructMember> toCollection() {
		return list;
	}

	public final StringBuilder toString(StringBuilder b) {
		toCollection().stream()
				.filter(StructMember::isPrintable)
				.forEach(h -> h.toString(b).append(", "));

		return b;
	}

	public final StringBuilder toString(StringBuilder b, MemorySegment segment) {
		String temp = list.stream()
				.filter(StructMember::isPrintable)
				.map(h -> h.toString(new StringBuilder(), segment).toString())
				.collect(Collectors.joining(", "));

		return b.append(temp);
	}

	@SuppressWarnings("unchecked")
	<T extends StructMember> T getMember(String name) {
		if (!map.containsKey(name))
			throw new IllegalStateException("named '%s' struct member does not exist".formatted(name));

		return (T) map.get(name);
	}

	public long bitSize() {
		return layout.bitSize();
	}

	public long byteSize() {
		return bitSize() >> 3;
	}

	public long byteOffset(String path) {
		return layout.byteOffset(MemoryUtils.path(path));
	}

	public MemoryLayout layout() {
		return layout;
	}
}
