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
package com.slytechs.jnet.protocol.core;

import com.slytechs.jnet.protocol.constants.CoreConstants;
import com.slytechs.jnet.protocol.constants.CoreHeaderInfo;
import com.slytechs.jnet.protocol.constants.EtherType;
import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.meta.Meta;
import com.slytechs.jnet.protocol.packet.meta.MetaResource;
import com.slytechs.jnet.runtime.internal.layout.BitField;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("ethernet-meta.json")
public class Ethernet extends Header {
	public static final int ID = CoreHeaderInfo.CORE_ID_ETHER;

	private static final long ETHER_FIELD_DST_MASK64 = 0xFFFF_FFFFFFFFl;
	private static final long ETHER_FIELD_SRC_MASK64 = 0xFFFF_FFFFFFFFl;

	public Ethernet() {
		super(ID);
	}

	@Meta
	public byte[] destination() {
		return destination(new byte[CoreConstants.ETHER_FIELD_DST_LEN], 0);
	}

	public byte[] destination(byte[] dst) {
		return destination(dst, 0);
	}

	public byte[] destination(byte[] dst, int offset) {
		buffer().get(CoreConstants.ETHER_FIELD_DST, dst, offset, CoreConstants.ETHER_FIELD_DST_LEN);

		return dst;
	}

	public MacAddress dstGetAsAddress() {
		return new MacAddress(destination());
	}

	public long dstGetAsLong() {
		return buffer().getLong(CoreConstants.ETHER_FIELD_DST) & ETHER_FIELD_DST_MASK64;
	}

	String macIgBitInfo(BitField field) {
		return field.getBit(buffer())
				? "Group address (multicast)"
				: "Individual address (unicast)";
	}

	String macLgBitInfo(BitField field) {
		return field.getBit(buffer())
				? "Locally unique address"
				: "Globally unique address (factory default)";
	}

	@Meta
	public byte[] source() {
		return source(new byte[CoreConstants.ETHER_FIELD_SRC_LEN], 0);
	}

	public byte[] source(byte[] src) {
		return source(src, 0);
	}

	public byte[] source(byte[] src, int offset) {
		buffer().get(CoreConstants.ETHER_FIELD_SRC, src, offset, CoreConstants.ETHER_FIELD_SRC_LEN);

		return src;
	}

	public MacAddress srcGetAsMacAddress() {
		return new MacAddress(source());
	}

	public long srcGetAsLong() {
		return buffer().getLong(CoreConstants.ETHER_FIELD_SRC) & ETHER_FIELD_SRC_MASK64;
	}

	@Meta
	public int type() {
		return Short.toUnsignedInt(buffer().getShort(CoreConstants.ETHER_FIELD_TYPE));
	}

	public EtherType typeGetAsEtherType() {
		return EtherType.valueOfEtherType(type());
	}

}
