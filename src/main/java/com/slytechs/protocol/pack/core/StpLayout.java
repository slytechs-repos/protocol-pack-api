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

import com.slytechs.protocol.runtime.internal.layout.ArrayField;
import com.slytechs.protocol.runtime.internal.layout.BinaryLayout;
import com.slytechs.protocol.runtime.internal.layout.BitField;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int16be;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int16le;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int32be;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int64be;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Int8;
import com.slytechs.protocol.runtime.internal.layout.PredefinedLayout.Padding;

/**
 * ARP header format layout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
enum StpLayout implements BitField.Proxy {

	PROTOCOL("protocol"),
	VERSION("version"),
	TYPE("type"),
	FLAGS("flags"),
	ROOT_ID("root_id"),
	ROOT_PATH_COST("root_cost"),
	BRIDGE_ID("bridge_id"),
	PORT_ID("port_id"),
	MSG_AGE("msg_age"),
	MAX_AGE("max_time"),
	HELLO("hello"),
	FORWARD("forward"),

	ROOT_BRIDGE_PRIORITY("root_bridge_priority"),
	ROOT_BRIDGE_ID_EXT("root_bridge_id_ext"),

	SYSTEM_BRIDGE_PRIORITY("system_bridge_priority"),
	SYSTEM_BRIDGE_ID_EXT("system_bridge_id_ext"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant HEADER. */
		private final static BinaryLayout STRUCT = structLayout(

				/* Word0 */
				Int16le.BITS_16.withName("protocol"),
				Int16be.BITS_08.withName("version"),
				Int16be.BITS_08.withName("type"),
				Int16be.BITS_08.withName("flags"),
				BinaryLayout.unionLayout(
						Int64be.BITS_64.withName("root_id"),
						structLayout(
								Int8.BITS_08.withName("root_bridge_priority"),
								Int8.BITS_08.withName("root_bridge_id_ext"),
								sequenceLayout(6, Int8.BITS_08).withName("root_bridge_id"))),
				Int32be.BITS_32.withName("root_cost"),
				BinaryLayout.unionLayout(
						Int64be.BITS_64.withName("bridge_id"),
						structLayout(
								Int8.BITS_08.withName("system_bridge_priority"),
								Int8.BITS_08.withName("system_bridge_id_ext"),
								sequenceLayout(6, Int8.BITS_08).withName("system_bridge_id"))),
				Int16be.BITS_16.withName("port_id"),
				Int16le.BITS_16.withName("msg_age"),
				Int16le.BITS_16.withName("max_time"),
				Int16le.BITS_16.withName("hello"),
				Int16le.BITS_16.withName("forward"),

				Padding.BITS_00);
	}
	
	/** Sender hardware address. */
	public static final ArrayField ROOT_BRIDGE_ID = Struct.STRUCT.arrayField("root_bridge_id");
	public static final ArrayField SYSTEM_BRIDGE_ID = Struct.STRUCT.arrayField("system_bridge_id");


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
