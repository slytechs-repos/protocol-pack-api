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

import com.slytechs.protocol.Header;
import com.slytechs.protocol.descriptor.PacketDescriptor;
import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.meta.MetaResource;
import com.slytechs.protocol.pack.core.constants.CoreConstants;
import com.slytechs.protocol.pack.core.constants.CoreIdTable;
import com.slytechs.protocol.pack.core.constants.EtherType;
import com.slytechs.protocol.runtime.util.Checksums;
import com.slytechs.protocol.runtime.util.HexStrings;

/**
 * Ethernet II protocol header.
 * 
 * <p>
 * The IEEE 802.3 standard defines the fundamental frame format that is
 * necessary for all MAC implementations. However, several optional formats are
 * also used to expand the protocol's basic capabilities. An Ethernet frame
 * begins with the Preamble and SFD, which operate at the physical layer.
 * Following this, the Ethernet header includes both the Source and Destination
 * MAC addresses, after which the frame payload is located. Finally, the CRC
 * field is included to detect errors. We will now examine each field of the
 * basic frame format in detail.
 * </p>
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("ethernet-meta.json")
public final class Ethernet extends Header {

	/** Core protocol pack assigned header ID */
	public static final int ID = CoreIdTable.CORE_ID_ETHER;

	/** The Constant ETHER_FIELD_DST_MASK64. */
	private static final long ETHER_FIELD_DST_MASK64 = 0xFFFF_FFFFFFFFl;

	/** The Constant ETHER_FIELD_SRC_MASK64. */
	private static final long ETHER_FIELD_SRC_MASK64 = 0xFFFF_FFFFFFFFl;

	/**
	 * A flag which specifies if CRC field at the end of Ether II frame has been
	 * captured. Depends on capture configuration and hardware used.
	 */
	private boolean crcFlag;

	/**
	 * A flag which specifies if preamble preceding the Ether II frame has been
	 * captured. Depends on capture configuration and hardware used.
	 */
	private boolean preambleFlag;

	private ByteBuffer frameBuffer;

	private boolean isTruncated;

	/**
	 * Instantiates a new ethernet header.
	 */
	public Ethernet() {
		super(ID);
	}

	/**
	 * Recalculate payload length based on dissector flags which indicates if
	 * preamble or CRC fields are present.
	 * 
	 * @see com.slytechs.protocol.Header#calcPayloadLength(java.nio.ByteBuffer,
	 *      com.slytechs.protocol.descriptor.PacketDescriptor, int, int)
	 */
	@Override
	protected int calcPayloadLength(ByteBuffer packet, PacketDescriptor descriptor, int offset, int length) {
		int captureLength = descriptor.captureLength();
		int wireLength = descriptor.wireLength();
		this.isTruncated = (captureLength < wireLength);

		int flags = descriptor.flags();
		this.preambleFlag = (flags & CoreConstants.DESC_PKT_FLAG_PREAMBLE) > 0;
		this.crcFlag = (flags & CoreConstants.DESC_PKT_FLAG_CRC) > 0 && !isTruncated;

		/*
		 * Preserve the frameBuffer so we can access these outside fields and/or used to
		 * recalculate this frames CRC
		 */
		frameBuffer = packet.slice();

		int payloadLength = captureLength - (offset + length);
		if (preambleFlag)
			payloadLength -= CoreConstants.ETHER_FIELD_LEN_PREAMBLE;

		if (crcFlag)
			payloadLength -= CoreConstants.ETHER_FIELD_LEN_CRC;

		return payloadLength;
	}

	/**
	 * Computes the frame's CRC value based on {@link #destination()},
	 * {@link #source()}, {@link #type()} and {@code Payload} data fields.
	 *
	 * @return the long
	 */
	public long computeFrameCrc() {
		if (isTruncated)
			return 0; // Do not have all of the data to make the calculation

		frameBuffer
				.clear()
				.limit(offset() + length() + payloadLength())
				.position(offset());

		return Checksums.crc32(buffer());
	}

	/**
	 * CRC is 4 Byte field. This field contains a 32-bits hash code of data, which
	 * is generated over the Destination Address, Source Address, Length, and Data
	 * field. If the checksum computed by destination is not the same as sent
	 * checksum value, data received is corrupted.
	 *
	 * @return the unsinged 32-bit CRC field value if captured, otherwise a 0
	 */
	public long crc() {
		if (!isCrcPresent())
			return 0;

		return Integer.toUnsignedLong(
				frameBuffer.getInt(payloadLength()));
	}

	/**
	 * This is a 6-Byte field that contains the MAC address of the machine for which
	 * data is destined. The method allocates a new array to store the Mac address.
	 *
	 * @return MAC address
	 * @see HexStrings#toMacString(byte[])
	 */
	@Meta
	public byte[] destination() {
		return destination(new byte[CoreConstants.ETHER_FIELD_DST_LEN], 0);
	}

	/**
	 * This is a 6-Byte field that contains the MAC address of the machine for which
	 * data is destined.
	 *
	 * @param dst the destination array where the MAC address will to written to
	 * @return MAC address
	 * @see HexStrings#toMacString(byte[])
	 */
	public byte[] destination(byte[] dst) {
		return destination(dst, 0);
	}

	/**
	 * This is a 6-Byte field that contains the MAC address of the machine for which
	 * data is destined.
	 *
	 * @param dst    the destination array where the MAC address will to written to
	 * @param offset the offset into the destination array for storing the MAC
	 *               address
	 * @return MAC address
	 * @see HexStrings#toMacString(byte[])
	 */
	public byte[] destination(byte[] dst, int offset) {
		buffer().get(CoreConstants.ETHER_FIELD_DST, dst, offset, CoreConstants.ETHER_FIELD_DST_LEN);

		return dst;
	}

	/**
	 * This is a 6-Byte field that contains the MAC address of the machine for which
	 * data is destined.
	 *
	 * @return MAC address model
	 */
	public MacAddress dstGetAsAddress() {
		return new MacAddress(destination());
	}

	/**
	 * This is a 6-Byte field that contains the MAC address of the machine for which
	 * data is destined.
	 *
	 * @return MAC address stored in the first 6 LSB bytes of the long primitive
	 */
	public long dstGetAsLong() {
		return buffer().getLong(CoreConstants.ETHER_FIELD_DST) & ETHER_FIELD_DST_MASK64;
	}

	/**
	 * Checks if is crc field has been captured.
	 *
	 * @return true, if is crc present
	 */
	public boolean isCrcPresent() {
		return crcFlag;
	}

	/**
	 * Checks if is preamble data present. Most captures do not include preamble
	 * data, which is a specific pattern of bits as specified part of IEEE Ethernet
	 * specification. However some hardware does and when set, the preamble is
	 * included as part of the packet and ethernet header start after the preable.
	 *
	 * @return true, if is preamble present
	 */
	public boolean isPreamblePresent() {
		return preambleFlag;
	}

	/**
	 * @see com.slytechs.protocol.Header#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		frameBuffer = null;
	}

	/**
	 * Ethernet frame starts with a 7-Bytes Preamble.
	 *
	 * @return the array containing preamble values or null if preamble was not
	 *         captured
	 */
	public byte[] preamble() {
		return preamble(new byte[CoreConstants.ETHER_FIELD_LEN_PREAMBLE]);
	}

	/**
	 * Ethernet frame starts with a 7-Bytes Preamble.
	 *
	 * @param dst the array where to copy the preamble bytes to
	 * @return the array containing preamble values or null if preamble was not
	 *         captured
	 */
	public byte[] preamble(byte[] dst) {
		return preamble(dst, 0);
	}

	/**
	 * Ethernet frame starts with a 7-Bytes Preamble.
	 *
	 * @param dst    the array where to copy the preamble bytes to
	 * @param offset offset into the dst array
	 * @return the array containing preamble values or null if preamble was not
	 *         captured
	 */
	public byte[] preamble(byte[] dst, int offset) {
		if (!isPreamblePresent())
			return null;

		buffer().get(0, dst, CoreConstants.ETHER_FIELD_PREAMBLE, CoreConstants.ETHER_FIELD_LEN_PREAMBLE);

		return dst;
	}

	/**
	 * This is a 6-Byte field that contains the MAC address of the source machine.
	 * As Source Address is always an individual address (Unicast), the least
	 * significant bit of the first byte is always 0.
	 *
	 * @return MAC address
	 */
	@Meta
	public byte[] source() {
		return source(new byte[CoreConstants.ETHER_FIELD_SRC_LEN], 0);
	}

	/**
	 * This is a 6-Byte field that contains the MAC address of the source machine.
	 * As Source Address is always an individual address (Unicast), the least
	 * significant bit of the first byte is always 0.
	 *
	 * @param dst the destination array where the MAC address will to written to
	 * @return MAC address
	 */
	public byte[] source(byte[] dst) {
		return source(dst, 0);
	}

	/**
	 * This is a 6-Byte field that contains the MAC address of the source machine.
	 * As Source Address is always an individual address (Unicast), the least
	 * significant bit of the first byte is always 0.
	 *
	 * @param dst    the destination array where the MAC address will to written to
	 * @param offset the offset into the destination array for storing the MAC
	 *               address
	 * @return MAC address
	 */
	public byte[] source(byte[] dst, int offset) {
		buffer().get(CoreConstants.ETHER_FIELD_SRC, dst, offset, CoreConstants.ETHER_FIELD_SRC_LEN);

		return dst;
	}

	/**
	 * This is a 6-Byte field that contains the MAC address of the source machine.
	 * As Source Address is always an individual address (Unicast), the least
	 * significant bit of the first byte is always 0.
	 *
	 * @return MAC address stored in the first 6 LSB bytes of the long primitive
	 */
	public long srcGetAsLong() {
		return buffer().getLong(CoreConstants.ETHER_FIELD_SRC) & ETHER_FIELD_SRC_MASK64;
	}

	/**
	 * This is a 6-Byte field that contains the MAC address of the source machine.
	 * As Source Address is always an individual address (Unicast), the least
	 * significant bit of the first byte is always 0.
	 *
	 * @return MAC address model
	 */
	public MacAddress srcGetAsMacAddress() {
		return new MacAddress(source());
	}

	/**
	 * The EtherType field in the Ethernet frame header identifies the protocol
	 * carried in the payload of the frame. For example, a value of 0x0800 indicates
	 * that the payload is an IP packet, while a value of 0x0806 indicates that the
	 * payload is an ARP (Address Resolution Protocol) packet. .
	 *
	 * @return ether type constant
	 */
	@Meta
	public int type() {
		return Short.toUnsignedInt(buffer().getShort(CoreConstants.ETHER_FIELD_TYPE));
	}

	/**
	 * The EtherType field in the Ethernet frame header identifies the protocol
	 * carried in the payload of the frame. For example, a value of 0x0800 indicates
	 * that the payload is an IP packet, while a value of 0x0806 indicates that the
	 * payload is an ARP (Address Resolution Protocol) packet. .
	 *
	 * @return ether type enum constant
	 */
	public EtherType typeGetAsEtherType() {
		return EtherType.valueOfEtherType(type());
	}

}
