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

import com.slytechs.protocol.descriptor.CompactDescriptor;

/**
 * A specialized header subclass implemented by all header extensions and
 * options.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @param <T> the generic header extension type
 */
public abstract non-sealed class HeaderExtension<T extends Header>
		extends Header
		implements HasExtension<T> {

	/** The source buffer. */
	private ByteBuffer sourceBuffer;

	/** The lookup. */
	private HeaderLookup lookup;

	/** The meta. */
	private int meta;

	/**
	 * Reset this header extension instance, importantly reset the reference to
	 * sourceBuffer to release it
	 *
	 * @see com.slytechs.protocol.Header#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		sourceBuffer = null;
		lookup = null;
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
	 * @see com.slytechs.protocol.HasExtension#getExtension(com.slytechs.protocol.Header,
	 *      int)
	 */
	@Override
	public T getExtension(T extension, int depth) throws HeaderNotFound {
		T t = peekExtension(extension, depth);
		if (t == null)
			throw new HeaderNotFound(extension.headerName());

		return t;
	}

	/**
	 * @see com.slytechs.protocol.HasExtension#hasExtension(int, int)
	 */
	@Override
	public boolean hasExtension(int extensionId, int depth) {
		long cp = lookup.lookupHeaderExtension(super.id, extensionId, depth, meta);

		return (cp != CompactDescriptor.ID_NOT_FOUND);
	}

	/**
	 * @see com.slytechs.protocol.HasExtension#peekExtension(com.slytechs.protocol.Header,
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
	 * @see com.slytechs.protocol.Header#bindExtensionsToPacket(java.nio.ByteBuffer,
	 *      com.slytechs.protocol.HeaderLookup, int)
	 */
	@Override
	void bindExtensionsToPacket(ByteBuffer sourceBuffer, HeaderLookup lookup, int meta) {
		this.sourceBuffer = sourceBuffer;
		this.lookup = lookup;
		this.meta = meta;
	}

}
