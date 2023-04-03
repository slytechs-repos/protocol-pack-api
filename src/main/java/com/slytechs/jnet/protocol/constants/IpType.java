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
 * IEEE Ethernet type field values table.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public enum IpType implements IntSupplier {

	ICMPv4(IpType.IP_TYPE_ICMPv4),
	IPv4_IN_IP(IpType.IP_TYPE_IPv4_IN_IP),
	TCP(IpType.IP_TYPE_TCP),
	UDP(IpType.IP_TYPE_UDP),
	IPv6_IN_IP(IpType.IP_TYPE_IPv6_IN_IP),
	GRE(IpType.IP_TYPE_GRE),
	SCTP(IpType.IP_TYPE_SCTP),
	IPv6_HOP_BY_HOP(IpType.IP_TYPE_IPv6_HOP_BY_HOP),
	IPv6_ROUTING_HEADER(IpType.IP_TYPE_IPv6_ROUTING_HEADER),
	IPv6_FRAGMENT_HEADER(IpType.IP_TYPE_IPv6_FRAGMENT_HEADER),
	IPv6_ENCAPSULATING_SECURITY_PAYLOAD(IpType.IP_TYPE_IPv6_ENCAPSULATING_SECURITY_PAYLOAD),
	IPv6_AUTHENTICATION_HEADER(IpType.IP_TYPE_IPv6_AUTHENTICATION_HEADER),
	ICMPv6(IpType.IP_TYPE_ICMPv6),
	NO_NEXT(IpType.IP_TYPE_NO_NEXT),
	IPv6_DESTINATION_OPTIONS(IpType.IP_TYPE_IPv6_DESTINATION_OPTIONS),
	IPv6_MOBILITY_HEADER(IpType.IP_TYPE_IPv6_MOBILITY_HEADER),
	IPv6_HOST_IDENTITY_PROTOCOL(IpType.IP_TYPE_IPv6_HOST_IDENTITY_PROTOCOL),
	IPv6_SHIM6_PROTOCOL(IpType.IP_TYPE_IPv6_SHIM6_PROTOCOL),

	;

	// @formatter:off
	public static final int IP_TYPE_ICMPv4 = 1;
	public static final int IP_TYPE_IPv4_IN_IP = 4;
	public static final int IP_TYPE_TCP = 6;
	public static final int IP_TYPE_UDP = 17;
	public static final int IP_TYPE_IPv6_IN_IP = 41;
	public static final int IP_TYPE_GRE = 47;
	public static final int IP_TYPE_SCTP = 132;
	public static final int IP_TYPE_IPv6_HOP_BY_HOP = 0;
	public static final int IP_TYPE_IPv6_ROUTING_HEADER = 43;
	public static final int IP_TYPE_IPv6_FRAGMENT_HEADER = 44;
	public static final int IP_TYPE_IPv6_ENCAPSULATING_SECURITY_PAYLOAD = 50;
	public static final int IP_TYPE_IPv6_AUTHENTICATION_HEADER = 51;
	public static final int IP_TYPE_ICMPv6 = 58;
	public static final int IP_TYPE_NO_NEXT = 59;
	public static final int IP_TYPE_IPv6_DESTINATION_OPTIONS = 60;
	public static final int IP_TYPE_IPv6_MOBILITY_HEADER = 135;
	public static final int IP_TYPE_IPv6_HOST_IDENTITY_PROTOCOL = 139;
	public static final int IP_TYPE_IPv6_SHIM6_PROTOCOL = 140;
	// @formatter:on

	public static String resolve(Object type) {
		return Enums.resolve(type, IpType.class);
	}

	public static IpType valueOfIpType(int type) {
		return Enums.valueOf(type, IpType.class);
	}

	private final int type;

	IpType(int type) {
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
