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

import static com.slytechs.protocol.pack.ProtocolPackTable.*;

import com.slytechs.protocol.Header;
import com.slytechs.protocol.HeaderExtensionInfo;
import com.slytechs.protocol.HeaderSupplier;
import com.slytechs.protocol.pack.PackId;
import com.slytechs.protocol.pack.ProtocolPackTable;
import com.slytechs.protocol.pack.core.TcpOption.TcpEndOfListOption;

/**
 * The Enum TcpOptionInfo.
 */
public enum Icmp4OptionInfo implements HeaderExtensionInfo {

	/** The eol. */
	ECHO_REQUEST(CoreConstants.ICMPv4_TYPE_ECHO_REQUEST, "REQEST", TcpEndOfListOption::new),
	ECHO_REPLY(CoreConstants.ICMPv4_TYPE_ECHO_REPLY, "REPLY", TcpEndOfListOption::new),

	;

	/** The Constant TCP_OPT_ID_EOL. */
	// @formatter:off
	public static final int ICMPv4_OPT_ID_ECHO_REQUEST       = 0 | PACK_ID_OPTIONS;
	public static final int ICMPv4_OPT_ID_ECHO_REPLY         = 1 | PACK_ID_OPTIONS;
	public static final int ICMPv4_OPT_ID_UNREACHABLE        = 2 | PACK_ID_OPTIONS;
	public static final int ICMPv4_OPT_ID_SOURCE_QUENCH      = 3 | PACK_ID_OPTIONS;
	public static final int ICMPv4_OPT_ID_REDIRECT           = 4 | PACK_ID_OPTIONS;
	public static final int ICMPv4_OPT_ID_TIME_EXEEDED       = 5 | PACK_ID_OPTIONS;
	public static final int ICMPv4_OPT_ID_PARAMETER          = 6 | PACK_ID_OPTIONS;
	public static final int ICMPv4_OPT_ID_TIMESTAMP_REQUEST  = 7 | PACK_ID_OPTIONS;
	public static final int ICMPv4_OPT_ID_TIMESTAMP_REPLY    = 7 | PACK_ID_OPTIONS;
	// @formatter:on

	/** The Constant MAP_TABLE. */
	private static final int[] MAP_TABLE = new int[256];

	static {
		for (Icmp4OptionInfo opt : values())
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

	/** The id. */
	private final int id;

	/** The abbr. */
	private final String abbr;

	/** The type. */
	private final int type;

	/** The supplier. */
	private final HeaderSupplier supplier;

	/**
	 * Instantiates a new tcp option info.
	 *
	 * @param type     the type
	 * @param abbr     the abbr
	 * @param supplier the supplier
	 */
	Icmp4OptionInfo(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.id = PackId.encodeId(ProtocolPackTable.OPTS, ordinal());
		this.supplier = supplier;
	}

	/**
	 * Value of.
	 *
	 * @param id the id
	 * @return the tcp option info
	 */
	public static Icmp4OptionInfo valueOf(int id) {
		int pack = PackId.decodePackId(id);
		if (pack != ProtocolPackTable.PACK_ID_OPTIONS)
			return null;

		int index = PackId.decodeIdOrdinal(id);
		return values()[index];
	}

	/**
	 * Gets the parent header id.
	 *
	 * @return the parent header id
	 * @see com.slytechs.protocol.pack.core.IpOptionInfo#getParentHeaderId()
	 */
	@Override
	public int getParentHeaderId() {
		return CoreId.CORE_ID_TCP;
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
	 * @see com.slytechs.protocol.HeaderInfo#id()
	 */
	@Override
	public int id() {
		return id;
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
