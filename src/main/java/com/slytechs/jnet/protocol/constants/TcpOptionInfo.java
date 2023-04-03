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
import com.slytechs.jnet.protocol.core.TcpOption.TcpEndOfListOption;
import com.slytechs.jnet.protocol.core.TcpOption.TcpFastOpenOption;
import com.slytechs.jnet.protocol.core.TcpOption.TcpMSSOption;
import com.slytechs.jnet.protocol.core.TcpOption.TcpNoOption;
import com.slytechs.jnet.protocol.core.TcpOption.TcpSelectiveAckOption;
import com.slytechs.jnet.protocol.core.TcpOption.TcpTimestampOption;
import com.slytechs.jnet.protocol.core.TcpOption.TcpWindowScaleOption;
import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.HeaderExtensionInfo;

public enum TcpOptionInfo implements HeaderExtensionInfo {

	EOL(CoreConstants.TCP_OPTION_KIND_EOL, "EOL", TcpEndOfListOption::new),
	NOP(CoreConstants.TCP_OPTION_KIND_NOP, "NOP", TcpNoOption::new),
	MSS(CoreConstants.TCP_OPTION_KIND_MSS, "MSS", TcpMSSOption::new),
	WIN_SCALE(CoreConstants.TCP_OPTION_KIND_WIN_SCALE, "WIN", TcpWindowScaleOption::new),
	SACK(CoreConstants.TCP_OPTION_KIND_SACK, "SACK", TcpSelectiveAckOption::new),
	TIMESTAMP(CoreConstants.TCP_OPTION_KIND_TIMESTAMP, "TS", TcpTimestampOption::new),
	FASTOPEN(CoreConstants.TCP_OPTION_KIND_FASTOPEN, "FAST", TcpFastOpenOption::new),

	;

	private static final int PACK_ID_OPTS = (PACK_ID_OPTIONS << HeaderId.PROTO_SHIFT_PACK);

	// @formatter:off
	public static final int TCP_OPT_ID_EOL       = 0 | PACK_ID_OPTS;
	public static final int TCP_OPT_ID_NOP       = 1 | PACK_ID_OPTS;
	public static final int TCP_OPT_ID_MSS       = 2 | PACK_ID_OPTS;
	public static final int TCP_OPT_ID_WIN_SCALE = 3 | PACK_ID_OPTS;
	public static final int TCP_OPT_ID_SACK      = 4 | PACK_ID_OPTS;
	public static final int TCP_OPT_ID_TIMESTAMP = 5 | PACK_ID_OPTS;
	public static final int TCP_OPT_ID_FASTOPEN  = 6 | PACK_ID_OPTS;
	// @formatter:on

	private static final int[] MAP_TABLE = new int[256];

	static {
		for (TcpOptionInfo opt : values())
			MAP_TABLE[opt.type] = opt.id;
	}

	public static int mapKindToId(int type) {
		return MAP_TABLE[type];
	}

	private final int id;
	private final String abbr;
	private final int type;
	private final HeaderSupplier supplier;

	/**
	 * @param string
	 */
	TcpOptionInfo(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.id = HeaderId.encodeId(PackInfo.OPTS, ordinal());
		this.supplier = supplier;
	}

	public static TcpOptionInfo valueOf(int id) {
		int pack = HeaderId.decodePackId(id);
		if (pack != PackInfo.PACK_ID_OPTIONS)
			return null;

		int index = HeaderId.decodeIdOrdinal(id);
		return values()[index];
	}

	/**
	 * @see com.slytechs.jnet.protocol.core.IpOptionInfo#getParentHeaderId()
	 */
	@Override
	public int getParentHeaderId() {
		return CoreHeaderInfo.CORE_ID_TCP;
	}

	/**
	 * @see com.slytechs.jnet.protocol.core.IpOptionInfo#getExtensionAbbr()
	 */
	@Override
	public String getExtensionAbbr() {
		return abbr;
	}

	/**
	 * @see com.slytechs.jnet.protocol.HeaderInfo#getHeaderId()
	 */
	@Override
	public int getHeaderId() {
		return id;
	}

	/**
	 * @see com.slytechs.jnet.protocol.HeaderSupplier#newHeaderInstance()
	 */
	@Override
	public Header newHeaderInstance() {
		return supplier != null ? supplier.newHeaderInstance() : null;
	}

}
