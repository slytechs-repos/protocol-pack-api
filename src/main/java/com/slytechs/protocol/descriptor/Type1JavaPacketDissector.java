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
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public class Type1JavaPacketDissector extends JavaPacketDissector {

	/**
	 * @see com.slytechs.protocol.descriptor.JavaPacketDissector#dissectPacket(java.nio.ByteBuffer,
	 *      long, int, int)
	 */
	@Override
	public int dissectPacket(ByteBuffer buffer, long timestamp, int caplen, int wirelen) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see com.slytechs.protocol.descriptor.JavaPacketDissector#setDatalinkType(com.slytechs.protocol.pack.core.constants.L2FrameType)
	 */
	@Override
	public PacketDissector setDatalinkType(L2FrameType l2Type) throws ProtocolException {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @see com.slytechs.protocol.descriptor.JavaPacketDissector#writeDescriptor(java.nio.ByteBuffer)
	 */
	@Override
	public int writeDescriptor(ByteBuffer buffer) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public Type1JavaPacketDissector() {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.JavaPacketDissector#destroyDissector()
	 */
	@Override
	protected void destroyDissector() {
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissector#reset()
	 */
	@Override
	public void reset() {
		throw new UnsupportedOperationException("not implemented yet");
	}

}
