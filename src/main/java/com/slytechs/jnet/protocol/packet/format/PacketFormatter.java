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
package com.slytechs.jnet.protocol.packet.format;

import java.util.stream.LongStream;
import java.util.stream.Stream;

import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.HeaderFactory;
import com.slytechs.jnet.protocol.packet.Packet;
import com.slytechs.jnet.protocol.packet.descriptor.CompactDescriptor;
import com.slytechs.jnet.runtime.util.Detail;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class PacketFormatter extends HeaderFormatter {

	private final HeaderFactory headerFactory = HeaderFactory.syncLocal();

	public PacketFormatter(MetaContext context) {
		super(context, context.getHeaderLineFormatString());
	}

	public PacketFormatter() {
		this(new MetaContext());
	}

	public PacketFormatter(String formatString) {
		super(formatString);
	}

	private Stream<Header> streamHeaders(Packet packet) {
		return LongStream.of(packet.listHeaders())
				.mapToInt(CompactDescriptor::decodeId)
				.mapToObj(headerFactory::get);
	}

	public String formatPacket(Packet packet, Detail detail) {
		return formatPacket(new StringBuilder(), packet, detail).toString();
	}

	public Appendable formatPacket(Appendable out, Packet packet, Detail detail) {

		streamHeaders(packet)
				.forEach(h -> super.formatHeader(out, h, detail));

		return out;
	}

	public Appendable formatPacket(Appendable out, MetaPacket packet) {
		return formatPacket(out, packet, Detail.DEFAULT);
	}

	public Appendable formatPacket(Appendable out, MetaPacket packet, Detail detail) {

//		packet.streamHeaders()
//				.forEach(header -> formatHeader(out, header, detail));

		return out;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.HeaderFormatter#formatHeader(java.lang.Appendable,
	 *      com.slytechs.jnet.protocol.packet.meta.MetaHeader,
	 *      com.slytechs.jnet.runtime.util.Detail)
	 */
	@Override
	public Appendable formatHeader(Appendable out, MetaHeader header, Detail detail) {
		return super.formatHeader(out, header, detail);
	}

}
