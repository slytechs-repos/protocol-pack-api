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

import static com.slytechs.protocol.pack.core.constants.PackInfo.*;

import com.slytechs.protocol.Header;
import com.slytechs.protocol.HeaderId;
import com.slytechs.protocol.HeaderSupplier;
import com.slytechs.protocol.pack.core.IpOptionInfo;
import com.slytechs.protocol.pack.core.Ip6Option.Ip6AuthenticationOption;
import com.slytechs.protocol.pack.core.Ip6Option.Ip6DestinationOption;
import com.slytechs.protocol.pack.core.Ip6Option.Ip6HopByHopOption;
import com.slytechs.protocol.pack.core.Ip6Option.Ip6IdentityOption;
import com.slytechs.protocol.pack.core.Ip6Option.Ip6MobilityOption;
import com.slytechs.protocol.pack.core.Ip6Option.Ip6RoutingOption;
import com.slytechs.protocol.pack.core.Ip6Option.Ip6SecurityOption;
import com.slytechs.protocol.pack.core.Ip6Option.Ip6Shim6Option;

/**
 * The Enum Ip6OptionInfo.
 */
public enum Ip6OptionInfo implements IpOptionInfo {

	/** The hop by hop. */
	HOP_BY_HOP(0, "HOP", Ip6HopByHopOption::new),
	
	/** The fragment. */
	FRAGMENT(44, "FRAG", Ip6RoutingOption::new),
	
	/** The destination. */
	DESTINATION(60, "DST", Ip6DestinationOption::new),
	
	/** The routing. */
	ROUTING(43, "RR", Ip6RoutingOption::new),
	
	/** The security. */
	SECURITY(50, "ESP", Ip6SecurityOption::new),
	
	/** The authentication. */
	AUTHENTICATION(51, "AUTH", Ip6AuthenticationOption::new),
	
	/** The mobility. */
	MOBILITY(135, "MOB", Ip6MobilityOption::new),
	
	/** The identity. */
	IDENTITY(139, "HI", Ip6IdentityOption::new),
	
	/** The SHI mv 6. */
	SHIMv6(140, "SHIMv6", Ip6Shim6Option::new),

	;

	/** The Constant IPv6_OPTION_TYPE_HOP_BY_HOP. */
	// @formatter:off - types
	public static final int IPv6_OPTION_TYPE_HOP_BY_HOP     = 0;
	
	/** The Constant IPv6_OPTION_TYPE_FRAGMENT. */
	public static final int IPv6_OPTION_TYPE_FRAGMENT       = 44;
	
	/** The Constant IPv6_OPTION_TYPE_DESTINATION. */
	public static final int IPv6_OPTION_TYPE_DESTINATION    = 60;
	
	/** The Constant IPv6_OPTION_TYPE_ROUTING. */
	public static final int IPv6_OPTION_TYPE_ROUTING        = 43;
	
	/** The Constant IPv6_OPTION_TYPE_SECURITY. */
	public static final int IPv6_OPTION_TYPE_SECURITY       = 50;
	
	/** The Constant IPv6_OPTION_TYPE_AUTHENTICATION. */
	public static final int IPv6_OPTION_TYPE_AUTHENTICATION = 51;
	
	/** The Constant IPv6_OPTION_TYPE_NO_NEXT. */
	public static final int IPv6_OPTION_TYPE_NO_NEXT        = 59;
	
	/** The Constant IPv6_OPTION_TYPE_MOBILITY. */
	public static final int IPv6_OPTION_TYPE_MOBILITY       = 135;
	
	/** The Constant IPv6_OPTION_TYPE_IDENTITY. */
	public static final int IPv6_OPTION_TYPE_IDENTITY       = 139;
	
	/** The Constant IPv6_OPTION_TYPE_SHIMv6. */
	public static final int IPv6_OPTION_TYPE_SHIMv6         = 140;
	// @formatter:on

	/** The Constant IPv6_OPTION_ID_HOP_BY_HOP. */
	// @formatter:off - IDs
	public static final int IPv6_OPTION_ID_HOP_BY_HOP       = 0 | PACK_ID_OPTIONS;
	
	/** The Constant IPv6_OPTION_ID_FRAGMENT. */
	public static final int IPv6_OPTION_ID_FRAGMENT         = 1 | PACK_ID_OPTIONS;
	
	/** The Constant IPv6_OPTION_ID_DESTINATION. */
	public static final int IPv6_OPTION_ID_DESTINATION      = 2 | PACK_ID_OPTIONS;
	
	/** The Constant IPv6_OPTION_ID_ROUTING. */
	public static final int IPv6_OPTION_ID_ROUTING          = 3 | PACK_ID_OPTIONS;
	
	/** The Constant IPv6_OPTION_ID_SECURITY. */
	public static final int IPv6_OPTION_ID_SECURITY         = 4 | PACK_ID_OPTIONS;
	
	/** The Constant IPv6_OPTION_ID_AUTHENTICATION. */
	public static final int IPv6_OPTION_ID_AUTHENTICATION   = 5 | PACK_ID_OPTIONS;
	
	/** The Constant IPv6_OPTION_ID_NO_NEXT. */
	public static final int IPv6_OPTION_ID_NO_NEXT          = 6 | PACK_ID_OPTIONS;
	
	/** The Constant IPv6_OPTION_ID_MOBILITY. */
	public static final int IPv6_OPTION_ID_MOBILITY         = 7 | PACK_ID_OPTIONS;
	
	/** The Constant IPv6_OPTION_ID_IDENTITY. */
	public static final int IPv6_OPTION_ID_IDENTITY         = 8 | PACK_ID_OPTIONS;
	
	/** The Constant IPv6_OPTION_ID_SHIMv6. */
	public static final int IPv6_OPTION_ID_SHIMv6           = 9 | PACK_ID_OPTIONS;
	// @formatter:on

	/** The Constant IPv6_OPTION_TYPE_FRAGMENT_LEN. */
	// @formatter:off - lengths 
	public static final int IPv6_OPTION_TYPE_FRAGMENT_LEN   = 8;
	// @formatter:on

	/** The Constant MAP_TABLE. */
	private static final int[] MAP_TABLE = new int[256];

	static {
		for (Ip6OptionInfo opt : values())
			MAP_TABLE[opt.type] = opt.id;
	}

	/**
	 * Map type to id.
	 *
	 * @param type the type
	 * @return the int
	 */
	public static int mapTypeToId(int type) {
		return MAP_TABLE[type];
	}

	/** The abbr. */
	private final String abbr;
	
	/** The id. */
	private final int id;
	
	/** The type. */
	private final int type;
	
	/** The supplier. */
	private final HeaderSupplier supplier;

	/**
	 * Instantiates a new ip 6 option info.
	 *
	 * @param type     the type
	 * @param abbr     the abbr
	 * @param supplier the supplier
	 */
	Ip6OptionInfo(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.id = HeaderId.encodeId(PackInfo.OPTS, ordinal());
		this.supplier = supplier;
	}

	/**
	 * Gets the parent header id.
	 *
	 * @return the parent header id
	 * @see com.slytechs.protocol.pack.core.IpOptionInfo#getParentHeaderId()
	 */
	@Override
	public int getParentHeaderId() {
		return CoreHeaders.CORE_ID_IPv6;
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
		return (supplier != null) ? supplier.newHeaderInstance() : null;
	}

}