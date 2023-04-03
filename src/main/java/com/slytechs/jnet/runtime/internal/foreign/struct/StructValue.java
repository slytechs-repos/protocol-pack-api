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

import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongUnaryOperator;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class StructValue extends StructMember {

	private final VarHandle handle;
	private final LongUnaryOperator bitOperator;

	StructValue(String name, StructMembers members, VarHandle handle, long bitOffset, long bitSize,
			LongUnaryOperator bitOperator) {
		super(name, members, bitOffset, bitSize);
		this.handle = handle;
		this.bitOperator = bitOperator;
	}

	public Object get(MemorySegment segment) {
		if (bitOperator == null)
			return handle.get(segment);

		Number raw = (Number) handle.get(segment);

		Number bits = bitOperator.applyAsLong(raw.longValue());

		return switch (raw) {

		case Byte v -> bits.byteValue();
		case Short v -> bits.shortValue();
		case Integer v -> bits.intValue();
		case Long v -> bits.longValue();

		default -> throw new IllegalArgumentException("Unexpected value: " + raw);
		};
	}

	public MemoryAddress getAddress(MemorySegment segment) {
		return (MemoryAddress) get(segment);
	}

	public boolean getBoolean(MemorySegment segment) {
		return ((Number) get(segment)).intValue() != 0;
	}

	public byte getByte(MemorySegment segment) {
		return ((Number) get(segment)).byteValue();
	}

	public int getInt(MemorySegment segment) {
		return ((Number) get(segment)).intValue();
	}

	public long getLong(MemorySegment segment) {
		return ((Number) get(segment)).longValue();
	}

	public short getShort(MemorySegment segment) {
		return ((Number) get(segment)).shortValue();
	}

	public int getUnsignedByte(MemorySegment segment) {
		return Byte.toUnsignedInt(getByte(segment));
	}

	public long getUnsignedInt(MemorySegment segment) {
		return Integer.toUnsignedLong(getInt(segment));
	}

	public int getUnsignedShort(MemorySegment segment) {
		return Short.toUnsignedInt(getShort(segment));
	}

	public <T extends Enum<T>> T mapToEnum(Class<T> enumType, MemorySegment segment) {
		T[] array = enumType.getEnumConstants();

		return array[getInt(segment)];
	}

	public <T extends Enum<T>> T mapToEnum(IntFunction<T> enumMapper, MemorySegment segment) {
		return enumMapper.apply(getInt(segment));
	}

	public <T> T mapToObj(Function<MemorySegment, T> mapper, MemorySegment segment, long sliceOffset) {
		return mapper.apply(segment.asSlice(sliceOffset));
	}

	public <T> T mapToObj(Function<MemorySegment, T> mapper, MemorySegment segment, long sliceOffset,
			long sliceLength) {
		return mapper.apply(segment.asSlice(sliceOffset, sliceLength));
	}

	public void setBoolean(MemorySegment segment, boolean b) {
		handle.set(segment, b ? 1 : 0);
	}

	public void setByte(MemorySegment segment, byte v) {
		handle.set(segment, v);
	}

	public void setInt(MemorySegment segment, int v) {
		handle.set(segment, v);
	}

	public void setLong(MemorySegment segment, long v) {
		handle.set(segment, v);
	}

	public void setShort(MemorySegment segment, short v) {
		handle.set(segment, v);
	}

	@Override
	public String toString() {
		return name();
	}

	@Override
	public StringBuilder toString(StringBuilder b) {
		return b.append(name());
	}

	@Override
	public StringBuilder toString(StringBuilder b, MemorySegment segment) {
		return b.append(name())
				.append('=')
				.append(get(segment));
	}
}
