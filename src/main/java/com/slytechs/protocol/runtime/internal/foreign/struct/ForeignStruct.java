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

import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

import com.slytechs.protocol.runtime.util.Detail;

/**
 * The Class ForeignStruct.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class ForeignStruct {

	/**
	 * The Class Encapsulated.
	 */
	public static class Encapsulated extends ForeignStruct {

		/** The outter. */
		private final ForeignStruct outter;
		
		/** The inner. */
		private final ForeignStruct inner;

		/**
		 * Instantiates a new encapsulated.
		 *
		 * @param outterMembers the outter members
		 * @param outterSegment the outter segment
		 * @param innerMembers  the inner members
		 * @param innerPath     the inner path
		 */
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

		/**
		 * Instantiates a new encapsulated.
		 *
		 * @param innerMembers  the inner members
		 * @param innerSegment  the inner segment
		 * @param outterMembers the outter members
		 * @param outterSegment the outter segment
		 */
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

		/**
		 * Instantiates a new encapsulated.
		 *
		 * @param innerMembers  the inner members
		 * @param innerSegment  the inner segment
		 * @param outterMembers the outter members
		 * @param outterSegment the outter segment
		 */
		public Encapsulated(
				StructMembers innerMembers, MemorySegment innerSegment,
				StructMembers outterMembers, MemorySegment outterSegment) {
			super(innerMembers, innerSegment);
			this.outter = new ForeignStruct(outterMembers, outterSegment);
			this.inner = this;
		}

		/**
		 * Inner.
		 *
		 * @return the foreign struct
		 */
		public ForeignStruct inner() {
			return inner;
		}

		/**
		 * Outter.
		 *
		 * @return the foreign struct
		 */
		public ForeignStruct outter() {
			return outter;
		}

		/**
		 * To string.
		 *
		 * @param b the b
		 * @return the string builder
		 * @see com.slytechs.jnet.jnapatech.internal.foreign.struct.ForeignStruct#buildString(java.lang.StringBuilder,
		 *      Detail)
		 */
		@Override
		public StringBuilder toString(StringBuilder b) {

			super.toString(b);
			b.append("::");
			outter().toString(b);

			return b;
		}
	}

	/** The members. */
	private final StructMembers members;
	
	/** The segment. */
	private final MemorySegment segment;

	/**
	 * Instantiates a new foreign struct.
	 *
	 * @param members the members
	 * @param segment the segment
	 */
	public ForeignStruct(StructMembers members, MemorySegment segment) {
		this.members = members;
		this.segment = segment;
	}

	/**
	 * Address.
	 *
	 * @return the memory address
	 */
	public MemoryAddress address() {
		return (segment == null) ? null : segment.address();
	}

	/**
	 * Gets the address.
	 *
	 * @param member the member
	 * @return the address
	 */
	public MemoryAddress getAddress(String member) {
		StructValue value = getMember(member);

		return value.getAddress(segment);
	}

	/**
	 * Gets the boolean.
	 *
	 * @param member the member
	 * @return the boolean
	 */
	public boolean getBoolean(String member) {
		StructValue value = getMember(member);

		return value.getBoolean(segment);
	}

	/**
	 * Gets the byte.
	 *
	 * @param member the member
	 * @return the byte
	 */
	public byte getByte(String member) {
		StructValue value = getMember(member);

		return value.getByte(segment);
	}

	/**
	 * Gets the int.
	 *
	 * @param member the member
	 * @return the int
	 */
	public int getInt(String member) {
		StructValue value = getMember(member);

		return value.getInt(segment);
	}

	/**
	 * Gets the long.
	 *
	 * @param member the member
	 * @return the long
	 */
	public long getLong(String member) {
		StructValue value = getMember(member);

		return value.getLong(segment);
	}

	/**
	 * Gets the member.
	 *
	 * @param <T>  the generic type
	 * @param name the name
	 * @return the member
	 */
	private <T extends StructMember> T getMember(String name) {
		return members.getMember(name);
	}

	/**
	 * Gets the short.
	 *
	 * @param member the member
	 * @return the short
	 */
	public short getShort(String member) {
		StructValue value = getMember(member);

		return value.getShort(segment);
	}

	/**
	 * Gets the unsigned byte.
	 *
	 * @param member the member
	 * @return the unsigned byte
	 */
	public int getUnsignedByte(String member) {
		StructValue value = getMember(member);

		return value.getUnsignedByte(segment);
	}

	/**
	 * Gets the unsigned int.
	 *
	 * @param member the member
	 * @return the unsigned int
	 */
	public long getUnsignedInt(String member) {
		StructValue value = getMember(member);

		return value.getUnsignedInt(segment);
	}

	/**
	 * Gets the unsigned short.
	 *
	 * @param member the member
	 * @return the unsigned short
	 */
	public int getUnsignedShort(String member) {
		StructValue value = getMember(member);

		return value.getUnsignedShort(segment);
	}

	/**
	 * Gets the utf 8 string.
	 *
	 * @param member the member
	 * @return the utf 8 string
	 */
	public String getUtf8String(String member) {
		StructArray<String> value = getMember(member);

		return value.getUtf8String(segment);
	}

	/**
	 * Map.
	 *
	 * @param <T>    the generic type
	 * @param member the member
	 * @return the t
	 */
	public <T> T map(String member) {
		StructGroup<T> structMember = getMember(member);

		return structMember.map(segment);
	}

	/**
	 * Map to enum.
	 *
	 * @param <T>      the generic type
	 * @param member   the member
	 * @param enumType the enum type
	 * @return the t
	 */
	public <T extends Enum<T>> T mapToEnum(String member, Class<T> enumType) {
		T[] array = enumType.getEnumConstants();

		return array[getInt(member)];
	}

	/**
	 * Segment.
	 *
	 * @return the memory segment
	 */
	public synchronized MemorySegment segment() {
		return segment;
	}

	/**
	 * Sets the boolean.
	 *
	 * @param member the member
	 * @param b      the b
	 */
	public void setBoolean(String member, boolean b) {
		StructValue value = getMember(member);

		value.setBoolean(segment, b);
	}

	/**
	 * Sets the byte.
	 *
	 * @param member the member
	 * @param v      the v
	 */
	public void setByte(String member, byte v) {
		StructValue value = getMember(member);

		value.setByte(segment, v);
	}

	/**
	 * Sets the int.
	 *
	 * @param member the member
	 * @param v      the v
	 */
	public void setInt(String member, int v) {
		StructValue value = getMember(member);

		value.setInt(segment, v);
	}

	/**
	 * Sets the long.
	 *
	 * @param member the member
	 * @param v      the v
	 */
	public void setLong(String member, long v) {
		StructValue value = getMember(member);

		value.setLong(segment, v);
	}

	/**
	 * Sets the short.
	 *
	 * @param member the member
	 * @param v      the v
	 */
	public void setShort(String member, short v) {
		StructValue value = getMember(member);

		value.setShort(segment, v);
	}

	/**
	 * To array.
	 *
	 * @param <T>          the generic type
	 * @param member       the member
	 * @param arrayFactory the array factory
	 * @return the t[]
	 */
	public <T> T[] toArray(String member, IntFunction<T[]> arrayFactory) {
		StructArray<T> structArray = getMember(member);

		return structArray.toArray(arrayFactory, segment);
	}

	/**
	 * To array element.
	 *
	 * @param <T>    the generic type
	 * @param member the member
	 * @param index  the index
	 * @return the t
	 */
	public <T> T toArrayElement(String member, int index) {
		StructArray<T> structArray = getMember(member);

		return structArray.toArrayGetter(segment).apply(index);
	}

	/**
	 * To array getter.
	 *
	 * @param <T>    the generic type
	 * @param member the member
	 * @return the int function
	 */
	public <T> IntFunction<T> toArrayGetter(String member) {
		StructArray<T> structArray = getMember(member);

		return structArray.toArrayGetter(segment);
	}

	/**
	 * To list.
	 *
	 * @param <T>    the generic type
	 * @param member the member
	 * @return the list
	 */
	public <T> List<T> toList(String member) {
		StructArray<T> structArray = getMember(member);

		return structArray.toList(segment);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toString(new StringBuilder()).toString();
	}

	/**
	 * To string.
	 *
	 * @param b the b
	 * @return the string builder
	 */
	public StringBuilder toString(StringBuilder b) {
		String name = getClass().getSimpleName();

		b.append(name)
				.append(" [");

		b = members.toString(b, segment());

		b.append(']');

		return b;
	}
}
