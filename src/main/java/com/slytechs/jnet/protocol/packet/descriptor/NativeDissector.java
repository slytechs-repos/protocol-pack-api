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

import java.net.ProtocolException;
import java.nio.ByteBuffer;

import com.slytechs.jnet.protocol.constants.L2FrameType;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class NativeDissector extends AbstractDissector {

	/**
	 * 
	 */
	public NativeDissector() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDissector#dissectPacket(java.nio.ByteBuffer,
	 *      long, int, int)
	 */
	@Override
	public int dissectPacket(ByteBuffer buffer, long timestamp, int caplen, int wirelen) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDissector#writeDescriptor(java.nio.ByteBuffer)
	 */
	@Override
	public int writeDescriptor(ByteBuffer buffer) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDissector#reset()
	 */
	@Override
	public PacketDissector reset() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDissector#setDatalinkType(com.slytechs.jnet.protocol.constants.L2FrameType)
	 */
	@Override
	public PacketDissector setDatalinkType(L2FrameType l2Type) throws ProtocolException {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDissector#isNative()
	 */
	@Override
	public boolean isNative() {
		throw new UnsupportedOperationException("not implemented yet");
	}

}
