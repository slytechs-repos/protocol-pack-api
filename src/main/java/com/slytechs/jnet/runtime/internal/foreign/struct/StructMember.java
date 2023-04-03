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

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public abstract class StructMember {

	private final String name;
	private final long bitOffset;
	private final long bitSize;

	public StructMember(String name, StructMembers members, long bitOffset) {
		this.name = name;
		this.bitOffset = bitOffset;
		this.bitSize = -1;
	}

	public StructMember(String name, StructMembers members, long bitOffset, long bitSize) {
		this.name = name;
		this.bitOffset = bitOffset;
		this.bitSize = bitSize;
	}
	
	public boolean isPrintable() {
		return true;
	}

	public String name() {
		return name;
	}

	public long bitSize() {
		return bitSize;
	}

	public long byteSize() {
		return bitSize >> 3;
	}

	public long bitOffset() {
		return bitOffset;
	}

	public long byteOffset() {
		return bitOffset >> 3;
	}

	@Override
	public String toString() {
		return toString(new StringBuilder()).toString();
	}

	public StringBuilder toString(StringBuilder b) {
		return b.append(name());
	}

	public abstract StringBuilder toString(StringBuilder b, MemorySegment segment);
}
