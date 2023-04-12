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
package com.slytechs.protocol.runtime.internal.foreign;

import java.lang.foreign.Addressable;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;

/**
 * The Class ForeignUtils.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 */
final class ForeignUtils {

	/**
	 * To java string.
	 *
	 * @param value the value
	 * @return the string
	 */
	public static String toJavaString(Object value) {
		return toJavaString(((Addressable) value).address());
	}

	/**
	 * To utf 8 string.
	 *
	 * @param str   the str
	 * @param scope the scope
	 * @return the memory segment
	 */
	public static MemorySegment toUtf8String(String str, MemorySession scope) {
		return scope.allocateUtf8String(str);
	}

	/**
	 * To java string.
	 *
	 * @param addr the addr
	 * @return the string
	 */
	public static String toJavaString(MemoryAddress addr) {
		if ((addr == null) || (addr == MemoryAddress.NULL))
			return null;

		return addr.getUtf8String(0);
	}

	/**
	 * Instantiates a new foreign utils.
	 */
	private ForeignUtils() {
	}

}
