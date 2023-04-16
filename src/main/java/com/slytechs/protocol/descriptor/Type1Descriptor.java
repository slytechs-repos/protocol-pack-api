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
package com.slytechs.protocol.descriptor;

import static com.slytechs.protocol.descriptor.Type1DescriptorLayout.*;

import com.slytechs.protocol.pack.core.constants.CoreConstants;
import com.slytechs.protocol.pack.core.constants.L3FrameType;
import com.slytechs.protocol.pack.core.constants.PacketDescriptorType;
import com.slytechs.protocol.runtime.util.Detail;

/**
 * The Class Type1Descriptor.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class Type1Descriptor extends PacketDescriptor {

	/** Length in bytes of type1 descriptor. */
	public static final int LENGTH = CoreConstants.DESC_TYPE1_BYTE_SIZE;

	/**
	 * Instantiates a new type 1 descriptor.
	 */
	public Type1Descriptor() {
		super(PacketDescriptorType.TYPE1);
	}

	/**
	 * Checks if is header extension supported.
	 *
	 * @return true, if is header extension supported
	 * @see com.slytechs.protocol.HeaderLookup#isHeaderExtensionSupported()
	 */
	@Override
	public boolean isHeaderExtensionSupported() {
		return false;
	}

	/**
	 * List headers.
	 *
	 * @return the long[]
	 * @see com.slytechs.protocol.HeaderLookup#listHeaders()
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
	 * @see com.slytechs.protocol.HeaderLookup#lookupHeader(int, int)
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
	 * @see com.slytechs.protocol.HeaderLookup#lookupHeaderExtension(int, int, int,
	 *      int)
	 */
	@Override
	public long lookupHeaderExtension(int headerId, int extId, int depth, int recordIndexHint) {
		return CompactDescriptor.ID_NOT_FOUND;
	}

	/**
	 * Byte size.
	 *
	 * @return the int
	 * @see com.slytechs.protocol.descriptor.PacketDescriptor#byteSize()
	 */
	@Override
	public int byteSize() {
		return LENGTH;
	}

	/**
	 * Timestamp.
	 *
	 * @return the long
	 * @see com.slytechs.protocol.descriptor.PacketDescriptor#timestamp()
	 */
	@Override
	public long timestamp() {
		return TIMESTAMP.getLong(buffer());
	}

	/**
	 * Capture length.
	 *
	 * @return the int
	 * @see com.slytechs.protocol.descriptor.PacketDescriptor#captureLength()
	 */
	@Override
	public int captureLength() {
		return CAPLEN.getUnsignedShort(buffer());
	}

	/**
	 * Wire length.
	 *
	 * @return the int
	 * @see com.slytechs.protocol.descriptor.PacketDescriptor#wireLength()
	 */
	@Override
	public int wireLength() {
		return WIRELEN.getUnsignedShort(buffer());
	}

	@Override
	public int l2FrameType() {
		return L2_FRAME_TYPE.getUnsignedShort(buffer());
	}

	public L3FrameType l3FrameTypeAsContant() {
		return L3FrameType.valueOfL3FrameType(l3FrameType());
	}

	public int l3FrameType() {
		return L3_FRAME_TYPE.getUnsignedShort(buffer());
	}

	public int l3Offset() {
		return L3_OFFSET.getUnsignedShort(buffer());
	}

	public int l3Size() {
		return L3_SIZE.getUnsignedShort(buffer());
	}

	public int l4FrameType() {
		return L4_FRAME_TYPE.getUnsignedShort(buffer());
	}

	public int l4Offset() {
		return l3Offset() + l3Size();
	}

	public int l4Size() {
		return L4_SIZE.getUnsignedShort(buffer());
	}

	/**
	 * Builds the detailed string.
	 *
	 * @param toAppendTo the b
	 * @param detail     the detail
	 * @return the string builder
	 * @see com.slytechs.protocol.descriptor.PacketDescriptor#buildDetailedString(java.lang.StringBuilder,
	 *      com.slytechs.protocol.runtime.util.Detail)
	 */
	@Override
	protected StringBuilder buildDetailedString(StringBuilder toAppendTo, Detail detail) {
		if (detail == Detail.LOW) {
			toAppendTo
					.append("cap=%d".formatted(captureLength()))
					.append(", ts=\"%tc\"%n".formatted(timestamp()));

		} else if (detail == Detail.MEDIUM) {
			toAppendTo
					.append("cap=%d".formatted(captureLength()))
					.append(", wire=%d".formatted(wireLength()))
					.append(", l3=%s".formatted(L3FrameType.valueOfL3FrameType(l3FrameType())))
					.append(", ts=\"%tc\"%n".formatted(timestamp()));

		} else { // Detail.HIGH
			toAppendTo
					.append("captureLength=%d".formatted(captureLength()))
					.append(", wireLength=%d".formatted(wireLength()))
					.append(", l3FrameType=%s".formatted(L3FrameType.valueOfL3FrameType(l3FrameType())))
					.append(", timestamp=\"%tc\"%n".formatted(timestamp()));

		}

		return toAppendTo;
	}

}
