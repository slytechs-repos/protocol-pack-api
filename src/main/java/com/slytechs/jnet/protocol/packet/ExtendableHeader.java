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

import java.nio.ByteBuffer;

import com.slytechs.jnet.protocol.packet.descriptor.CompactDescriptor;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public abstract class ExtendableHeader<T extends Header>
		extends Header
		implements HasExtension<T> {

	private ByteBuffer sourceBuffer;
	private HeaderLookup lookup;
	private int meta;

	/**
	 * @see com.slytechs.jnet.protocol.packet.Header#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		sourceBuffer = null;
		lookup = null;
		meta = 0;

		super.onUnbind();
	}

	/**
	 * @param id
	 */
	protected ExtendableHeader(int id) {
		super(id);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HasExtension#getExtension(com.slytechs.jnet.protocol.packet.ExtendableHeader,
	 *      int)
	 */
	@Override
	public T getExtension(T extension, int depth) throws HeaderNotFound {
		T t = peekExtension(extension, depth);
		if (t == null)
			throw new HeaderNotFound(extension.name());

		return t;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HasExtension#hasExtension(int, int)
	 */
	@Override
	public boolean hasExtension(int extensionId, int depth) {
		long cp = lookup.lookupHeaderExtension(super.id, extensionId, depth, meta);

		return (cp != CompactDescriptor.ID_NOT_FOUND);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HasExtension#peekExtension(com.slytechs.jnet.protocol.packet.ExtendableHeader,
	 *      int)
	 */
	@Override
	public T peekExtension(T extension, int depth) {

		long cp = lookup.lookupHeaderExtension(super.id, extension.id(), depth, meta);
		if (cp == CompactDescriptor.ID_NOT_FOUND) {
			/*
			 * Make sure extension is unbound from any previous bindings. We don't want user
			 * accidently accessing previous extension binding.
			 */
			extension.unbind();

			return null;
		}

		int offset = CompactDescriptor.decodeOffset(cp);
		int length = CompactDescriptor.decodeLength(cp);

		ByteBuffer extBuffer = sourceBuffer.slice(offset, length);
		extension.bind(extBuffer);

		return extension;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.Header#bindExtensionsToPacket(java.nio.ByteBuffer,
	 *      com.slytechs.jnet.protocol.packet.HeaderLookup, int)
	 */
	@Override
	void bindExtensionsToPacket(ByteBuffer sourceBuffer, HeaderLookup lookup, int meta) {
		this.sourceBuffer = sourceBuffer;
		this.lookup = lookup;
		this.meta = meta;
	}

}
