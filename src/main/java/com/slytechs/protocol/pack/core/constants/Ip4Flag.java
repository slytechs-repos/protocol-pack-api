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

import java.util.EnumSet;
import java.util.Set;

/**
 * The Enum Ip4Flag.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public enum Ip4Flag {

	/** The reserved. */
	RESERVED,

	/** The df. */
	DF,

	/** The mf. */
	MF,

	;

	/** The Constant IPv4_FLAG_RESERVED. */
	public static final int IPv4_FLAG_RESERVED = 0x4;

	/** The Constant IPv4_FLAG_DF. */
	public static final int IPv4_FLAG03_DF = 0x2;

	/** The Constant IPv4_FLAG_MF. */
	public static final int IPv4_FLAG03_MF = 0x1;

	/** The Constant IPv4_FLAG16_DF. */
	public static final int IPv4_FLAG16_DF = 0x4000;

	/** The Constant IPv4_FLAG16_MF. */
	public static final int IPv4_FLAG16_MF = 0x2000;

	/**
	 * Value of.
	 *
	 * @param flags03 the flags
	 * @return the sets the
	 */
	public static Set<Ip4Flag> valueOfInt03(int flags03) {
		Set<Ip4Flag> set = EnumSet.noneOf(Ip4Flag.class);

		if ((flags03 & IPv4_FLAG03_DF) > 0)
			set.add(DF);

		if ((flags03 & IPv4_FLAG03_MF) > 0)
			set.add(MF);

		return set;
	}

	/**
	 * Value of int 16.
	 *
	 * @param flags16 the flags 16
	 * @return the sets the
	 */
	public static Set<Ip4Flag> valueOfInt16(int flags16) {
		Set<Ip4Flag> set = EnumSet.noneOf(Ip4Flag.class);

		if ((flags16 & IPv4_FLAG16_DF) > 0)
			set.add(DF);

		if ((flags16 & IPv4_FLAG16_MF) > 0)
			set.add(MF);

		return set;
	}
}
