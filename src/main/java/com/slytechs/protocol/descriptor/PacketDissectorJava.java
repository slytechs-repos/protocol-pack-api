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

import static com.slytechs.protocol.pack.core.constants.CoreConstants.*;

import java.net.ProtocolException;
import java.nio.ByteBuffer;

import com.slytechs.protocol.pack.core.constants.CoreConstants;
import com.slytechs.protocol.pack.core.constants.CoreIdTable;
import com.slytechs.protocol.pack.core.constants.L2FrameType;
import com.slytechs.protocol.pack.core.constants.L3FrameType;
import com.slytechs.protocol.pack.core.constants.L4FrameType;
import com.slytechs.protocol.runtime.time.TimestampUnit;
import com.slytechs.protocol.runtime.util.Bits;

/**
 * The Class JavaDissector.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
abstract class PacketDissectorJava extends AbstractPacketDissector {

	protected static final int NO_NEXT_HEADER = -1;

	/** The Constant DEFAULT_L2_TYPE. */
	protected final static L2FrameType DEFAULT_L2_TYPE = L2FrameType.ETHER;

	/** The timestamp unit. */
	protected TimestampUnit timestampUnit = TimestampUnit.PCAP_MICRO;

	protected ByteBuffer buf;
	protected long timestamp;
	protected int captureLength;
	protected int wireLength;
	protected int dltType = DEFAULT_L2_TYPE.getAsInt();
	protected int l2Type;
	protected int l3Type;
	protected int l3Offset;
	protected int l3Size;
	protected boolean l3IsFrag;
	protected boolean l3LastFrag;
	protected int l4Type;
	protected int l4Size;
	protected int vlanCount;
	protected int mplsCount;

	/**
	 * Instantiates a new java dissector.
	 */
	protected PacketDissectorJava() {
	}

	protected abstract boolean addRecord(int id, int offset, int length);

	/**
	 * Calc ip version.
	 *
	 * @param versionField the version field
	 * @return the int
	 */
	protected final int calcIpVersion(byte versionField) {
		return (versionField >> 4) & Bits.BITS_04;
	}

	/**
	 * Destroy dissector.
	 */
	protected abstract void destroyDissector();

	protected final void dissectEthernet(int offset) {

		if (!hasRemaining(offset, ETHER_HEADER_LEN))
			return;

		int type = Short.toUnsignedInt(buf.getShort(offset + ETHER_FIELD_TYPE));

		if (type > ETHER_MIN_VALUE_FOR_TYPE) {
			// Ethernet2 frame type

			addRecord(CoreIdTable.CORE_ID_ETHER, offset, ETHER_HEADER_LEN);
			l2Type = L2FrameType.L2_FRAME_TYPE_ETHER;
			offset += ETHER_HEADER_LEN;

			dissectEthType(offset, type);

		} else if (hasRemaining(offset, LLC_HEADER_LEN)) { // Nope 802.3 frame
			// THE 802.2 LOGICAL LINK CONTROL (LLC) HEADER

			offset += ETHER_HEADER_LEN;
			addRecord(CoreIdTable.CORE_ID_LLC, offset, LLC_HEADER_LEN);
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

				addRecord(CoreIdTable.CORE_ID_SNAP, offset, SNAP_HEADER_LEN);
				l2Type = L2FrameType.L2_FRAME_TYPE_SNAP;
				type = buf.getShort(offset + SNAP_FIELD_TYPE);

				offset += SNAP_HEADER_LEN;

				dissectEthType(offset, type);

			} else if ((control == LLC_TYPE_FRAME)
					&& (dsap == LLC_TYPE_NETWARE)
					&& (ssap == LLC_TYPE_NETWARE)
					&& hasRemaining(offset, IPX_HEADER_LEN)) {

				// Internetwork Packet Exchange
				dissectIpx(offset);

			} else if ((control == LLC_TYPE_FRAME)
					&& (dsap == LLC_TYPE_STP)
					&& (ssap == LLC_TYPE_STP)
					&& hasRemaining(offset, STP_HEADER_LEN)) {

				addRecord(CoreIdTable.CORE_ID_STP, offset, STP_HEADER_LEN);
			} else {
				return; // Its a pure LLC frame, no protocols

			}
		}
	}

	/**
	 * Dissect eth type.
	 *
	 * @param offset the offset
	 * @param type   the type
	 */
	protected final void dissectEthType(int offset, int type) {

		switch (type) {
		case ETHER_TYPE_IPv4:
		case ETHER_TYPE_IPv6:
			dissectIp(offset);

			break;

		case ETHER_TYPE_VLAN:
			if (addRecord(CoreIdTable.CORE_ID_VLAN, offset, VLAN_HEADER_LEN)) {
				this.vlanCount++;

				type = Short.toUnsignedInt(buf.getShort(offset + VLAN_FIELD_TYPE));
				offset += VLAN_HEADER_LEN;
				dissectEthType(offset, type);
			}

			break;

		case ETHER_TYPE_IPX:
			dissectIpx(offset);

			break;

		case ETHER_TYPE_MPLS:
		case ETHER_TYPE_MPLS_UPSTREAM:

			boolean bottomOfstack = false;
			do {
				this.mplsCount++;

				int label = buf.getInt(offset);
				bottomOfstack = (label & MPLS_BITMASK_BOTTOM) != 0;

				if (!addRecord(CoreIdTable.CORE_ID_MPLS, offset, MPLS_HEADER_LEN))
					return;

				offset += MPLS_HEADER_LEN; // MPLS header length
			} while (!bottomOfstack);

			dissectIp(offset);

			break;

		case ETHER_TYPE_ARP:
		case ETHER_TYPE_RARP:
			addRecord(CoreIdTable.CORE_ID_ARP, offset, ARP_HEADER_LEN);
			break;

		default:
			dissectExtensionType(buf, offset, CoreIdTable.CORE_ID_ETHER, type);
			break;
		}
	}

	protected abstract void dissectExtensionPorts(ByteBuffer buf, int offset, int id, int src, int dst);

	protected abstract void dissectExtensionType(ByteBuffer buf, int offset, int id, int nextHeader);

	protected abstract void dissectGre(int offset);

	protected abstract void dissectIcmp4(int offset);

	protected abstract void dissectIcmp6(int offset);

	/**
	 * Dissect ip.
	 *
	 * @param l3Offset the offset
	 */
	protected final void dissectIp(int offset) {
		if (!hasRemaining(offset, 1))
			return;

		this.l3Offset = offset;

		int r0 = Byte.toUnsignedInt(buf.get(l3Offset + IPv4_FIELD_VER)); // 07:00 IP header len & version
		int ver = calcIpVersion((byte) r0); // Common to IPv4 and IPv6

		int nextHeader = NO_NEXT_HEADER;

		if ((ver == 4) && hasRemaining(l3Offset, IPv4_HEADER_LEN)) {
			this.l3Type = L3FrameType.L3_FRAME_TYPE_IPv4;
			this.l3Size = ((r0 >> 0) & Bits.BITS_04);
			int len = l3Size << 2;
			nextHeader = buf.get(l3Offset + IPv4_FIELD_PROTOCOL);

			if (!addRecord(CoreIdTable.CORE_ID_IPv4, l3Offset, len))
				return;

			int sword3 = buf.getShort(l3Offset + IPv4_FIELD_FLAGS);
			boolean mf = (sword3 & IPv4_FLAG16_MF) > 0;
			int fragOff = (sword3 & IPv4_MASK16_FRAGOFF);

			this.l3IsFrag = mf || (fragOff > 0);
			this.l3LastFrag = !mf && (fragOff > 0);

			dissectIp4Options(l3Offset, len, nextHeader);

			dissectIpType(offset + len, nextHeader);

		} else if (hasRemaining(l3Offset, IPv6_HEADER_LEN)) {
			this.l3Type = L3FrameType.L3_FRAME_TYPE_IPv6;
			this.l3Size = IPv6_HEADER_LEN >> 2;

			nextHeader = Byte.toUnsignedInt(buf.get(l3Offset + IPv6_FIELD_NEXT_HOP));

			dissectIp6Options(l3Offset, nextHeader);
		}
	}

	protected abstract void dissectIp4Options(int offset, int hlen, int nextHeader);

	protected abstract void dissectIp6Options(int offset, int nextHeader);

	/**
	 * Dissect ip type.
	 *
	 * @param offset     the offset
	 * @param nextHeader the next header
	 */
	protected final void dissectIpType(int offset, int nextHeader) {

		EXIT: while (nextHeader != NO_NEXT_HEADER) {
			switch (nextHeader) {
			case IP_TYPE_ICMPv4:
				dissectIcmp4(offset);

				break EXIT;

			case IP_TYPE_IPv4_IN_IP:
				dissectIp(offset);

				break EXIT;

			case IP_TYPE_TCP:
				dissectTcp(offset);

				break EXIT;

			case IP_TYPE_UDP:
				dissectUdp(offset);

				break EXIT;

			case IP_TYPE_IPv6_IN_IP: // IPv6
				dissectIp(offset);

				break EXIT;

			case IP_TYPE_GRE:
				dissectGre(offset);

				break EXIT;

			case IP_TYPE_SCTP:
				dissectSctp(offset);

				break EXIT;

			case IP_TYPE_ICMPv6:
				dissectIcmp6(offset);

				break EXIT;

			case IP_TYPE_NO_NEXT:
				dissectSctp(offset);

				return;

			default:
				dissectExtensionType(buf, offset, CoreIdTable.CORE_ID_IPv4, nextHeader);
				break EXIT;
			}
		}

	}

	protected void dissectIpx(int offset) {
		if (!hasRemaining(offset, IPX_HEADER_LEN))
			return;

		this.l3Type = L3FrameType.L3_FRAME_TYPE_IPX;
		this.l3Offset = offset;
		this.l3Size = IPX_HEADER_LEN;
	}

	/**
	 * Dissect L 2.
	 *
	 * @param dlt    the dlt
	 * @param buf    the buf
	 * @param offset the offset
	 * @return the int
	 */
	protected final int dissectL2(int dlt, ByteBuffer buf, int offset) {
		int l2Type = L2FrameType.L2_FRAME_TYPE_OTHER;

		switch (dlt) { // L2 Datalink Type
		case L2FrameType.L2_FRAME_TYPE_ETHER:
			if (hasRemaining(offset, CoreConstants.ETHER_HEADER_LEN)) {
				l2Type = L2FrameType.L2_FRAME_TYPE_ETHER;
				dissectEthernet(offset);
			}
			break;

		case L2FrameType.L2_FRAME_TYPE_NOVELL_RAW:
			if (hasRemaining(offset, CoreConstants.ETHER_HEADER_LEN)) {
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

		}

		return l2Type;
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
		this.buf = buffer;
		this.timestamp = timestamp;
		this.captureLength = caplen;
		this.wireLength = wirelen;

		this.l2Type = dissectL2(dltType, buf, 0);

		/*
		 * Advance the position manually, since we only reference the buffer data in
		 * absolute mode.
		 */
		buffer.position(buffer.position() + captureLength);

		return captureLength;
	}

	protected abstract void dissectSctp(int offset);

	protected final void dissectTcp(int offset) {
		if (!hasRemaining(offset, TCP_HEADER_LEN))
			return;

		this.l4Type = L4FrameType.L4_FRAME_TYPE_TCP;

		int r0 = buf.get(offset + TCP_FIELD_IHL);
		this.l4Size = ((r0 >> 4) & Bits.BITS_04);
		int len = l4Size << 2;

		addRecord(CoreIdTable.CORE_ID_TCP, offset, len);

		dissectTcpOptions(offset, len);

		int src = Short.toUnsignedInt(buf.getShort(offset + TCP_FIELD_SRC));
		int dst = Short.toUnsignedInt(buf.getShort(offset + TCP_FIELD_DST));

		offset += len;

		dissectExtensionPorts(buf, offset, CoreIdTable.CORE_ID_TCP, src, dst);
	}

	protected abstract void dissectTcpOptions(int offset, int tcpHeaderLenth);

	protected abstract void dissectUdp(int offset);

	/**
	 * Checks for remaining.
	 *
	 * @param offset the offset
	 * @return true, if successful
	 */
	protected final boolean hasRemaining(int offset) {
		return offset <= captureLength;
	}

	/**
	 * Checks for remaining.
	 *
	 * @param offset the offset
	 * @param length the length
	 * @return true, if successful
	 */
	protected final boolean hasRemaining(int offset, int length) {
		return (offset + length) <= captureLength;
	}

	/**
	 * Checks if is native.
	 *
	 * @return true, if is native
	 * @see com.slytechs.protocol.descriptor.PacketDissector#isNative()
	 */
	@Override
	public boolean isNative() {
		return false;
	}

	/**
	 * @see com.slytechs.protocol.descriptor.PacketDissector#reset()
	 */
	@Override
	public void reset() {
		timestamp = captureLength = wireLength = 0;

		l2Type = 0;

		l3Type = l3Offset = l3Size = 0;
		l3IsFrag = l3LastFrag = false;

		l4Type = l4Size = 0;

		vlanCount = mplsCount = 0;
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
	public final PacketDissector setDatalinkType(L2FrameType l2Type) throws ProtocolException {
		this.dltType = l2Type.getL2FrameTypeAsInt();

		return this;
	}

}
