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
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int32;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int64;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public enum Type2Layout implements BitField.Proxy {

	TIMESTAMP("timestamp"),
	CAPLEN("caplen"),
	COLOR1("color1"),
	COLOR2("color2"),
	HASH24("hash24"),
	WIRELEN("wirelen"),
	L2_TYPE("l2_type"),
	RX_PORT("rx_port"),
	TX_PORT("tx_port"),
	HASH_TYPE("hash_type"),
	TX_NOW("tx_now"),
	TX_IGNORE("tx_ignore"),
	TX_CRC_OVERRIDE("tx_crc_override"),
	TX_SET_CLOCK("tx_set_clock"),
	RECORD_COUNT("record_count"),
	BITMASK("bitmask"),
	RECORD("record"),
	HASH32("hash32"),
	ARRAY("array"),

	;

	private static class Struct {
		private static final BinaryLayout TYPE2_STRUCT = unionLayout(
				/* length: 24-184 bytes */
				structLayout(

						/* Word0&1 */
						Int64.BITS_64.withName("timestamp"),

						/* Word2 */
						Int16.BITS_16.withName("caplen"),
						Int16.BITS_08.withName("rx_port"),
						Int16.BITS_08.withName("tx_port"),

						/* Word3 */
						Int16.BITS_16.withName("wirelen"),
						Int16.BITS_01.withName("tx_now"),
						Int16.BITS_01.withName("tx_ignore"),
						Int16.BITS_01.withName("tx_crc_override"),
						Int16.BITS_01.withName("tx_set_clock"),
						Int16.BITS_04.withName("l2_type"),
						Int16.BITS_03.withName("color1"),
						Int16.BITS_05.withName("record_count"),

						/* Word4 */
						unionLayout(
								Int32.BITS_32.withName("hash32"),
								Int32.BITS_32.withName("color2"),
								structLayout(
										Int32.BITS_24.withName("hash24"),
										Int32.BITS_05.withName("hash_type"))),

						/* Word5 */
						Int32.BITS_32.withName("bitmask"),

						/* Word6-38 */
						sequenceLayout(32, Int32.BITS_32).withName("record")

				),
				sequenceLayout(32 + 6, Int32.BITS_32).withName("array")

		);
	}

	private final BitField field;

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

	public static int encodeWord2BE(int captureLength, int rxPort, int txPort) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public static int encodeWord2LE(int captureLength, int rxPort, int txPort) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public static int encodeWord3BE(int wireLength, int txNow, int txIgnore, int txCrcOverride, int txSetClock,
			int l2Type, int i,
			int recordCount) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public static int encodeWord3LE(int wireLength, int txNow, int txIgnore, int txCrcOverride, int txSetClock,
			int l2Type, int i,
			int recordCount) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}
