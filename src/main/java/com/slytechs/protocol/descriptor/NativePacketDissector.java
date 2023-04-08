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

import com.slytechs.protocol.pack.core.constants.L2FrameType;

/**
 * The Class NativeDissector.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class NativePacketDissector extends AbstractPacketDissector {

	/**
	 * Instantiates a new native dissector.
	 */
	public NativePacketDissector() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Dissect packet.
	 *
	 * @param buffer    the buffer
	 * @param timestamp the timestamp
	 * @param caplen    the caplen
	 * @param wirelen   the wirelen
	 * @return the int
	 * @see com.slytechs.protocol.descriptor.PacketDissector#dissectPacket(java.nio.ByteBuffer,
	 *      long, int, int)
	 */
	@Override
	public int dissectPacket(ByteBuffer buffer, long timestamp, int caplen, int wirelen) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Write descriptor.
	 *
	 * @param buffer the buffer
	 * @return the int
	 * @see com.slytechs.protocol.descriptor.PacketDissector#writeDescriptor(java.nio.ByteBuffer)
	 */
	@Override
	public int writeDescriptor(ByteBuffer buffer) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Reset.
	 *
	 * @return the packet dissector
	 * @see com.slytechs.protocol.descriptor.PacketDissector#reset()
	 */
	@Override
	public PacketDissector reset() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Sets the datalink type.
	 *
	 * @param l2Type the l 2 type
	 * @return the packet dissector
	 * @throws ProtocolException the protocol exception
	 * @see com.slytechs.protocol.descriptor.PacketDissector#setDatalinkType(com.slytechs.protocol.pack.core.constants.L2FrameType)
	 */
	@Override
	public PacketDissector setDatalinkType(L2FrameType l2Type) throws ProtocolException {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Checks if is native.
	 *
	 * @return true, if is native
	 * @see com.slytechs.protocol.descriptor.PacketDissector#isNative()
	 */
	@Override
	public boolean isNative() {
		throw new UnsupportedOperationException("not implemented yet");
	}

}
