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
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int16be;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int8;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Padding;

/**
 * ARP header format layout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
enum PppLayout implements BitField.Proxy {

	/** Hardware type. */
	FLAG("flag"),
	ADDRESS("address"),
	CONTROL("control"),
	PROTOCOL("protocol"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant HEADER. */
		private final static BinaryLayout STRUCT = structLayout(

				/* Word0 */
				Int8.BITS_08.withName("flag"),
				Int8.BITS_08.withName("address"),
				Int8.BITS_08.withName("control"),
				Int16be.BITS_16.withName("protocol"),

				Padding.BITS_00);
	}

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new ip 6 layout.
	 *
	 * @param path the path
	 */
	PppLayout(String path) {
		this.field = Struct.STRUCT.bitField(path);
	}

	/**
	 * Proxy bit field.
	 *
	 * @return the bit field
	 * @see com.slytechs.jnetpcap.pro.PacketDescriptorProxy.BitField.Proxy#proxyBitField()
	 */
	@Override
	public BitField proxyBitField() {
		return field;
	}
}
