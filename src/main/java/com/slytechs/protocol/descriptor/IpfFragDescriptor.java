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

import static com.slytechs.protocol.pack.core.constants.CoreConstants.*;

import java.nio.ByteBuffer;

import com.slytechs.protocol.pack.core.constants.IpType;
import com.slytechs.protocol.pack.core.constants.IpfDescriptorType;
import com.slytechs.protocol.pack.core.constants.L3FrameType;
import com.slytechs.protocol.runtime.util.Detail;
import com.slytechs.protocol.runtime.util.HexStrings;

/**
 * Ip fragmentation descriptor. A fragmentation descriptor provides information
 * about tracking and reassembly of IP fragments.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public class IpfFragDescriptor extends Ipfdescriptor {

	/**
	 * Instantiates a new ipf descriptor.
	 */
	public IpfFragDescriptor() {
		super(IpfDescriptorType.IPF_FRAG);
	}

	/**
	 * @param ipfDescBuffer
	 */
	public IpfFragDescriptor(ByteBuffer descBuf) {
		super(IpfDescriptorType.IPF_FRAG);
		bind(descBuf);
	}

	public int ipType() {
		return IpfFragLayout.IP_TYPE.getByte(buffer());
	}

	public L3FrameType ipTypeAsL3FrameType() {
		return L3FrameType.valueOfL3FrameType(ipType());
	}

	public boolean isIp4() {
		return !IpfFragLayout.IP_TYPE.getBit(buffer());
	}

	public boolean isIp6() {
		return IpfFragLayout.IP_TYPE.getBit(buffer());
	}

	public boolean isFrag() {
		return IpfFragLayout.IP_IS_FRAG.getBit(buffer());
	}

	public boolean isLastFrag() {
		return IpfFragLayout.IP_IS_LAST.getBit(buffer());
	}

	/**
	 * Checks if this fragment is overlapping some portion of data that has already
	 * been seen or reassembled. Duplicate packets do not count toward this flag,
	 * because exact duplicates are tracked separately {@link #isDuplicate()}.
	 *
	 * @return true, if this fragment overlapped a previous one
	 */
	public boolean isOverlap() {
		return IpfFragLayout.IP_IS_OVERLAP.getBit(buffer());
	}

	/**
	 * Checks if this fragment is exact duplicate of another fragment that has
	 * already been seen or reassembled. Only exact fragment duplicates are recorded
	 * here as any previous fragments that overlap previously seen fragment data,
	 * but are not exactly of the same fragment offset and size, are tracked
	 * separately by {@link #isOverlap()} flag.
	 *
	 * @return true, if is duplicate
	 */
	public boolean isDuplicate() {
		return IpfFragLayout.IP_IS_OVERLAP.getBit(buffer());
	}

	public int headerOffset() {
		return IpfFragLayout.IP_HDR_OFFSET.getByte(buffer());
	}

	public int headerAndRequiredOptionsLength() {
		return IpfFragLayout.IP_HDR_LEN.getByte(buffer());
	}

	public int nextHeader() {
		return IpfFragLayout.IP_NEXT.getByte(buffer());
	}

	public IpType nextHeaderAsIpType() {
		return IpType.valueOfIpType(nextHeader());
	}

	public int fragOffset() {
		return IpfFragLayout.FIELD_FRAG_OFFSET.getUnsignedShort(buffer());
	}

	public int identifier() {
		return IpfFragLayout.FIELD_IDENTIFIER.getUnsignedShort(buffer());
	}

	public int dataOffset() {
		return IpfFragLayout.FRAG_DATA_OFFSET.getUnsignedShort(buffer());
	}

	public int dataLength() {
		return IpfFragLayout.FRAG_DATA_LEN.getUnsignedShort(buffer());
	}

	public byte[] ipSrc() {
		if (ipType() == L3FrameType.L3_FRAME_TYPE_IPv4)
			return IpfFragLayout.KEY_IPv4_SRC.getByteArray(buffer());
		else
			return IpfFragLayout.KEY_IPv6_SRC.getByteArray(buffer());
	}

	public String ipSrcAsString() {
		return HexStrings.toIpString(ipSrc());
	}

	public byte[] ipDst() {
		if (ipType() == L3FrameType.L3_FRAME_TYPE_IPv4)
			return IpfFragLayout.KEY_IPv4_DST.getByteArray(buffer());
		else
			return IpfFragLayout.KEY_IPv6_DST.getByteArray(buffer());
	}

	public ByteBuffer keyBuffer() {
		if (ipType() == L3FrameType.L3_FRAME_TYPE_IPv4)
			return buffer().slice(DESC_IPF_FRAG_KEY, DESC_IPF_FRAG_IPv4_KEY_BYTE_SIZE);
		else
			return buffer().slice(DESC_IPF_FRAG_KEY, DESC_IPF_FRAG_IPv6_KEY_BYTE_SIZE);
	}

	public String ipDstAsString() {
		return HexStrings.toIpString(ipDst());
	}

	/**
	 * Builds the detailed string.
	 *
	 * @param toAppendTo the b
	 * @param detail     the detail
	 * @return the string builder
	 * @see com.slytechs.protocol.descriptor.Descriptor#buildDetailedString(java.lang.StringBuilder,
	 *      com.slytechs.protocol.runtime.util.Detail)
	 */
	@Override
	protected StringBuilder buildDetailedString(StringBuilder toAppendTo, Detail detail) {
		if (detail == Detail.LOW) {
			toAppendTo
					.append("ipType=%s".formatted(ipTypeAsL3FrameType()));

		} else if (detail == Detail.MEDIUM) {
			toAppendTo
					.append("ipType=%s".formatted(ipTypeAsL3FrameType()));

		} else { // Detail.HIGH
			toAppendTo

					.append("  ipType=%s (%d)%n".formatted(ipTypeAsL3FrameType(), ipType()))
					.append("  isFrag=%s%n".formatted(isFrag()))
					.append("  isLastFrag=%s%n".formatted(isLastFrag()))
					.append("  headerOffset=%d%n".formatted(headerOffset()))
					.append("  headerLength=%d%n".formatted(headerAndRequiredOptionsLength()))
					.append("  fragOffset=%d%n".formatted(fragOffset()))
					.append("  dataOffset=%s%n".formatted(dataOffset()))
					.append("  dataLength=%s%n".formatted(dataLength()))
					.append("  key.identifier=%s%n".formatted(identifier()))
					.append("  key.nextHeader=%s (%d)%n".formatted(nextHeaderAsIpType(), nextHeader()))
					.append("  key.source=%s%n".formatted(ipSrcAsString()))
					.append("  key.destination=%s%n".formatted(ipDstAsString()))

			;

		}
		return toAppendTo;
	}
}
