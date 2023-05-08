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

import static com.slytechs.protocol.descriptor.CompactDescriptor.*;

import java.nio.ByteBuffer;
import java.util.Objects;

import com.slytechs.protocol.descriptor.CompactDescriptor;
import com.slytechs.protocol.descriptor.Descriptor;
import com.slytechs.protocol.descriptor.DescriptorType;
import com.slytechs.protocol.descriptor.PacketDescriptor;
import com.slytechs.protocol.descriptor.Type2Descriptor;
import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.meta.Meta.MetaType;
import com.slytechs.protocol.meta.MetaResource;
import com.slytechs.protocol.meta.PacketFormat;
import com.slytechs.protocol.pack.core.constants.CoreId;
import com.slytechs.protocol.pack.core.constants.PacketDescriptorType;
import com.slytechs.protocol.runtime.MemoryBinding;
import com.slytechs.protocol.runtime.time.Timestamp;
import com.slytechs.protocol.runtime.time.TimestampUnit;
import com.slytechs.protocol.runtime.util.Detail;
import com.slytechs.protocol.runtime.util.DetailedString;
import com.slytechs.protocol.runtime.util.HexStrings;

/**
 * Main packet class which encapsulates raw packet data and retains reference to
 * descriptor information. A packet is designed to work with certain packet
 * descriptors which allow this packet object to perform protocol header lookups
 * for different headers
 * 
 * <p>
 * Descriptors held by this packet, are daisy chained for very efficient
 * insertion of new descriptors. Descriptors are a way network hardware and low
 * level libraries provide information about network packets. For example, when
 * IP fragment reassembly or tracking is enabled, additional IPF related
 * descriptors are attached to header packet.
 * </p>
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("packet-meta.json")
public final class Packet
		extends MemoryBinding
		implements HasHeader, Cloneable, AutoCloseable, DetailedString {

	/** The Constant MAX_PACKET_LENGTH. */
	public static final int MAX_PACKET_LENGTH = 1538;

	/** The descriptor. */
	private PacketDescriptor descriptor;

	/** The lookup. */
	private final HeaderLookup lookup;

	/** The formatter. */
	private PacketFormat formatter;

	/**
	 * Instantiates a new packet.
	 */
	public Packet() {
		this(new Type2Descriptor());
	}

	/**
	 * Instantiates a new packet.
	 *
	 * @param packet the packet
	 */
	public Packet(ByteBuffer packet) {
		this(new Type2Descriptor());

		bind(packet);
	}

	/**
	 * Instantiates a new packet.
	 *
	 * @param packet     the packet
	 * @param descriptor the descriptor.
	 */
	public Packet(ByteBuffer packet, PacketDescriptor descriptor) {
		this(descriptor);

		bind(packet);
	}

	/**
	 * Instantiates a new packet.
	 *
	 * @param packet the packet
	 * @param type   the type
	 */
	public Packet(ByteBuffer packet, PacketDescriptorType type) {
		this(type);

		bind(packet);
	}

	/**
	 * Instantiates a new packet with a specific descriptor.
	 *
	 * @param descriptor the descriptor
	 */
	public Packet(PacketDescriptor descriptor) {
		this.descriptor = descriptor;
		this.lookup = descriptor;
	}

	/**
	 * Instantiates a new packet with a specific descriptor.
	 *
	 * @param type the type
	 */
	public Packet(PacketDescriptorType type) {
		this.descriptor = type.newDescriptor();
		this.lookup = descriptor;
	}

	/**
	 * Bind frame header.
	 *
	 * @param frame the frame
	 */
	private void bindFrameHeader(Frame frame) {
		bindHeader(frame, 0, captureLength(), 0);
		frame.bindDescriptor(descriptor);
	}

	/**
	 * Bind header.
	 *
	 * @param <T>    the generic type
	 * @param header the header
	 * @param offset the offset
	 * @param length the length
	 * @param meta   the meta
	 * @return true, if successful
	 */
	private <T extends Header> boolean bindHeader(T header, int offset, int length, int meta) {
		ByteBuffer buffer = buffer();

		header.bindHeaderToPacket(buffer, descriptor, offset, length);
		header.bindExtensionsToPacket(buffer, descriptor, meta);

		header.setFormatter(formatter);

		return true;
	}

	/**
	 * Bind payload header.
	 *
	 * @param payload the payload
	 */
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

	/**
	 * Capture length.
	 *
	 * @return the int
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int captureLength() {
		return descriptor.captureLength();
	}

	/**
	 * Clone.
	 *
	 * @return the packet
	 * @see com.slytechs.protocol.runtime.MemoryBinding#clone()
	 */
	@Override
	public Packet clone() {
		Packet clone = (Packet) super.clone();

		return clone;
	}

	/**
	 * Clone to.
	 *
	 * @param dst the dst
	 * @return the packet
	 */
	@Override
	public Packet cloneTo(ByteBuffer dst) {
		PacketDescriptor cloneDsc = (PacketDescriptor) descriptor.cloneTo(dst);
		Packet clone = (Packet) super.cloneTo(dst);
		clone.descriptor = cloneDsc;

		return clone;
	}

	/**
	 * Close.
	 *
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() {
		unbind();
	}

	/**
	 * Descriptor.
	 *
	 * @param <D> the generic type
	 * @return the d
	 */
	@SuppressWarnings("unchecked")
	public <D extends PacketDescriptor> D descriptor() {
		return (D) descriptor;
	}

	/**
	 * Descriptor.
	 *
	 * @param <D>            the generic type
	 * @param descriptorType the descriptor type
	 * @return the d
	 */
	@SuppressWarnings("unchecked")
	public <D extends Descriptor> D descriptor(DescriptorType<?> type) {
		return (D) descriptor.peekDescriptor(type);
	}

	/**
	 * Gets the header.
	 *
	 * @param <T>    the generic type
	 * @param header the header
	 * @param depth  the depth
	 * @return the header
	 * @throws HeaderNotFound the header not found
	 * @see com.slytechs.protocol.HasHeader#getHeader(com.slytechs.protocol.Header)
	 */
	@Override
	public <T extends Header> T getHeader(T header, int depth) throws HeaderNotFound {
		T newHeader = peekHeader(header, depth);
		if (newHeader == null)
			throw new HeaderNotFound(header.headerName());

		return newHeader;
	}

	/**
	 * Checks for header.
	 *
	 * @param headerId the header id
	 * @param depth    the depth
	 * @return true, if successful
	 */
	@Override
	public final boolean hasHeader(int headerId, int depth) {
		return lookupHeader(headerId, depth) != ID_NOT_FOUND;
	}

	/**
	 * Checks if any payload data is available or if all of the packet bytes are
	 * consumed by known headers. Payload starts on the next byte passed the last
	 * header and ends at packet length.
	 *
	 * @return true, if successful
	 */
	public boolean hasPayload() {
		return payloadLength() > 0;
	}

	/**
	 * Checks if the packet is truncated. If {@code caplen < wirelen} there were
	 * fewer bytes captured than there were original seen on the network.
	 *
	 * @return true, if is truncated
	 */
	public boolean isTruncated() {
		return descriptor.captureLength() < descriptor.wireLength();
	}

	/**
	 * Lookup header.
	 *
	 * @param id    the id
	 * @param depth the depth
	 * @return the long
	 */
	protected final long lookupHeader(int id, int depth) {
		return lookup.lookupHeader(id, 0);
	}

	/**
	 * Payload length.
	 *
	 * @return the int
	 */
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

	/**
	 * Peek header.
	 *
	 * @param <T>    the generic type
	 * @param header the header
	 * @param depth  the depth
	 * @return the t
	 * @see com.slytechs.protocol.HasHeader#peekHeader(com.slytechs.protocol.Header,
	 *      int)
	 */
	@Override
	public <T extends Header> T peekHeader(T header, int depth) {
		Objects.requireNonNull(header, "header"); // User error

		try {
			header.unbind();

			int id = header.id();

			if ((id == CoreId.CORE_ID_FRAME) && (header instanceof Frame frame)) {
				bindFrameHeader(frame);

			} else if (id == CoreId.CORE_ID_PAYLOAD && (header instanceof Payload payload)) {
				bindPayloadHeader(payload);

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

			/*
			 * Any exceptions inside here are illegal and a bug. The code inside is designed
			 * to handle all errors without throwing ANY exceptions.
			 */
		} catch (Throwable e) {
			throw new IllegalStateException("Unexpected error in %s"
					.formatted(toString(Detail.MEDIUM, null)), e);
		}
	}

	/**
	 * Sets the descriptor.
	 *
	 * @param descriptor the descriptor
	 * @return the packet
	 */
	public Packet setDescriptor(PacketDescriptor descriptor) {
		this.descriptor = descriptor;

		return this;
	}

	/**
	 * Sets the formatter.
	 *
	 * @param formatter the new formatter
	 */
	public void setFormatter(PacketFormat formatter) {
		this.formatter = formatter;
	}

	/**
	 * Gets the currently assigned packet formatter.
	 *
	 * @return the formatter if assigned, otherwise null
	 */
	public PacketFormat getFormatter() {
		return this.formatter;
	}

	/**
	 * Timestamp.
	 *
	 * @return the long
	 */
	public final long timestamp() {
		return descriptor.timestamp();
	}

	/**
	 * Timestamp unit.
	 *
	 * @return the timestamp unit
	 */
	public final TimestampUnit timestampUnit() {
		return descriptor.timestampUnit();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toString(Detail.LOW, formatter);
	}

	/**
	 * To string.
	 *
	 * @param detail the detail
	 * @return the string
	 */
	@Override
	public String toString(Detail detail) {
		return toString(detail, formatter);
	}

	/**
	 * To string.
	 *
	 * @param detail    the detail
	 * @param formatter the formatter
	 * @return the string
	 */
	private String toString(Detail detail, PacketFormat formatter) {
		if (formatter != null)
			return formatter.format(this, detail);

		return switch (detail) {
		case LOW -> "Packet [#%2d: caplen=%4d, timestamp=%s]"
				.formatted(
						descriptor().frameNo(),
						captureLength(),
						new Timestamp(timestamp(), timestampUnit()));

		case MEDIUM -> "Packet [#%2d: caplen=%4d, wirelen=%4d, timestamp=%s]"
				.formatted(
						descriptor().frameNo(),
						captureLength(),
						wireLength(),
						new Timestamp(timestamp(), timestampUnit()));

		case HIGH -> "Packet [#%2d: caplen=%4d, wirelen=%4d, timestamp=%s%n%s]"
				.formatted(
						descriptor().frameNo(),
						captureLength(),
						wireLength(),
						new Timestamp(timestamp(), timestampUnit()),
						HexStrings.toHexTextDump(buffer()));

		case DEBUG -> "Packet [#%d: caplen=%d, wirelen=%d, timestamp=%s, ]"
				.formatted(descriptor().frameNo(), captureLength(), wireLength(), new Timestamp(
						timestamp(), timestampUnit()), descriptor);
		case NONE -> "";
		default -> throw new IllegalArgumentException("Unexpected value: " + detail);
		};
	}

	/**
	 * Wire length.
	 *
	 * @return the int
	 */
	public int wireLength() {
		return descriptor.wireLength();
	}

}
