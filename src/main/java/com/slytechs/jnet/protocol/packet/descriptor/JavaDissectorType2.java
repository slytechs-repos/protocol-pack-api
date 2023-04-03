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

import static com.slytechs.jnet.protocol.constants.CoreConstants.*;
import static com.slytechs.jnet.protocol.packet.descriptor.Type2Layout.*;

import java.net.ProtocolException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.function.IntConsumer;

import com.slytechs.jnet.protocol.HeaderId;
import com.slytechs.jnet.protocol.constants.CoreConstants;
import com.slytechs.jnet.protocol.constants.CoreHeaderInfo;
import com.slytechs.jnet.protocol.constants.Ip4OptionInfo;
import com.slytechs.jnet.protocol.constants.Ip6OptionInfo;
import com.slytechs.jnet.protocol.constants.L2FrameType;
import com.slytechs.jnet.protocol.packet.HeaderExtensionInfo;
import com.slytechs.jnet.runtime.time.TimestampUnit;
import com.slytechs.jnet.runtime.util.Bits;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class JavaDissectorType2 extends JavaDissector {

	private static final int RECORD_START = CoreConstants.DESC_TYPE2_BYTE_SIZE_MIN;

	private static final int WORD0_1 = 0;
	private static final int WORD2 = 8;
	private static final int WORD3 = 12;
	private static final int WORD5 = 20;

	private final static L2FrameType DEFAULT_L2_TYPE = L2FrameType.ETHER;

	private final DissectorExtension extensions;
	private ByteBuffer buf;

	private boolean recordExtensions = true;
	private TimestampUnit timestampUnit = TimestampUnit.PCAP_MICRO;

	private int ip4DisableBitmask = 0;
	private int ip6DisableBitmask = 0;
	private int tcpDisableBitmask = 0;
	private int defaultBitmask = Bits.BITS_00;

	private int dltType = DEFAULT_L2_TYPE.getL2FrameTypeAsInt();

	private long timestamp;
	private int captureLength;
	private int wireLength;

	private int rxPort;
	private int txPort;
	private int txNow;
	private int txIgnore;
	private int txCrcOverride;
	private int txSetClock;
	private int l2Type;
	private int hashType;
	private int recordCount;
	private int hash;
	private int bitmask;
	private final int[] record = new int[DESC_TYPE2_RECORD_MAX_COUNT];

	JavaDissectorType2() {
		this(DissectorExtension.EMPTY);
	}

	JavaDissectorType2(DissectorExtension context) {
		this.extensions = context;

		reset();
	}

	private boolean addRecord(int id, int offset, int length) {
		if ((recordCount == DESC_TYPE2_RECORD_MAX_COUNT) || ((offset + length) > captureLength))
			return false;

		record[recordCount++] = HeaderId.encodeRecord(id, offset, length);
		bitmask = HeaderId.bitmaskSet(bitmask, id);

		return true;
	}

	private boolean updateRecord(int recordIndex, int id, int offset, int length) {
		if ((recordIndex >= recordCount) || ((offset + length) > captureLength))
			return false;

		record[recordIndex] = HeaderId.encodeRecord(id, offset, length);

		return true;
	}

	private int calcIpVersion(byte versionField) {
		return (versionField >> 4) & Bits.BITS_04;
	}

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

	private int descriptorLength() {
		return CoreConstants.DESC_TYPE2_BYTE_SIZE_MAX;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.descriptor.JavaDissector#destroyDissector()
	 */
	@Override
	protected void destroyDissector() {
		// Nothing to destroy with type1
	}

	private void dissectEthType(int offset) {
		if (!hasRemaining(offset, ETHER_FIELD_LEN_TYPE))
			return;

		int type = Short.toUnsignedInt(buf.getShort(offset + 0)); // Offset is already at Ether 'type' field
		offset += ETHER_FIELD_LEN_TYPE;

		if (type > ETHER_MIN_VALUE_FOR_TYPE) {
			// Ethernet2 frame type

			addRecord(CoreHeaderInfo.CORE_ID_ETHER, 0, CoreConstants.ETHER_HEADER_LEN);
			l2Type = L2FrameType.L2_FRAME_TYPE_ETHER;

		} else if (hasRemaining(offset, LLC_HEADER_LEN)) { // Nope 802.3 frame
			// THE 802.2 LOGICAL LINK CONTROL (LLC) HEADER

			addRecord(CoreHeaderInfo.CORE_ID_LLC, offset, CoreConstants.LLC_HEADER_LEN);
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

				addRecord(CoreHeaderInfo.CORE_ID_SNAP, offset, CoreConstants.SNAP_HEADER_LEN);
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
			if (addRecord(CoreHeaderInfo.CORE_ID_VLAN, offset, VLAN_HEADER_LEN)) {
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

				if (!addRecord(CoreHeaderInfo.CORE_ID_MPLS, offset, MPLS_HEADER_LEN))
					return;

				offset += MPLS_HEADER_LEN; // MPLS header length
			} while (!bottomOfstack);

			dissectIp(offset);

			break;
		}
	}

	private void dissectIpx(int offset) {
		addRecord(CoreHeaderInfo.CORE_ID_IPX, offset, CoreConstants.IPX_HEADER_LEN);
	}

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

	private void dissectIcmp6(int offset) {
		int type = Byte.toUnsignedInt(buf.get(offset)); // type field
		int len = 0;

		switch (type) {
		case ICMP_TYPE_UNREACHABLE:
		case ICMPv6_TYPE_TIME_EXEEDED:
		case ICMPv6_TYPE_PARAMETER_PROBLEM:
			len = 8;
			if (!addRecord(CoreHeaderInfo.CORE_ID_ICMPv6, offset, len))
				return;
			offset += len;
			dissectIp(offset);
			return;

		case ICMPv6_TYPE_ECHO_REQUEST:
		case ICMPv6_TYPE_ECHO_REPLY:
			len = 8;
			addRecord(CoreHeaderInfo.CORE_ID_ICMPv6, offset, len);
			return;

		case ICMPv6_TYPE_ROUTER_SOLICITATION:
			len = 8;
			addRecord(CoreHeaderInfo.CORE_ID_ICMPv6, offset, len);
			// TODO: parse options
			break;

		case ICMPv6_TYPE_ROUTER_ADVERTISEMENT:
			len = 16;
			addRecord(CoreHeaderInfo.CORE_ID_ICMPv6, offset, len);
			// TODO: parse options
			break;

		case ICMPv6_TYPE_NEIGHBOR_SOLICITATION:
		case ICMPv6_TYPE_NEIGHBOR_ADVERTISEMENT:
			len = 24;
			addRecord(CoreHeaderInfo.CORE_ID_ICMPv6, offset, len);
			// TODO: parse options
			break;

		case ICMPv6_TYPE_REDIRECT:
			len = 40;
			addRecord(CoreHeaderInfo.CORE_ID_ICMPv6, offset, len);
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
			addRecord(CoreHeaderInfo.CORE_ID_ICMPv6, offset, len);
			return;
		}
	}

	private void dissectIpType(int offset, int nextHeader) {
		int r0, len;

		EXIT: while (nextHeader != -1) {
			switch (nextHeader) {
			case IP_TYPE_ICMPv4:
				addRecord(CoreHeaderInfo.CORE_ID_ICMPv4, offset, CoreConstants.ICMPv4_HEADER_LEN);

				break EXIT;

			case IP_TYPE_IPv4_IN_IP:
				dissectIp(offset);

				break EXIT;

			case IP_TYPE_TCP:
				if (hasRemaining(offset, CoreConstants.TCP_HEADER_LEN)) {
					r0 = buf.get(offset + CoreConstants.TCP_FIELD_IHL);
					len = ((r0 >> 4) & Bits.BITS_04) << 2;

					addRecord(CoreHeaderInfo.CORE_ID_TCP, offset, len);
				}

				break EXIT;

			case IP_TYPE_UDP:
				addRecord(CoreHeaderInfo.CORE_ID_UDP, offset, CoreConstants.UDP_HEADER_LEN);

				break EXIT;

			case IP_TYPE_IPv6_IN_IP: // IPv6
				dissectIp(offset);

				break EXIT;

			case IP_TYPE_GRE:
				if (hasRemaining(offset, 2)) {
					r0 = buf.getShort(offset + 0);
					len = calculateGreHeaderLength((short) r0);

					addRecord(CoreHeaderInfo.CORE_ID_GRE, offset, len);
				}
				break EXIT;

			case IP_TYPE_SCTP:
				addRecord(CoreHeaderInfo.CORE_ID_SCTP, offset, CoreConstants.SCTP_HEADER_LEN);
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

	private boolean checkBitmask(int mask, int id) {
		int index = HeaderId.decodeRecordOrdinal(id);

		return (mask & (1 << index)) != 0;
	}

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

	private void dissectIp6Options(int offset, int nextHeader) {
		final int ip6RecordIndex = recordCount;
		final int ip6OffsetStart = offset;

		if (!addRecord(CoreHeaderInfo.CORE_ID_IPv6, offset, IPv6_HEADER_LEN))
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

		if ((extLen > 0) && !updateRecord(ip6RecordIndex, CoreHeaderInfo.CORE_ID_IPv6,
				ip6OffsetStart, IPv6_HEADER_LEN + extLen))
			return;

		dissectIpType(offset, nextHeader);
	}

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

			if (!addRecord(CoreHeaderInfo.CORE_ID_IPv4, offset, len))
				return;

			dissectIp4Options(offset, len, nextHeader);

		} else if (hasRemaining(offset, IPv6_HEADER_LEN)) {
			nextHeader = Byte.toUnsignedInt(buf.get(offset + IPv6_FIELD_NEXT_HOP));
			dissectIp6Options(offset, nextHeader);
		}
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDissector#dissectPacket(java.nio.ByteBuffer,
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

	private boolean hasRemaining(int offset) {
		return offset <= captureLength;
	}

	private boolean hasRemaining(int offset, int length) {
		return (offset + length) <= captureLength;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDissector#reset()
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
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDissector#setDatalinkType(com.slytechs.jnet.protocol.constants.L2FrameType)
	 */
	@Override
	public PacketDissector setDatalinkType(L2FrameType l2Type) throws ProtocolException {
		this.dltType = l2Type.getL2FrameTypeAsInt();

		return this;
	}

	public JavaDissectorType2 setHash(int hash, int hashType) {
		this.hashType = hashType;
		this.hash = hash;

		return this;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.descriptor.PacketDissector#writeDescriptor(java.nio.ByteBuffer)
	 */
	@Override
	public int writeDescriptor(ByteBuffer desc) {
		int len = writeDescriptorUsingLayout(desc);

		desc.position(desc.position() + len);

		return len;
	}

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

	public final int writeDescriptorFastPath(ByteBuffer desc) {
		final boolean big = (desc.order() == ByteOrder.BIG_ENDIAN);

		// Struct/Layout class has the private encoders we utilize here
		int word2 = big
				? Type2Layout
						.encodeWord2BE(captureLength, rxPort, txPort)
				: Type2Layout
						.encodeWord2LE(captureLength, rxPort, txPort);

		int word3 = big
				? Type2Layout
						.encodeWord3BE(wireLength, txNow, txIgnore, txCrcOverride, txSetClock, l2Type, 0, recordCount)
				: Type2Layout
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

	public JavaDissectorType2 disableBitmaskRecording() {
		// Turning on all bits, effectively disables bitmask checks
		bitmask = defaultBitmask = Bits.BITS_32;

		return this;
	}

	public JavaDissectorType2 disableExtensionRecordingForAll() {
		this.recordExtensions = false;

		return this;
	}

	public JavaDissectorType2 disableExtensionRecordingFor(
			CoreHeaderInfo protocolId, HeaderExtensionInfo... extentionIds) {

		return disableExtensionRecordingInCoreProtocol(protocolId.getHeaderId(),
				Arrays.stream(extentionIds)
						.mapToInt(HeaderExtensionInfo::getHeaderId)
						.toArray());
	}

	public JavaDissectorType2 disableExtensionRecordingInCoreProtocol(int protocolId, int... extensionIds) {

		if (extensionIds.length > 0) {
			IntConsumer bitmaskSet = switch (protocolId) {
			case CoreHeaderInfo.CORE_ID_IPv4 -> id -> ip4DisableBitmask = HeaderId.bitmaskSet(ip4DisableBitmask, id);
			case CoreHeaderInfo.CORE_ID_IPv6 -> id -> ip6DisableBitmask = HeaderId.bitmaskSet(ip6DisableBitmask, id);
			case CoreHeaderInfo.CORE_ID_TCP -> id -> tcpDisableBitmask = HeaderId.bitmaskSet(tcpDisableBitmask, id);
			default -> id -> {};
			};

			for (int extId : extensionIds)
				bitmaskSet.accept(extId);

		} else {
			/* If no specifics extensions specified, disable them all */
			switch (protocolId) {
			case CoreHeaderInfo.CORE_ID_IPv4 -> ip4DisableBitmask = Bits.BITS_32;
			case CoreHeaderInfo.CORE_ID_IPv6 -> ip6DisableBitmask = Bits.BITS_32;
			case CoreHeaderInfo.CORE_ID_TCP -> tcpDisableBitmask = Bits.BITS_32;
			};
		}

		return this;
	}

	/**
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
