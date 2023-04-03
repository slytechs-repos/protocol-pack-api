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
package com.slytechs.jnet.protocol.core;

import com.slytechs.jnet.protocol.HeaderInfo;
import com.slytechs.jnet.protocol.HeaderId;
import com.slytechs.jnet.protocol.constants.CoreHeaderInfo;
import com.slytechs.jnet.protocol.constants.Pack;
import com.slytechs.jnet.protocol.constants.PackInfo;
import com.slytechs.jnet.protocol.packet.HeaderNotFound;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class CorePack extends Pack<CoreHeaderInfo> {

	/** Core Protocol Pack singleton definition. */
	private static final CorePack SINGLETON = new CorePack();

	public static CorePack singleton() {
		return SINGLETON;
	}

	/**
	 * Pack definitions are designed to be singltons.
	 */
	private CorePack() {
		super(PackInfo.CORE, CoreHeaderInfo.values());
	}

	/**
	 * @see com.slytechs.jnet.protocol.constants.Pack#getHeader(int)
	 */
	@Override
	public HeaderInfo getHeader(int id) throws HeaderNotFound {
		int packId = HeaderId.decodePackId(id);
		int hdrOrdinal = HeaderId.decodeIdOrdinal(id);
		if (packId != PackInfo.PACK_ID_CORE)
			throw new HeaderNotFound("invalid pack id [%d] not applicable to [%s] pack"
					.formatted(packId, super.getPackName()));

		var headers = CoreHeaderInfo.values();

		if (hdrOrdinal > headers.length)
			throw new HeaderNotFound("header id [%d] in [%s] pack"
					.formatted(id, super.getPackName()));

		return CoreHeaderInfo.values()[hdrOrdinal];
	}

}
