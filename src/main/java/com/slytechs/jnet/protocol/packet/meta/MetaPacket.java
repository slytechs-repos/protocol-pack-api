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
package com.slytechs.jnet.protocol.packet.meta;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.slytechs.jnet.protocol.packet.Frame;
import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.HeaderFactory;
import com.slytechs.jnet.protocol.packet.Packet;
import com.slytechs.jnet.protocol.packet.Payload;
import com.slytechs.jnet.protocol.packet.descriptor.CompactDescriptor;
import com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class MetaPacket
		extends MetaElement
		implements Iterable<MetaHeader>, MetaMapped {

	private final HeaderFactory headerFactory = HeaderFactory.newInstance();

	private final Packet packet;
	private final List<MetaHeader> headers = new ArrayList<>();
	private final MetaHeader lastHeader;

	public MetaPacket(Packet packet) {
		this(new MapMetaContext("packet", 1), packet);
	}

	public MetaPacket(MetaDomain domain, Packet packet) {
		super(domain, MetaDomain.global(Packet.class, ReflectedClass::parse));
		this.packet = packet;

		headers.add(new MetaHeader(domain, packet.getHeader(new Frame(), 0)));

		long[] compactArray = packet.descriptor().listHeaders();
		for (long cp : compactArray) {
			int id = CompactDescriptor.decodeId(cp);
			Header header = headerFactory.get(id);
			packet.getHeader(header, 0);

			MetaHeader metaHdr = new MetaHeader(domain, header);

			headers.add(metaHdr);
		}

		this.lastHeader = headers.get(headers.size() - 1);

		headers.add(new MetaHeader(domain, packet.getHeader(new Payload(), 0)));
	}

	public Object getTarget() {
		return packet;
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<MetaHeader> iterator() {
		return listHeaders().iterator();
	}

	public List<MetaHeader> listHeaders() {
		return headers;
	}

	public MetaHeader getHeader(String name) {
		return headers.stream()
				.filter(h -> h.name().equals(name))
				.findAny()
				.orElse(null);
	}

	public Optional<MetaHeader> findHeader(String name) {
		if (name.equals("last"))
			return Optional.of(lastHeader);

		return headers.stream()
//				.peek(h -> System.out.println("findHeader:: " + h.name()))
				.filter(h -> h.name().equals(name))
				.findAny();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <K, V> V get(K key) {
		String name = (String) key;
		return (V) headers.stream()
				.filter(h -> h.name().equals(name))
				.findAny()
				.orElse(null);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#size()
	 */
	@Override
	public int size() {
		return headers.size();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaElement#searchForField(com.slytechs.jnet.protocol.packet.meta.MetaPath)
	 */
	@Override
	public Optional<MetaField> searchForField(MetaPath path) {
		if (path.isEmpty())
			return Optional.empty();

		String name = path.stack(0);

		if (path.size() == 1)
			return findField(name);

		Optional<MetaHeader> header = findHeader(name);

		return header
				.flatMap(hdr -> hdr.searchForField(path.pop()));
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MetaPacket [%s]"
				.formatted(headers.stream()
						.map(MetaHeader::name)
						.collect(Collectors.joining(", ")));
	}

	public int captureLength() {
		return packet.captureLength();
	}

	public int wireLength() {
		return packet.wireLength();
	}

	public ByteBuffer buffer() {
		return packet.buffer();
	}
}
