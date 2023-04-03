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

import static com.slytechs.jnet.runtime.internal.layout.BinaryLayout.*;

import com.slytechs.jnet.runtime.internal.layout.BinaryLayout;
import com.slytechs.jnet.runtime.internal.layout.BitField;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int16;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public enum Type2Layout implements BitField.Proxy {

	CAPLEN("bitmask"),
	COLOR1("bitmask"),
	COLOR2("bitmask"),
	HASH24("bitmask"),
	HASH_TYPE("bitmask"),
	HASH32("bitmask"),
	BITMASK("bitmask"),
	L2_TYPE("bitmask"),
	RX_PORT("bitmask"),
	TX_PORT("bitmask"),
	TIMESTAMP("bitmask"),
	TX_CRC_OVERRIDE("bitmask"),
	TX_IGNORE("bitmask"),
	TX_NOW("bitmask"),
	TX_SET_CLOCK("bitmask"),
	WIRELEN("bitmask"),
	RECORD_COUNT("bitmask"),
	ARRAY("bitmask"),;

	private static class Struct {
		private static final BinaryLayout TYPE2_STRUCT = unionLayout(
				structLayout(
						Int16.BITS_16.withName("udp.srcport"),
						Int16.BITS_16.withName("udp.dstport"),
						Int16.BITS_16.withName("udp.length"),
						Int16.BITS_16.withName("udp.checksum")));
	}

	private final BitField field;

	/**
	 * 
	 */
	Type2Layout(String path) {
		this.field = Struct.TYPE2_STRUCT.bitField(path);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField.Proxy#proxyBitField()
	 */
	@Override
	public BitField proxyBitField() {
		return field;
	}

	/**
	 * @param captureLength
	 * @param rxPort
	 * @param txPort
	 * @return
	 */
	public static int encodeWord2BE(int captureLength, int rxPort, int txPort) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @param captureLength
	 * @param rxPort
	 * @param txPort
	 * @return
	 */
	public static int encodeWord2LE(int captureLength, int rxPort, int txPort) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @param wireLength
	 * @param txNow
	 * @param txIgnore
	 * @param txCrcOverride
	 * @param txSetClock
	 * @param l2Type
	 * @param i
	 * @param recordCount
	 * @return
	 */
	public static int encodeWord3BE(int wireLength, int txNow, int txIgnore, int txCrcOverride, int txSetClock,
			int l2Type, int i,
			int recordCount) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * @param wireLength
	 * @param txNow
	 * @param txIgnore
	 * @param txCrcOverride
	 * @param txSetClock
	 * @param l2Type
	 * @param i
	 * @param recordCount
	 * @return
	 */
	public static int encodeWord3LE(int wireLength, int txNow, int txIgnore, int txCrcOverride, int txSetClock,
			int l2Type, int i,
			int recordCount) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}
