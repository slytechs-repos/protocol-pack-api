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

import com.slytechs.protocol.runtime.util.Enums;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public enum Icmp4Type implements IntSupplier {
	ECHO_REPLY(Icmp4Type.ICMPv4_TYPE_ECHO_REPLY),
	DESTINATION_UNREACHABLE(Icmp4Type.ICMPv4_TYPE_DESTINATION_UNREACHABLE),
	SOURCE_QUENCE(Icmp4Type.ICMPv4_TYPE_SOURCE_QUENCE),
	REDIRECT(Icmp4Type.ICMPv4_TYPE_REDIRECT),
	ECHO(Icmp4Type.ICMPv4_TYPE_ECHO_REQUEST),
	ROUTER_ADVERTISEMENT(Icmp4Type.ICMPv4_TYPE_ROUTER_ADVERTISEMENT),
	ROUTER_SOLICITATION(Icmp4Type.ICMPv4_TYPE_ROUTER_SOLICITATION),
	TIME_EXCEEDED(Icmp4Type.ICMPv4_TYPE_TIME_EXCEEDED),
	PARAMTER_PROBLEM_BAD_IP(Icmp4Type.ICMPv4_TYPE_PARAMTER_PROBLEM_BAD_IP),
	TIMESTAMP(Icmp4Type.ICMPv4_TYPE_TIMESTAMP),
	TIMESTAMP_REPLY(Icmp4Type.ICMPv4_TYPE_TIMESTAMP_REPLY),
	INFO_REQUEST(Icmp4Type.ICMPv4_TYPE_INFO_REQUEST),
	INFO_REPLY(Icmp4Type.ICMPv4_TYPE_INFO_REPLY),
	ADDRESS_MASK_REQUEST(Icmp4Type.ICMPv4_TYPE_ADDRESS_MASK_REQUEST),
	ADDRESS_MASK_REPLY(Icmp4Type.ICMPv4_TYPE_ADDRESS_MASK_REPLY),
	TRACEROUTE(Icmp4Type.ICMPv4_TYPE_TRACEROUTE),
	EXTENDED_REQUEST(Icmp4Type.ICMPv4_TYPE_EXTENDED_REQUEST),
	EXTENDED_REPLY(Icmp4Type.ICMPv4_TYPE_EXTENDED_REPLY),
	;

	/** The Constant ICMPv6_TYPE_RESERVED. */
	public static final int ICMPv4_TYPE_ECHO_REPLY = 0;
	public static final int ICMPv4_TYPE_DESTINATION_UNREACHABLE = 3;
	public static final int ICMPv4_TYPE_SOURCE_QUENCE = 4;
	public static final int ICMPv4_TYPE_REDIRECT = 5;
	public static final int ICMPv4_TYPE_ECHO_REQUEST = 8;
	public static final int ICMPv4_TYPE_ROUTER_ADVERTISEMENT = 9;
	public static final int ICMPv4_TYPE_ROUTER_SOLICITATION = 10;
	public static final int ICMPv4_TYPE_TIME_EXCEEDED = 11;
	public static final int ICMPv4_TYPE_PARAMTER_PROBLEM_BAD_IP = 12;
	public static final int ICMPv4_TYPE_TIMESTAMP = 13;
	public static final int ICMPv4_TYPE_TIMESTAMP_REPLY = 14;
	public static final int ICMPv4_TYPE_INFO_REQUEST = 15;
	public static final int ICMPv4_TYPE_INFO_REPLY = 16;
	public static final int ICMPv4_TYPE_ADDRESS_MASK_REQUEST = 17;
	public static final int ICMPv4_TYPE_ADDRESS_MASK_REPLY = 18;
	public static final int ICMPv4_TYPE_TRACEROUTE = 30;
	public static final int ICMPv4_TYPE_EXTENDED_REQUEST = 42;
	public static final int ICMPv4_TYPE_EXTENDED_REPLY = 43;
	
	/**
	 * Resolve.
	 *
	 * @param type the type
	 * @return the string
	 */
	public static String resolve(Object type) {
		return Enums.resolve(type, Icmp4Type.class);
	}

	/** The type. */
	private final int type;

	/**
	 * Instantiates a new icmp 6 type.
	 *
	 * @param type the type
	 */
	Icmp4Type(int type) {
		this.type = type;
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}

	/**
	 * Value of.
	 *
	 * @param intValue the int value
	 * @return the icmp 4 type
	 */
	public static Icmp4Type valueOf(int intValue) {
		return values()[intValue];
	}
}
