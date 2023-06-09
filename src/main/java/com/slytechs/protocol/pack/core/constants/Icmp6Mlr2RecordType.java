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
package com.slytechs.protocol.pack.core.constants;

import java.util.function.IntSupplier;

import com.slytechs.protocol.runtime.util.Enums;

/**
 * MLDv2 record type (defined in RFC 3810).
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum Icmp6Mlr2RecordType implements IntSupplier {
	MODE_IS_INCLUDE(1),
	MODE_IS_EXCLUDE(2),
	CHANGE_TO_INCLUDE(3),
	CHANGE_TO_EXCLUDE(4),
	ALLOW_NEW_SOURCES(5),
	BLOCK_OLD_SOURCES(6);

	private final int type;

	public static String resolve(Object type) {
		return Enums.resolve(type, Icmp6Mlr2RecordType.class);
	}

	/**
	 * @param i
	 */
	Icmp6Mlr2RecordType(int type) {
		this.type = type;
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}
}
