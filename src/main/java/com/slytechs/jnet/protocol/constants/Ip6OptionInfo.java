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
import com.slytechs.jnet.protocol.core.Ip6Option.Ip6AuthenticationOption;
import com.slytechs.jnet.protocol.core.Ip6Option.Ip6DestinationOption;
import com.slytechs.jnet.protocol.core.Ip6Option.Ip6HopByHopOption;
import com.slytechs.jnet.protocol.core.Ip6Option.Ip6IdentityOption;
import com.slytechs.jnet.protocol.core.Ip6Option.Ip6MobilityOption;
import com.slytechs.jnet.protocol.core.Ip6Option.Ip6RoutingOption;
import com.slytechs.jnet.protocol.core.Ip6Option.Ip6SecurityOption;
import com.slytechs.jnet.protocol.core.Ip6Option.Ip6Shim6Option;
import com.slytechs.jnet.protocol.core.IpOptionInfo;
import com.slytechs.jnet.protocol.packet.Header;

public enum Ip6OptionInfo implements IpOptionInfo {

	HOP_BY_HOP(0, "HOP", Ip6HopByHopOption::new),
	FRAGMENT(44, "FRAG", Ip6RoutingOption::new),
	DESTINATION(60, "DST", Ip6DestinationOption::new),
	ROUTING(43, "RR", Ip6RoutingOption::new),
	SECURITY(50, "ESP", Ip6SecurityOption::new),
	AUTHENTICATION(51, "AUTH", Ip6AuthenticationOption::new),
	MOBILITY(135, "MOB", Ip6MobilityOption::new),
	IDENTITY(139, "HI", Ip6IdentityOption::new),
	SHIMv6(140, "SHIMv6", Ip6Shim6Option::new),

	;

	// @formatter:off - types
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
	// @formatter:on

	// @formatter:off - IDs
	public static final int IPv6_OPTION_ID_HOP_BY_HOP       = 0 | PACK_ID_OPTIONS;
	public static final int IPv6_OPTION_ID_FRAGMENT         = 1 | PACK_ID_OPTIONS;
	public static final int IPv6_OPTION_ID_DESTINATION      = 2 | PACK_ID_OPTIONS;
	public static final int IPv6_OPTION_ID_ROUTING          = 3 | PACK_ID_OPTIONS;
	public static final int IPv6_OPTION_ID_SECURITY         = 4 | PACK_ID_OPTIONS;
	public static final int IPv6_OPTION_ID_AUTHENTICATION   = 5 | PACK_ID_OPTIONS;
	public static final int IPv6_OPTION_ID_NO_NEXT          = 6 | PACK_ID_OPTIONS;
	public static final int IPv6_OPTION_ID_MOBILITY         = 7 | PACK_ID_OPTIONS;
	public static final int IPv6_OPTION_ID_IDENTITY         = 8 | PACK_ID_OPTIONS;
	public static final int IPv6_OPTION_ID_SHIMv6           = 9 | PACK_ID_OPTIONS;
	// @formatter:on

	// @formatter:off - lengths 
	public static final int IPv6_OPTION_TYPE_FRAGMENT_LEN   = 8;
	// @formatter:on

	private static final int[] MAP_TABLE = new int[Byte.MAX_VALUE];

	static {
		for (Ip6OptionInfo opt : values())
			MAP_TABLE[opt.type] = opt.id;
	}

	public static int mapTypeToId(int type) {
		return MAP_TABLE[type];
	}

	private final String abbr;
	private final int id;
	private final int type;
	private final HeaderSupplier supplier;

	Ip6OptionInfo(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.id = HeaderId.encodeId(PackInfo.OPTS, ordinal());
		this.supplier = supplier;
	}

	/**
	 * @see com.slytechs.jnet.protocol.core.IpOptionInfo#getParentHeaderId()
	 */
	@Override
	public int getParentHeaderId() {
		return CoreHeaderInfo.CORE_ID_IPv6;
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
		return (supplier != null) ? supplier.newHeaderInstance() : null;
	}

}