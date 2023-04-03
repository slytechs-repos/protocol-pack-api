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

import static com.slytechs.jnet.protocol.constants.PackInfo.*;

import java.util.function.Supplier;

import com.slytechs.jnet.protocol.HeaderInfo;
import com.slytechs.jnet.protocol.HeaderSupplier;
import com.slytechs.jnet.protocol.HeaderId;
import com.slytechs.jnet.protocol.core.Ethernet;
import com.slytechs.jnet.protocol.core.Icmp4;
import com.slytechs.jnet.protocol.core.Icmp6;
import com.slytechs.jnet.protocol.core.Ip4;
import com.slytechs.jnet.protocol.core.Ip6;
import com.slytechs.jnet.protocol.core.Tcp;
import com.slytechs.jnet.protocol.core.Udp;
import com.slytechs.jnet.protocol.packet.Frame;
import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.HeaderExtensionInfo;
import com.slytechs.jnet.protocol.packet.Other;
import com.slytechs.jnet.protocol.packet.Payload;

/**
 * Core protocol pack. Table of all protocols included in the core protocol
 * pack.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public enum CoreHeaderInfo implements HeaderInfo {

	PAYLOAD(Payload::new),
	FRAME(Frame::new),
	ETHER(Ethernet::new),
	IPv4(Ip4::new, Ip4OptionInfo::values),
	IPv6(Ip6::new, Ip6OptionInfo::values),
	UDP(Udp::new),
	TCP(Tcp::new, TcpOptionInfo::values),
	SCTP,
	ICMPv4(Icmp4::new),
	ICMPv6(Icmp6::new),
	LLC,
	SNAP,
	VLAN,
	MPLS,
	IPX,
	GRE,
	PPP,
	FDDI,
	ATM,
	ARP,
	RARP,
	STP,
	DHCPv4,
	DHCPv6,
	IGMP

	;

	// @formatter:off
	public static final int CORE_ID_PAYLOAD  = 0  | PACK_ID_CORE;
	public static final int CORE_ID_FRAME    = 1  | PACK_ID_CORE;
	public static final int CORE_ID_ETHER    = 2  | PACK_ID_CORE;
	public static final int CORE_ID_IPv4     = 3  | PACK_ID_CORE;
	public static final int CORE_ID_IPv6     = 4  | PACK_ID_CORE;
	public static final int CORE_ID_UDP      = 5  | PACK_ID_CORE;
	public static final int CORE_ID_TCP      = 6  | PACK_ID_CORE;
	public static final int CORE_ID_SCTP     = 7  | PACK_ID_CORE;
	public static final int CORE_ID_ICMPv4   = 8  | PACK_ID_CORE;
	public static final int CORE_ID_ICMPv6   = 9  | PACK_ID_CORE;
	public static final int CORE_ID_LLC      = 10 | PACK_ID_CORE;
	public static final int CORE_ID_SNAP     = 11 | PACK_ID_CORE;
	public static final int CORE_ID_VLAN     = 12 | PACK_ID_CORE;
	public static final int CORE_ID_MPLS     = 13 | PACK_ID_CORE;
	public static final int CORE_ID_IPX      = 14 | PACK_ID_CORE;
	public static final int CORE_ID_GRE      = 15 | PACK_ID_CORE;
	public static final int CORE_ID_PPP      = 16 | PACK_ID_CORE;
	public static final int CORE_ID_FDDI     = 17 | PACK_ID_CORE;
	public static final int CORE_ID_ATM      = 18 | PACK_ID_CORE;
	public static final int CORE_ID_ARM      = 19 | PACK_ID_CORE;
	public static final int CORE_ID_RARP     = 20 | PACK_ID_CORE;
	public static final int CORE_ID_STP      = 21 | PACK_ID_CORE;
	public static final int CORE_ID_DHCPv4   = 22 | PACK_ID_CORE;
	public static final int CORE_ID_DHCPv6   = 23 | PACK_ID_CORE;
	public static final int CORE_ID_IGMP     = 24 | PACK_ID_CORE;
	// @formatter:on

	public static CoreHeaderInfo valueOf(int id) {
		int pack = HeaderId.decodePackId(id);
		if (pack != PackInfo.PACK_ID_CORE)
			return null;

		int ordinal = HeaderId.decodeIdOrdinal(id);
		return values()[ordinal];
	}

	private final int id;
	private final HeaderSupplier supplier;
	private final Supplier<HeaderExtensionInfo[]> extensionsSupplier;

	CoreHeaderInfo() {
		this.id = HeaderId.encodeId(PackInfo.CORE, ordinal());
		this.supplier = Other::new;
		this.extensionsSupplier = () -> HeaderExtensionInfo.EMPTY_ARRAY;
	}

	CoreHeaderInfo(HeaderSupplier supplier) {
		this.id = HeaderId.encodeId(PackInfo.CORE, ordinal());
		this.supplier = supplier;
		this.extensionsSupplier = () -> HeaderExtensionInfo.EMPTY_ARRAY;
	}

	CoreHeaderInfo(HeaderSupplier supplier, Supplier<HeaderExtensionInfo[]> extensionsSupplier) {
		this.id = HeaderId.encodeId(PackInfo.CORE, ordinal());
		this.supplier = supplier;
		this.extensionsSupplier = extensionsSupplier;
	}

	/**
	 * @see com.slytechs.jnet.protocol.HeaderInfo#getExtensionInfos()
	 */
	@Override
	public HeaderExtensionInfo[] getExtensionInfos() {
		return extensionsSupplier.get();
	}

	/**
	 * @see com.slytechs.jnet.protocol.HeaderInfo#getHeaderId()
	 */
	@Override
	public int getHeaderId() {
		return id;
	}

	/**
	 * @param decodeId
	 * @return
	 */
	public static CoreHeaderInfo toStringId(int id) {
		return values()[HeaderId.decodeIdOrdinal(id)];
	}

	/**
	 * @see com.slytechs.jnet.protocol.HeaderSupplier#newHeaderInstance()
	 */
	@Override
	public Header newHeaderInstance() {
		return (supplier != null) ? supplier.newHeaderInstance() : null;
	}

}
