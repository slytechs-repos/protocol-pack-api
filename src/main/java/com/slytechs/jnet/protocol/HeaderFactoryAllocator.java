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
package com.slytechs.jnet.protocol;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import com.slytechs.jnet.protocol.core.constants.PackInfo;

/**
 * The Class HeaderFactoryAllocator.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class HeaderFactoryAllocator implements HeaderFactory {

	/** The table. */
	@SuppressWarnings("unchecked")
	private final Reference<HeaderFactory>[] table = new Reference[PackInfo.values().length];

	/**
	 * Gets the.
	 *
	 * @param id the id
	 * @return the header
	 * @see com.slytechs.jnet.protocol.HeaderFactory#get(int)
	 */
	@Override
	public Header get(int id) {
		HeaderFactory perPack = lookupPackFactory(id);

		return perPack.get(id);
	}

	/**
	 * Lookup pack factory.
	 *
	 * @param id the id
	 * @return the header factory
	 */
	private HeaderFactory lookupPackFactory(int id) {
		ProtocolPack<?> pack = ProtocolPack.getLoadedPack(id);
		int ordinal = HeaderId.decodePackOrdinal(id);

		Reference<HeaderFactory> entry = table[ordinal];

		if (entry == null || entry.get() == null) {
			int packId = HeaderId.decodePackId(id);

			entry = table[ordinal] = new WeakReference<>(new PackHeaderFactory(packId, pack.toArray()));
		}

		return entry.get();
	}

	/**
	 * Gets the extension.
	 *
	 * @param primaryId   the primary id
	 * @param extensionId the extension id
	 * @return the extension
	 * @see com.slytechs.jnet.protocol.HeaderFactory#getExtension(int, int)
	 */
	@Override
	public Header getExtension(int primaryId, int extensionId) {
		HeaderFactory perPack = lookupPackFactory(primaryId);

		return perPack.getExtension(primaryId, extensionId);
	}
}
