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
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

import com.slytechs.jnet.runtime.util.Detail;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class ForeignStruct {

	public static class Encapsulated extends ForeignStruct {

		private final ForeignStruct outter;
		private final ForeignStruct inner;

		public Encapsulated(
				StructMembers outterMembers, MemorySegment outterSegment,
				StructMembers innerMembers, String innerPath) {
			this(

					innerMembers,
					outterSegment.asSlice(outterMembers.byteOffset(innerPath), innerMembers.byteSize()),
					outterMembers,
					outterSegment

			);
		}

		public Encapsulated(
				StructMembers innerMembers, Function<MemorySegment, MemorySegment> innerSegment,
				StructMembers outterMembers, MemorySegment outterSegment) {
			this(

					innerMembers,
					innerSegment.apply(outterSegment),
					outterMembers,
					outterSegment

			);
		}

		public Encapsulated(
				StructMembers innerMembers, MemorySegment innerSegment,
				StructMembers outterMembers, MemorySegment outterSegment) {
			super(innerMembers, innerSegment);
			this.outter = new ForeignStruct(outterMembers, outterSegment);
			this.inner = this;
		}

		public ForeignStruct inner() {
			return inner;
		}

		public ForeignStruct outter() {
			return outter;
		}

		/**
		 * @see com.slytechs.jnet.jnapatech.internal.foreign.struct.ForeignStruct#buildString(java.lang.StringBuilder, Detail)
		 */
		@Override
		public StringBuilder toString(StringBuilder b) {

			super.toString(b);
			b.append("::");
			outter().toString(b);

			return b;
		}
	}

	private final StructMembers members;
	private final MemorySegment segment;

	public ForeignStruct(StructMembers members, MemorySegment segment) {
		this.members = members;
		this.segment = segment;
	}

	public MemoryAddress address() {
		return (segment == null) ? null : segment.address();
	}

	public MemoryAddress getAddress(String member) {
		StructValue value = getMember(member);

		return value.getAddress(segment);
	}

	public boolean getBoolean(String member) {
		StructValue value = getMember(member);

		return value.getBoolean(segment);
	}

	public byte getByte(String member) {
		StructValue value = getMember(member);

		return value.getByte(segment);
	}

	public int getInt(String member) {
		StructValue value = getMember(member);

		return value.getInt(segment);
	}

	public long getLong(String member) {
		StructValue value = getMember(member);

		return value.getLong(segment);
	}

	private <T extends StructMember> T getMember(String name) {
		return members.getMember(name);
	}

	public short getShort(String member) {
		StructValue value = getMember(member);

		return value.getShort(segment);
	}

	public int getUnsignedByte(String member) {
		StructValue value = getMember(member);

		return value.getUnsignedByte(segment);
	}

	public long getUnsignedInt(String member) {
		StructValue value = getMember(member);

		return value.getUnsignedInt(segment);
	}

	public int getUnsignedShort(String member) {
		StructValue value = getMember(member);

		return value.getUnsignedShort(segment);
	}

	public String getUtf8String(String member) {
		StructArray<String> value = getMember(member);

		return value.getUtf8String(segment);
	}

	public <T> T map(String member) {
		StructGroup<T> structMember = getMember(member);

		return structMember.map(segment);
	}

	public <T extends Enum<T>> T mapToEnum(String member, Class<T> enumType) {
		T[] array = enumType.getEnumConstants();

		return array[getInt(member)];
	}

	public synchronized MemorySegment segment() {
		return segment;
	}

	public void setBoolean(String member, boolean b) {
		StructValue value = getMember(member);

		value.setBoolean(segment, b);
	}

	public void setByte(String member, byte v) {
		StructValue value = getMember(member);

		value.setByte(segment, v);
	}

	public void setInt(String member, int v) {
		StructValue value = getMember(member);

		value.setInt(segment, v);
	}

	public void setLong(String member, long v) {
		StructValue value = getMember(member);

		value.setLong(segment, v);
	}

	public void setShort(String member, short v) {
		StructValue value = getMember(member);

		value.setShort(segment, v);
	}

	public <T> T[] toArray(String member, IntFunction<T[]> arrayFactory) {
		StructArray<T> structArray = getMember(member);

		return structArray.toArray(arrayFactory, segment);
	}

	public <T> T toArrayElement(String member, int index) {
		StructArray<T> structArray = getMember(member);

		return structArray.toArrayGetter(segment).apply(index);
	}

	public <T> IntFunction<T> toArrayGetter(String member) {
		StructArray<T> structArray = getMember(member);

		return structArray.toArrayGetter(segment);
	}

	public <T> List<T> toList(String member) {
		StructArray<T> structArray = getMember(member);

		return structArray.toList(segment);
	}

	@Override
	public String toString() {
		return toString(new StringBuilder()).toString();
	}

	public StringBuilder toString(StringBuilder b) {
		String name = getClass().getSimpleName();

		b.append(name)
				.append(" [");

		b = members.toString(b, segment());

		b.append(']');

		return b;
	}
}
