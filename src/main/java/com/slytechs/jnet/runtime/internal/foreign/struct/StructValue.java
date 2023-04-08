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

import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongUnaryOperator;

/**
 * The Class StructValue.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public final class StructValue extends StructMember {

	/** The handle. */
	private final VarHandle handle;
	
	/** The bit operator. */
	private final LongUnaryOperator bitOperator;

	/**
	 * Instantiates a new struct value.
	 *
	 * @param name        the name
	 * @param members     the members
	 * @param handle      the handle
	 * @param bitOffset   the bit offset
	 * @param bitSize     the bit size
	 * @param bitOperator the bit operator
	 */
	StructValue(String name, StructMembers members, VarHandle handle, long bitOffset, long bitSize,
			LongUnaryOperator bitOperator) {
		super(name, members, bitOffset, bitSize);
		this.handle = handle;
		this.bitOperator = bitOperator;
	}

	/**
	 * Gets the.
	 *
	 * @param segment the segment
	 * @return the object
	 */
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

	/**
	 * Gets the address.
	 *
	 * @param segment the segment
	 * @return the address
	 */
	public MemoryAddress getAddress(MemorySegment segment) {
		return (MemoryAddress) get(segment);
	}

	/**
	 * Gets the boolean.
	 *
	 * @param segment the segment
	 * @return the boolean
	 */
	public boolean getBoolean(MemorySegment segment) {
		return ((Number) get(segment)).intValue() != 0;
	}

	/**
	 * Gets the byte.
	 *
	 * @param segment the segment
	 * @return the byte
	 */
	public byte getByte(MemorySegment segment) {
		return ((Number) get(segment)).byteValue();
	}

	/**
	 * Gets the int.
	 *
	 * @param segment the segment
	 * @return the int
	 */
	public int getInt(MemorySegment segment) {
		return ((Number) get(segment)).intValue();
	}

	/**
	 * Gets the long.
	 *
	 * @param segment the segment
	 * @return the long
	 */
	public long getLong(MemorySegment segment) {
		return ((Number) get(segment)).longValue();
	}

	/**
	 * Gets the short.
	 *
	 * @param segment the segment
	 * @return the short
	 */
	public short getShort(MemorySegment segment) {
		return ((Number) get(segment)).shortValue();
	}

	/**
	 * Gets the unsigned byte.
	 *
	 * @param segment the segment
	 * @return the unsigned byte
	 */
	public int getUnsignedByte(MemorySegment segment) {
		return Byte.toUnsignedInt(getByte(segment));
	}

	/**
	 * Gets the unsigned int.
	 *
	 * @param segment the segment
	 * @return the unsigned int
	 */
	public long getUnsignedInt(MemorySegment segment) {
		return Integer.toUnsignedLong(getInt(segment));
	}

	/**
	 * Gets the unsigned short.
	 *
	 * @param segment the segment
	 * @return the unsigned short
	 */
	public int getUnsignedShort(MemorySegment segment) {
		return Short.toUnsignedInt(getShort(segment));
	}

	/**
	 * Map to enum.
	 *
	 * @param <T>      the generic type
	 * @param enumType the enum type
	 * @param segment  the segment
	 * @return the t
	 */
	public <T extends Enum<T>> T mapToEnum(Class<T> enumType, MemorySegment segment) {
		T[] array = enumType.getEnumConstants();

		return array[getInt(segment)];
	}

	/**
	 * Map to enum.
	 *
	 * @param <T>        the generic type
	 * @param enumMapper the enum mapper
	 * @param segment    the segment
	 * @return the t
	 */
	public <T extends Enum<T>> T mapToEnum(IntFunction<T> enumMapper, MemorySegment segment) {
		return enumMapper.apply(getInt(segment));
	}

	/**
	 * Map to obj.
	 *
	 * @param <T>         the generic type
	 * @param mapper      the mapper
	 * @param segment     the segment
	 * @param sliceOffset the slice offset
	 * @return the t
	 */
	public <T> T mapToObj(Function<MemorySegment, T> mapper, MemorySegment segment, long sliceOffset) {
		return mapper.apply(segment.asSlice(sliceOffset));
	}

	/**
	 * Map to obj.
	 *
	 * @param <T>         the generic type
	 * @param mapper      the mapper
	 * @param segment     the segment
	 * @param sliceOffset the slice offset
	 * @param sliceLength the slice length
	 * @return the t
	 */
	public <T> T mapToObj(Function<MemorySegment, T> mapper, MemorySegment segment, long sliceOffset,
			long sliceLength) {
		return mapper.apply(segment.asSlice(sliceOffset, sliceLength));
	}

	/**
	 * Sets the boolean.
	 *
	 * @param segment the segment
	 * @param b       the b
	 */
	public void setBoolean(MemorySegment segment, boolean b) {
		handle.set(segment, b ? 1 : 0);
	}

	/**
	 * Sets the byte.
	 *
	 * @param segment the segment
	 * @param v       the v
	 */
	public void setByte(MemorySegment segment, byte v) {
		handle.set(segment, v);
	}

	/**
	 * Sets the int.
	 *
	 * @param segment the segment
	 * @param v       the v
	 */
	public void setInt(MemorySegment segment, int v) {
		handle.set(segment, v);
	}

	/**
	 * Sets the long.
	 *
	 * @param segment the segment
	 * @param v       the v
	 */
	public void setLong(MemorySegment segment, long v) {
		handle.set(segment, v);
	}

	/**
	 * Sets the short.
	 *
	 * @param segment the segment
	 * @param v       the v
	 */
	public void setShort(MemorySegment segment, short v) {
		handle.set(segment, v);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.foreign.struct.StructMember#toString()
	 */
	@Override
	public String toString() {
		return name();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.foreign.struct.StructMember#toString(java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder toString(StringBuilder b) {
		return b.append(name());
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.foreign.struct.StructMember#toString(java.lang.StringBuilder, java.lang.foreign.MemorySegment)
	 */
	@Override
	public StringBuilder toString(StringBuilder b, MemorySegment segment) {
		return b.append(name())
				.append('=')
				.append(get(segment));
	}
}
