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

import static com.slytechs.protocol.pack.DeclaredPackIds.*;

import com.slytechs.protocol.Header;
import com.slytechs.protocol.HeaderExtensionInfo;
import com.slytechs.protocol.HeaderSupplier;
import com.slytechs.protocol.Other;
import com.slytechs.protocol.pack.PackId;
import com.slytechs.protocol.pack.DeclaredPackIds;
import com.slytechs.protocol.pack.core.IpOptionInfo;
import com.slytechs.protocol.pack.core.Ip4Option.Ip4RouterOption;

/**
 * The Enum Ip4OptionInfo.
 */
public enum Ip4OptionInfo implements IpOptionInfo {

	/** The end of options. */
	END_OF_OPTIONS(0x01, "EOOL"),
	
	/** The no operation. */
	NO_OPERATION(0x02, "NOP"),
	
	/** The security defunct. */
	SECURITY_DEFUNCT(0x02, "SEC_DEF"),
	
	/** The record route. */
	RECORD_ROUTE(0x07, "RR"),
	
	/** The exp1 measurement. */
	EXP1_MEASUREMENT(0x0A, "ZSU"),
	
	/** The mtu probe. */
	MTU_PROBE(0x0B, "MTUP"),
	
	/** The mtu reply. */
	MTU_REPLY(0x0C, "MTUR"),
	
	/** The encode. */
	ENCODE(0x0F, "ENC"),
	
	/** The quick start. */
	QUICK_START(0x19, "QS"),
	
	/** The exp1 rfc3692. */
	EXP1_RFC3692(0x1E, "EXP1"),
	
	/** The timestamp. */
	TIMESTAMP(0x44, "TS"),
	
	/** The traceroute. */
	TRACEROUTE(0x52, "RT"),
	
	/** The exp2 rfc3692. */
	EXP2_RFC3692(0x5E, "EXP2"),
	
	/** The security. */
	SECURITY(0x82, "SEC"),
	
	/** The loose source route. */
	LOOSE_SOURCE_ROUTE(0x83, "LSR"),
	
	/** The extended security. */
	EXTENDED_SECURITY(0x85, "E-SEC"),
	
	/** The commerical ip security. */
	COMMERICAL_IP_SECURITY(0x86, "CIPSO"),
	
	/** The stream id. */
	STREAM_ID(0x88, "SID"),
	
	/** The strict source route. */
	STRICT_SOURCE_ROUTE(0x89, "SSR"),
	
	/** The exp3 access control. */
	EXP3_ACCESS_CONTROL(0x8E, "VISA"),
	
	/** The imi traffic descriptor. */
	IMI_TRAFFIC_DESCRIPTOR(0x90, "IMITD"),
	
	/** The extended ip. */
	EXTENDED_IP(0x91, "E-IP"),
	
	/** The address extension. */
	ADDRESS_EXTENSION(0x93, "E-ADDR"),
	
	/** The router alert. */
	ROUTER_ALERT(0x94, "RTR_ALT", Ip4RouterOption::new),
	
	/** The selective directed broadcast. */
	SELECTIVE_DIRECTED_BROADCAST(0x95, "SBD"),
	
	/** The dynamic packet state. */
	DYNAMIC_PACKET_STATE(0x97, "DPS"),
	
	/** The upstream multicast packet. */
	UPSTREAM_MULTICAST_PACKET(0x98, "UMP"),
	
	/** The exp4 rfc3692. */
	EXP4_RFC3692(0x9E, "EXP4"),
	
	/** The exp5 flow control. */
	EXP5_FLOW_CONTROL(0xCD, "EXP5"),
	
	/** The exp6 rfc3692. */
	EXP6_RFC3692(0xDE, "EXP6"),

	;

	/** The Constant IPv4_OPTION_TYPE_EOOL. */
	// @formatter:off
	public static final int IPv4_OPTION_TYPE_EOOL      = 0x00;
	
	/** The Constant IPv4_OPTION_TYPE_NOP. */
	public static final int IPv4_OPTION_TYPE_NOP       = 0x01;
	
	/** The Constant IPv4_OPTION_TYPE_SEC_DEF. */
	public static final int IPv4_OPTION_TYPE_SEC_DEF   = 0x02;
	
	/** The Constant IPv4_OPTION_TYPE_RR. */
	public static final int IPv4_OPTION_TYPE_RR        = 0x07;
	
	/** The Constant IPv4_OPTION_TYPE_EXP1_ZSU. */
	public static final int IPv4_OPTION_TYPE_EXP1_ZSU  = 0x0A;
	
	/** The Constant IPv4_OPTION_TYPE_MTUP. */
	public static final int IPv4_OPTION_TYPE_MTUP      = 0x0B;
	
	/** The Constant IPv4_OPTION_TYPE_MTUR. */
	public static final int IPv4_OPTION_TYPE_MTUR      = 0x0C;
	
	/** The Constant IPv4_OPTION_TYPE_ENCODE. */
	public static final int IPv4_OPTION_TYPE_ENCODE    = 0x0F;
	
	/** The Constant IPv4_OPTION_TYPE_QS. */
	public static final int IPv4_OPTION_TYPE_QS        = 0x19;
	
	/** The Constant IPv4_OPTION_TYPE_EXP1. */
	public static final int IPv4_OPTION_TYPE_EXP1      = 0x1E;
	
	/** The Constant IPv4_OPTION_TYPE_TS. */
	public static final int IPv4_OPTION_TYPE_TS        = 0x44;
	
	/** The Constant IPv4_OPTION_TYPE_RT. */
	public static final int IPv4_OPTION_TYPE_RT        = 0x52;
	
	/** The Constant IPv4_OPTION_TYPE_EXP2. */
	public static final int IPv4_OPTION_TYPE_EXP2      = 0x5E;
	
	/** The Constant IPv4_OPTION_TYPE_SEC. */
	public static final int IPv4_OPTION_TYPE_SEC       = 0x82;
	
	/** The Constant IPv4_OPTION_TYPE_LSR. */
	public static final int IPv4_OPTION_TYPE_LSR       = 0x83;
	
	/** The Constant IPv4_OPTION_TYPE_E_SEC. */
	public static final int IPv4_OPTION_TYPE_E_SEC     = 0x85;
	
	/** The Constant IPv4_OPTION_TYPE_CIPSO. */
	public static final int IPv4_OPTION_TYPE_CIPSO     = 0x86;
	
	/** The Constant IPv4_OPTION_TYPE_SID. */
	public static final int IPv4_OPTION_TYPE_SID       = 0x88;
	
	/** The Constant IPv4_OPTION_TYPE_SSR. */
	public static final int IPv4_OPTION_TYPE_SSR       = 0x89;
	
	/** The Constant IPv4_OPTION_TYPE_EXP3_VISA. */
	public static final int IPv4_OPTION_TYPE_EXP3_VISA = 0x8E;
	
	/** The Constant IPv4_OPTION_TYPE_IMITD. */
	public static final int IPv4_OPTION_TYPE_IMITD     = 0x90;
	
	/** The Constant IPv4_OPTION_TYPE_E_IP. */
	public static final int IPv4_OPTION_TYPE_E_IP      = 0x91;
	
	/** The Constant IPv4_OPTION_TYPE_E_ADDR. */
	public static final int IPv4_OPTION_TYPE_E_ADDR    = 0x93;
	
	/** The Constant IPv4_OPTION_TYPE_RTRALT. */
	public static final int IPv4_OPTION_TYPE_RTRALT    = 0x94;
	
	/** The Constant IPv4_OPTION_TYPE_SBD. */
	public static final int IPv4_OPTION_TYPE_SBD       = 0x95;
	
	/** The Constant IPv4_OPTION_TYPE_DPS. */
	public static final int IPv4_OPTION_TYPE_DPS       = 0x97;
	
	/** The Constant IPv4_OPTION_TYPE_UMP. */
	public static final int IPv4_OPTION_TYPE_UMP       = 0x98;
	
	/** The Constant IPv4_OPTION_TYPE_EXP4. */
	public static final int IPv4_OPTION_TYPE_EXP4      = 0x9E;
	
	/** The Constant IPv4_OPTION_TYPE_EXP5. */
	public static final int IPv4_OPTION_TYPE_EXP5      = 0xCD;
	
	/** The Constant IPv4_OPTION_TYPE_EXP6. */
	public static final int IPv4_OPTION_TYPE_EXP6      = 0xDE;
	// @formatter:on

	/** The Constant IPv4_OPT_ID_EOOL. */
	// @formatter:off
	public static final int IPv4_OPT_ID_EOOL      = 0 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_NOP. */
	public static final int IPv4_OPT_ID_NOP       = 1 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_SEC_DEF. */
	public static final int IPv4_OPT_ID_SEC_DEF   = 2 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_RR. */
	public static final int IPv4_OPT_ID_RR        = 3 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_EXP1_ZSU. */
	public static final int IPv4_OPT_ID_EXP1_ZSU  = 4 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_MTUP. */
	public static final int IPv4_OPT_ID_MTUP      = 5 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_MTUR. */
	public static final int IPv4_OPT_ID_MTUR      = 6 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_ENCODE. */
	public static final int IPv4_OPT_ID_ENCODE    = 7 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_QS. */
	public static final int IPv4_OPT_ID_QS        = 8 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_EXP1. */
	public static final int IPv4_OPT_ID_EXP1      = 9 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_TS. */
	public static final int IPv4_OPT_ID_TS        = 10 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_RT. */
	public static final int IPv4_OPT_ID_RT        = 11 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_EXP2. */
	public static final int IPv4_OPT_ID_EXP2      = 12 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_SEC. */
	public static final int IPv4_OPT_ID_SEC       = 13 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_LSR. */
	public static final int IPv4_OPT_ID_LSR       = 14 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_E_SEC. */
	public static final int IPv4_OPT_ID_E_SEC     = 15 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_CIPSO. */
	public static final int IPv4_OPT_ID_CIPSO     = 16 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_SID. */
	public static final int IPv4_OPT_ID_SID       = 17 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_SSR. */
	public static final int IPv4_OPT_ID_SSR       = 18 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_EXP3_VISA. */
	public static final int IPv4_OPT_ID_EXP3_VISA = 19 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_IMITD. */
	public static final int IPv4_OPT_ID_IMITD     = 20 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_E_IP. */
	public static final int IPv4_OPT_ID_E_IP      = 21 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_E_ADDR. */
	public static final int IPv4_OPT_ID_E_ADDR    = 22 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_RTRALT. */
	public static final int IPv4_OPT_ID_RTRALT    = 23 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_SBD. */
	public static final int IPv4_OPT_ID_SBD       = 24 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_DPS. */
	public static final int IPv4_OPT_ID_DPS       = 25 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_UMP. */
	public static final int IPv4_OPT_ID_UMP       = 26 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_EXP4. */
	public static final int IPv4_OPT_ID_EXP4      = 27 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_EXP5. */
	public static final int IPv4_OPT_ID_EXP5      = 28 | PACK_ID_OPTIONS;
	
	/** The Constant IPv4_OPT_ID_EXP6. */
	public static final int IPv4_OPT_ID_EXP6      = 29 | PACK_ID_OPTIONS;
	// @formatter:on

	/** The Constant MAP_TABLE. */
	private static final int[] MAP_TABLE = new int[256];

	static {
		for (Ip4OptionInfo opt : values())
			MAP_TABLE[opt.type] = opt.id;
	}

	/**
	 * Map kind to id.
	 *
	 * @param type the type
	 * @return the int
	 */
	public static int mapKindToId(int type) {
		return MAP_TABLE[type];
	}

	/**
	 * Value of.
	 *
	 * @param id the id
	 * @return the ip 4 option info
	 */
	public static Ip4OptionInfo valueOf(int id) {
		int pack = PackId.decodePackId(id);
		if (pack != DeclaredPackIds.PACK_ID_OPTIONS)
			return null;

		int index = PackId.decodeIdOrdinal(id);
		return values()[index];
	}

	/** The id. */
	private final int id;

	/** The abbr. */
	private final String abbr;
	
	/** The type. */
	private final int type;
	
	/** The supplier. */
	private final HeaderSupplier supplier;

	/**
	 * Instantiates a new ip 4 option info.
	 *
	 * @param type the type
	 * @param abbr the abbr
	 */
	Ip4OptionInfo(int type, String abbr) {
		this.type = type;
		this.abbr = abbr;
		this.id = PackId.encodeId(DeclaredPackIds.OPTS, ordinal());
		this.supplier = Other::new;
	}

	/**
	 * Instantiates a new ip 4 option info.
	 *
	 * @param type     the type
	 * @param abbr     the abbr
	 * @param supplier the supplier
	 */
	Ip4OptionInfo(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.supplier = supplier;
		this.id = PackId.encodeId(DeclaredPackIds.OPTS, ordinal());
	}

	/**
	 * Gets the extension abbr.
	 *
	 * @return the extension abbr
	 * @see com.slytechs.protocol.pack.core.IpOptionInfo#getExtensionAbbr()
	 */
	@Override
	public String getExtensionAbbr() {
		return abbr;
	}

	/**
	 * Gets the header id.
	 *
	 * @return the header id
	 * @see com.slytechs.protocol.pack.core.IpOptionInfo#getHeaderId()
	 */
	@Override
	public int getHeaderId() {
		return id;
	}

	/**
	 * Gets the extension infos.
	 *
	 * @return the extension infos
	 * @see com.slytechs.protocol.HeaderInfo#getExtensionInfos()
	 */
	@Override
	public HeaderExtensionInfo[] getExtensionInfos() {
		return values();
	}

	/**
	 * Gets the parent header id.
	 *
	 * @return the parent header id
	 * @see com.slytechs.protocol.HeaderExtensionInfo#getParentHeaderId()
	 */
	@Override
	public int getParentHeaderId() {
		return CorePackIds.CORE_ID_IPv4;
	}

	/**
	 * New header instance.
	 *
	 * @return the header
	 * @see com.slytechs.protocol.HeaderSupplier#newHeaderInstance()
	 */
	@Override
	public Header newHeaderInstance() {
		return supplier != null ? supplier.newHeaderInstance() : null;
	}

}