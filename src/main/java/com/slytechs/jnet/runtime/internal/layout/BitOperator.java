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
 * The Interface BitOperator.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
interface BitOperator {

	/**
	 * Gets the byte.
	 *
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the byte
	 */
	// @formatter:off
    byte   getByte  (byte  carrier, long strideOffset);
	
	/**
	 * Gets the short.
	 *
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the short
	 */
	short  getShort (short carrier, long strideOffset);
	
	/**
	 * Gets the int.
	 *
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the int
	 */
	int    getInt   (int   carrier, long strideOffset);
	
	/**
	 * Gets the long.
	 *
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the long
	 */
	long   getLong  (long  carrier, long strideOffset);

    /**
	 * Sets the byte.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the byte
	 */
    byte   setByte  (byte   value, byte  carrier, long strideOffset);
	
	/**
	 * Sets the short.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the short
	 */
	short  setShort (short  value, short carrier, long strideOffset);
	
	/**
	 * Sets the int.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the int
	 */
	int    setInt   (int    value, int   carrier, long strideOffset);
	
	/**
	 * Sets the long.
	 *
	 * @param value        the value
	 * @param carrier      the carrier
	 * @param strideOffset the stride offset
	 * @return the long
	 */
	long   setLong  (long   value, long  carrier, long strideOffset);
	// @formatter:on

	/**
	 * Bitshift.
	 *
	 * @param strideOffset the stride offset
	 * @return the int
	 */
	int bitshift(long strideOffset);

	/**
	 * Bitmask.
	 *
	 * @return the long
	 */
	long bitmask();

}