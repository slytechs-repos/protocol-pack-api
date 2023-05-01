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

import static com.slytechs.protocol.runtime.internal.layout.BinaryLayout.*;

import com.slytechs.protocol.runtime.internal.layout.BinaryLayout;
import com.slytechs.protocol.runtime.internal.layout.BitField;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int16;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int64;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int8;

/**
 * The Enum IpfLayout structure for a IpfDescriptor type.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
enum IpfTrackingLayout implements BitField.Proxy {

	FLAGS("flags"),
	IP_IS_REASSEMBLED("ip_is_reassembled"),
	IP_IS_COMPLETE("ip_is_complete"),
	IP_IS_TIMEOUT("ip_is_timeout"),
	IP_IS_HOLE("ip_is_hole"),
	IP_IS_OVERLAP("ip_is_overlap"),
	TABLE_SIZE("table_size"),
	REASSEMBLED_BYTES("reassembled_bytes"),
	REASSEMBLED_MILLI("reassembled_milli"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant TYPE2_STRUCT. */
		private static final BinaryLayout STRUCT = structLayout(

				/* Word0 */
				unionLayout(
						structLayout(
								Int8.BITS_01.withName("ip_is_reassembled"),
								Int8.BITS_01.withName("ip_is_complete"),
								Int8.BITS_01.withName("ip_is_timeout"),
								Int8.BITS_01.withName("ip_is_hole"),
								Int8.BITS_01.withName("ip_is_overlap"),
								Int8.BITS_03),
						Int8.BITS_08.withName("flags")),

				Int8.BITS_08.withName("table_size"),
				Int16.BITS_16.withName("reassembled_bytes"),

				/* Word1 */
				Int16.BITS_16.withName("hole_bytes"),
				Int16.BITS_16.withName("overlap_bytes"),

				/* Word2&3 */
				Int64.BITS_64.withName("reassembled_milli"),

				/* Word4+ */
				sequenceLayout(32, structLayout(

						Int64.BITS_64.withName("frag_pkt_index"),
						Int16.BITS_16.withName("frag_offset"),
						Int16.BITS_16.withName("frag_length")

				).withName("frag_table"))

		);
	}

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new type 2 layout.
	 *
	 * @param path the path
	 */
	IpfTrackingLayout(String path) {
		this.field = Struct.STRUCT.bitField(path);
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
