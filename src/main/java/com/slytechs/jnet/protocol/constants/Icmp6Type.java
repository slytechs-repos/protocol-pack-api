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

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public enum Icmp6Type implements IntSupplier {

	RESERVED(Icmp6Type.ICMPv6_TYPE_RESERVED),
	UNREACHABLE(Icmp6Type.ICMPv6_TYPE_UNREACHABLE),
	PACKET_TOO_BIG(Icmp6Type.ICMPv6_TYPE_PACKET_TOO_BIG),
	TIME_EXEEDED(Icmp6Type.ICMPv6_TYPE_TIME_EXEEDED),
	PARAMETER_PROBLEM(Icmp6Type.ICMPv6_TYPE_PARAMETER_PROBLEM),
	ECHO_REQUEST(Icmp6Type.ICMPv6_TYPE_ECHO_REQUEST),
	ECHO_REPLY(Icmp6Type.ICMPv6_TYPE_ECHO_REPLY),
	MULTICAST_LISTENER_QUERY(Icmp6Type.ICMPv6_TYPE_MULTICAST_LISTENER_QUERY),
	MULTICAST_LISTENER_REPORT(Icmp6Type.ICMPv6_TYPE_MULTICAST_LISTENER_REPORT),
	MULTICAST_LISTENER_DONE(Icmp6Type.ICMPv6_TYPE_MULTICAST_LISTENER_DONE),
	ROUTER_SOLICITATION(Icmp6Type.ICMPv6_TYPE_ROUTER_SOLICITATION),
	ROUTER_ADVERTISEMENT(Icmp6Type.ICMPv6_TYPE_ROUTER_ADVERTISEMENT),
	NEIGHBOR_SOLICITATION(Icmp6Type.ICMPv6_TYPE_NEIGHBOR_SOLICITATION),
	NEIGHBOR_ADVERTISEMENT(Icmp6Type.ICMPv6_TYPE_NEIGHBOR_ADVERTISEMENT),
	REDIRECT(Icmp6Type.ICMPv6_TYPE_REDIRECT),
	ROUTER_RENUMBER(Icmp6Type.ICMPv6_TYPE_ROUTER_RENUMBER),
	NODE_INFO_QUERY(Icmp6Type.ICMPv6_TYPE_NODE_INFO_QUERY),
	NODE_INFO_RESPONSE(Icmp6Type.ICMPv6_TYPE_NODE_INFO_RESPONSE),
	INVERSE_NEIGHBOR_SOLICITATION(Icmp6Type.ICMPv6_TYPE_INVERSE_NEIGHBOR_SOLICITATION),
	INVERSE_NEIGHBOR_ADVERTISEMENT(Icmp6Type.ICMPv6_TYPE_INVERSE_NEIGHBOR_ADVERTISEMENT),
	HOME_AGENT_REQUEST(Icmp6Type.ICMPv6_TYPE_HOME_AGENT_REQUEST),
	HOME_AGENT_REPLY(Icmp6Type.ICMPv6_TYPE_HOME_AGENT_REPLY),
	MOBILE_PREFIX_SOLICITATION(Icmp6Type.ICMPv6_TYPE_MOBILE_PREFIX_SOLICITATION),
	MOBILE_PREFIX_ADVERTISEMENT(Icmp6Type.ICMPv6_TYPE_MOBILE_PREFIX_ADVERTISEMENT),
	FMIPv6_MESSAGE(Icmp6Type.ICMPv6_TYPE_FMIPv6_MESSAGE),
	RPL_CONTROL_MESSAGE(Icmp6Type.ICMPv6_TYPE_RPL_CONTROL_MESSAGE),
	ILNPv6_LOCATOR_UPDATE_MESSAGE(Icmp6Type.ICMPv6_TYPE_ILNPv6_LOCATOR_UPDATE_MESSAGE),
	DUPLICATE_ADDRESS_REQUEST_CODE_SUFFIX(Icmp6Type.ICMPv6_TYPE_DUPLICATE_ADDRESS_REQUEST_CODE_SUFFIX),
	DUPLICATE_ADDRESS_CONFIRMATION_CODE_SUFFIX(Icmp6Type.ICMPv6_TYPE_DUPLICATE_ADDRESS_CONFIRMATION_CODE_SUFFIX),
	MPL_CONTROL_MESSAGE(Icmp6Type.ICMPv6_TYPE_MPL_CONTROL_MESSAGE),
	EXTENDED_ECHO_REQUEST(Icmp6Type.ICMPv6_TYPE_EXTENDED_ECHO_REQUEST),
	EXTENDED_ECHO_REPLY(Icmp6Type.ICMPv6_TYPE_EXTENDED_ECHO_REPLY),

	;

	public static final int ICMPv6_TYPE_RESERVED = 0;
	public static final int ICMPv6_TYPE_UNREACHABLE = 1;
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
	public static final int ICMPv6_TYPE_EXTENDED_ECHO_REPLY = 161

	;

	private final int type;

	/**
	 * @param icmpv6TypeReserved
	 */
	Icmp6Type(int type) {
		this.type = type;
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}

	public static Icmp6Type valueOf(int intValue) {
		return values()[intValue];
	}
}
