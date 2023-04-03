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
package com.slytechs.jnet.runtime.util;

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
	NONE,
	LOW,
	MEDIUM,
	HIGH,
	DEBUG;

	public static final Detail DEFAULT = HIGH;

	public boolean isNone() {
		return compareTo(NONE) == 0;
	}

	public boolean isLow() {
		return compareTo(LOW) == 0;
	}

	public boolean isMedium() {
		return compareTo(MEDIUM) == 0;
	}

	public boolean isHigh() {
		return compareTo(HIGH) == 0;
	}

	public boolean isDebug() {
		return compareTo(DEBUG) == 0;
	}

}