/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.protocol.packet.descriptor;

import java.lang.foreign.Addressable;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.nio.ByteBuffer;

import com.slytechs.jnet.protocol.constants.PacketDescriptorType;
import com.slytechs.jnet.runtime.time.Timestamp;
import com.slytechs.jnet.runtime.time.TimestampUnit;
import com.slytechs.jnet.runtime.util.Detail;

public final class PcapDescriptor extends PacketDescriptor {

	public static final int ID = PacketDescriptorType.PACKET_DESCRIPTOR_TYPE_PCAP;
	public final static int PCAP_DESCRIPTOR_LENGTH = 24;

	public static PcapDescriptor ofAddress(Addressable header, MemorySession scope) {
		return ofMemorySegment(MemorySegment.ofAddress(header.address(), PCAP_DESCRIPTOR_LENGTH, scope));
	}

	public static PcapDescriptor ofMemorySegment(MemorySegment headerSegment) {
		PcapDescriptor descriptor = new PcapDescriptor();
		ByteBuffer buffer = headerSegment.asByteBuffer();
		descriptor.bind(buffer, headerSegment);

		return descriptor;
	}

	private static final long[] EMPTY_HEADER_ARRAY = new long[0];

	public PcapDescriptor() {
		super(PacketDescriptorType.PCAP);
	}

	@Override
	public int captureLength() {
		return PcapLayout.CAPLEN.getUnsignedShort(buffer());
	}

	/**
	 * @see com.slytechs.jnet.protocol.deprecated.PacketDescriptor2#toLongArrayHeaders()
	 */
	@Override
	public long[] listHeaders() {
		return EMPTY_HEADER_ARRAY;
	}

	@Override
	public long lookupHeader(int headerId, int depth) {
		return CompactDescriptor.ID_NOT_FOUND;
	}

	@Override
	public long timestamp() {
		return PcapLayout.CAPLEN.getLong(buffer());
	}

	@Override
	public StringBuilder buildDetailedString(StringBuilder b, Detail detail) {
		new Timestamp(TimestampUnit.EPOCH_MILLI.convert(timestamp(), TimestampUnit.PCAP_MICRO))
				.buildString(b, detail);
		b.append(" caplen=");
		b.append(captureLength());
		b.append(" wirelen=");
		b.append(wireLength());

		return b;
	}

	@Override
	public int wireLength() {
		return PcapLayout.WIRELEN.getUnsignedShort(buffer());
	}

	/**
	 * @see com.slytechs.jnet.protocol.deprecated.PacketDescriptor2#cloneTo(java.nio.ByteBuffer)
	 */
	@Override
	public PcapDescriptor cloneTo(ByteBuffer dst) {
		return (PcapDescriptor) super.cloneTo(dst);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HeaderLookup#isHeaderExtensionSupported()
	 */
	@Override
	public boolean isHeaderExtensionSupported() {
		return false;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDescriptor#byteSize()
	 */
	@Override
	public int byteSize() {
		return PCAP_DESCRIPTOR_LENGTH;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HeaderLookup#lookupHeaderExtension(int,
	 *      int, int, int)
	 */
	@Override
	public long lookupHeaderExtension(int headerId, int extId, int depth, int recordIndexHint) {
		return CompactDescriptor.ID_NOT_FOUND;
	}
}
