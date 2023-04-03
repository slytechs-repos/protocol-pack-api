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

import java.util.function.IntSupplier;

import com.slytechs.jnet.runtime.util.Enums;

/**
 * EtherType is a two-octet field in an Ethernet frame. It is used to indicate
 * which protocol is encapsulated in the payload of the frame and is used at the
 * receiving end by the data link layer to determine how the payload is
 * processed. The same field is also used to indicate the size of some Ethernet
 * frames.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public enum EtherType implements IntSupplier {

	IPv4(EtherType.ETHER_TYPE_IPv4),
	IPv6(EtherType.ETHER_TYPE_IPv6),
	VLAN(EtherType.ETHER_TYPE_VLAN),
	IPX(EtherType.ETHER_TYPE_IPX),
	MPLS_UNI(EtherType.ETHER_TYPE_MPLS),
	MPLS_MULTI(EtherType.ETHER_TYPE_MPLS_UPSTREAM),

	;

	// @formatter:off
	public static final int ETHER_TYPE_IPv4            = 0x0800;
	public static final int ETHER_TYPE_IPv6            = 0x86DD;
	public static final int ETHER_TYPE_VLAN            = 0x8100;
	public static final int ETHER_TYPE_IPX             = 0x8137;
	public static final int ETHER_TYPE_MPLS            = 0x8847;
	public static final int ETHER_TYPE_MPLS_UPSTREAM   = 0x8848;
	public static final int ETHER_TYPE_PPPoE_DISCOVERY = 0x8848;
	public static final int ETHER_TYPE_PPPoE_SESSION   = 0x8848;
	// @formatter:on

	public static String resolve(Object obj) {
		return Enums.resolve(obj, EtherType.class);
	}

	public static EtherType valueOfEtherType(int type) {
		return Enums.valueOf(type, EtherType.class);
	}

	private final int type;

	EtherType(int type) {
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
