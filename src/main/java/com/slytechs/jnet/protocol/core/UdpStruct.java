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

import static com.slytechs.jnet.runtime.internal.layout.BinaryLayout.*;

import com.slytechs.jnet.protocol.constants.CoreConstants;
import com.slytechs.jnet.runtime.internal.layout.ArrayField;
import com.slytechs.jnet.runtime.internal.layout.BinaryLayout;
import com.slytechs.jnet.runtime.internal.layout.BitField;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int16;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int8;

public enum UdpStruct implements BitField.Proxy {
	SRC_PORT("udp.srcport"),
	DST_PORT("udp.dstport"),
	LENGTH("udp.length"),
	CHECKSUM("udp.checksum");

	private static class Struct {
		private static final BinaryLayout UDP_STRUCT = unionLayout(
				structLayout(
						Int16.BITS_16.withName("udp.srcport"),
						Int16.BITS_16.withName("udp.dstport"),
						Int16.BITS_16.withName("udp.length"),
						Int16.BITS_16.withName("udp.checksum")),
				sequenceLayout(CoreConstants.UDP_HEADER_LEN, Int8.BITS_08).withName("udp.bytes"));
	}

	public static final ArrayField HEADER_BYTES = Struct.UDP_STRUCT.arrayField("udp.bytes");

	private final BitField field;

	UdpStruct(String path) {
		this.field = Struct.UDP_STRUCT.bitField(path);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField.Proxy#proxyBitField()
	 */
	@Override
	public BitField proxyBitField() {
		return field;
	}

}
