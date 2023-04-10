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
public enum PacketDefinition {

	/**
	 * 1 0.000000 Cisco251_af:f4:54 Broadcast ARP 60 Who has 24.166.173.159? Tell
	 * 24.166.172.1
	 * 
	 * <pre>
	Frame 1: 60 bytes on wire (480 bits), 60 bytes captured (480 bits)
	Ethernet II, Src: Cisco251_af:f4:54 (00:07:0d:af:f4:54), Dst: Broadcast (ff:ff:ff:ff:ff:ff)
	    Destination: Broadcast (ff:ff:ff:ff:ff:ff)
	    Source: Cisco251_af:f4:54 (00:07:0d:af:f4:54)
	    Type: ARP (0x0806)
	    Trailer: 060104000000000201000302000005010301
	Address Resolution Protocol (request)
	    Hardware type: Ethernet (1)
	    Protocol type: IPv4 (0x0800)
	    Hardware size: 6
	    Protocol size: 4
	    Opcode: request (1)
	    Sender MAC address: Cisco251_af:f4:54 (00:07:0d:af:f4:54)
	    Sender IP address: 24.166.172.1
	    Target MAC address: 00:00:00_00:00:00 (00:00:00:00:00:00)
	    Target IP address: 24.166.173.159
	 * 
	 * </pre>
	 */
	ARP1_REQUEST("ffffffffffff00070daff4540806" + "000108000604000100070daff45418a6ac0100000000000018a6ad9f"),

	/**
	 * 1 0.000000 VMware_34:0b:de Broadcast RARP 42 Who is 00:0c:29:34:0b:de? Tell
	 * 00:0c:29:34:0b:de
	 * 
	 * <pre>
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
	 * 
	 * </pre>
	 */
	RARP1_REQUEST("ffffffffffff000c29340bde8035" + "0001080006040003000c29340bde00000000000c29340bde00000000"),

	/**
	 * 2 0.002000 VMware_c5:f6:9b VMware_34:0b:de RARP 42 00:0c:29:34:0b:de is at
	 * 10.1.1.100
	 * 
	 * <pre>
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
	 * 
	 * </pre>
	 */
	RARP1_REPLY("000c29340bde000c29c5f69b8035" + "0001080006040004000c29c5f69b0a01010a000c29340bde0a010164"),

	;

	private final byte[] array;

	PacketDefinition(String hexbytes) {
		this.array = HexStrings.parseHexString(hexbytes);
	}

	public byte[] toArray() {
		return array;
	}

	public ByteBuffer toByteBuffer() {
		return ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN);
	}

	public Packet toPacket() {
		var packet = new Packet(toByteBuffer());

		return packet;
	}

}
