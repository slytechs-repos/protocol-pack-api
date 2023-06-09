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
package com.slytechs.protocol.pack.core;

import com.slytechs.protocol.HasExtension;
import com.slytechs.protocol.HeaderNotFound;
import com.slytechs.protocol.meta.MetaResource;
import com.slytechs.protocol.pack.core.constants.CoreId;

/**
 * IPv6 hop-by-hop extension header.
 * <p>
 * The IPv6 hop-by-hop header is an extension header that is used to carry
 * information that needs to be processed by all the nodes that the packet
 * passes through. This header is optional and can be used to carry a variety of
 * options
 * </p>
 * <p>
 * The hop-by-hop header is used by the sender to specify options that need to
 * be processed by all the nodes that the packet passes through. The options are
 * then processed by the routers and nodes that the packet passes through.
 * </p>
 */
@MetaResource("ip6-ext-hop-by-hop-meta.json")
public final class Ip6ExtHopOptions
		extends Ip6OptExtension
		implements HasExtension<Ip6Option> {

	/** The id. */
	public static int ID = CoreId.CORE_ID_IPv6_EXT_HOP_BY_HOP_OPTIONS;

	/**
	 * Instantiates a new ip 6 hop by hop option.
	 */
	public Ip6ExtHopOptions() {
		super(ID);
	}

	/**
	 * @see com.slytechs.protocol.HasExtension#getExtension(com.slytechs.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends Ip6Option> E getExtension(E extension, int depth) throws HeaderNotFound {
		return super.getExtensionHeader(extension, depth);
	}

	/**
	 * @see com.slytechs.protocol.HasExtension#hasExtension(int, int)
	 */
	@Override
	public boolean hasExtension(int extensionId, int depth) {
		return super.hasExtensionHeader(extensionId, depth);
	}

	/**
	 * @see com.slytechs.protocol.HasExtension#peekExtension(com.slytechs.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends Ip6Option> E peekExtension(E extension, int depth) {
		return super.peekExtensionHeader(extension, depth);
	}
}