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
package com.slytechs.jnet.jnetruntime.internal.layout;

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