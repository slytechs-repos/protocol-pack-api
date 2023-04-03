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

import java.util.Optional;

import com.slytechs.jnet.protocol.packet.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.packet.meta.MetaContext.MetaMapped;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public abstract class MetaElement
		implements Comparable<MetaElement>, MetaDomain {

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String className = getClass().getSimpleName();
		return className
				+ " [name=" + name() + "]";
	}

	protected final ReflectedComponent meta;
	private final MetaType metaType;
	private final MetaDomain domain;

	protected MetaElement(MetaDomain domain, ReflectedComponent metaContainer) {
		this.meta = metaContainer;
		this.metaType = metaContainer.getMetaType(MetaInfo.class).metaType();

		if (this instanceof MetaMapped mapped) {
			this.domain = MetaMapped.Proxy.of(domain, mapped);
		} else
			this.domain = domain;
	}

	public final String name() {
		return meta.name();
	}

	public final String abbr() {
		return meta.abbr();
	}

	public final String note() {
		return meta.note();
	}

	public final int ordinal() {
		return meta.ordinal();
	}

	public final MetaType metaType() {
		return metaType;
	}

	public final boolean isField() {
		return (metaType == MetaType.FIELD);
	}

	public final boolean isAttribute() {
		return (metaType == MetaType.ATTRIBUTE);
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(MetaElement o) {
		return ordinal() - o.ordinal();
	}

	public final <T extends MetaInfoType> T getMeta(Class<T> type) {
		return meta.getMetaType(type);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#searchForDomain(com.slytechs.jnet.protocol.packet.meta.MetaPath)
	 */
	@Override
	public MetaContext searchForDomain(MetaPath path) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#searchForField(com.slytechs.jnet.protocol.packet.meta.MetaPath)
	 */
	@Override
	public Optional<MetaField> searchForField(MetaPath path) {
		throw new UnsupportedOperationException();

	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#searchFor(MetaPath,
	 *      java.lang.Object, Class)
	 */
	@Override
	public <K, V> Optional<V> searchFor(MetaPath domain, K key, Class<V> valueType) {
		throw new UnsupportedOperationException();
	}

}
