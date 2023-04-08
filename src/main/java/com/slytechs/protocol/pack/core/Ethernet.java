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
import com.slytechs.protocol.pack.core.constants.CoreHeaderInfo;
import com.slytechs.protocol.pack.core.constants.EtherType;
import com.slytechs.protocol.runtime.internal.layout.BitField;

/**
 * The Class Ethernet.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("ethernet-meta.json")
public class Ethernet extends Header {
	
	/** The Constant ID. */
	public static final int ID = CoreHeaderInfo.CORE_ID_ETHER;

	/** The Constant ETHER_FIELD_DST_MASK64. */
	private static final long ETHER_FIELD_DST_MASK64 = 0xFFFF_FFFFFFFFl;
	
	/** The Constant ETHER_FIELD_SRC_MASK64. */
	private static final long ETHER_FIELD_SRC_MASK64 = 0xFFFF_FFFFFFFFl;

	/**
	 * Instantiates a new ethernet.
	 */
	public Ethernet() {
		super(ID);
	}

	/**
	 * Destination.
	 *
	 * @return the byte[]
	 */
	@Meta
	public byte[] destination() {
		return destination(new byte[CoreConstants.ETHER_FIELD_DST_LEN], 0);
	}

	/**
	 * Destination.
	 *
	 * @param dst the dst
	 * @return the byte[]
	 */
	public byte[] destination(byte[] dst) {
		return destination(dst, 0);
	}

	/**
	 * Destination.
	 *
	 * @param dst    the dst
	 * @param offset the offset
	 * @return the byte[]
	 */
	public byte[] destination(byte[] dst, int offset) {
		buffer().get(CoreConstants.ETHER_FIELD_DST, dst, offset, CoreConstants.ETHER_FIELD_DST_LEN);

		return dst;
	}

	/**
	 * Dst get as address.
	 *
	 * @return the mac address
	 */
	public MacAddress dstGetAsAddress() {
		return new MacAddress(destination());
	}

	/**
	 * Dst get as long.
	 *
	 * @return the long
	 */
	public long dstGetAsLong() {
		return buffer().getLong(CoreConstants.ETHER_FIELD_DST) & ETHER_FIELD_DST_MASK64;
	}

	/**
	 * Mac ig bit info.
	 *
	 * @param field the field
	 * @return the string
	 */
	String macIgBitInfo(BitField field) {
		return field.getBit(buffer())
				? "Group address (multicast)"
				: "Individual address (unicast)";
	}

	/**
	 * Mac lg bit info.
	 *
	 * @param field the field
	 * @return the string
	 */
	String macLgBitInfo(BitField field) {
		return field.getBit(buffer())
				? "Locally unique address"
				: "Globally unique address (factory default)";
	}

	/**
	 * Source.
	 *
	 * @return the byte[]
	 */
	@Meta
	public byte[] source() {
		return source(new byte[CoreConstants.ETHER_FIELD_SRC_LEN], 0);
	}

	/**
	 * Source.
	 *
	 * @param src the src
	 * @return the byte[]
	 */
	public byte[] source(byte[] src) {
		return source(src, 0);
	}

	/**
	 * Source.
	 *
	 * @param src    the src
	 * @param offset the offset
	 * @return the byte[]
	 */
	public byte[] source(byte[] src, int offset) {
		buffer().get(CoreConstants.ETHER_FIELD_SRC, src, offset, CoreConstants.ETHER_FIELD_SRC_LEN);

		return src;
	}

	/**
	 * Src get as mac address.
	 *
	 * @return the mac address
	 */
	public MacAddress srcGetAsMacAddress() {
		return new MacAddress(source());
	}

	/**
	 * Src get as long.
	 *
	 * @return the long
	 */
	public long srcGetAsLong() {
		return buffer().getLong(CoreConstants.ETHER_FIELD_SRC) & ETHER_FIELD_SRC_MASK64;
	}

	/**
	 * Type.
	 *
	 * @return the int
	 */
	@Meta
	public int type() {
		return Short.toUnsignedInt(buffer().getShort(CoreConstants.ETHER_FIELD_TYPE));
	}

	/**
	 * Type get as ether type.
	 *
	 * @return the ether type
	 */
	public EtherType typeGetAsEtherType() {
		return EtherType.valueOfEtherType(type());
	}

}
