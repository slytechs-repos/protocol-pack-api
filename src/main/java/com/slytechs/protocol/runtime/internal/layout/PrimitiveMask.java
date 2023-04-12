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
 * The Enum PrimitiveMask.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum PrimitiveMask {

	/** The byte. */
	BYTE(PrimitiveMask.BYTE_MASK),
	
	/** The short. */
	SHORT(PrimitiveMask.SHORT_MASK),
	
	/** The char. */
	CHAR(PrimitiveMask.CHAR_MASK),
	
	/** The int. */
	INT(PrimitiveMask.INT_MASK),
	
	/** The long. */
	LONG(PrimitiveMask.LONG_MASK),
	
	/** The float. */
	FLOAT(PrimitiveMask.FLOAT_MASK),
	
	/** The double. */
	DOUBLE(PrimitiveMask.DOUBLE_MASK),

	;

	/** The Constant MASK08. */
	public final static long MASK08 = 0x0000_0000_0000_00FFl;
	
	/** The Constant MASK16. */
	public final static long MASK16 = 0x0000_0000_0000_FFFFl;
	
	/** The Constant MASK32. */
	public final static long MASK32 = 0x0000_0000_FFFF_FFFFl;
	
	/** The Constant MASK64. */
	public final static long MASK64 = 0xFFFF_FFFF_FFFF_FFFFl;

	/** The Constant BYTE_MASK. */
	public final static long BYTE_MASK = MASK08;
	
	/** The Constant SHORT_MASK. */
	public final static long SHORT_MASK = MASK16;
	
	/** The Constant CHAR_MASK. */
	public final static long CHAR_MASK = MASK16;
	
	/** The Constant INT_MASK. */
	public final static long INT_MASK = MASK32;
	
	/** The Constant LONG_MASK. */
	public final static long LONG_MASK = MASK64;
	
	/** The Constant FLOAT_MASK. */
	public final static long FLOAT_MASK = MASK32;
	
	/** The Constant DOUBLE_MASK. */
	public final static long DOUBLE_MASK = MASK64;

	/** The mask. */
	private final long mask;

	/**
	 * Instantiates a new primitive mask.
	 *
	 * @param mask the mask
	 */
	PrimitiveMask(long mask) {
		this.mask = mask;
	}

	/**
	 * Primitive mask.
	 *
	 * @return the bitSize
	 */
	public long primitiveMask() {
		return mask;
	}
}
