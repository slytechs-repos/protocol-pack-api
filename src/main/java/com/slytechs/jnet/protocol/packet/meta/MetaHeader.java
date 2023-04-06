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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.slytechs.jnet.protocol.packet.Header;
import com.slytechs.jnet.protocol.packet.Packet;
import com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped;

/**
 * The Class MetaHeader.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public final class MetaHeader
		extends MetaElement
		implements Iterable<MetaField>, MetaMapped {

	/** The header. */
	private final Header header;

	/** The fields. */
	private final List<MetaField> fields;
	
	/** The attributes. */
	private final List<MetaField> attributes;
	
	/** The elements. */
	private final List<MetaField> elements;
	
	/** The element map. */
	private volatile Map<String, MetaField> elementMap;

	/**
	 * Instantiates a new meta header.
	 *
	 * @param domain         the domain
	 * @param target         the target
	 * @param reflectedClass the reflected class
	 */
	private MetaHeader(MetaDomain domain, Header target, ReflectedClass reflectedClass) {
		super(domain, reflectedClass);
		this.header = target;
		this.elements = Arrays.stream(reflectedClass.getFields())
				.map(reflectedMember -> new MetaField(this, reflectedMember))
				.collect(Collectors.toList());

		this.fields = elements.stream().filter(MetaField::isField).toList();
		this.attributes = elements.stream().filter(MetaField::isAttribute).toList();
		this.elementMap = new HashMap<>();

		fields.forEach(e -> elementMap.put(e.name(), e));
		attributes.forEach(e -> elementMap.put(e.name(), e));
	}

	/**
	 * Instantiates a new meta header.
	 *
	 * @param packet the packet
	 * @param header the header
	 */
	public MetaHeader(Packet packet, Header header) {
		this(MetaContext.newRoot(), new MetaPacket(packet), header);
	}

	/**
	 * Instantiates a new meta header.
	 *
	 * @param ctx    the ctx
	 * @param packet the packet
	 * @param header the header
	 */
	public MetaHeader(MetaDomain ctx, MetaPacket packet, Header header) {
		this(ctx,
				header,
				Global.compute(header.getClass(), ReflectedClass::parse));
	}

	/**
	 * Instantiates a new meta header.
	 *
	 * @param ctx    the ctx
	 * @param header the header
	 */
	public MetaHeader(MetaDomain ctx, Header header) {
		this(ctx,
				header,
				Global.compute(header.getClass(), ReflectedClass::parse));
	}

	/**
	 * Gets the extension.
	 *
	 * @param name the name
	 * @return the extension
	 */
	public MetaHeader getExtension(String name) {
		return null;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#getField(java.lang.String)
	 */
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

	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public Object getTarget() {
		return header;
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<MetaField> iterator() {
		return fields.iterator();
	}

	/**
	 * List attributes.
	 *
	 * @return the list
	 */
	public List<MetaField> listAttributes() {
		return attributes;
	}

	/**
	 * List fields.
	 *
	 * @return the list
	 */
	public List<MetaField> listFields() {
		return fields;
	}

	/**
	 * List all elements.
	 *
	 * @return the list
	 */
	public List<MetaField> listAllElements() {
		return elements;
	}

	/**
	 * List sub headers.
	 *
	 * @return the list
	 */
	public List<MetaHeader> listSubHeaders() {
		return Collections.emptyList();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "%s [fields=%s]"
				.formatted(name(), fields);
	}

	/**
	 * Gets the.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param key the key
	 * @return the v
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <K, V> V get(K key) {
		return (V) elementMap.get(key);
	}

	/**
	 * Size.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped#size()
	 */
	@Override
	public int size() {
		return elements.size();
	}

	/**
	 * Buffer.
	 *
	 * @return the byte buffer
	 */
	public ByteBuffer buffer() {
		return header.buffer();
	}

	/**
	 * Offset.
	 *
	 * @return the int
	 */
	public int offset() {
		return header.offset();
	}

	/**
	 * Length.
	 *
	 * @return the int
	 */
	public int length() {
		return header.length();
	}

	/**
	 * Find key.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param key the key
	 * @return the optional
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#findKey(java.lang.Object)
	 */
	@Override
	public <K, V> Optional<V> findKey(K key) {
		if (key instanceof String name)
			return Optional.ofNullable((V) elementMap.get(name));

		return Optional.empty();
	}

	/**
	 * Find domain.
	 *
	 * @param name the name
	 * @return the meta domain
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#findDomain(java.lang.String)
	 */
	@Override
	public MetaDomain findDomain(String name) {
		return (MetaDomain) findKey(name).orElse(null);
	}
}
