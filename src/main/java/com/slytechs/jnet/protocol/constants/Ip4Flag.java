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
package com.slytechs.jnet.protocol.constants;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public enum Ip4Flag {
	
	RESERVED,
	DF,
	MF,

	;

	public static final int IPv4_FLAG_RESERVED = 0x0;
	public static final int IPv4_FLAG_DF = 0x2;
	public static final int IPv4_FLAG_MF = 0x8;

	public static Set<Ip4Flag> valueOf(int flags) {
		Set<Ip4Flag> set = EnumSet.noneOf(Ip4Flag.class);
		
		if ((flags & IPv4_FLAG_DF) > 0)
			set.add(DF);

		if ((flags & IPv4_FLAG_MF) > 0)
			set.add(MF);

		return set;
	}
}
