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
 * A packet dissector which records the state of the packet. A dissectors
 * decodes the contents of the packet and breaks it down into individual headers
 * and other relevant information. The dissector stores the result internally
 * until the next reset on it called. The dissectors state is written to a
 * descriptor buffer which for the specific descriptor type that was specified
 * at the time the dissector was allocated.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface PacketDissector {

	/**
	 * Interface used to record new header records after dissection.
	 */
	@FunctionalInterface
	public interface RecordRecorder {

		/**
		 * Adds the record.
		 *
		 * @param id     the id
		 * @param offset the offset
		 * @param length the length
		 * @return true, if successful
		 */
		boolean addRecord(int id, int offset, int length);
	}

	/**
	 * Allocated a new packet dissector of the given type. First a native dissector
	 * is allocated if available otherwise a java implemented dissector is
	 * allocated.
	 *
	 * @param type the descriptor type for which appropriate dissector is found
	 * @return newly allocated packet dissector
	 */
	static PacketDissector dissector(PacketDescriptorType type) {
		return isNativeDissectorSupported(type)
				? nativeDissector(type)
				: javaDissector(type);
	}

	/**
	 * Allocated a new packet dissector of the given type. A java implemented
	 * dissector is allocated.
	 *
	 * @param type the descriptor type for which appropriate dissector is found
	 * @return newly allocated packet dissector
	 */
	static PacketDissector javaDissector(PacketDescriptorType type) {

		return switch (type) {
		case TYPE1 -> new Type1DissectorJavaImpl();
		case TYPE2 -> new Type2DissectorJavaImpl();

		default -> throw new UnsupportedOperationException("Not implemented yet, dissector [%s]".formatted(type
				.name()));
		};

	}

	/**
	 * Checks if is native dissector supported of given type.
	 *
	 * @param type the descriptor type
	 * @return true, if is native dissector supported
	 */
	static boolean isNativeDissectorSupported(PacketDescriptorType type) {
		return PacketDissectorNative.isSupported(type);
	}

	/**
	 * Allocated a new packet dissector of the given type. A native implemented
	 * dissector is allocated.
	 *
	 * @param type the descriptor type for which appropriate dissector is found
	 * @return newly allocated packet dissector
	 */
	static PacketDissector nativeDissector(PacketDescriptorType type) {
		if (!isNativeDissectorSupported(type))
			throw new UnsupportedOperationException("No native implementation for dissector type %s was found"
					.formatted(type.name()));

		return new PacketDissectorNative(type);
	}

	/**
	 * Dissect a packet and store its state.
	 *
	 * @param buffer the packet buffer
	 * @return number of bytes processed in the buffer
	 */
	default int dissectPacket(ByteBuffer buffer) {
		int caplen = buffer.remaining();
		long timestamp = TimestampSource.system().timestamp();

		return dissectPacket(buffer, timestamp, caplen, caplen);
	}

	/**
	 * Dissect a packet and store its state.
	 *
	 * @param packet the packet
	 * @return number of bytes processed in the buffer
	 */
	default int dissectPacket(Packet packet) {
		return dissectPacket(packet.buffer());
	}

	/**
	 * Dissect a packet and store its state.
	 *
	 * @param buffer    the packet buffer
	 * @param timestamp the timestamp
	 * @param caplen    the caplen
	 * @param wirelen   the wirelen
	 * @return number of bytes processed in the buffer
	 */
	int dissectPacket(ByteBuffer buffer, long timestamp, int caplen, int wirelen);

	/**
	 * Checks if is native.
	 *
	 * @return true, if is native
	 */
	boolean isNative();

	/**
	 * Reset the state of the dissector. Calling a reset on a dissector will reset
	 * it state and get it ready for the next dissection.
	 */
	void reset();

	/**
	 * Sets a datalink type for all packets being dissected.
	 *
	 * @param l2Type the l 2 type
	 * @return the packet dissector
	 * @throws ProtocolException the protocol exception
	 */
	PacketDissector setDatalinkType(L2FrameType l2Type) throws ProtocolException;

	/**
	 * Write the state of the dissection into the provided descriptor.
	 *
	 * @param buffer the descriptor buffer
	 * @return number of byte written
	 */
	int writeDescriptor(ByteBuffer buffer);

	/**
	 * Write the state of the dissection into the provided descriptor.
	 *
	 * @param descriptor the descriptor
	 * @return number of byte written
	 */
	default int writeDescriptor(PacketDescriptor descriptor) {
		return writeDescriptor(descriptor.buffer());
	}

}
