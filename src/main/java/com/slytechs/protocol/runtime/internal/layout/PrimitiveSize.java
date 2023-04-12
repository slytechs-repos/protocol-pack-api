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
package com.slytechs.protocol.runtime.internal.layout;

/**
 * The Enum PrimitiveSize.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum PrimitiveSize {

	/** The byte. */
	BYTE(PrimitiveSize.BYTE_SIZE),
	
	/** The short. */
	SHORT(PrimitiveSize.SHORT_SIZE),
	
	/** The char. */
	CHAR(PrimitiveSize.CHAR_SIZE),
	
	/** The int. */
	INT(PrimitiveSize.INT_SIZE),
	
	/** The long. */
	LONG(PrimitiveSize.LONG_SIZE),
	
	/** The float. */
	FLOAT(PrimitiveSize.FLOAT_SIZE),
	
	/** The double. */
	DOUBLE(PrimitiveSize.DOUBLE_SIZE),

	;

	/** The Constant BYTE_SIZE. */
	public final static long BYTE_SIZE = 8l;
	
	/** The Constant SHORT_SIZE. */
	public final static long SHORT_SIZE = 16l;
	
	/** The Constant CHAR_SIZE. */
	public final static long CHAR_SIZE = 16l;
	
	/** The Constant INT_SIZE. */
	public final static long INT_SIZE = 32l;
	
	/** The Constant LONG_SIZE. */
	public final static long LONG_SIZE = 64l;
	
	/** The Constant FLOAT_SIZE. */
	public final static long FLOAT_SIZE = 32l;
	
	/** The Constant DOUBLE_SIZE. */
	public final static long DOUBLE_SIZE = 64l;

	/** The bit size. */
	private final long bitSize;

	/**
	 * Instantiates a new primitive size.
	 *
	 * @param bitSize the bit size
	 */
	PrimitiveSize(long bitSize) {
		this.bitSize = bitSize;
	}

	/**
	 * Primitive size.
	 *
	 * @return the bitSize
	 */
	public long primitiveSize() {
		return bitSize;
	}
}
