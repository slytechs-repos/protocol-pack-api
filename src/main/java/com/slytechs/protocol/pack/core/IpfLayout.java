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

import com.slytechs.protocol.runtime.internal.layout.BinaryLayout;
import com.slytechs.protocol.runtime.internal.layout.BitField;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int32;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int64;

/**
 * The Enum IpfLayout structure for a IpfDescriptor type.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
enum IpfLayout implements BitField.Proxy {

	/** The assembled bytes. */
	ASSEMBLED_BYTES("assembled_bytes"),
	
	/** The remaining bytes. */
	REMAINING_BYTES("remaining_bytes"),
	
	/** The duplicate bytes. */
	DUPLICATE_BYTES("duplicate_bytes"),
	
	/** The flags. */
	FLAGS("flags"),
	
	/** The frame no. */
	FRAME_NO("frame_no"),
	
	/** The prev no. */
	PREV_NO("prev_no"),
	
	/** The next no. */
	NEXT_NO("next_no"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant TYPE2_STRUCT. */
		private static final BinaryLayout STRUCT = unionLayout(
				structLayout(

						Int32.BITS_16.withName("assembled_bytes"),
						Int32.BITS_16.withName("remaining_bytes"),
						Int32.BITS_16.withName("duplicate_bytes"),
						Int32.BITS_16.withName("flags"),

						Int64.BITS_64.withName("frame_no"),
						Int64.BITS_64.withName("frame_no"),
						Int64.BITS_64.withName("prev_no"),
						Int64.BITS_64.withName("next_no")

				),
				sequenceLayout(6, Int32.BITS_32).withName("array")

		);
	}

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new type 2 layout.
	 *
	 * @param path the path
	 */
	IpfLayout(String path) {
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
