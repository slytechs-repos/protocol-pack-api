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

import com.slytechs.jnet.protocol.HeaderId;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class HeaderFactorySyncLocalImpl implements HeaderFactory {

	private final HeaderFactory allocator = HeaderFactory.newInstance();

	private final Header[][] cache0 = new Header[PROTO_MAX_PACKS][PROTO_MAX_ORDINALS];
	private final Header[][][] cache1 = new Header[PROTO_MAX_PACKS][PROTO_MAX_ORDINALS][PROTO_MAX_ORDINALS];

	/**
	 * @see com.slytechs.jnet.protocol.packet.HeaderFactory#get(int)
	 */
	@Override
	public Header get(int id) {
		int proto = HeaderId.decodeIdOrdinal(id);
		int pack = HeaderId.decodePackOrdinal(id);

		Header header = cache0[pack][proto];
		if (header == null)
			cache0[pack][proto] = header = allocator.get(id);

		header.getLock().lock();

		return header;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HeaderFactory#isReleaseSupported()
	 */
	@Override
	public boolean isReleaseSupported() {
		return true;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HeaderFactory#release(com.slytechs.jnet.protocol.packet.Header)
	 */
	@Override
	public void release(Header header) {
		header.unbind();

			header.getLock().unlock();

		if (allocator.isReleaseSupported())
			allocator.release(header);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HeaderFactory#getExtension(int, int)
	 */
	@Override
	public Header getExtension(int primaryId, int extensionId) {
		int proto0 = HeaderId.decodeIdOrdinal(primaryId);
		int pack0 = HeaderId.decodePackOrdinal(primaryId);
		int proto1 = HeaderId.decodeIdOrdinal(extensionId);

		Header header = cache1[pack0][proto0][proto1];
		if (header == null)
			cache1[pack0][proto0][proto1] = header = allocator.get(primaryId);

		header.getLock().lock();

		return header;
	}

}
