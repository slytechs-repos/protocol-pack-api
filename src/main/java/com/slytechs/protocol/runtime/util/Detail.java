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
package com.slytechs.protocol.runtime.util;

/**
 * A constant which specifies the detail level to generate by various utility
 * methods.
 * 
 * <p>
 * When applied to <code>PacketFormat(Detail detail)</code>:
 * </p>
 * 
 * <pre>
 *  packet = high
 *    header = high
 *    field = high
 *    
 *  packet = Medium
 *    header = Low
 *    field = None
 *    
 *  packet = Low
 *    header = None
 *    field = None
 *    
 *  header = high
 *    field = high
 *    
 *  header = medium
 *    field = medium
 *    
 *  header = low
 *    field = none
 * </pre>
 */
public enum Detail {
	
	/** The none. */
	NONE,
	
	/** The low. */
	LOW,
	
	/** The medium. */
	MEDIUM,
	
	/** The high. */
	HIGH,
	
	/** The debug. */
	DEBUG;

	/** The Constant DEFAULT. */
	public static final Detail DEFAULT = HIGH;

	/**
	 * Checks if is none.
	 *
	 * @return true, if is none
	 */
	public boolean isNone() {
		return compareTo(NONE) == 0;
	}

	/**
	 * Checks if is low.
	 *
	 * @return true, if is low
	 */
	public boolean isLow() {
		return compareTo(LOW) == 0;
	}

	/**
	 * Checks if is medium.
	 *
	 * @return true, if is medium
	 */
	public boolean isMedium() {
		return compareTo(MEDIUM) == 0;
	}

	/**
	 * Checks if is high.
	 *
	 * @return true, if is high
	 */
	public boolean isHigh() {
		return compareTo(HIGH) == 0;
	}

	/**
	 * Checks if is debug.
	 *
	 * @return true, if is debug
	 */
	public boolean isDebug() {
		return compareTo(DEBUG) == 0;
	}

}