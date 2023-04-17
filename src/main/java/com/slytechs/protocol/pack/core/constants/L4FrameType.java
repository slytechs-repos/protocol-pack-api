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

/**
 * Layer 4 frame type.
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum L4FrameType implements IntSupplier {

	TCP,
	UDP,
	ICMP,
	OTHER,
	GRE,
	SCTP,
	IPv4,
	IPv6,

	;

	/** @formatter:off L4 frame descriptor values */
	public final static int L4_FRAME_TYPE_TCP = 0;
	public final static int L4_FRAME_TYPE_UDP = 1;
	public final static int L4_FRAME_TYPE_ICMP = 2;
	public final static int L4_FRAME_TYPE_OTHER = 3;
	public final static int L4_FRAME_TYPE_GRE = 4;
	public final static int L4_FRAME_TYPE_SCTP = 5;
	public final static int L4_FRAME_TYPE_IPv4 = 6;
	public final static int L4_FRAME_TYPE_IPv6 = 7;
	/** @formatter:off */

	/**
	 * Get the value of L4 frame type constant.
	 *
	 * @param type the L4 type
	 * @return the layer 4 constant
	 */
	public static L4FrameType valueOfL4FrameType(int type) {
		return values()[type];
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return ordinal();
	}

}
