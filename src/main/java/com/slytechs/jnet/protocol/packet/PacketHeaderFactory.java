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

import static com.slytechs.jnet.protocol.HeaderId.*;

import java.util.function.Supplier;

import com.slytechs.jnet.protocol.HeaderInfo;
import com.slytechs.jnet.protocol.HeaderId;
import com.slytechs.jnet.protocol.constants.PackInfo;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class PackHeaderFactory implements HeaderFactory {

	private interface HeaderSupplier extends Supplier<Header> {
		HeaderSupplier EMPTY = () -> null;

		@Override
		Header get();
	}

	/**
	 * Lazy supplier doesn't load any classes until actually invoked. Also only hold
	 * weak references to classes so that they can be GCed when not needed anymore.
	 */
	private static class LazySupplier implements HeaderSupplier {

		private final HeaderInfo info;
		private Header header;

		LazySupplier(HeaderInfo info) {
			this.info = info;
		}

		@Override
		public Header get() {
			if (header == null)
				info.newHeaderInstance();

			return header;
		}
	}

	private final HeaderSupplier[] primary;
	private final HeaderSupplier[][] extension;
	private final int pack;

	public PackHeaderFactory(int pack, HeaderInfo[] packIds) {
		this.pack = pack;
		this.primary = initializeProtocolTable(packIds);
		this.extension = initializeExtensionTable(packIds);
	}

	private HeaderSupplier lookupSupplier(int id) {
		int pack = HeaderId.decodePackId(id);
		int ordinal = HeaderId.decodeIdOrdinal(id);

		if (pack != this.pack)
			return null;

		return primary[ordinal];
	}

	private HeaderSupplier lookupSupplier(int primaryId, int extensionId) {
		int pack0 = HeaderId.decodePackId(primaryId);
		int ordinal0 = HeaderId.decodeIdOrdinal(primaryId);
		int pack1 = HeaderId.decodePackId(extensionId);
		int ordinal1 = HeaderId.decodeIdOrdinal(extensionId);

		if ((pack0 != this.pack) || (pack1 != PackInfo.PACK_ID_OPTIONS))
			return null;

		HeaderSupplier[] table = extension[ordinal0];
		if (table == null)
			return null;

		return table[ordinal1];
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HeaderFactory#get(int)
	 */
	@Override
	public Header get(int id) {
		HeaderSupplier supplier = lookupSupplier(id);
		if (supplier == null)
			return null;

		return supplier.get();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HeaderFactory#getExtension(int, int)
	 */
	@Override
	public Header getExtension(int primaryId, int extensionId) {
		HeaderSupplier supplier = lookupSupplier(primaryId, extensionId);
		if (supplier == null)
			return null;

		return supplier.get();
	}

	private HeaderSupplier[][] initializeExtensionTable(HeaderInfo[] protocolIds) {
		HeaderSupplier[][] table = new HeaderSupplier[PROTO_MAX_ORDINALS][];

		/* Initialize entire table even for out of range values for quick lookups */
		for (int i = 0; i < protocolIds.length; i++) {
			Class<? extends Enum<? extends HeaderExtensionInfo>> cl = protocolIds[i]
					.getExtensionInfoClass();
			if (cl == null)
				continue;

			HeaderExtensionInfo[] exts = (HeaderExtensionInfo[]) cl
					.getEnumConstants();
			if (exts == null)
				continue;

			table[i] = initializeProtocolTable(exts);
		}

		return table;
	}

	private HeaderSupplier[] initializeProtocolTable(HeaderInfo[] headerInfos) {
		HeaderSupplier[] table = new HeaderSupplier[headerInfos.length];

		/* Initialize entire table even for out of range values for quick lookups */
		for (int i = 0; i < table.length; i++)
			table[i] = HeaderSupplier.EMPTY;

		for (int i = 0; i < headerInfos.length; i++)
			table[i] = new LazySupplier(headerInfos[i]);

		return table;
	}

}
