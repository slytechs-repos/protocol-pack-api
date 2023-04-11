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
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int32be;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int64be;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Padding;

/**
 * ARP header format layout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
enum StpLayout implements BitField.Proxy {

	/** Hardware type. */
	PROTOCOL("protocol"),
	VERSION("version"),
	TYPE("type"),
	FLAGS("flags"),
	ROOT_ID("root_id"),
	ROOT_PATH_COST("root_path_cost"),
	BRIDGE_ID("bridge_id"),
	PORT_ID("port_id"),
	MSG_AGE("msg_age"),
	MAX_AGE("max_age"),
	HELLO("hello"),
	FORWARD("forward"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant HEADER. */
		private final static BinaryLayout STRUCT = structLayout(

				/* Word0 */
				Int16be.BITS_16.withName("protocol"),
				Int16be.BITS_08.withName("version"),
				Int16be.BITS_08.withName("type"),
				Int16be.BITS_08.withName("flags"),
				Int64be.BITS_64.withName("root_id"),
				Int32be.BITS_32.withName("root_path_cost"),
				Int64be.BITS_64.withName("bridge_id"),
				Int16be.BITS_16.withName("port_id"),
				Int16be.BITS_16.withName("msg_age"),
				Int16be.BITS_16.withName("max_age"),
				Int16be.BITS_16.withName("hello"),
				Int16be.BITS_16.withName("forward"),

				Padding.BITS_00);
	}

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new ip 6 layout.
	 *
	 * @param path the path
	 */
	StpLayout(String path) {
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
