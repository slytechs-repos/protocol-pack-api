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

import static com.slytechs.protocol.descriptor.Type2DescriptorLayout.*;
import static com.slytechs.protocol.pack.core.constants.CoreConstants.*;

import java.net.ProtocolException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.function.IntConsumer;

import com.slytechs.protocol.HeaderExtensionInfo;
import com.slytechs.protocol.pack.PackId;
import com.slytechs.protocol.pack.core.constants.CoreConstants;
import com.slytechs.protocol.pack.core.constants.CoreIdTable;
import com.slytechs.protocol.pack.core.constants.Ip4OptionInfo;
import com.slytechs.protocol.pack.core.constants.Ip6OptionInfo;
import com.slytechs.protocol.pack.core.constants.L2FrameType;
import com.slytechs.protocol.runtime.time.TimestampUnit;
import com.slytechs.protocol.runtime.util.Bits;

/**
 * The Class JavaDissectorType2.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class Type2JavaPacketDissector extends JavaPacketDissector {

	/** The Constant RECORD_START. */
	private static final int RECORD_START = CoreConstants.DESC_TYPE2_BYTE_SIZE_MIN;

	/** The Constant WORD0_1. */
	private static final int WORD0_1 = 0;
	
	/** The Constant WORD2. */
	private static final int WORD2 = 8;
	
	/** The Constant WORD3. */
	private static final int WORD3 = 12;
	
	/** The Constant WORD5. */
	private static final int WORD5 = 20;

	/** The Constant DEFAULT_L2_TYPE. */
	private final static L2FrameType DEFAULT_L2_TYPE = L2FrameType.ETHER;

	/** The extensions. */
	private final DissectorExtension extensions;
	
	/** The buf. */
	private ByteBuffer buf;

	/** The record extensions. */
	private boolean recordExtensions = true;
	
	/** The timestamp unit. */
	private TimestampUnit timestampUnit = TimestampUnit.PCAP_MICRO;

	/** The ip 4 disable bitmask. */
	private int ip4DisableBitmask = 0;
	
	/** The ip 6 disable bitmask. */
	private int ip6DisableBitmask = 0;
	
	/** The tcp disable bitmask. */
	private int tcpDisableBitmask = 0;
	
	/** The default bitmask. */
	private int defaultBitmask = Bits.BITS_00;

	/** The dlt type. */
	private int dltType = DEFAULT_L2_TYPE.getL2FrameTypeAsInt();

	/** The timestamp. */
	private long timestamp;
	
	/** The capture length. */
	private int captureLength;
	
	/** The wire length. */
	private int wireLength;

	/** The rx port. */
	private int rxPort;
	
	/** The tx port. */
	private int txPort;
	
	/** The tx now. */
	private int txNow;
	
	/** The tx ignore. */
	private int txIgnore;
	
	/** The tx crc override. */
	private int txCrcOverride;
	
	/** The tx set clock. */
	private int txSetClock;
	
	/** The l 2 type. */
	private int l2Type;
	
	/** The hash type. */
	private int hashType;
	
	/** The record count. */
	private int recordCount;
	
	/** The hash. */
	private int hash;
	
	/** The bitmask. */
	private int bitmask;
	
	/** The record. */
	private final int[] record = new int[DESC_TYPE2_RECORD_MAX_COUNT];

	/**
	 * Instantiates a new java dissector type 2.
	 */
	Type2JavaPacketDissector() {
		this(DissectorExtension.EMPTY);
	}

	/**
	 * Instantiates a new java dissector type 2.
	 *
	 * @param context the context
	 */
	Type2JavaPacketDissector(DissectorExtension context) {
		this.extensions = context;

		reset();
	}

	/**
	 * Adds the record.
	 *
	 * @param id     the id
	 * @param offset the offset
	 * @param length the length
	 * @return true, if successful
	 */
	private boolean addRecord(int id, int offset, int length) {
		if ((recordCount == DESC_TYPE2_RECORD_MAX_COUNT) || ((offset + length) > captureLength))
			return false;

		record[recordCount++] = PackId.encodeRecord(id, offset, length);
		bitmask = PackId.bitmaskSet(bitmask, id);

		return true;
	}

	/**
	 * Update record.
	 *
	 * @param recordIndex the record index
	 * @param id          the id
	 * @param offset      the offset
	 * @param length      the length
	 * @return true, if successful
	 */
	private boolean updateRecord(int recordIndex, int id, int offset, int length) {
		if ((recordIndex >= recordCount) || ((offset + length) > captureLength))
			return false;

		record[recordIndex] = PackId.encodeRecord(id, offset, length);

		return true;
	}

	/**
	 * Calc ip version.
	 *
	 * @param versionField the version field
	 * @return the int
	 */
	private int calcIpVersion(byte versionField) {
		return (versionField >> 4) & Bits.BITS_04;
	}

	/**
	 * Calculate gre header length.
	 *
	 * @param flags the flags
	 * @return the int
	 */
	private int calculateGreHeaderLength(short flags) {
		int size = 4;

		if ((flags & GRE_BITMASK_CHKSUM_FLAG) != 0)
			size += 4;

		if ((flags & GRE_BITMASK_KEY_FLAG) != 0)
			size += 4;

		if ((flags & GRE_BITMASK_SEQ_FLAG) != 0)
			size += 4;

		return size;
	}

	/**
	 * Calculate ip 6 options length and next hop.
	 *
	 * @param offset     the offset
	 * @param nextHeader the next header
	 * @return the int
	 */
	private int calculateIp6OptionsLengthAndNextHop(int offset, int nextHeader) {
		int len = 0;
		offset += 40;

		// Calculate IPv6 header size including options
		LOOP: while (hasRemaining(offset + 2)) {

			// IPv6 options
			switch (nextHeader) {
			case IP_TYPE_IPv6_FRAGMENT_HEADER:
				nextHeader = buf.get(offset + len + 0);
				len += 8;
				break;

			case IP_TYPE_IPv6_HOP_BY_HOP:
			case IP_TYPE_IPv6_DESTINATION_OPTIONS:
			case IP_TYPE_IPv6_ROUTING_HEADER:
			case IP_TYPE_IPv6_ENCAPSULATING_SECURITY_PAYLOAD:
			case IP_TYPE_IPv6_AUTHENTICATION_HEADER: // Authentication header
			case IP_TYPE_IPv6_MOBILITY_HEADER: // Mobility header
			case IP_TYPE_IPv6_HOST_IDENTITY_PROTOCOL: // Host identity
			case IP_TYPE_IPv6_SHIM6_PROTOCOL: // Shim6 protocol
				nextHeader = buf.get(offset + len + 0);
				len += (buf.get(offset + len + 1) << 3); // (in units of 8 bytes)
				break;

			default:
				break LOOP;
			}

		} // End LOOP:

		return (nextHeader << 16) | len;
	}

	/**
	 * Descriptor length.
	 *
	 * @return the int
	 */
	private int descriptorLength() {
		return CoreConstants.DESC_TYPE2_BYTE_SIZE_MAX;
	}

	/**
	 * Destroy dissector.
	 *
	 * @see com.slytechs.protocol.descriptor.JavaPacketDissector#destroyDissector()
	 */
	@Override
	protected void destroyDissector() {
		// Nothing to destroy with type1
	}

	/**
	 * Dissect eth type.
	 *
	 * @param offset the offset
	 */
	private void dissectEthType(int offset) {
		if (!hasRemaining(offset, ETHER_FIELD_LEN_TYPE))
			return;

		int type = Short.toUnsignedInt(buf.getShort(offset + 0)); // Offset is already at Ether 'type' field
		offset += ETHER_FIELD_LEN_TYPE;

		if (type > ETHER_MIN_VALUE_FOR_TYPE) {
			// Ethernet2 frame type

			addRecord(CoreIdTable.CORE_ID_ETHER, 0, CoreConstants.ETHER_HEADER_LEN);
			l2Type = L2FrameType.L2_FRAME_TYPE_ETHER;

		} else if (hasRemaining(offset, LLC_HEADER_LEN)) { // Nope 802.3 frame
			// THE 802.2 LOGICAL LINK CONTROL (LLC) HEADER

			addRecord(CoreIdTable.CORE_ID_LLC, offset, CoreConstants.LLC_HEADER_LEN);
			l2Type = L2FrameType.L2_FRAME_TYPE_LLC;
			int dsap = Byte.toUnsignedInt(buf.get(offset + LLC_FIELD_DSAP));
			int ssap = Byte.toUnsignedInt(buf.get(offset + LLC_FIELD_SSAP));
			int control = Byte.toUnsignedInt(buf.get(offset + LLC_FIELD_CONTROL));

			offset += LLC_HEADER_LEN;

			if ((control == LLC_TYPE_FRAME)
					&& (ssap == LLC_TYPE_SNAP)
					&& (dsap == LLC_TYPE_SNAP)
					&& hasRemaining(offset, SNAP_HEADER_LEN)) { // Snap + Frame = SNAP
				// THE SUB-NETWORK ACCESS PROTOCOL (SNAP) HEADER

				addRecord(CoreIdTable.CORE_ID_SNAP, offset, CoreConstants.SNAP_HEADER_LEN);
				l2Type = L2FrameType.L2_FRAME_TYPE_SNAP;
				type = buf.getShort(offset + SNAP_FIELD_TYPE);
				int oui = buf.getInt(offset + SNAP_FIELD_OUI) >> 8;
				if (oui != SNAP_TYPE_ETHER) // A pure SNAP frame
					return;

				offset += SNAP_HEADER_LEN;

			} else if ((control == LLC_TYPE_FRAME)
					&& (dsap == LLC_TYPE_NETWARE)
					&& (ssap == LLC_TYPE_NETWARE)
					&& hasRemaining(offset, IPX_HEADER_LEN)) { // Netware + Frame = IPX
				// Internetwork Packet Exchange
				dissectIpx(offset);
				return;

			} else {
				return; // Its a pure LLC frame, no protocols

			}
		} else
			return;

		switch (type) {
		case ETHER_TYPE_IPv4:
		case ETHER_TYPE_IPv6:
			dissectIp(offset);

			break;

		case ETHER_TYPE_VLAN:
			if (addRecord(CoreIdTable.CORE_ID_VLAN, offset, VLAN_HEADER_LEN)) {
				offset += VLAN_FIELD_LEN_TCI; // Tag Control Information (2 bytes)
				dissectEthType(offset);
			}

			break;

		case ETHER_TYPE_IPX:
			dissectIpx(offset);

			break;

		case ETHER_TYPE_MPLS:
		case ETHER_TYPE_MPLS_UPSTREAM:

			boolean bottomOfstack = false;
			do {
				int label = buf.getInt(offset);
				bottomOfstack = (label & MPLS_BITMASK_BOTTOM) != 0;

				if (!addRecord(CoreIdTable.CORE_ID_MPLS, offset, MPLS_HEADER_LEN))
					return;

				offset += MPLS_HEADER_LEN; // MPLS header length
			} while (!bottomOfstack);

			dissectIp(offset);

			break;
		}
	}

	/**
	 * Dissect ipx.
	 *
	 * @param offset the offset
	 */
	private void dissectIpx(int offset) {
		addRecord(CoreIdTable.CORE_ID_IPX, offset, CoreConstants.IPX_HEADER_LEN);
	}

	/**
	 * Dissect L 2.
	 *
	 * @param dlt    the dlt
	 * @param buf    the buf
	 * @param offset the offset
	 * @return the int
	 */
	private int dissectL2(int dlt, ByteBuffer buf, int offset) {
		int l2Type = L2FrameType.L2_FRAME_TYPE_OTHER;

		switch (dlt) { // L2 Datalink Type
		case L2FrameType.L2_FRAME_TYPE_ETHER:
			if (hasRemaining(0, CoreConstants.ETHER_HEADER_LEN)) {
				l2Type = L2FrameType.L2_FRAME_TYPE_ETHER;
				dissectEthType(ETHER_FIELD_TYPE);
			}
			break;

		case L2FrameType.L2_FRAME_TYPE_NOVELL_RAW:
			if (hasRemaining(0, CoreConstants.ETHER_HEADER_LEN)) {
				l2Type = L2FrameType.L2_FRAME_TYPE_ETHER;
				int first2bytes = buf.getShort(ETHER_HEADER_LEN);

				/*
				 * In raw mode, IPX follows immediately 802.3 header instead of LLC/SNAP but 1st
				 * two bytes must be 0xFFFF
				 */
				if (first2bytes == IPX_FIELD_VALUE_CHECKSUM)
					dissectIpx(ETHER_HEADER_LEN); // Sets L3Type to IPX...
			}
			break;

		default:
			l2Type = extensions.dissectL2(dlt, buf, offset);
		}

		return l2Type;
	}

	/**
	 * Dissect icmp 6.
	 *
	 * @param offset the offset
	 */
	private void dissectIcmp6(int offset) {
		int type = Byte.toUnsignedInt(buf.get(offset)); // type field
		int len = 0;

		switch (type) {
		case ICMP_TYPE_UNREACHABLE:
		case ICMPv6_TYPE_TIME_EXEEDED:
		case ICMPv6_TYPE_PARAMETER_PROBLEM:
			len = 8;
			if (!addRecord(CoreIdTable.CORE_ID_ICMPv6, offset, len))
				return;
			offset += len;
			dissectIp(offset);
			return;

		case ICMPv6_TYPE_ECHO_REQUEST:
		case ICMPv6_TYPE_ECHO_REPLY:
			len = 8;
			addRecord(CoreIdTable.CORE_ID_ICMPv6, offset, len);
			return;

		case ICMPv6_TYPE_ROUTER_SOLICITATION:
			len = 8;
			addRecord(CoreIdTable.CORE_ID_ICMPv6, offset, len);
			// TODO: parse options
			break;

		case ICMPv6_TYPE_ROUTER_ADVERTISEMENT:
			len = 16;
			addRecord(CoreIdTable.CORE_ID_ICMPv6, offset, len);
			// TODO: parse options
			break;

		case ICMPv6_TYPE_NEIGHBOR_SOLICITATION:
		case ICMPv6_TYPE_NEIGHBOR_ADVERTISEMENT:
			len = 24;
			addRecord(CoreIdTable.CORE_ID_ICMPv6, offset, len);
			// TODO: parse options
			break;

		case ICMPv6_TYPE_REDIRECT:
			len = 40;
			addRecord(CoreIdTable.CORE_ID_ICMPv6, offset, len);
			// TODO: parse options
			break;

		case ICMPv6_TYPE_PACKET_TOO_BIG:
		case ICMPv6_TYPE_MULTICAST_LISTENER_QUERY:
		case ICMPv6_TYPE_MULTICAST_LISTENER_REPORT:
		case ICMPv6_TYPE_MULTICAST_LISTENER_DONE:

		case ICMPv6_TYPE_ROUTER_RENUMBER:
		case ICMPv6_TYPE_NODE_INFO_QUERY:
		case ICMPv6_TYPE_NODE_INFO_RESPONSE:
		case ICMPv6_TYPE_INVERSE_NEIGHBOR_SOLICITATION:
		case ICMPv6_TYPE_INVERSE_NEIGHBOR_ADVERTISEMENT:
		case ICMPv6_TYPE_HOME_AGENT_REQUEST:
		case ICMPv6_TYPE_HOME_AGENT_REPLY:
		case ICMPv6_TYPE_MOBILE_PREFIX_SOLICITATION:
		case ICMPv6_TYPE_MOBILE_PREFIX_ADVERTISEMENT:
			len = 4;
			addRecord(CoreIdTable.CORE_ID_ICMPv6, offset, len);
			return;
		}
	}

	/**
	 * Dissect ip type.
	 *
	 * @param offset     the offset
	 * @param nextHeader the next header
	 */
	private void dissectIpType(int offset, int nextHeader) {
		int r0, len;

		EXIT: while (nextHeader != -1) {
			switch (nextHeader) {
			case IP_TYPE_ICMPv4:
				addRecord(CoreIdTable.CORE_ID_ICMPv4, offset, CoreConstants.ICMPv4_HEADER_LEN);

				break EXIT;

			case IP_TYPE_IPv4_IN_IP:
				dissectIp(offset);

				break EXIT;

			case IP_TYPE_TCP:
				if (hasRemaining(offset, CoreConstants.TCP_HEADER_LEN)) {
					r0 = buf.get(offset + CoreConstants.TCP_FIELD_IHL);
					len = ((r0 >> 4) & Bits.BITS_04) << 2;

					addRecord(CoreIdTable.CORE_ID_TCP, offset, len);
				}

				break EXIT;

			case IP_TYPE_UDP:
				addRecord(CoreIdTable.CORE_ID_UDP, offset, CoreConstants.UDP_HEADER_LEN);

				break EXIT;

			case IP_TYPE_IPv6_IN_IP: // IPv6
				dissectIp(offset);

				break EXIT;

			case IP_TYPE_GRE:
				if (hasRemaining(offset, 2)) {
					r0 = buf.getShort(offset + 0);
					len = calculateGreHeaderLength((short) r0);

					addRecord(CoreIdTable.CORE_ID_GRE, offset, len);
				}
				break EXIT;

			case IP_TYPE_SCTP:
				addRecord(CoreIdTable.CORE_ID_SCTP, offset, CoreConstants.SCTP_HEADER_LEN);
				break EXIT;

			case IP_TYPE_ICMPv6:
				dissectIcmp6(offset);
				break EXIT;

			case IP_TYPE_NO_NEXT:
				return;

			default:
				break EXIT;
			}
		}

	}

	/**
	 * Check bitmask.
	 *
	 * @param mask the mask
	 * @param id   the id
	 * @return true, if successful
	 */
	private boolean checkBitmask(int mask, int id) {
		int index = PackId.decodeRecordOrdinal(id);

		return (mask & (1 << index)) != 0;
	}

	/**
	 * Dissect ip 4 options.
	 *
	 * @param offset     the offset
	 * @param hlen       the hlen
	 * @param nextHeader the next header
	 */
	private void dissectIp4Options(int offset, int hlen, int nextHeader) {
		int l4Offset = offset + hlen;

		/* check if any options are present */
		if (hlen > IPv4_HEADER_LEN) {
			offset += IPv4_HEADER_LEN; // Align at start of Ip4 options

			while (offset < l4Offset) {
				int type = Byte.toUnsignedInt(buf.get(offset + 0)); // option type
				int len = buf.get(offset + 1); // option length

				int id = 0;
				switch (type) {
				case Ip4OptionInfo.IPv4_OPTION_TYPE_EOOL:
				case Ip4OptionInfo.IPv4_OPTION_TYPE_NOP:
					len = 1;

					// fall through for all options
				default:
					id = Ip4OptionInfo.mapKindToId(type);
				}

				if (recordExtensions && !checkBitmask(ip4DisableBitmask, id))
					addRecord(id, offset, len);
				offset += len;
			}
		}

		dissectIpType(l4Offset, nextHeader);
	}

	/**
	 * Dissect ip 6 options.
	 *
	 * @param offset     the offset
	 * @param nextHeader the next header
	 */
	private void dissectIp6Options(int offset, int nextHeader) {
		final int ip6RecordIndex = recordCount;
		final int ip6OffsetStart = offset;

		if (!addRecord(CoreIdTable.CORE_ID_IPv6, offset, IPv6_HEADER_LEN))
			return;

		int extLen = 0;
		offset += IPv6_HEADER_LEN;

		// Calculate IPv6 header size including options
		LOOP: while (hasRemaining(offset + 2)) {

			// IPv6 options
			switch (nextHeader) {
			case IP_TYPE_IPv6_FRAGMENT_HEADER:
			case IP_TYPE_IPv6_HOP_BY_HOP:
			case IP_TYPE_IPv6_DESTINATION_OPTIONS:
			case IP_TYPE_IPv6_ROUTING_HEADER:
			case IP_TYPE_IPv6_ENCAPSULATING_SECURITY_PAYLOAD:
			case IP_TYPE_IPv6_AUTHENTICATION_HEADER: // Authentication header
			case IP_TYPE_IPv6_MOBILITY_HEADER: // Mobility header
			case IP_TYPE_IPv6_HOST_IDENTITY_PROTOCOL: // Host identity
			case IP_TYPE_IPv6_SHIM6_PROTOCOL: // Shim6 protocol
				nextHeader = buf.get(offset + extLen + 0);
				int len = (buf.get(offset + extLen + 1) << 3) + 8; // (in units of 8 bytes)

				int id = Ip6OptionInfo.mapTypeToId(nextHeader);

				if (recordExtensions && !checkBitmask(ip4DisableBitmask, id))
					addRecord(id, offset, len);

				extLen += len;
				offset += len;
				break;

			case IP_TYPE_NO_NEXT: // No Next Header
			default:
				break LOOP;
			}

		} // End LOOP:

		if ((extLen > 0) && !updateRecord(ip6RecordIndex, CoreIdTable.CORE_ID_IPv6,
				ip6OffsetStart, IPv6_HEADER_LEN + extLen))
			return;

		dissectIpType(offset, nextHeader);
	}

	/**
	 * Dissect ip.
	 *
	 * @param offset the offset
	 */
	private void dissectIp(int offset) {
		if (!hasRemaining(offset, 1))
			return;

		int r0 = buf.get(offset + IPv4_FIELD_VER); // 07:00 IP header len & version
		int ver = calcIpVersion((byte) r0); // Common to IPv4 and IPv6

		int nextHeader = -1;
		int len;

		if ((ver == 4) && hasRemaining(offset, IPv4_HEADER_LEN)) {
			len = ((r0 >> 0) & Bits.BITS_04) << 2;
			nextHeader = buf.get(offset + IPv4_FIELD_PROTOCOL);

			if (!addRecord(CoreIdTable.CORE_ID_IPv4, offset, len))
				return;

			dissectIp4Options(offset, len, nextHeader);

		} else if (hasRemaining(offset, IPv6_HEADER_LEN)) {
			nextHeader = Byte.toUnsignedInt(buf.get(offset + IPv6_FIELD_NEXT_HOP));
			dissectIp6Options(offset, nextHeader);
		}
	}

	/**
	 * Dissect packet.
	 *
	 * @param buffer        the buffer
	 * @param timestamp     the timestamp
	 * @param captureLength the capture length
	 * @param wireLength    the wire length
	 * @return the int
	 * @see com.slytechs.protocol.descriptor.PacketDissector#dissectPacket(java.nio.ByteBuffer,
	 *      long, int, int)
	 */
	@Override
	public int dissectPacket(ByteBuffer buffer, long timestamp, int captureLength, int wireLength) {
		this.buf = buffer;
		this.timestamp = timestamp;
		this.captureLength = captureLength;
		this.wireLength = wireLength;

		this.l2Type = dissectL2(dltType, buf, 0);

		/*
		 * Advance the position manually, since we only reference the buffer data in
		 * absolute mode.
		 */
		buffer.position(buffer.position() + captureLength);

		return captureLength;
	}

	/**
	 * Checks for remaining.
	 *
	 * @param offset the offset
	 * @return true, if successful
	 */
	private boolean hasRemaining(int offset) {
		return offset <= captureLength;
	}

	/**
	 * Checks for remaining.
	 *
	 * @param offset the offset
	 * @param length the length
	 * @return true, if successful
	 */
	private boolean hasRemaining(int offset, int length) {
		return (offset + length) <= captureLength;
	}

	/**
	 * Reset.
	 *
	 * @return the packet dissector
	 * @see com.slytechs.protocol.descriptor.PacketDissector#reset()
	 */
	@Override
	public PacketDissector reset() {
		timestamp = captureLength = wireLength = 0;
		hash = hashType = l2Type = 0;
		rxPort = txPort = 0;
		txNow = txIgnore = txCrcOverride = txSetClock = 0;

		recordCount = 0;
		bitmask = defaultBitmask;

		return this;
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
		this.dltType = l2Type.getL2FrameTypeAsInt();

		return this;
	}

	/**
	 * Sets the hash.
	 *
	 * @param hash     the hash
	 * @param hashType the hash type
	 * @return the java dissector type 2
	 */
	public Type2JavaPacketDissector setHash(int hash, int hashType) {
		this.hashType = hashType;
		this.hash = hash;

		return this;
	}

	/**
	 * Write descriptor.
	 *
	 * @param desc the desc
	 * @return the int
	 * @see com.slytechs.protocol.descriptor.PacketDissector#writeDescriptor(java.nio.ByteBuffer)
	 */
	@Override
	public int writeDescriptor(ByteBuffer desc) {
		int len = writeDescriptorUsingLayout(desc);

		desc.position(desc.position() + len);

		return len;
	}

	/**
	 * Write descriptor using layout.
	 *
	 * @param desc the desc
	 * @return the int
	 */
	public final int writeDescriptorUsingLayout(ByteBuffer desc) {
		TIMESTAMP.setLong(timestamp, desc);

		CAPLEN.setInt(captureLength, desc);
		RX_PORT.setInt(rxPort, desc);
		TX_PORT.setInt(txPort, desc);

		WIRELEN.setInt(wireLength, desc);
		TX_NOW.setInt(txNow, desc);
		TX_IGNORE.setInt(txIgnore, desc);
		TX_CRC_OVERRIDE.setInt(txCrcOverride, desc);
		TX_SET_CLOCK.setInt(txSetClock, desc);
		L2_TYPE.setInt(l2Type, desc);
		HASH_TYPE.setInt(hashType, desc);
		RECORD_COUNT.setInt(recordCount, desc);

		HASH24.setInt(hash, desc);
		BITMASK.setInt(bitmask, desc);

		for (int i = 0, j = 24; i < recordCount; i++, j += 4)
			desc.putInt(j, record[i]);

		return descriptorLength();
	}

	/**
	 * Write descriptor fast path.
	 *
	 * @param desc the desc
	 * @return the int
	 */
	public final int writeDescriptorFastPath(ByteBuffer desc) {
		final boolean big = (desc.order() == ByteOrder.BIG_ENDIAN);

		// Struct/Layout class has the private encoders we utilize here
		int word2 = big
				? Type2DescriptorLayout
						.encodeWord2BE(captureLength, rxPort, txPort)
				: Type2DescriptorLayout
						.encodeWord2LE(captureLength, rxPort, txPort);

		int word3 = big
				? Type2DescriptorLayout
						.encodeWord3BE(wireLength, txNow, txIgnore, txCrcOverride, txSetClock, l2Type, 0, recordCount)
				: Type2DescriptorLayout
						.encodeWord3LE(wireLength, txNow, txIgnore, txCrcOverride, txSetClock, l2Type, 0, recordCount);

		// @formatter:off
		desc.putLong(WORD0_1, timestamp) // 07-00 Word0&1
				.putInt(WORD2, word2)    // 11-08 Word2
				.putInt(WORD3, word3)    // 15-12 Word3
				                         // 19-16 Word4 hash/color2 which is not set by the dissector
				.putInt(WORD5, bitmask); // 23-20 Word5 recorded protocol bitmask (1 bit per proto)
		// @formatter:on

		for (int i = 0, j = RECORD_START; i < recordCount; i++, j += 4)
			desc.putInt(j, record[i]); // 152-24 (up to 152 bytes eq. (32 * 4) + 24)

		return descriptorLength();
	}

	/**
	 * Disable bitmask recording.
	 *
	 * @return the java dissector type 2
	 */
	public Type2JavaPacketDissector disableBitmaskRecording() {
		// Turning on all bits, effectively disables bitmask checks
		bitmask = defaultBitmask = Bits.BITS_32;

		return this;
	}

	/**
	 * Disable extension recording for all.
	 *
	 * @return the java dissector type 2
	 */
	public Type2JavaPacketDissector disableExtensionRecordingForAll() {
		this.recordExtensions = false;

		return this;
	}

	/**
	 * Disable extension recording for.
	 *
	 * @param protocolId   the protocol id
	 * @param extentionIds the extention ids
	 * @return the java dissector type 2
	 */
	public Type2JavaPacketDissector disableExtensionRecordingFor(
			CoreIdTable protocolId, HeaderExtensionInfo... extentionIds) {

		return disableExtensionRecordingInCoreProtocol(protocolId.id(),
				Arrays.stream(extentionIds)
						.mapToInt(HeaderExtensionInfo::id)
						.toArray());
	}

	/**
	 * Disable extension recording in core protocol.
	 *
	 * @param protocolId   the protocol id
	 * @param extensionIds the extension ids
	 * @return the java dissector type 2
	 */
	public Type2JavaPacketDissector disableExtensionRecordingInCoreProtocol(int protocolId, int... extensionIds) {

		if (extensionIds.length > 0) {
			IntConsumer bitmaskSet = switch (protocolId) {
			case CoreIdTable.CORE_ID_IPv4 -> id -> ip4DisableBitmask = PackId.bitmaskSet(ip4DisableBitmask, id);
			case CoreIdTable.CORE_ID_IPv6 -> id -> ip6DisableBitmask = PackId.bitmaskSet(ip6DisableBitmask, id);
			case CoreIdTable.CORE_ID_TCP -> id -> tcpDisableBitmask = PackId.bitmaskSet(tcpDisableBitmask, id);
			default -> id -> {};
			};

			for (int extId : extensionIds)
				bitmaskSet.accept(extId);

		} else {
			/* If no specifics extensions specified, disable them all */
			switch (protocolId) {
			case CoreIdTable.CORE_ID_IPv4 -> ip4DisableBitmask = Bits.BITS_32;
			case CoreIdTable.CORE_ID_IPv6 -> ip6DisableBitmask = Bits.BITS_32;
			case CoreIdTable.CORE_ID_TCP -> tcpDisableBitmask = Bits.BITS_32;
			};
		}

		return this;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JavaDissectorType2 [dltType=" + dltType + ", timestamp=" + timestamp + ", captureLength="
				+ captureLength + ", wireLength=" + wireLength + ", rxPort=" + rxPort + ", txPort=" + txPort
				+ ", txNow=" + txNow + ", txIgnore=" + txIgnore + ", txCrcOverride=" + txCrcOverride + ", txSetClock="
				+ txSetClock + ", l2Type=" + l2Type + ", hashType=" + hashType + ", recordCount=" + recordCount
				+ ", hash=" + hash + ", bitmask=" + bitmask + "]";
	}

}
