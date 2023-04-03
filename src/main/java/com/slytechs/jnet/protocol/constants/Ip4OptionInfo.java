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

import com.slytechs.jnet.protocol.HeaderSupplier;
import com.slytechs.jnet.protocol.HeaderId;
import com.slytechs.jnet.protocol.core.Ip4Option.Ip4OptRouter;
import com.slytechs.jnet.protocol.core.IpOptionInfo;
import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.HeaderExtensionInfo;
import com.slytechs.jnet.protocol.packet.Other;

public enum Ip4OptionInfo implements IpOptionInfo {

	END_OF_OPTIONS(0x01, "EOOL"),
	NO_OPERATION(0x02, "NOP"),
	SECURITY_DEFUNCT(0x02, "SEC_DEF"),
	RECORD_ROUTE(0x07, "RR"),
	EXP1_MEASUREMENT(0x0A, "ZSU"),
	MTU_PROBE(0x0B, "MTUP"),
	MTU_REPLY(0x0C, "MTUR"),
	ENCODE(0x0F, "ENC"),
	QUICK_START(0x19, "QS"),
	EXP1_RFC3692(0x1E, "EXP1"),
	TIMESTAMP(0x44, "TS"),
	TRACEROUTE(0x52, "RT"),
	EXP2_RFC3692(0x5E, "EXP2"),
	SECURITY(0x82, "SEC"),
	LOOSE_SOURCE_ROUTE(0x83, "LSR"),
	EXTENDED_SECURITY(0x85, "E-SEC"),
	COMMERICAL_IP_SECURITY(0x86, "CIPSO"),
	STREAM_ID(0x88, "SID"),
	STRICT_SOURCE_ROUTE(0x89, "SSR"),
	EXP3_ACCESS_CONTROL(0x8E, "VISA"),
	IMI_TRAFFIC_DESCRIPTOR(0x90, "IMITD"),
	EXTENDED_IP(0x91, "E-IP"),
	ADDRESS_EXTENSION(0x93, "E-ADDR"),
	ROUTER_ALERT(0x94, "RTR_ALT", Ip4OptRouter::new),
	SELECTIVE_DIRECTED_BROADCAST(0x95, "SBD"),
	DYNAMIC_PACKET_STATE(0x97, "DPS"),
	UPSTREAM_MULTICAST_PACKET(0x98, "UMP"),
	EXP4_RFC3692(0x9E, "EXP4"),
	EXP5_FLOW_CONTROL(0xCD, "EXP5"),
	EXP6_RFC3692(0xDE, "EXP6"),

	;

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
	public static final int IPv4_OPT_ID_EOOL      = 0 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_NOP       = 1 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_SEC_DEF   = 2 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_RR        = 3 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_EXP1_ZSU  = 4 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_MTUP      = 5 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_MTUR      = 6 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_ENCODE    = 7 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_QS        = 8 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_EXP1      = 9 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_TS        = 10 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_RT        = 11 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_EXP2      = 12 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_SEC       = 13 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_LSR       = 14 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_E_SEC     = 15 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_CIPSO     = 16 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_SID       = 17 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_SSR       = 18 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_EXP3_VISA = 19 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_IMITD     = 20 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_E_IP      = 21 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_E_ADDR    = 22 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_RTRALT    = 23 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_SBD       = 24 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_DPS       = 25 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_UMP       = 26 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_EXP4      = 27 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_EXP5      = 28 | PACK_ID_OPTIONS;
	public static final int IPv4_OPT_ID_EXP6      = 29 | PACK_ID_OPTIONS;
	// @formatter:on

	private static final int[] MAP_TABLE = new int[256];

	static {
		for (Ip4OptionInfo opt : values())
			MAP_TABLE[opt.type] = opt.id;
	}

	public static int mapKindToId(int type) {
		return MAP_TABLE[type];
	}

	public static Ip4OptionInfo valueOf(int id) {
		int pack = HeaderId.decodePackId(id);
		if (pack != PackInfo.PACK_ID_OPTIONS)
			return null;

		int index = HeaderId.decodeIdOrdinal(id);
		return values()[index];
	}

	private final int id;

	private final String abbr;
	private final int type;
	private final HeaderSupplier supplier;

	/**
	 * @param string
	 */
	Ip4OptionInfo(int type, String abbr) {
		this.type = type;
		this.abbr = abbr;
		this.id = HeaderId.encodeId(PackInfo.OPTS, ordinal());
		this.supplier = Other::new;
	}

	Ip4OptionInfo(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.supplier = supplier;
		this.id = HeaderId.encodeId(PackInfo.OPTS, ordinal());
	}

	/**
	 * @see com.slytechs.jnet.protocol.core.IpOptionInfo#getExtensionAbbr()
	 */
	@Override
	public String getExtensionAbbr() {
		return abbr;
	}

	/**
	 * @see com.slytechs.jnet.protocol.core.IpOptionInfo#getHeaderId()
	 */
	@Override
	public int getHeaderId() {
		return id;
	}

	/**
	 * @see com.slytechs.jnet.protocol.HeaderInfo#getExtensionInfos()
	 */
	@Override
	public HeaderExtensionInfo[] getExtensionInfos() {
		return values();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HeaderExtensionInfo#getParentHeaderId()
	 */
	@Override
	public int getParentHeaderId() {
		return CoreHeaderInfo.CORE_ID_IPv4;
	}

	/**
	 * @see com.slytechs.jnet.protocol.HeaderSupplier#newHeaderInstance()
	 */
	@Override
	public Header newHeaderInstance() {
		return supplier != null ? supplier.newHeaderInstance() : null;
	}

}