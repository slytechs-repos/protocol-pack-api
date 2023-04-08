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

/**
 * The Class StructMember.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract class StructMember {

	/** The name. */
	private final String name;
	
	/** The bit offset. */
	private final long bitOffset;
	
	/** The bit size. */
	private final long bitSize;

	/**
	 * Instantiates a new struct member.
	 *
	 * @param name      the name
	 * @param members   the members
	 * @param bitOffset the bit offset
	 */
	public StructMember(String name, StructMembers members, long bitOffset) {
		this.name = name;
		this.bitOffset = bitOffset;
		this.bitSize = -1;
	}

	/**
	 * Instantiates a new struct member.
	 *
	 * @param name      the name
	 * @param members   the members
	 * @param bitOffset the bit offset
	 * @param bitSize   the bit size
	 */
	public StructMember(String name, StructMembers members, long bitOffset, long bitSize) {
		this.name = name;
		this.bitOffset = bitOffset;
		this.bitSize = bitSize;
	}
	
	/**
	 * Checks if is printable.
	 *
	 * @return true, if is printable
	 */
	public boolean isPrintable() {
		return true;
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	public String name() {
		return name;
	}

	/**
	 * Bit size.
	 *
	 * @return the long
	 */
	public long bitSize() {
		return bitSize;
	}

	/**
	 * Byte size.
	 *
	 * @return the long
	 */
	public long byteSize() {
		return bitSize >> 3;
	}

	/**
	 * Bit offset.
	 *
	 * @return the long
	 */
	public long bitOffset() {
		return bitOffset;
	}

	/**
	 * Byte offset.
	 *
	 * @return the long
	 */
	public long byteOffset() {
		return bitOffset >> 3;
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
		return b.append(name());
	}

	/**
	 * To string.
	 *
	 * @param b       the b
	 * @param segment the segment
	 * @return the string builder
	 */
	public abstract StringBuilder toString(StringBuilder b, MemorySegment segment);
}
