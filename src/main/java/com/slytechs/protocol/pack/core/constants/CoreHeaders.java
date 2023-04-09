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

import static com.slytechs.protocol.pack.core.constants.PackInfo.*;

import java.util.function.Supplier;

import com.slytechs.protocol.Frame;
import com.slytechs.protocol.Header;
import com.slytechs.protocol.HeaderExtensionInfo;
import com.slytechs.protocol.HeaderId;
import com.slytechs.protocol.HeaderInfo;
import com.slytechs.protocol.HeaderSupplier;
import com.slytechs.protocol.Other;
import com.slytechs.protocol.Payload;
import com.slytechs.protocol.pack.core.Ethernet;
import com.slytechs.protocol.pack.core.Icmp4;
import com.slytechs.protocol.pack.core.Icmp6;
import com.slytechs.protocol.pack.core.Ip4;
import com.slytechs.protocol.pack.core.Ip6;
import com.slytechs.protocol.pack.core.Tcp;
import com.slytechs.protocol.pack.core.Udp;

/**
 * Core protocol pack. Table of all protocols included in the core protocol
 * pack.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public enum CoreHeaders implements HeaderInfo {

	/** The payload. */
	PAYLOAD(Payload::new),

	/** The frame. */
	FRAME(Frame::new),

	/** The ether. */
	ETHER(Ethernet::new),

	/** The I pv 4. */
	IPv4(Ip4::new, Ip4OptionInfo::values),

	/** The I pv 6. */
	IPv6(Ip6::new, Ip6OptionInfo::values),

	/** The udp. */
	UDP(Udp::new),

	/** The tcp. */
	TCP(Tcp::new, TcpOptionInfo::values),

	/** The sctp. */
	SCTP,

	/** The ICM pv 4. */
	ICMPv4(Icmp4::new),

	/** The ICM pv 6. */
	ICMPv6(Icmp6::new),

	/** The llc. */
	LLC,

	/** The snap. */
	SNAP,

	/** The vlan. */
	VLAN,

	/** The mpls. */
	MPLS,

	/** The ipx. */
	IPX,

	/** The gre. */
	GRE,

	/** The ppp. */
	PPP,

	/** The fddi. */
	FDDI,

	/** The atm. */
	ATM,

	/** The arp. */
	ARP,

	/** The rarp. */
	RARP,

	/** The stp. */
	STP,

	/** The DHC pv 4. */
	DHCPv4,

	/** The DHC pv 6. */
	DHCPv6,

	/** The igmp. */
	IGMP

	;

	/** The Constant CORE_ID_PAYLOAD. */
	// @formatter:off
	public static final int CORE_ID_OTHER    = -1;
	
	public static final int CORE_ID_PAYLOAD  = 0  | PACK_ID_CORE;
	
	/** The Constant CORE_ID_FRAME. */
	public static final int CORE_ID_FRAME    = 1  | PACK_ID_CORE;
	
	/** The Constant CORE_ID_ETHER. */
	public static final int CORE_ID_ETHER    = 2  | PACK_ID_CORE;
	
	/** The Constant CORE_ID_IPv4. */
	public static final int CORE_ID_IPv4     = 3  | PACK_ID_CORE;
	
	/** The Constant CORE_ID_IPv6. */
	public static final int CORE_ID_IPv6     = 4  | PACK_ID_CORE;
	
	/** The Constant CORE_ID_UDP. */
	public static final int CORE_ID_UDP      = 5  | PACK_ID_CORE;
	
	/** The Constant CORE_ID_TCP. */
	public static final int CORE_ID_TCP      = 6  | PACK_ID_CORE;
	
	/** The Constant CORE_ID_SCTP. */
	public static final int CORE_ID_SCTP     = 7  | PACK_ID_CORE;
	
	/** The Constant CORE_ID_ICMPv4. */
	public static final int CORE_ID_ICMPv4   = 8  | PACK_ID_CORE;
	
	/** The Constant CORE_ID_ICMPv6. */
	public static final int CORE_ID_ICMPv6   = 9  | PACK_ID_CORE;
	
	/** The Constant CORE_ID_LLC. */
	public static final int CORE_ID_LLC      = 10 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_SNAP. */
	public static final int CORE_ID_SNAP     = 11 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_VLAN. */
	public static final int CORE_ID_VLAN     = 12 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_MPLS. */
	public static final int CORE_ID_MPLS     = 13 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_IPX. */
	public static final int CORE_ID_IPX      = 14 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_GRE. */
	public static final int CORE_ID_GRE      = 15 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_PPP. */
	public static final int CORE_ID_PPP      = 16 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_FDDI. */
	public static final int CORE_ID_FDDI     = 17 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_ATM. */
	public static final int CORE_ID_ATM      = 18 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_ARM. */
	public static final int CORE_ID_ARM      = 19 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_RARP. */
	public static final int CORE_ID_RARP     = 20 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_STP. */
	public static final int CORE_ID_STP      = 21 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_DHCPv4. */
	public static final int CORE_ID_DHCPv4   = 22 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_DHCPv6. */
	public static final int CORE_ID_DHCPv6   = 23 | PACK_ID_CORE;
	
	/** The Constant CORE_ID_IGMP. */
	public static final int CORE_ID_IGMP     = 24 | PACK_ID_CORE;
	// @formatter:on

	/**
	 * Value of.
	 *
	 * @param id the id
	 * @return the core header info
	 */
	public static CoreHeaders valueOf(int id) {
		int pack = HeaderId.decodePackId(id);
		if (pack != PackInfo.PACK_ID_CORE)
			return null;

		int ordinal = HeaderId.decodeIdOrdinal(id);
		return values()[ordinal];
	}

	/** The id. */
	private final int id;

	/** The supplier. */
	private final HeaderSupplier supplier;

	/** The extensions supplier. */
	private final Supplier<HeaderExtensionInfo[]> extensionsSupplier;

	/**
	 * Instantiates a new core header info.
	 */
	CoreHeaders() {
		this.id = HeaderId.encodeId(PackInfo.CORE, ordinal());
		this.supplier = Other::new;
		this.extensionsSupplier = () -> HeaderExtensionInfo.EMPTY_ARRAY;
	}

	/**
	 * Instantiates a new core header info.
	 *
	 * @param supplier the supplier
	 */
	CoreHeaders(HeaderSupplier supplier) {
		this.id = HeaderId.encodeId(PackInfo.CORE, ordinal());
		this.supplier = supplier;
		this.extensionsSupplier = () -> HeaderExtensionInfo.EMPTY_ARRAY;
	}

	/**
	 * Instantiates a new core header info.
	 *
	 * @param supplier           the supplier
	 * @param extensionsSupplier the extensions supplier
	 */
	CoreHeaders(HeaderSupplier supplier, Supplier<HeaderExtensionInfo[]> extensionsSupplier) {
		this.id = HeaderId.encodeId(PackInfo.CORE, ordinal());
		this.supplier = supplier;
		this.extensionsSupplier = extensionsSupplier;
	}

	/**
	 * Gets the extension infos.
	 *
	 * @return the extension infos
	 * @see com.slytechs.protocol.HeaderInfo#getExtensionInfos()
	 */
	@Override
	public HeaderExtensionInfo[] getExtensionInfos() {
		return extensionsSupplier.get();
	}

	/**
	 * Gets the header id.
	 *
	 * @return the header id
	 * @see com.slytechs.protocol.HeaderInfo#getHeaderId()
	 */
	@Override
	public int getHeaderId() {
		return id;
	}

	/**
	 * To string id.
	 *
	 * @param id the id
	 * @return the core header info
	 */
	public static CoreHeaders toStringId(int id) {
		return values()[HeaderId.decodeIdOrdinal(id)];
	}

	/**
	 * New header instance.
	 *
	 * @return the header
	 * @see com.slytechs.protocol.HeaderSupplier#newHeaderInstance()
	 */
	@Override
	public Header newHeaderInstance() {
		return (supplier != null) ? supplier.newHeaderInstance() : null;
	}

}
