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
import com.slytechs.protocol.pack.PackId;
import com.slytechs.protocol.pack.DeclaredPackIds;
import com.slytechs.protocol.pack.core.TcpOption.TcpEndOfListOption;
import com.slytechs.protocol.pack.core.TcpOption.TcpFastOpenOption;
import com.slytechs.protocol.pack.core.TcpOption.TcpMSSOption;
import com.slytechs.protocol.pack.core.TcpOption.TcpNoOption;
import com.slytechs.protocol.pack.core.TcpOption.TcpSelectiveAckOption;
import com.slytechs.protocol.pack.core.TcpOption.TcpTimestampOption;
import com.slytechs.protocol.pack.core.TcpOption.TcpWindowScaleOption;

/**
 * The Enum TcpOptionInfo.
 */
public enum TcpOptionInfo implements HeaderExtensionInfo {

	/** The eol. */
	EOL(CoreConstants.TCP_OPTION_KIND_EOL, "EOL", TcpEndOfListOption::new),
	
	/** The nop. */
	NOP(CoreConstants.TCP_OPTION_KIND_NOP, "NOP", TcpNoOption::new),
	
	/** The mss. */
	MSS(CoreConstants.TCP_OPTION_KIND_MSS, "MSS", TcpMSSOption::new),
	
	/** The win scale. */
	WIN_SCALE(CoreConstants.TCP_OPTION_KIND_WIN_SCALE, "WIN", TcpWindowScaleOption::new),
	
	/** The sack. */
	SACK(CoreConstants.TCP_OPTION_KIND_SACK, "SACK", TcpSelectiveAckOption::new),
	
	/** The timestamp. */
	TIMESTAMP(CoreConstants.TCP_OPTION_KIND_TIMESTAMP, "TS", TcpTimestampOption::new),
	
	/** The fastopen. */
	FASTOPEN(CoreConstants.TCP_OPTION_KIND_FASTOPEN, "FAST", TcpFastOpenOption::new),

	;

	/** The Constant PACK_ID_OPTS. */
	private static final int PACK_ID_OPTS = (PACK_ID_OPTIONS << PackId.PROTO_SHIFT_PACK);

	/** The Constant TCP_OPT_ID_EOL. */
	// @formatter:off
	public static final int TCP_OPT_ID_EOL       = 0 | PACK_ID_OPTS;
	
	/** The Constant TCP_OPT_ID_NOP. */
	public static final int TCP_OPT_ID_NOP       = 1 | PACK_ID_OPTS;
	
	/** The Constant TCP_OPT_ID_MSS. */
	public static final int TCP_OPT_ID_MSS       = 2 | PACK_ID_OPTS;
	
	/** The Constant TCP_OPT_ID_WIN_SCALE. */
	public static final int TCP_OPT_ID_WIN_SCALE = 3 | PACK_ID_OPTS;
	
	/** The Constant TCP_OPT_ID_SACK. */
	public static final int TCP_OPT_ID_SACK      = 4 | PACK_ID_OPTS;
	
	/** The Constant TCP_OPT_ID_TIMESTAMP. */
	public static final int TCP_OPT_ID_TIMESTAMP = 5 | PACK_ID_OPTS;
	
	/** The Constant TCP_OPT_ID_FASTOPEN. */
	public static final int TCP_OPT_ID_FASTOPEN  = 6 | PACK_ID_OPTS;
	// @formatter:on

	/** The Constant MAP_TABLE. */
	private static final int[] MAP_TABLE = new int[256];

	static {
		for (TcpOptionInfo opt : values())
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
	TcpOptionInfo(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.id = PackId.encodeId(DeclaredPackIds.OPTS, ordinal());
		this.supplier = supplier;
	}

	/**
	 * Value of.
	 *
	 * @param id the id
	 * @return the tcp option info
	 */
	public static TcpOptionInfo valueOf(int id) {
		int pack = PackId.decodePackId(id);
		if (pack != DeclaredPackIds.PACK_ID_OPTIONS)
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
		return CorePackIds.CORE_ID_TCP;
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
	 * @see com.slytechs.protocol.HeaderInfo#getHeaderId()
	 */
	@Override
	public int getHeaderId() {
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
