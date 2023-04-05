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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.Packet;
import com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class MetaHeader
		extends MetaElement
		implements Iterable<MetaField>, MetaMapped {

	private final Header header;

	private final List<MetaField> fields;
	private final List<MetaField> attributes;
	private final List<MetaField> elements;
	private volatile Map<String, MetaField> elementMap;

	private MetaHeader(MetaDomain domain, Header target, ReflectedClass reflectedClass) {
		super(domain, reflectedClass);
		this.header = target;
		this.elements = Arrays.stream(reflectedClass.getFields())
				.map(reflectedMember -> new MetaField(domain, header, this, reflectedMember))
				.collect(Collectors.toList());

		this.fields = elements.stream().filter(MetaField::isField).toList();
		this.attributes = elements.stream().filter(MetaField::isAttribute).toList();
	}

	public MetaHeader(Packet packet, Header header) {
		this(MetaContext.newRoot(), new MetaPacket(packet), header);
	}

	public MetaHeader(MetaDomain ctx, MetaPacket packet, Header header) {
		this(ctx,
				header,
				Global.compute(header.getClass(), ReflectedClass::parse));
	}

	public MetaHeader(MetaDomain ctx, Header header) {
		this(ctx,
				header,
				Global.compute(header.getClass(), ReflectedClass::parse));
	}

	public MetaHeader getExtension(String name) {
		return null;
	}

	@Override
	public MetaField getField(String name) {
		if (elementMap == null) {// Lazy allocate map
			var fieldsMap = elements.stream()
					.collect(Collectors.toConcurrentMap(MetaField::name, f -> f));

			while (this.elementMap == null)
				this.elementMap = fieldsMap;
		}

		return this.elementMap.get(name);
	}

	public Object getTarget() {
		return header;
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<MetaField> iterator() {
		return fields.iterator();
	}

	public List<MetaField> listAttributes() {
		return attributes;
	}

	public List<MetaField> listFields() {
		return fields;
	}

	public List<MetaField> listAllElements() {
		return elements;
	}

	public List<MetaHeader> listSubHeaders() {
		return Collections.emptyList();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "%s [fields=%s]"
				.formatted(name(), fields);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <K, V> V get(K key) {
		return (V) elementMap.get(key);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#size()
	 */
	@Override
	public int size() {
		return elements.size();
	}

	public ByteBuffer buffer() {
		return header.buffer();
	}

	public int offset() {
		return header.offset();
	}

	public int length() {
		return header.length();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#findKey(java.lang.Object)
	 */
	@Override
	public <K, V> Optional<V> findKey(K key) {
		if (key instanceof String name)
			return Optional.ofNullable((V) elementMap.get(name));

		return Optional.empty();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#findDomain(java.lang.String)
	 */
	@Override
	public MetaDomain findDomain(String name) {
		return (MetaDomain) findKey(name).orElse(null);
	}
}
