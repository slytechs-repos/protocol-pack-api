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
import com.slytechs.jnet.protocol.constants.PacketDescriptorType;
import com.slytechs.jnet.runtime.time.TimestampSource;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public interface PacketDissector {

	static PacketDissector dissector(PacketDescriptorType type) {
		return javaDissector(type);
	}

	static PacketDissector javaDissector(PacketDescriptorType type) {

		return switch (type) {
		case TYPE2 -> new JavaDissectorType2();

		default -> throw new UnsupportedOperationException("Not implemented yet, dissector [%s]".formatted(type
				.name()));
		};

	}

	static PacketDissector nativeDissector(PacketDescriptorType type) {
		throw new UnsupportedOperationException();
	}

	default int dissectPacket(ByteBuffer buffer) {
		int caplen = buffer.remaining();
		long timestamp = TimestampSource.system().timestamp();

		return dissectPacket(buffer, timestamp, caplen, caplen);
	}

	int dissectPacket(ByteBuffer buffer, long timestamp, int caplen, int wirelen);

	boolean isNative();

	PacketDissector reset();

	PacketDissector setDatalinkType(L2FrameType l2Type) throws ProtocolException;

	int writeDescriptor(ByteBuffer buffer);
}
