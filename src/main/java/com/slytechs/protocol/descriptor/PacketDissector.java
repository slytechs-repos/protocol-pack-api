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

import java.net.ProtocolException;
import java.nio.ByteBuffer;

import com.slytechs.protocol.Packet;
import com.slytechs.protocol.pack.core.constants.L2FrameType;
import com.slytechs.protocol.pack.core.constants.PacketDescriptorType;
import com.slytechs.protocol.runtime.time.TimestampSource;

/**
 * The Interface PacketDissector.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public interface PacketDissector {

	/**
	 * Interface used to record new header records after dissection.
	 */
	@FunctionalInterface
	public interface RecordRecorder {
		boolean addRecord(int id, int offset, int length);
	}

	/**
	 * Dissector.
	 *
	 * @param type the type
	 * @return the packet dissector
	 */
	static PacketDissector dissector(PacketDescriptorType type) {
		return javaDissector(type);
	}

	/**
	 * Java dissector.
	 *
	 * @param type the type
	 * @return the packet dissector
	 */
	static PacketDissector javaDissector(PacketDescriptorType type) {

		return switch (type) {
		case TYPE2 -> new Type2JavaPacketDissector();

		default -> throw new UnsupportedOperationException("Not implemented yet, dissector [%s]".formatted(type
				.name()));
		};

	}

	/**
	 * Native dissector.
	 *
	 * @param type the type
	 * @return the packet dissector
	 */
	static PacketDissector nativeDissector(PacketDescriptorType type) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Dissect packet.
	 *
	 * @param buffer the buffer
	 * @return the int
	 */
	default int dissectPacket(ByteBuffer buffer) {
		int caplen = buffer.remaining();
		long timestamp = TimestampSource.system().timestamp();

		return dissectPacket(buffer, timestamp, caplen, caplen);
	}

	/**
	 * Dissect packet.
	 *
	 * @param packet the packet
	 * @return the int
	 */
	default int dissectPacket(Packet packet) {
		return dissectPacket(packet.buffer());
	}

	/**
	 * Dissect packet.
	 *
	 * @param buffer    the buffer
	 * @param timestamp the timestamp
	 * @param caplen    the caplen
	 * @param wirelen   the wirelen
	 * @return the int
	 */
	int dissectPacket(ByteBuffer buffer, long timestamp, int caplen, int wirelen);

	/**
	 * Checks if is native.
	 *
	 * @return true, if is native
	 */
	boolean isNative();

	/**
	 * Reset.
	 */
	void reset();

	/**
	 * Sets the datalink type.
	 *
	 * @param l2Type the l 2 type
	 * @return the packet dissector
	 * @throws ProtocolException the protocol exception
	 */
	PacketDissector setDatalinkType(L2FrameType l2Type) throws ProtocolException;

	/**
	 * Write descriptor.
	 *
	 * @param buffer the buffer
	 * @return the int
	 */
	int writeDescriptor(ByteBuffer buffer);

	/**
	 * Write descriptor.
	 *
	 * @param descriptor the descriptor
	 * @return the int
	 */
	default int writeDescriptor(PacketDescriptor descriptor) {
		return writeDescriptor(descriptor.buffer());
	}

	default void loadAllLoadedExtensions() {

	}
}
