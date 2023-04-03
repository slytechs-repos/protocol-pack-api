/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.protocol.core;

import static com.slytechs.jnet.runtime.internal.layout.BinaryLayout.*;

import com.slytechs.jnet.runtime.internal.layout.ArrayField;
import com.slytechs.jnet.runtime.internal.layout.BinaryLayout;
import com.slytechs.jnet.runtime.internal.layout.BitField;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int16be;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int32be;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int64be;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int8;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum Ip6Layout implements BitField.Proxy {
	VERSION("ip.version"),
	DSFIELD("ip.dsfield"),
	FLOW("ip.flow"),
	PAYLOAD_LENGTH("ip.plen"),
	NEXT("ip.nxt"),
	HOP_LIMIT("ip.hlim"),
	SRC_AS_INT("ip.src.ints"),
	DST_AS_INT("ip.dst.ints"),
	SRC_AS_LONG("ip.src.longs"),
	DST_AS_LONG("ip.dst.longs");

	private static class Struct {

		/** The Constant HEADER. */
		private final static BinaryLayout IP6_STRUCT = structLayout(

				/* Word0 */
				unionLayout(
						Int32be.BITS_04.withName("ip.version"),
						unionLayout(
								structLayout(
										Int32be.BITS_04, // ignore
										Int32be.BITS_08.withName("ip.dsfield"),
										Int32be.BITS_20.withName("ip.flow")),
								structLayout(
										Int32be.BITS_04, // ignore
										Int32be.BITS_06.withName("ip.dsfield.dscp"),
										Int32be.BITS_03.withName("ip.dsfield.ecn")),
								structLayout(
										Int32be.BITS_04, // ignore
										Int32be.BITS_03.withName("ip.dsfield.dscp.select"),
										Int32be.BITS_03.withName("ip.dsfield.dscp.code")))),

				/* Word1 */
				Int16be.BITS_16.withName("ip.plen"),
				Int8.BITS_08.withName("ip.nxt"),
				Int8.BITS_08.withName("ip.hlim"),

				/* Word2,3,4,5 */
				unionLayout( /* Alternative address layouts */
						sequenceLayout(16, Int8.BITS_08).withName("ip.src"),
						sequenceLayout(128, Int8.BITS_01).withName("ip.src.bits"),
						sequenceLayout(4, Int32be.BITS_32).withName("ip.src.ints"),
						sequenceLayout(2, Int64be.BITS_64).withName("ip.src.longs")),

				/* Word6,7,8,9 */
				unionLayout( /* Alternative address layouts */
						sequenceLayout(16, Int8.BITS_08).withName("ip.dst"),
						sequenceLayout(128, Int8.BITS_01).withName("ip.dst.bits"),
						sequenceLayout(4, Int32be.BITS_32).withName("ip.dst.ints"),
						sequenceLayout(2, Int64be.BITS_64).withName("ip.dst.longs")));
	}

	public static final ArrayField SRC_BYTES = Struct.IP6_STRUCT.arrayField("ip.src");
	public static final ArrayField DST_BYTES = Struct.IP6_STRUCT.arrayField("ip.dst");

	private final BitField field;

	Ip6Layout(String path) {
		this.field = Struct.IP6_STRUCT.bitField(path);
	}

	/**
	 * @see com.slytechs.jnet.layout.BitField.Proxy#proxyBitField()
	 */
	@Override
	public BitField proxyBitField() {
		return field;
	}
}
