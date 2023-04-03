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
package com.slytechs.jnet.protocol.packet;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import com.slytechs.jnet.protocol.HeaderId;
import com.slytechs.jnet.protocol.constants.Pack;
import com.slytechs.jnet.protocol.constants.PackInfo;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class HeaderFactoryAllocator implements HeaderFactory {

	@SuppressWarnings("unchecked")
	private final Reference<HeaderFactory>[] table = new Reference[PackInfo.values().length];

	/**
	 * @see com.slytechs.jnet.protocol.packet.HeaderFactory#get(int)
	 */
	@Override
	public Header get(int id) {
		HeaderFactory perPack = lookupPackFactory(id);

		return perPack.get(id);
	}

	private HeaderFactory lookupPackFactory(int id) {
		Pack<?> pack = Pack.getLoadedPack(id);
		int ordinal = HeaderId.decodePackOrdinal(id);

		Reference<HeaderFactory> entry = table[ordinal];

		if (entry == null || entry.get() == null) {
			int packId = HeaderId.decodePackId(id);

			entry = table[ordinal] = new WeakReference<>(new PackHeaderFactory(packId, pack.toArray()));
		}

		return entry.get();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HeaderFactory#getExtension(int, int)
	 */
	@Override
	public Header getExtension(int primaryId, int extensionId) {
		HeaderFactory perPack = lookupPackFactory(primaryId);

		return perPack.getExtension(primaryId, extensionId);
	}
}
