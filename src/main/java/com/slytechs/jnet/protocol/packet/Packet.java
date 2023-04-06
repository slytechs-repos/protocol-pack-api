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

import static com.slytechs.jnet.protocol.packet.descriptor.CompactDescriptor.*;

import java.nio.ByteBuffer;

import com.slytechs.jnet.protocol.constants.CoreHeaderInfo;
import com.slytechs.jnet.protocol.packet.descriptor.CompactDescriptor;
import com.slytechs.jnet.protocol.packet.descriptor.PacketDescriptor;
import com.slytechs.jnet.protocol.packet.descriptor.Type1Descriptor;
import com.slytechs.jnet.protocol.packet.meta.Meta;
import com.slytechs.jnet.protocol.packet.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.packet.meta.MetaResource;
import com.slytechs.jnet.protocol.packet.meta.PacketFormat;
import com.slytechs.jnet.runtime.resource.MemoryBinding;
import com.slytechs.jnet.runtime.time.Timestamp;
import com.slytechs.jnet.runtime.time.TimestampUnit;
import com.slytechs.jnet.runtime.util.Detail;
import com.slytechs.jnet.runtime.util.HexStrings;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("packet-meta.json")
public final class Packet
		extends MemoryBinding
		implements HasHeader, Cloneable {

	public static final int MAX_PACKET_LENGTH = 1538;

	private PacketDescriptor descriptor;
	private final HeaderLookup lookup;
	private PacketFormat formatter;

	public Packet() {
		this(new Type1Descriptor());
	}

	public Packet(PacketDescriptor descriptor) {
		this.descriptor = descriptor;
		this.lookup = descriptor;
	}

	private <T extends Header> boolean bindHeader(T header, int offset, int length, int meta) {
		ByteBuffer buffer = buffer();
		int payloadLength = descriptor.captureLength() - (offset + length);

		header.bindHeaderToPacket(buffer, offset, length, payloadLength);
		header.bindExtensionsToPacket(buffer, descriptor, meta);

		header.setFormatter(formatter);

		return true;
	}

	private void bindFrameHeader(Frame frame) {
		bindHeader(frame, 0, captureLength(), 0);
		frame.bindDescriptor(descriptor);
	}

	private void bindPayloadHeader(Payload payload) {
		var headers = lookup.listHeaders();

		if (headers.length == 0) {
			bindHeader(payload, 0, captureLength(), 0);
			return;
		}

		var lastHeader = headers[headers.length - 1];
		int offset = CompactDescriptor.decodeOffset(lastHeader)
				+ CompactDescriptor.decodeLength(lastHeader);
		int length = captureLength() - offset;

		bindHeader(payload, offset, length, 0);
	}

	public int payloadLength() {
		var headers = lookup.listHeaders();

		if (headers.length == 0)
			return captureLength();

		var lastHeader = headers[headers.length - 1];
		int offset = CompactDescriptor.decodeOffset(lastHeader)
				+ CompactDescriptor.decodeLength(lastHeader);
		int length = captureLength() - offset;

		return length;
	}

	@Meta(MetaType.ATTRIBUTE)
	public int captureLength() {
		return descriptor.captureLength();
	}

	@Override
	public Packet clone() {
		Packet clone = (Packet) super.clone();

		return clone;
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.foreign.MemoryBinding#cloneTo(java.nio.ByteBuffer)
	 */
	@Override
	public Packet cloneTo(ByteBuffer dst) {
		PacketDescriptor cloneDsc = (PacketDescriptor) descriptor.cloneTo(dst);
		Packet clone = (Packet) super.cloneTo(dst);
		clone.descriptor = cloneDsc;

		return clone;
	}

	public void close() {
		unbind();
	}

	@SuppressWarnings("unchecked")
	public <D extends PacketDescriptor> D descriptor() {
		return (D) descriptor;
	}

	@SuppressWarnings("unchecked")
	public <D extends PacketDescriptor> D descriptor(Class<D> descriptorType) {
		return (D) descriptor;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HasHeader#getHeader(com.slytechs.jnet.protocol.packet.Header)
	 */
	@Override
	public <T extends Header> T getHeader(T header, int depth) throws HeaderNotFound {
		T newHeader = peekHeader(header, depth);
		if (newHeader == null)
			throw new HeaderNotFound(header.name());

		return newHeader;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HasHeader#hasHeader(long)
	 */
	@Override
	public final boolean hasHeader(int headerId, int depth) {
		return lookupHeader(headerId, depth) != ID_NOT_FOUND;
	}

	protected final long lookupHeader(int id, int depth) {
		return lookup.lookupHeader(id, 0);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.HasHeader#peekHeader(com.slytechs.jnet.protocol.packet.Header,
	 *      int)
	 */
	@Override
	public <T extends Header> T peekHeader(T header, int depth) {
		header.unbind();
		
		int id = header.id();

		if (id == CoreHeaderInfo.CORE_ID_PAYLOAD && (header instanceof Payload payload)) {
			bindPayloadHeader(payload);

		} else if ((id == CoreHeaderInfo.CORE_ID_FRAME) && (header instanceof Frame frame)) {
			bindFrameHeader(frame);

		} else {

			long compact = lookupHeader(id, depth);
			if (compact == CompactDescriptor.ID_NOT_FOUND) {
				header.unbind();
				return null;
			}

			int offset = CompactDescriptor.decodeOffset(compact);
			int length = CompactDescriptor.decodeLength(compact);
			int meta = CompactDescriptor.decodeMeta(compact);

			bindHeader(header, offset, length, meta);
		}

		return header;
	}

	public Packet setDescriptor(PacketDescriptor descriptor) {
		this.descriptor = descriptor;

		return this;
	}

	public void setFormatter(PacketFormat formatter) {
		this.formatter = formatter;
	}

	public final long timestamp() {
		return descriptor.timestamp();
	}

	public final TimestampUnit timestampUnit() {
		return descriptor.timestampUnit();
	}

	@Override
	public String toString() {
		return toString(Detail.LOW);
	}

	public String toString(Detail detail) {
		if (formatter != null)
			return formatter.format(this, detail);

		return switch (detail) {
		case LOW -> "Packet [#%d: caplen=%d, timestamp=%s]"
				.formatted(descriptor().frameNo(), captureLength(), new Timestamp(timestamp()));

		case MEDIUM -> "Packet [#%d: caplen=%d, wirelen=%d, timestamp=%s]"
				.formatted(descriptor().frameNo(), captureLength(), captureLength(), wireLength(), new Timestamp(
						timestamp(), timestampUnit()));

		case HIGH -> "Packet [#%d: caplen=%d, wirelen=%d, timestamp=%s%n%s]"
				.formatted(descriptor().frameNo(), captureLength(), captureLength(), wireLength(), new Timestamp(
						timestamp(), timestampUnit()),
						HexStrings.toHexTextDump(buffer()));

		case DEBUG -> "Packet [#%d: caplen=%d, wirelen=%d, timestamp=%s, ]"
				.formatted(descriptor().frameNo(), captureLength(), captureLength(), wireLength(), new Timestamp(
						timestamp(), timestampUnit()), descriptor);
		case NONE -> "";
		default -> throw new IllegalArgumentException("Unexpected value: " + detail);
		};
	}

	public int wireLength() {
		return descriptor.wireLength();
	}

}
