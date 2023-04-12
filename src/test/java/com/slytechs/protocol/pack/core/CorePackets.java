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
public enum CorePackets {

	/**
	 * Cisco251_af:f4:54 Broadcast ARP 60 Who has 24.166.173.159? Tell 24.166.172.1
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
	ARP1_REQUEST(""
			+ "ffffffffffff00070daff4540806"
			+ "000108000604000100070daff45418a6ac0100000000000018a6ad9f"),

	/**
	 * VMware_34:0b:de Broadcast RARP 42 Who is 00:0c:29:34:0b:de? Tell
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
	RARP1_REQUEST(""
			+ "ffffffffffff000c29340bde8035"
			+ "0001080006040003000c29340bde00000000000c29340bde00000000"),

	/**
	 * VMware_c5:f6:9b VMware_34:0b:de RARP 42 00:0c:29:34:0b:de is at 10.1.1.100
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
	RARP1_REPLY(""
			+ "000c29340bde000c29c5f69b8035"
			+ "0001080006040004000c29c5f69b0a01010a000c29340bde0a010164"),

	/**
	 * 131.151.32.129 131.151.32.21 X11 1518 Requests: FreePixmap,
	 * 
	 * <pre>
	Frame 1: 1518 bytes on wire (12144 bits), 1518 bytes captured (12144 bits)
	Ethernet II, Src: AniCommu_40:ef:24 (00:40:05:40:ef:24), Dst: 3com_9f:b1:f3 (00:60:08:9f:b1:f3)
	    Destination: 3com_9f:b1:f3 (00:60:08:9f:b1:f3)
	    Source: AniCommu_40:ef:24 (00:40:05:40:ef:24)
	    Type: 802.1Q Virtual LAN (0x8100)
	802.1Q Virtual LAN, PRI: 0, DEI: 0, ID: 32
	    000. .... .... .... = Priority: Best Effort (default) (0)
	    ...0 .... .... .... = DEI: Ineligible
	    .... 0000 0010 0000 = ID: 32
	    Type: IPv4 (0x0800)
	Internet Protocol Version 4, Src: 131.151.32.129, Dst: 131.151.32.21
	    0100 .... = Version: 4
	    .... 0101 = Header Length: 20 bytes (5)
	    Differentiated Services Field: 0x00 (DSCP: CS0, ECN: Not-ECT)
	    Total Length: 1500
	    Identification: 0x3b32 (15154)
	    010. .... = Flags: 0x2, Don't fragment
	    ...0 0000 0000 0000 = Fragment Offset: 0
	    Time to Live: 64
	    Protocol: TCP (6)
	    Header Checksum: 0xb225 [validation disabled]
	    [Header checksum status: Unverified]
	    Source Address: 131.151.32.129
	    Destination Address: 131.151.32.21
	Transmission Control Protocol, Src Port: health-trap (1162), Dst Port: x11 (6000), Seq: 1, Ack: 1, Len: 1448
	 * </pre>
	 */
	VLAN(""
			+ "0060089fb1f300400540ef248100"
			+ "00200800"
			+ "450005dc3b3240004006b2258397208183972015"
			+ "048a17704e14d0a94d3d54b9801870f810b800000101080a0004f0c70199a3c5"),

	/**
	 * 131.151.32.129 131.151.32.21 X11 1518 Requests: FreePixmap,
	 * 
	 * <pre>
	Frame 1: 1518 bytes on wire (12144 bits), 1518 bytes captured (12144 bits)
	Ethernet II, Src: AniCommu_40:ef:24 (00:40:05:40:ef:24), Dst: 3com_9f:b1:f3 (00:60:08:9f:b1:f3)
	    Destination: 3com_9f:b1:f3 (00:60:08:9f:b1:f3)
	    Source: AniCommu_40:ef:24 (00:40:05:40:ef:24)
	    Type: 802.1Q Virtual LAN (0x8100)
	802.1Q Virtual LAN, PRI: 0, DEI: 0, ID: 32
	Internet Protocol Version 4, Src: 131.151.32.129, Dst: 131.151.32.21
	    0100 .... = Version: 4
	    .... 0101 = Header Length: 20 bytes (5)
	    Differentiated Services Field: 0x00 (DSCP: CS0, ECN: Not-ECT)
	    Total Length: 1500
	    Identification: 0x3b32 (15154)
	    010. .... = Flags: 0x2, Don't fragment
	    ...0 0000 0000 0000 = Fragment Offset: 0
	    Time to Live: 64
	    Protocol: TCP (6)
	    Header Checksum: 0xb225 [validation disabled]
	    [Header checksum status: Unverified]
	    Source Address: 131.151.32.129
	    Destination Address: 131.151.32.21
	Transmission Control Protocol, Src Port: health-trap (1162), Dst Port: x11 (6000), Seq: 1, Ack: 1, Len: 1448
	X11, Request, opcode: 54 (FreePixmap)
	X11, Request, opcode: 54 (FreePixmap)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 61 (ClearArea)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 61 (ClearArea)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 61 (ClearArea)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 61 (ClearArea)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 12 (ConfigureWindow)
	X11, Request, opcode: 8 (MapWindow)
	 * 
	 * </pre>
	 */
	VLAN_1500BYTES(""
			+ "0060089fb1f300400540ef24810000200800450005dc3b3240004006b22583972081839"
			+ "72015048a17704e14d0a94d3d54b9801870f810b800000101080a0004f0c70199a3c536"
			+ "000200be00c00036000200bc00c0000c0005005000c0000c0000000b000000fc0000000"
			+ "c0005005000c00003000000020000000e0000003d0004003800c0000000f2ff00000000"
			+ "0c0005005000c00003000800020000000e0000003d0004003800c0000000f2ff0000000"
			+ "00c0007004300c0000f000000000000000000000038000000160000000c0004003a00c0"
			+ "004000210000000000080002003a00c0000c0007003a00c0000f0007003500000000000"
			+ "00006000000160000000c0007004400c0000f00070038000000000000003f0000001600"
			+ "00000c0004003b00c0004000070000000000080002003b00c0000c0007003b00c0000f0"
			+ "01f00740000000000000006000000160000000c0007004500c0000f0020007700000000"
			+ "0000009a000000160000000c0004003c00c0004000080000000000080002003c00c0000"
			+ "c0007003c00c0000f0004000e0100000000000006000000160000000c0007004600c000"
			+ "0f00000011010000000000009a000000160000000c0004003d00c000400004000000000"
			+ "0080002003d00c0000c0007003d00c0000f000000a80100000000000006000000160000"
			+ "000c0007004700c0000f002100ab010000000000003b000000160000000c0004003e00c"
			+ "0004000c00000000000080002003e00c0000c6f07003e00c0000f00c000e30100000000"
			+ "000006000000160000000c0007004800c0000f00c000e601000000000000ac010000160"
			+ "000000c6f04003f00c0004000c00000000000080002003f00c0000c0007003f00c0000f"
			+ "00c0008f0300000000000006000000160000000c0005005000c0000300c000020000000"
			+ "e0000000c0005004c00c0000c0008002d0200000b0000000c0005004c00c00003000000"
			+ "0e000000020000003d0004003800c0000000f2ff000000000c0007004300c0000f00100"
			+ "0000000000000000038000000160000000c0004003a00c0004000c00000000000080002"
			+ "003a00c0000c0007003a00c0000f000000350000000000000006000000160000000c000"
			+ "7004400c0000f004f4b380000000000000054000000160000000c0004003b00c0004000"
			+ "080000000000080002003b00c0000c0007003b00c0000f0004008900000000000000060"
			+ "00000160000000c0107004500c0000f0000008c000000000000009a000000160000000c"
			+ "0004003c00c0004000000000000000080002003c00c0000c0107003c00c0000f0006002"
			+ "30100000000000006000000160000000c0007004600c0000f00c0002601000000000000"
			+ "9a000000160000000c0004003d00c0004000000000000000080002003d00c0000c00070"
			+ "03d00c0000f002100bd0100000000000006000000160000000c0007004700c0000f00c0"
			+ "00c0010000000000003b000000160000000c0004003e00c0004000c0000000000008000"
			+ "2003e00c0000c0007003e00c0000f000700f80100000000000006000000160000000c00"
			+ "07004800c0000f000700fb01000000000000ac010000160000000c0004003f00c000400"
			+ "0040000000000080002003f00c0000c0007003f00c0000f000800a40300000000000006"
			+ "000000160000000c0005005000c0000300c000020000000e0000000c0005004c00c0000"
			+ "c000000210200000b0000000c0005004c00c000030000000e000000020000003d000400"
			+ "3800c0000000f2ff000000000c0007004300c0000f00c00000000000000000003800000"
			+ "0160000000c0104003a00c0004000150000000000080002003a00c0000c0007003a00c0"
			+ "000f000400350000000000000006000000160000000c0007004400c0000f00000038000"
			+ "0000000000054000000160000000c0004003b00c0004000020000000000080002003b00"
			+ "c0000c0007003b00c0000f000200890000000000000006000000160000000c000700450"
			+ "0c0000f00c0008c000000000000009a000000160000000c0004003c00c0004000020000"
			+ "000000080002003c00c0000c0007003c00c0000f00020023010000"),

	/**
	 * Cisco_0a:d7:40 STP-UplinkFast LLC 60 U, func=UI; SNAP, OUI 0x00000C (Cisco
	 * Systems, Inc), PID 0x0115
	 * 
	 * <pre>
	Frame 1: 60 bytes on wire (480 bits), 60 bytes captured (480 bits) on interface \Device\NPF_{C8AAF078-03B5-49B6-AD39-7ADFE9C665FE}, id 0
	IEEE 802.3 Ethernet 
	    Destination: STP-UplinkFast (01:00:0c:cd:cd:cd)
	    Source: Cisco_0a:d7:40 (00:1d:e5:0a:d7:40)
	    Length: 46
	Logical-Link Control
	    DSAP: SNAP (0xaa)
	    SSAP: SNAP (0xaa)
	    Control field: U, func=UI (0x03)
	    Organization Code: 00:00:0c (Cisco Systems, Inc)
	    PID: Unknown (0x0115)
	Data (38 bytes)
	 * </pre>
	 */
	IEEE8023_SNAP(""
			+ "01000ccdcdcd001de50ad740002e"
			+ "aa aa 03"
			+ "00000c 0115"),

	/**
	 * Cisco_87:85:04 Spanning-tree-(for-bridges)_00 STP 60 Conf. Root =
	 * 32768/100/00:1c:0e:87:78:00 Cost = 4 Port = 0x8004
	 * 
	 * <pre>
	Frame 1: 60 bytes on wire (480 bits), 60 bytes captured (480 bits)
	IEEE 802.3 Ethernet 
	Logical-Link Control
	    DSAP: Spanning Tree BPDU (0x42)
	    SSAP: Spanning Tree BPDU (0x42)
	    Control field: U, func=UI (0x03)
	Spanning Tree Protocol
	    Protocol Identifier: Spanning Tree Protocol (0x0000)
	    Protocol Version Identifier: Spanning Tree (0)
	    BPDU Type: Configuration (0x00)
	    BPDU flags: 0x00
	        0... .... = Topology Change Acknowledgment: No
	        .... ...0 = Topology Change: No
	    Root Identifier: 32768 / 100 / 00:1c:0e:87:78:00
	        Root Bridge Priority: 32768
	        Root Bridge System ID Extension: 100
	        Root Bridge System ID: Cisco_87:78:00 (00:1c:0e:87:78:00)
	    Root Path Cost: 4
	    Bridge Identifier: 32768 / 100 / 00:1c:0e:87:85:00
	        Bridge Priority: 32768
	        Bridge System ID Extension: 100
	        Bridge System ID: Cisco_87:85:00 (00:1c:0e:87:85:00)
	    Port identifier: 0x8004
	    Message Age: 1
	    Max Age: 20
	    Hello Time: 2
	    Forward Delay: 15
	 * </pre>
	 */
	STP(""
			+ "0180c2000000001c0e8785040026"
			+ "424203"
			+ "0000 00 00 00 8064001c0e877800 00000004 8064001c0e878500"
			+ "8004 0100 1400 0200 0f00"),

	/**
	 * fc00:2:0:2::1 fc00:2:0:1::1 TCP 94 43424 → http-alt(8080) [SYN] Seq=0
	 * Win=26460 Len=0 MSS=2940 SACK_PERM TSval=2149426466 TSecr=0 WS=128
	 * 
	 * <pre>
	Frame 1: 94 bytes on wire (752 bits), 94 bytes captured (752 bits)
	Ethernet II, Src: 86:93:23:d3:37:8e (86:93:23:d3:37:8e), Dst: 22:1a:95:d6:7a:23 (22:1a:95:d6:7a:23)
	Internet Protocol Version 6, Src: fc00:2:0:2::1, Dst: fc00:2:0:1::1
	    0110 .... = Version: 6
	    .... 0000 0000 .... .... .... .... .... = Traffic Class: 0x00 (DSCP: CS0, ECN: Not-ECT)
	    .... 1101 0110 1000 0100 1010 = Flow Label: 0xd684a
	    Payload Length: 40
	    Next Header: TCP (6)
	    Hop Limit: 64
	    Source Address: fc00:2:0:2::1
	    Destination Address: fc00:2:0:1::1
	Transmission Control Protocol, Src Port: 43424 (43424), Dst Port: http-alt (8080), Seq: 0, Len: 0
	 * </pre>
	 */
	ETH_IPv6_TCP(""
			+ "221a95d67a23869323d3378e86dd"
			+ "600d684a00280640fc000002000000020000000000000001fc000002000000010000000000000001"
			+ "a9a01f90021b638c00000000a002675c8eb9000002040b7c0402080a801da5220000000001030307"),

	/**
	 * 192.168.29.58 192.168.29.160 SNMP 108 get-request
	 * 
	 * <pre>
	Frame 1: 108 bytes on wire (864 bits), 108 bytes captured (864 bits)
	Ethernet II, Src: VMware_ae:76:f5 (00:50:56:ae:76:f5), Dst: ArubaaHe_64:8b:a0 (00:0b:86:64:8b:a0)
	Internet Protocol Version 4, Src: 192.168.29.58, Dst: 192.168.29.160
	    0100 .... = Version: 4
	    .... 0101 = Header Length: 20 bytes (5)
	    Differentiated Services Field: 0x00 (DSCP: CS0, ECN: Not-ECT)
	        0000 00.. = Differentiated Services Codepoint: Default (0)
	        .... ..00 = Explicit Congestion Notification: Not ECN-Capable Transport (0)
	    Total Length: 94
	    Identification: 0x5c65 (23653)
	    000. .... = Flags: 0x0
	        0... .... = Reserved bit: Not set
	        .0.. .... = Don't fragment: Not set
	        ..0. .... = More fragments: Not set
	    ...0 0000 0000 0000 = Fragment Offset: 0
	    Time to Live: 128
	    Protocol: UDP (17)
	    Header Checksum: 0x0000 [validation disabled]
	    [Header checksum status: Unverified]
	    Source Address: 192.168.29.58
	    Destination Address: 192.168.29.160
	User Datagram Protocol, Src Port: 60376 (60376), Dst Port: snmp (161)
	    Source Port: 60376 (60376)
	    Destination Port: snmp (161)
	    Length: 74
	    Checksum: 0xbc86 [unverified]
	    [Checksum Status: Unverified]
	    [Stream index: 1]
	    [Timestamps]
	    UDP payload (66 bytes)
	Simple Network Management Protocol
	 * 
	 * </pre>
	 */
	ETH_IPv4_UDP_SNMP(""
			+ "000b86648ba0005056ae76f50800"
			+ "4500005e5c65000080110000c0a81d3ac0a81da0"
			+ "ebd800a1004abc86"
			+ "3040020103300f02030091c8020205dc040104020103041530130400020100020100040561646d696e04000400301304000400a00d02030091c80201000201003000"),

	/**
	 * 192.168.1.140 174.143.213.184 TCP 74 57678 → http(80) [SYN] Seq=0 Win=5840
	 * Len=0 MSS=1460 SACK_PERM TSval=2216538 TSecr=0 WS=128
	 * 
	 * <pre>
	Frame 1: 74 bytes on wire (592 bits), 74 bytes captured (592 bits)
	Ethernet II, Src: ASUSTekC_b3:01:84 (00:1d:60:b3:01:84), Dst: Actionte_2f:47:87 (00:26:62:2f:47:87)
	Internet Protocol Version 4, Src: 192.168.1.140, Dst: 174.143.213.184
	Transmission Control Protocol, Src Port: 57678 (57678), Dst Port: http (80), Seq: 0, Len: 0
	    Source Port: 57678 (57678)
	    Destination Port: http (80)
	    [Stream index: 0]
	    [Conversation completeness: Complete, WITH_DATA (31)]
	    [TCP Segment Len: 0]
	    Sequence Number: 0    (relative sequence number)
	    Sequence Number (raw): 2387613953
	    [Next Sequence Number: 1    (relative sequence number)]
	    Acknowledgment Number: 0
	    Acknowledgment number (raw): 0
	    1010 .... = Header Length: 40 bytes (10)
	    Flags: 0x002 (SYN)
	    Window: 5840
	    [Calculated window size: 5840]
	    Checksum: 0x8f47 [unverified]
	    [Checksum Status: Unverified]
	    Urgent Pointer: 0
	    Options: (20 bytes), Maximum segment size, SACK permitted, Timestamps, No-Operation (NOP), Window scale
	        TCP Option - Maximum segment size: 1460 bytes
	        TCP Option - SACK permitted
	        TCP Option - Timestamps
	        TCP Option - No-Operation (NOP)
	        TCP Option - Window scale: 7 (multiply by 128)
	            Kind: Window Scale (3)
	            Length: 3
	            Shift count: 7
	            [Multiplier: 128]
	    [Timestamps]
	 * 
	 * </pre>
	 */
	ETH_IPv4_TCP_WCALEOPT(""
			+ "0026622f4787001d60b301840800"
			+ "4500003ccb5b4000400628e4c0a8018cae8fd5b8"
			+ "e14e00508e50190100000000a00216d08f470000020405b40402080a0021d25a0000000001030307"),

	;

	/** The array. */
	private final byte[] array;

	/**
	 * Instantiates a new packet definition.
	 *
	 * @param hexbytes the hexbytes
	 */
	CorePackets(String hexbytes) {
		this.array = HexStrings.parseHexString(hexbytes);
	}

	/**
	 * To array.
	 *
	 * @return the byte[]
	 */
	public byte[] toArray() {
		return array;
	}

	/**
	 * To byte buffer.
	 *
	 * @return the byte buffer
	 */
	public ByteBuffer toByteBuffer() {
		return ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN);
	}

	/**
	 * To packet.
	 *
	 * @return the packet
	 */
	public Packet toPacket() {
		var packet = new Packet(toByteBuffer());

		return packet;
	}

}
