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
package com.slytechs.protocol;

import java.nio.ByteBuffer;

import com.slytechs.protocol.descriptor.HeaderDescriptor;
import com.slytechs.protocol.descriptor.PacketDescriptor;
import com.slytechs.protocol.meta.Meta;

/**
 * A specialized header subclass implemented by all header extensions and
 * options.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @param <T> the generic header extension type
 */
@Meta
public abstract class HeaderExtension<T extends Header>
		extends Header
		implements HasExtension<T> {

	/** The source buffer. */
	private ByteBuffer packet;

	/** The descriptor. */
	private PacketDescriptor descriptor;

	/** The meta. */
	private int meta;

	/**
	 * Reset this header extension instance, importantly reset the reference to
	 * sourceBuffer to release it.
	 *
	 * @see com.slytechs.protocol.Header#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		packet = null;
		descriptor = null;
		meta = 0;

		super.onUnbind();
	}

	/**
	 * Instantiates a new extendable header.
	 *
	 * @param id the id
	 */
	protected HeaderExtension(int id) {
		super(id);
	}

	/**
	 * Gets the extension.
	 *
	 * @param <E>       the element type
	 * @param extension the extension
	 * @param depth     the depth
	 * @return the extension
	 * @throws HeaderNotFound the header not found
	 * @see com.slytechs.protocol.HasExtension#getExtension(com.slytechs.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends T> E getExtension(E extension, int depth) throws HeaderNotFound {
		E t = peekExtension(extension, depth);
		if (t == null)
			throw new HeaderNotFound(extension.headerName());

		return t;
	}

	/**
	 * Checks for extension.
	 *
	 * @param extensionId the extension id
	 * @param depth       the depth
	 * @return true, if successful
	 * @see com.slytechs.protocol.HasExtension#hasExtension(int, int)
	 */
	@Override
	public boolean hasExtension(int extensionId, int depth) {
		return descriptor.lookupHeaderExtension(super.id, extensionId, depth, meta, HeaderDescriptor.EMPTY);
	}

	/**
	 * Peek extension.
	 *
	 * @param <E>       the element type
	 * @param extension the extension
	 * @param depth     the depth
	 * @return the e
	 * @see com.slytechs.protocol.HasExtension#peekExtension(com.slytechs.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends T> E peekExtension(E extension, int depth) {
		
		if (descriptor.lookupHeaderExtension(super.id, extension.id(), depth, meta, extension.getHeaderDescriptor())) {
			extension.bindHeaderToPacket(packet, descriptor);

			return extension;
		} else {
			extension.unbind();

			return null;

		}
	}

	/**
	 * Bind extensions to packet.
	 *
	 * @param packet     the packet
	 * @param descriptor the descriptor
	 * @param meta       the meta
	 * @see com.slytechs.protocol.Header#bindExtensionsToPacket(java.nio.ByteBuffer,
	 *      com.slytechs.protocol.HeaderLookup, int)
	 */
	@Override
	void bindExtensionsToPacket(ByteBuffer packet, PacketDescriptor descriptor) {
		this.packet = packet;
		this.descriptor = descriptor;
		this.meta = super.getHeaderDescriptor().getMeta();
	}

	@Override
	void bindExtensionsToPacket(ByteBuffer packet, PacketDescriptor descriptor, int meta) {
		this.packet = packet;
		this.descriptor = descriptor;
		this.meta = meta;
	}

}
