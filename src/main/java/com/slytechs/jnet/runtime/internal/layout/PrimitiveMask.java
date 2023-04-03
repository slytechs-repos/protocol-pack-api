/*
 * MIT License
 * 
 * Copyright (c) 2020 Sly Technologies Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.slytechs.jnet.runtime.internal.layout;

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
