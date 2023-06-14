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
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public interface IsIntFlag extends IsIntBitmask {

	default boolean isSet(int value) {
		assert Integer.bitCount(getAsInt()) == 1 : "too many bits for a flag constants [%s]".formatted(toString());

		return (getAsInt() & value) != 0;
	}

	default boolean isClear(int value) {
		assert Integer.bitCount(getAsInt()) == 1 : "too many bits for a flag constants [%s]".formatted(toString());

		return (getAsInt() & value) != 0;
	}
}
