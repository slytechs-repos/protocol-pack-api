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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.slytechs.jnet.protocol.packet.meta.MetaValue.ValueResolver;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public final class MetaField extends MetaElement {

	private final MetaHeader header;
	private final ReflectedMember member;
	private final ValueResolver[] resolvers;
	private final Object target;

	/**
	 * @param memberInfo
	 */
	MetaField(MetaHeader header, ReflectedMember memberInfo) {
		super(header, memberInfo);
		this.target = header.getTarget();
		this.header = header;
		this.member = memberInfo;
		memberInfo.getMetaType(MetaInfo.class).metaType();
		var resolversInfo = memberInfo.getMetaType(ResolversInfo.class);

		if (resolversInfo != null) {
			this.resolvers = resolversInfo.resolvers();

			assert Stream.of(resolversInfo.resolvers())
					.filter(r -> r == null)
					.findAny()
					.isEmpty() : "Empty resolver for field [%s]".formatted(memberInfo.name());
		} else
			this.resolvers = new ValueResolver[0];
	}

	public MetaHeader getParentHeader() {
		return header;
	}

	public List<MetaField> listSubFields() {
		return Collections.emptyList();
	}

	public <V> V get() {
		return member.getValue(target);
	}

	public <V> void set(V newValue) {
		member.setValue(target, newValue);
	}

	public String stringValue() {
		return get();
	}

	public String getFormatted() {
		Object value = member.getValue(target);
		MetaInfo meta = getMeta(MetaInfo.class);

		return meta.formatter().format(value);
	}

	public String getResolved(int resolverIndex) {
		if (resolverIndex >= resolvers.length)
			return "";

		return resolvers[resolverIndex].resolveValue(get());
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#findKey(java.lang.Object)
	 */
	@Override
	public <K, V> Optional<V> findKey(K key) {
		if (name().equals(key))
			return Optional.of((V) this);

		return Optional.empty();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#findDomain(java.lang.String)
	 */
	@Override
	public MetaDomain findDomain(String name) {
		return null;
	}

}
