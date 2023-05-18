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
package com.slytechs.protocol.meta;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.slytechs.protocol.Frame;
import com.slytechs.protocol.Header;
import com.slytechs.protocol.HeaderFactory;
import com.slytechs.protocol.HeaderNotFound;
import com.slytechs.protocol.Packet;
import com.slytechs.protocol.Payload;
import com.slytechs.protocol.descriptor.CompactDescriptor;
import com.slytechs.protocol.meta.MetaContext.MetaMapped;
import com.slytechs.protocol.pack.PackId;
import com.slytechs.protocol.pack.ProtocolPackTable;
import com.slytechs.protocol.runtime.util.Detail;

/**
 * The Class MetaPacket.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public final class MetaPacket
		extends MetaElement
		implements Iterable<MetaHeader>, MetaMapped {

	@Meta(name = "*** Exception")
	private static class InternarErrorHeaderStub extends Header {

		private final String errorMessage;
		private final String headerName;
		private String cause;

		protected InternarErrorHeaderStub(String headerName, String errorMessage, Throwable cause) {
			super(-1);
			this.headerName = headerName;
			this.errorMessage = errorMessage;
			this.cause = (cause == null) ? "<not specified>" : cause.getMessage();
		}

		@Meta
		public String header() {
			return headerName;
		}

		@Meta
		public String error() {
			return errorMessage;
		}

		@Meta
		public String cause() {
			return cause == null ? "" : cause;
		}
	}

	/** The header factory. */
	private final HeaderFactory headerFactory = HeaderFactory.newInstance();

	/** The packet. */
	private final Packet packet;

	/** The headers. */
	private final List<MetaHeader> headers = new ArrayList<>();

	/** The last header. */
	private final MetaHeader lastHeader;

	/**
	 * Instantiates a new meta packet.
	 *
	 * @param packet the packet
	 */
	public MetaPacket(Packet packet) {
		this(new MapMetaContext("packet", 1), packet);
	}

	/**
	 * Instantiates a new meta packet.
	 *
	 * @param ctx    the ctx
	 * @param packet the packet
	 */
	public MetaPacket(MetaDomain ctx, Packet packet) {
		super(ctx, Global.compute(Packet.class, ReflectedClass::parse));
		this.packet = packet;

		try {
			headers.add(new MetaHeader(ctx, this, packet.getHeader(new Frame(), 0)));

			String lastHeaderName = "";
			int lastId = 0;
			long[] compactArray = packet.descriptor().listHeaders();
			for (long cp : compactArray) {
				try {
					int id = CompactDescriptor.decodeId(cp);
					int packId = PackId.decodePackId(id);
					boolean isOption = packId == ProtocolPackTable.PACK_ID_OPTIONS;

					final Header header;
					if (isOption) {
						header = headerFactory.getExtension(lastId, id);

					} else {
						header = headerFactory.get(id);
						lastId = id;
					}

					lastHeaderName = header.headerName();

					packet.getHeader(header, 0);
					lastHeaderName = header.toString(Detail.MEDIUM, null);

					MetaHeader metaHdr = new MetaHeader(ctx, this, header);
					headers.add(metaHdr);

				} catch (RuntimeException e) {
					MetaHeader errorHeaders = new MetaHeader(
							ctx,
							this,
							new InternarErrorHeaderStub(lastHeaderName, e.getMessage(), e.getCause()));
					headers.add(errorHeaders);

					e.printStackTrace();
				}
			}

			this.lastHeader = headers.get(headers.size() - 1);

			if (packet.hasPayload())
				headers.add(new MetaHeader(ctx, this, packet.getHeader(new Payload(), 0)));
		} catch (HeaderNotFound e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public Object getTarget() {
		return packet;
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<MetaHeader> iterator() {
		return listHeaders().iterator();
	}

	/**
	 * List headers.
	 *
	 * @return the list
	 */
	public List<MetaHeader> listHeaders() {
		return headers;
	}

	/**
	 * Gets the header.
	 *
	 * @param name the name
	 * @return the header
	 */
	public MetaHeader getHeader(String name) {
		return headers.stream()
				.filter(h -> h.name().equals(name))
				.findAny()
				.orElse(null);
	}

	/**
	 * Find header.
	 *
	 * @param name the name
	 * @return the optional
	 */
	public Optional<MetaHeader> findHeader(String name) {
		if (name.equals("last"))
			return Optional.of(lastHeader);

		return headers.stream()
//				.peek(h -> System.out.println("findHeader:: " + h.name()))
				.filter(h -> h.name().equals(name))
				.findAny();
	}

	/**
	 * Gets the.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param key the key
	 * @return the v
	 * @see com.slytechs.protocol.meta.MetaContext.MetaMapped#get(java.lang.Object)
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
	 * Size.
	 *
	 * @return the int
	 * @see com.slytechs.protocol.meta.MetaContext.MetaMapped#size()
	 */
	@Override
	public int size() {
		return headers.size();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MetaPacket [%s]"
				.formatted(headers.stream()
						.map(MetaHeader::name)
						.collect(Collectors.joining(", ")));
	}

	/**
	 * Capture length.
	 *
	 * @return the int
	 */
	public int captureLength() {
		return packet.captureLength();
	}

	/**
	 * Wire length.
	 *
	 * @return the int
	 */
	public int wireLength() {
		return packet.wireLength();
	}

	/**
	 * Buffer.
	 *
	 * @return the byte buffer
	 */
	public ByteBuffer buffer() {
		return packet.buffer();
	}

	/**
	 * Find key.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param key the key
	 * @return the optional
	 * @see com.slytechs.protocol.meta.MetaDomain#findKey(java.lang.Object)
	 */
	@Override
	public <K, V> Optional<V> findKey(K key) {
		return listHeaders().stream()
				.filter(h -> h.name().equals(key))
				.map(h -> (V) h)
				.findAny();
	}

	/**
	 * Find domain.
	 *
	 * @param name the name
	 * @return the meta domain
	 * @see com.slytechs.protocol.meta.MetaDomain#findDomain(java.lang.String)
	 */
	@Override
	public MetaDomain findDomain(String name) {
		return listHeaders().stream()
				.filter(h -> h.name().equals(name))
				.findAny()
				.orElse(null);
	}
}
