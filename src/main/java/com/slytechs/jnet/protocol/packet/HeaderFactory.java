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

import com.slytechs.jnet.protocol.HeaderInfo;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public interface HeaderFactory {

	static HeaderFactory newInstance() {
		return new HeaderFactoryAllocator();
	}

	/**
	 * A reusable instance of each header will be created and cached. A thread
	 * synchronization will be performed on each header, allowing multiple headers
	 * to be gotten at the same time, but for locked headers, a requesting thread
	 * will wait until that particular header is released.
	 *
	 * @return the header factory
	 */
	static HeaderFactory syncLocal() {
		return new HeaderFactorySyncLocalImpl();
	}

	@SuppressWarnings("unchecked")
	default <H extends Header> H getHeader(HeaderInfo header) {
		return (H) get(header.getHeaderId());
	}

	@SuppressWarnings("unchecked")
	default <H extends Header> H getExension(HeaderInfo primary, HeaderExtensionInfo extension) {
		return (H) getExtension(primary.getHeaderId(), extension.getHeaderId());
	}

	Header get(int id);

	@SuppressWarnings("unchecked")
	default <H extends Header> H getHeader(int id, Class<H> type) {
		return (H) get(id);
	}

	default Header getExtension(int primaryId, int extensionId) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	default <H extends Header> H get(int primaryId, int extensionId, Class<H> type) {
		return (H) getExtension(primaryId, extensionId);
	}

	default boolean isReleaseSupported() {
		return false;
	}

	default void release(Header header) {
		throw new UnsupportedOperationException();
	}

}
