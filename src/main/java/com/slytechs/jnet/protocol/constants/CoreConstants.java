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

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class CoreConstants {

	/* @formatter:off - Descriptor type2 constants */
	public static final int DESC_TYPE2_BYTE_SIZE_MIN    = 24;
	public static final int DESC_TYPE2_RECORD_BYTE_SIZE = 4;
	public static final int DESC_TYPE2_RECORD_MAX_COUNT = 32;
	public static final int DESC_TYPE2_BYTE_SIZE_MAX    = 0
			+ DESC_TYPE2_BYTE_SIZE_MIN
			+ (DESC_TYPE2_RECORD_MAX_COUNT * DESC_TYPE2_RECORD_BYTE_SIZE);
	/* @formatter:on - Descriptor type2 constants */

	/* @formatter:off - Descriptor Ethernet II constants */
	public static final int ETHER_HEADER_LEN           = 14;
	public static final int ETHER_FIELD_TYPE           = 12;
	public static final int ETHER_FIELD_DST            = 0;
	public static final int ETHER_FIELD_SRC            = 6;
	public static final int ETHER_FIELD_DST_LEN        = 6;
	public static final int ETHER_FIELD_SRC_LEN        = 6;
	public static final int ETHER_FIELD_LEN_TYPE       = 2;
	public static final int ETHER_MIN_VALUE_FOR_TYPE   = 0x600;
	public static final int ETHER_TYPE_IPv4            = 0x0800;
	public static final int ETHER_TYPE_IPv6            = 0x86DD;
	public static final int ETHER_TYPE_VLAN            = 0x8100;
	public static final int ETHER_TYPE_IPX             = 0x8137;
	public static final int ETHER_TYPE_MPLS            = 0x8847;
	public static final int ETHER_TYPE_MPLS_UPSTREAM   = 0x8848;
	public static final int ETHER_TYPE_PPPoE_DISCOVERY = 0x8848;
	public static final int ETHER_TYPE_PPPoE_SESSION   = 0x8848;
	/* @formatter:on - Descriptor Ethernet II constants */

	/* @formatter:off - Descriptor VLAN constants */
	public static final int VLAN_HEADER_LEN    = 0;
	public static final int VLAN_FIELD_LEN_TCI = 0;
	/* @formatter:on - Descriptor VLAN constants */

	/* Descriptor LLC constants */
	public static final int LLC_HEADER_LEN = 5;
	public static final int LLC_FIELD_DSAP = 5;
	public static final int LLC_FIELD_SSAP = 5;
	public static final int LLC_FIELD_CONTROL = 5;
	public static final int LLC_TYPE_FRAME = 5;
	public static final int LLC_TYPE_SNAP = 5;
	public static final int LLC_TYPE_NETWARE = 5;

	/* Descriptor SNAP constants */
	public static final int SNAP_HEADER_LEN = 4;
	public static final int SNAP_FIELD_TYPE = 4;
	public static final int SNAP_FIELD_OUI = 4;
	public static final int SNAP_TYPE_ETHER = 4;

	/* Descriptor MPLS constants */
	public static final int MPLS_HEADER_LEN = 0;
	public static final int MPLS_BITMASK_BOTTOM = 0;

	/* Descriptor IPX constants */
	public static final int IPX_HEADER_LEN = 0;
	public static final int IPX_FIELD_VALUE_CHECKSUM = 0;

	/* Descriptor ICMP constants */
	public static final int ICMPv4_HEADER_LEN = 0;
	public static final int ICMPv6_TYPE_RESERVED = 0;
	public static final int ICMP_TYPE_UNREACHABLE = 1;
	public static final int ICMPv6_TYPE_PACKET_TOO_BIG = 2;
	public static final int ICMPv6_TYPE_TIME_EXEEDED = 3;
	public static final int ICMPv6_TYPE_PARAMETER_PROBLEM = 4;
	public static final int ICMPv6_TYPE_ECHO_REQUEST = 128;
	public static final int ICMPv6_TYPE_ECHO_REPLY = 129;
	public static final int ICMPv6_TYPE_MULTICAST_LISTENER_QUERY = 130;
	public static final int ICMPv6_TYPE_MULTICAST_LISTENER_REPORT = 131;
	public static final int ICMPv6_TYPE_MULTICAST_LISTENER_DONE = 132;
	public static final int ICMPv6_TYPE_ROUTER_SOLICITATION = 133;
	public static final int ICMPv6_TYPE_ROUTER_ADVERTISEMENT = 134;
	public static final int ICMPv6_TYPE_NEIGHBOR_SOLICITATION = 135;
	public static final int ICMPv6_TYPE_NEIGHBOR_ADVERTISEMENT = 136;
	public static final int ICMPv6_TYPE_REDIRECT = 137;
	public static final int ICMPv6_TYPE_ROUTER_RENUMBER = 138;
	public static final int ICMPv6_TYPE_NODE_INFO_QUERY = 139;
	public static final int ICMPv6_TYPE_NODE_INFO_RESPONSE = 140;
	public static final int ICMPv6_TYPE_INVERSE_NEIGHBOR_SOLICITATION = 141;
	public static final int ICMPv6_TYPE_INVERSE_NEIGHBOR_ADVERTISEMENT = 142;
	public static final int ICMPv6_TYPE_HOME_AGENT_REQUEST = 144;
	public static final int ICMPv6_TYPE_HOME_AGENT_REPLY = 145;
	public static final int ICMPv6_TYPE_MOBILE_PREFIX_SOLICITATION = 146;
	public static final int ICMPv6_TYPE_MOBILE_PREFIX_ADVERTISEMENT = 147;
	public static final int ICMPv6_TYPE_FMIPv6_MESSAGE = 154;
	public static final int ICMPv6_TYPE_RPL_CONTROL_MESSAGE = 155;
	public static final int ICMPv6_TYPE_ILNPv6_LOCATOR_UPDATE_MESSAGE = 156;
	public static final int ICMPv6_TYPE_DUPLICATE_ADDRESS_REQUEST_CODE_SUFFIX = 157;
	public static final int ICMPv6_TYPE_DUPLICATE_ADDRESS_CONFIRMATION_CODE_SUFFIX = 158;
	public static final int ICMPv6_TYPE_MPL_CONTROL_MESSAGE = 159;
	public static final int ICMPv6_TYPE_EXTENDED_ECHO_REQUEST = 160;
	public static final int ICMPv6_TYPE_EXTENDED_ECHO_REPLY = 161;

	/* Descriptor IP constants */
	public static final int IPv4_HEADER_LEN = 20;
	public static final int IPv6_HEADER_LEN = 40;
	public static final int IPv4_FIELD_VER = 0;
	public static final int IPv4_FIELD_PROTOCOL = 9;
	public static final int IPv6_FIELD_NEXT_HOP = 0;
	public static final int IPv4_FIELD_DST_LEN = 0;
	public static final int IPv4_FIELD_DST = 0;
	public static final int IPv4_FIELD_SRC_LEN = 0;
	public static final int IPv4_FIELD_SRC = 0;
	public static final int IPv4_ADDRESS_SIZE = 4;
	public static final int IPv6_ADDRESS_SIZE = 16;
	public static final int IPv6_ADDRESS_STRING_SIZE = 39;
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
	// @formatter:off
	public static final int IPv4_OPTION_TYPE_EOOL      = 0x00;
	public static final int IPv4_OPTION_TYPE_NOP       = 0x01;
	public static final int IPv4_OPTION_TYPE_SEC_DEF   = 0x02;
	public static final int IPv4_OPTION_TYPE_RR        = 0x07;
	public static final int IPv4_OPTION_TYPE_EXP1_ZSU  = 0x0A;
	public static final int IPv4_OPTION_TYPE_MTUP      = 0x0B;
	public static final int IPv4_OPTION_TYPE_MTUR      = 0x0C;
	public static final int IPv4_OPTION_TYPE_ENCODE    = 0x0F;
	public static final int IPv4_OPTION_TYPE_QS        = 0x19;
	public static final int IPv4_OPTION_TYPE_EXP1      = 0x1E;
	public static final int IPv4_OPTION_TYPE_TS        = 0x44;
	public static final int IPv4_OPTION_TYPE_RT        = 0x52;
	public static final int IPv4_OPTION_TYPE_EXP2      = 0x5E;
	public static final int IPv4_OPTION_TYPE_SEC       = 0x82;
	public static final int IPv4_OPTION_TYPE_LSR       = 0x83;
	public static final int IPv4_OPTION_TYPE_E_SEC     = 0x85;
	public static final int IPv4_OPTION_TYPE_CIPSO     = 0x86;
	public static final int IPv4_OPTION_TYPE_SID       = 0x88;
	public static final int IPv4_OPTION_TYPE_SSR       = 0x89;
	public static final int IPv4_OPTION_TYPE_EXP3_VISA = 0x8E;
	public static final int IPv4_OPTION_TYPE_IMITD     = 0x90;
	public static final int IPv4_OPTION_TYPE_E_IP      = 0x91;
	public static final int IPv4_OPTION_TYPE_E_ADDR    = 0x93;
	public static final int IPv4_OPTION_TYPE_RTRALT    = 0x94;
	public static final int IPv4_OPTION_TYPE_SBD       = 0x95;
	public static final int IPv4_OPTION_TYPE_DPS       = 0x97;
	public static final int IPv4_OPTION_TYPE_UMP       = 0x98;
	public static final int IPv4_OPTION_TYPE_EXP4      = 0x9E;
	public static final int IPv4_OPTION_TYPE_EXP5      = 0xCD;
	public static final int IPv4_OPTION_TYPE_EXP6      = 0xDE;
	// @formatter:on

	// @formatter:off
	public static final int IPv6_OPTION_TYPE_HOP_BY_HOP     = 0;
	public static final int IPv6_OPTION_TYPE_FRAGMENT       = 44;
	public static final int IPv6_OPTION_TYPE_DESTINATION    = 60;
	public static final int IPv6_OPTION_TYPE_ROUTING        = 43;
	public static final int IPv6_OPTION_TYPE_SECURITY       = 50;
	public static final int IPv6_OPTION_TYPE_AUTHENTICATION = 51;
	public static final int IPv6_OPTION_TYPE_NO_NEXT        = 59;
	public static final int IPv6_OPTION_TYPE_MOBILITY       = 135;
	public static final int IPv6_OPTION_TYPE_IDENTITY       = 139;
	public static final int IPv6_OPTION_TYPE_SHIMv6         = 140;
	public static final int IPv6_OPTION_TYPE_FRAGMENT_LEN   = 8;
	// @formatter:on

	/* Descriptor TCP constants */
	public static final int TCP_HEADER_LEN = 20;
	public static final int TCP_FIELD_IHL = 12;
	public static final int TCP_OPTION_FIELD_KIND = 0;
	public static final int TCP_OPTION_FIELD_LENGTH = 1;
	public static final int TCP_OPTION_KIND_EOL = 0;
	public static final int TCP_OPTION_KIND_NOP = 1;
	public static final int TCP_OPTION_KIND_MSS = 2;
	public static final int TCP_OPTION_KIND_WIN_SCALE = 3;
	public static final int TCP_OPTION_KIND_SACK = 4;
	public static final int TCP_OPTION_KIND_TIMESTAMP = 8;
	public static final int TCP_OPTION_KIND_FASTOPEN = 34;
	public static final int TCP_OPTION_LEN_MSS = 4;
	public static final int TCP_OPTION_LEN_TIMESTAMP = 10;
	public static final int TCP_OPTION_LEN_WIN_SCALE = 3;
	public static final int TCP_OPTION_LEN_FASTOPEN = 18;
	public static final int TCP_OPTION_LEN_NOP = 1;
	public static final int TCP_OPTION_LEN_EOL = 1;

	/* Descriptor UDP constants */
	public static final int UDP_HEADER_LEN = 8;

	/* Descriptor SCTP constants */
	public static final int SCTP_HEADER_LEN = 0;

	/* Descriptor GRE constants */
	public static final int GRE_BITMASK_CHKSUM_FLAG = 0;
	public static final int GRE_BITMASK_KEY_FLAG = 0;
	public static final int GRE_BITMASK_SEQ_FLAG = 0;

	private CoreConstants() {
	}

}
