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
package com.slytechs.jnet.protocol.packet.descriptor;

import com.slytechs.jnet.protocol.core.constants.PacketDescriptorType;
import com.slytechs.jnet.runtime.util.Detail;

/**
 * The Class Type1Descriptor.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class Type1Descriptor extends PacketDescriptor {

	/**
	 * Instantiates a new type 1 descriptor.
	 */
	public Type1Descriptor() {
		super(PacketDescriptorType.TYPE1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Checks if is header extension supported.
	 *
	 * @return true, if is header extension supported
	 * @see com.slytechs.jnet.protocol.HeaderLookup#isHeaderExtensionSupported()
	 */
	@Override
	public boolean isHeaderExtensionSupported() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * List headers.
	 *
	 * @return the long[]
	 * @see com.slytechs.jnet.protocol.HeaderLookup#listHeaders()
	 */
	@Override
	public long[] listHeaders() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Lookup header.
	 *
	 * @param id    the id
	 * @param depth the depth
	 * @return the long
	 * @see com.slytechs.jnet.protocol.HeaderLookup#lookupHeader(int, int)
	 */
	@Override
	public long lookupHeader(int id, int depth) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Lookup header extension.
	 *
	 * @param headerId        the header id
	 * @param extId           the ext id
	 * @param depth           the depth
	 * @param recordIndexHint the record index hint
	 * @return the long
	 * @see com.slytechs.jnet.protocol.HeaderLookup#lookupHeaderExtension(int,
	 *      int, int, int)
	 */
	@Override
	public long lookupHeaderExtension(int headerId, int extId, int depth, int recordIndexHint) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Byte size.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDescriptor#byteSize()
	 */
	@Override
	public int byteSize() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Timestamp.
	 *
	 * @return the long
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDescriptor#timestamp()
	 */
	@Override
	public long timestamp() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Capture length.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDescriptor#captureLength()
	 */
	@Override
	public int captureLength() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Wire length.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDescriptor#wireLength()
	 */
	@Override
	public int wireLength() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Builds the detailed string.
	 *
	 * @param b      the b
	 * @param detail the detail
	 * @return the string builder
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDescriptor#buildDetailedString(java.lang.StringBuilder,
	 *      com.slytechs.jnet.runtime.util.Detail)
	 */
	@Override
	protected StringBuilder buildDetailedString(StringBuilder b, Detail detail) {
		throw new UnsupportedOperationException("not implemented yet");
	}

}
