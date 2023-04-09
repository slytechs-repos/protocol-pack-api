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

import com.slytechs.protocol.Header;
import com.slytechs.protocol.meta.Meta;
import com.slytechs.protocol.meta.MetaResource;
import com.slytechs.protocol.pack.core.constants.CoreConstants;
import com.slytechs.protocol.pack.core.constants.CorePackIds;
import com.slytechs.protocol.pack.core.constants.EtherType;
import com.slytechs.protocol.runtime.util.HexStrings;

/**
 * Ethernet protocol header model.
 * 
 * <p>
 * Basic frame format which is required for all MAC implementation is defined in
 * IEEE 802.3 standard. Though several optional formats are being used to extend
 * the protocol’s basic capability. Ethernet frame starts with Preamble and SFD,
 * both work at the physical layer. Ethernet header contains both the Source and
 * Destination MAC address, after which the payload of the frame is present. The
 * last field is CRC which is used to detect any transmission errors.
 * </p>
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("ethernet-meta.json")
public final class Ethernet extends Header {

	/** Core protocol pack assigned header ID */
	public static final int ID = CorePackIds.CORE_ID_ETHER;

	/** The Constant ETHER_FIELD_DST_MASK64. */
	private static final long ETHER_FIELD_DST_MASK64 = 0xFFFF_FFFFFFFFl;

	/** The Constant ETHER_FIELD_SRC_MASK64. */
	private static final long ETHER_FIELD_SRC_MASK64 = 0xFFFF_FFFFFFFFl;

	/**
	 * Instantiates a new ethernet header.
	 */
	public Ethernet() {
		super(ID);
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
	 * @return MAC address model
	 */
	public MacAddress srcGetAsMacAddress() {
		return new MacAddress(source());
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
