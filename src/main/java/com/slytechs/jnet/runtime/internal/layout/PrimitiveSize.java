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
