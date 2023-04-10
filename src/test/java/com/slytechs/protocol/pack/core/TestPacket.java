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
package com.slytechs.protocol.pack.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.slytechs.protocol.Packet;
import com.slytechs.protocol.runtime.util.HexStrings;

/**
 * Various hex packets used in testing.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public enum TestPacket {

	RARP1_REQUEST(Rarp1Req.DESCRIPTION, Rarp1Req.ETHER_HEADER + Rarp1Req.ARP_HEADER),
	RARP1_REPLY(Rarp1Reply.DESCRIPTION, Rarp1Reply.ETHER_HEADER + Rarp1Reply.ARP_HEADER),

	;

	interface Rarp1Req {
		String DESCRIPTION = """
				Frame 1: 42 bytes on wire (336 bits), 42 bytes captured (336 bits) on interface Unknown/not available in original file format(libpcap), id 0
				Ethernet II, Src: VMware_34:0b:de (00:0c:29:34:0b:de), Dst: Broadcast (ff:ff:ff:ff:ff:ff)
				    Destination: Broadcast (ff:ff:ff:ff:ff:ff)
				    Source: VMware_34:0b:de (00:0c:29:34:0b:de)
				    Type: RARP (0x8035)
				Address Resolution Protocol (reverse request)
				    Hardware type: Ethernet (1)
				    Protocol type: IPv4 (0x0800)
				    Hardware size: 6
				    Protocol size: 4
				    Opcode: reverse request (3)
				    Sender MAC address: VMware_34:0b:de (00:0c:29:34:0b:de)
				    Sender IP address: 0.0.0.0
				    Target MAC address: VMware_34:0b:de (00:0c:29:34:0b:de)
				    Target IP address: 0.0.0.0

												""";
		String ETHER_HEADER = "ffffffffffff000c29340bde8035";
		String ARP_HEADER = "0001080006040003000c29340bde00000000000c29340bde00000000";
	}

	interface Rarp1Reply {
		String DESCRIPTION = """
				Frame 2: 42 bytes on wire (336 bits), 42 bytes captured (336 bits) on interface Unknown/not available in original file format(libpcap), id 0
				Ethernet II, Src: VMware_c5:f6:9b (00:0c:29:c5:f6:9b), Dst: VMware_34:0b:de (00:0c:29:34:0b:de)
				    Destination: VMware_34:0b:de (00:0c:29:34:0b:de)
				    Source: VMware_c5:f6:9b (00:0c:29:c5:f6:9b)
				    Type: RARP (0x8035)
				Address Resolution Protocol (reverse reply)
				    Hardware type: Ethernet (1)
				    Protocol type: IPv4 (0x0800)
				    Hardware size: 6
				    Protocol size: 4
				    Opcode: reverse reply (4)
				    Sender MAC address: VMware_c5:f6:9b (00:0c:29:c5:f6:9b)
				    Sender IP address: 10.1.1.10
				    Target MAC address: VMware_34:0b:de (00:0c:29:34:0b:de)
				    Target IP address: 10.1.1.100
								""";
		String ETHER_HEADER = "000c29340bde000c29c5f69b8035";
		String ARP_HEADER = "0001080006040004000c29c5f69b0a01010a000c29340bde0a010164";
	}

	private final byte[] array;
	private final String description;

	TestPacket(String description, String hexbytes) {
		this.description = description;
		this.array = HexStrings.parseHexString(hexbytes);
	}

	public byte[] toArray() {
		return array;
	}

	public ByteBuffer toByteBuffer() {
		return ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN);
	}

	public String description() {
		return description;
	}

	public Packet toPacket() {
		var packet = new Packet(toByteBuffer());

		return packet;
	}
}
