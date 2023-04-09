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

import static com.slytechs.protocol.pack.core.constants.CorePackIds.*;

import com.slytechs.protocol.Header;
import com.slytechs.protocol.Payload;
import com.slytechs.protocol.pack.core.Ethernet;
import com.slytechs.protocol.pack.core.Icmp4;
import com.slytechs.protocol.pack.core.Icmp6;
import com.slytechs.protocol.pack.core.Ip4;
import com.slytechs.protocol.pack.core.Ip6;
import com.slytechs.protocol.pack.core.Tcp;
import com.slytechs.protocol.pack.core.Udp;

/**
 * A factory for creating Core objects.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public final class CoreFactory {

	/**
	 * Instantiates a new core factory.
	 */
	public CoreFactory() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Lookup header.
	 *
	 * @param id the id
	 * @return the header
	 */
	public static Header lookupHeader(int id) {
		return switch (id) {
		case CORE_ID_PAYLOAD -> new Payload();
		case CORE_ID_ETHER -> new Ethernet();
		case CORE_ID_IPv4 -> new Ip4();
		case CORE_ID_IPv6 -> new Ip6();
		case CORE_ID_UDP -> new Udp();
		case CORE_ID_TCP -> new Tcp();
		case CORE_ID_ICMPv4 -> new Icmp4();
		case CORE_ID_ICMPv6 -> new Icmp6();

//		case CORE_ID_LLC -> new Payload();
//		case CORE_ID_SNAP -> new Payload();
//		case CORE_ID_VLAN -> new Payload();
//		case CORE_ID_MPLS -> new Payload();
//		case CORE_ID_IPX -> new Payload();
//		case CORE_ID_GRE -> new Payload();
//		case CORE_ID_PPP -> new Payload();
//		case CORE_ID_FDDI -> new Payload();
//		case CORE_ID_ATM -> new Payload();
//		case CORE_ID_ARM -> new Payload();
//		case CORE_ID_RARP -> new Payload();
//		case CORE_ID_STP -> new Payload();
//		case CORE_ID_DHCPv4 -> new Payload();
//		case CORE_ID_DHCPv6 -> new Payload();
//		case CORE_ID_IGMP -> new Payload();

		default -> null;
		};
	}
}
