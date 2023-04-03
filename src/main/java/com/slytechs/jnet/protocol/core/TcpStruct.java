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
import com.slytechs.jnet.runtime.internal.layout.EnumBitField;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int16be;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int32be;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int8;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Padding;

public enum TcpStruct implements EnumBitField<TcpStruct> {

	// TCP HEADER FIELDS

	SRC_PORT(Layout.TCP_STRUCT, "tcp.srcport"),
	DST_PORT(Layout.TCP_STRUCT, "tcp.dstport"),
	SEQ(Layout.TCP_STRUCT, "tcp.seq"),
	ACK(Layout.TCP_STRUCT, "tcp.ack"),
	HDR_LEN(Layout.TCP_STRUCT, "tcp.hdr_len"),
	RESERVED(Layout.TCP_STRUCT, "tcp.res"),
	FLAGS(Layout.TCP_STRUCT, "tcp.flags"),
	WIN_SIZE(Layout.TCP_STRUCT, "tcp.window_size_value"),
	CHECKSUM(Layout.TCP_STRUCT, "tcp.checksum"),
	URGENT_POINTER(Layout.TCP_STRUCT, "tcp.urgent_pointer"),

	// TCP OPTION FIELDS

	OPT_KIND(Layout.OPTION_LAYOUT, "tcp.opt.kind"),
	OPT_LEN(Layout.OPTION_LAYOUT, "tcp.opt.len"),
	OPT_MSS(Layout.OPTION_LAYOUT, "tcp.opt.mss"),
	OPT_TSSEND(Layout.OPTION_LAYOUT, "tcp.opt.ts.send_ts"),
	OPT_TSRECV(Layout.OPTION_LAYOUT, "tcp.opt.ts.recv_ts"),
	OPT_WIN_SCALE(Layout.OPTION_LAYOUT, "tcp.opt.win_scale");

	private static class Layout {
		private static final BinaryLayout TCP_STRUCT = unionLayout(
				structLayout(

						/* Word0 */
						Int16be.BITS_16.withName("tcp.srcport"),
						Int16be.BITS_16.withName("tcp.dstport"),

						/* Word1 */
						Int32be.BITS_32.withName("tcp.seq"),

						/* Word2 */
						Int32be.BITS_32.withName("tcp.ack"),

						/* Word3 */
						Int16be.BITS_04.withName("tcp.hdr_len"),
						Int16be.BITS_03.withName("tcp.res"),
						Int16be.BITS_09.withName("tcp.flags"),
						Int16be.BITS_16.withName("tcp.window_size_value"),

						/* Word4 */
						Int16be.BITS_16.withName("tcp.checksum"),
						Int16be.BITS_16.withName("tcp.urgent_pointer")),

				sequenceLayout(CoreConstants.TCP_HEADER_LEN / 1, Int8.BITS_08).withName("tcp.bytes"),
				sequenceLayout(CoreConstants.TCP_HEADER_LEN / 2, Int16be.BITS_16).withName("tcp.shorts"),
				sequenceLayout(CoreConstants.TCP_HEADER_LEN / 4, Int32be.BITS_32).withName("tcp.ints"));

		private static final BinaryLayout OPTION_LAYOUT = unionLayout(
				structLayout(
						Int8.BITS_08.withName("tcp.opt.kind"),
						Int8.BITS_08.withName("tcp.opt.len")),

				structLayout(
						Padding.BITS_16,
						Int16be.BITS_16.withName("tcp.opt.mss")),

				structLayout(
						Padding.BITS_16,
						Int32be.BITS_32.withName("tcp.opt.ts.send_ts"),
						Int32be.BITS_32.withName("tcp.opt.ts.recv_ts")),

				structLayout(
						Padding.BITS_16,
						sequenceLayout(16, Int8.BITS_08).withName("tcp.opt.fastopen.cookie")),

				structLayout(
						Padding.BITS_16,
						Int8.BITS_08.withName("tcp.opt.win_scale")));

	}

	public static final ArrayField HEADER_BYTES = Layout.TCP_STRUCT.arrayField("tcp.bytes");
	public static final ArrayField HEADER_SHORTS = Layout.TCP_STRUCT.arrayField("tcp.shorts");
	public static final ArrayField HEADER_INTS = Layout.TCP_STRUCT.arrayField("tcp.ints");
	public static final ArrayField OPT_COOKIE = Layout.OPTION_LAYOUT.arrayField("tcp.opt.fastopen.cookie");

	private final BitField bits;

	private TcpStruct(BinaryLayout layout, String path) {
		this.bits = layout.bitField(path)
				.formatted()
				.format("%d");
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.layout.BitField.Proxy#proxyBitField()
	 */
	@Override
	public BitField proxyBitField() {
		return bits;
	}
}
