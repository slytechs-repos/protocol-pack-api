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
package com.slytechs.jnet.protocol.core.network;

import com.slytechs.jnet.protocol.HasOption;
import com.slytechs.jnet.protocol.Header;
import com.slytechs.jnet.protocol.HeaderNotFound;
import com.slytechs.jnet.protocol.meta.Meta;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
@Meta
public sealed class Icmp6OptionsHeader<T extends Header>
		extends Icmp6
		implements HasOption<T>
		permits Icmp6NeighborSolicitation {

	/**
	 * @param id
	 */
	public Icmp6OptionsHeader(int id) {
		super(id);
	}

	/**
	 * @see com.slytechs.jnet.protocol.HasOption#getOption(com.slytechs.jnet.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends T> E getOption(E extension, int depth) throws HeaderNotFound {
		return super.getOptionHeader(extension, depth);
	}

	/**
	 * @see com.slytechs.jnet.protocol.HasOption#hasOption(int, int)
	 */
	@Override
	public boolean hasOption(int extensionId, int depth) {
		return super.hasOptionHeader(extensionId, depth);
	}

	/**
	 * @see com.slytechs.jnet.protocol.HasOption#peekOption(com.slytechs.jnet.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends T> E peekOption(E extension, int depth) {
		return super.peekOptionHeader(extension, depth);
	}
}
