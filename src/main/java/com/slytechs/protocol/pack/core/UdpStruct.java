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

import static com.slytechs.protocol.runtime.internal.layout.BinaryLayout.*;

import com.slytechs.protocol.pack.core.constants.CoreConstants;
import com.slytechs.protocol.runtime.internal.layout.ArrayField;
import com.slytechs.protocol.runtime.internal.layout.BinaryLayout;
import com.slytechs.protocol.runtime.internal.layout.BitField;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int16;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int8;

/**
 * The Enum UdpStruct.
 */
public enum UdpStruct implements BitField.Proxy {
	
	/** The src port. */
	SRC_PORT("udp.srcport"),
	
	/** The dst port. */
	DST_PORT("udp.dstport"),
	
	/** The length. */
	LENGTH("udp.length"),
	
	/** The checksum. */
	CHECKSUM("udp.checksum");

	/**
	 * The Class Struct.
	 */
	private static class Struct {
		
		/** The Constant UDP_STRUCT. */
		private static final BinaryLayout UDP_STRUCT = unionLayout(
				structLayout(
						Int16.BITS_16.withName("udp.srcport"),
						Int16.BITS_16.withName("udp.dstport"),
						Int16.BITS_16.withName("udp.length"),
						Int16.BITS_16.withName("udp.checksum")),
				sequenceLayout(CoreConstants.UDP_HEADER_LEN, Int8.BITS_08).withName("udp.bytes"));
	}

	/** The Constant HEADER_BYTES. */
	public static final ArrayField HEADER_BYTES = Struct.UDP_STRUCT.arrayField("udp.bytes");

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new udp struct.
	 *
	 * @param path the path
	 */
	UdpStruct(String path) {
		this.field = Struct.UDP_STRUCT.bitField(path);
	}

	/**
	 * Proxy bit field.
	 *
	 * @return the bit field
	 * @see com.slytechs.protocol.runtime.internal.layout.BitField.Proxy#proxyBitField()
	 */
	@Override
	public BitField proxyBitField() {
		return field;
	}

}
