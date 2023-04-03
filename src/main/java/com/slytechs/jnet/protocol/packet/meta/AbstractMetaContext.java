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

import java.util.Objects;
import java.util.Optional;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class AbstractMetaContext implements MetaDomain, MetaContext {
	static class Global extends MapMetaContext {

		static final MapMetaContext GLOBAL_STATIC_CTX = new Global();

		public Global() {
			super(GLOBAL_STATIC_CTX, "Global");
		}

		/**
		 * @see com.slytechs.jnet.protocol.packet.meta.AbstractMetaContext#searchForDomain(com.slytechs.jnet.protocol.packet.meta.MetaPath)
		 */
		@Override
		public MetaContext searchForDomain(MetaPath path) {
			return null; // We're at the top
		}

		/**
		 * @see com.slytechs.jnet.protocol.packet.meta.AbstractMetaContext#searchForField(com.slytechs.jnet.protocol.packet.meta.MetaPath)
		 */
		@Override
		public Optional<MetaField> searchForField(MetaPath path) {
			if (path.size() == 1)
				return Optional.ofNullable(getField(path.toStringLast()));

			return Optional.empty();
		}
	}

	private final MetaContext parent;
	private final String name;

	protected AbstractMetaContext(MetaContext parent, String name) {
		this.parent = Objects.requireNonNull(parent, "parent domain");
		this.name = name;

		if (name.equals(GLOBAL_DOMAINNAME))
			throw new IllegalArgumentException("reserved context name [%s]"
					.formatted(name));
	}

	protected AbstractMetaContext(String name) {

		this.name = name;
		this.parent = name.equals(GLOBAL_DOMAINNAME)
				? null
				: MetaDomain.getGlobalDomain();

		if ((getClass() != Global.class) && (name.equals(GLOBAL_DOMAINNAME)))
			throw new IllegalArgumentException("reserved context name [%s]"
					.formatted(name));
	}

	public final String name() {
		return name;
	}

	public final MetaDomain parent() {
		return parent;
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#searchForDomain(com.slytechs.jnet.protocol.packet.meta.MetaPath)
	 */
	@Override
	public MetaContext searchForDomain(MetaPath path) {
		if (path.match(GLOBAL_DOMAINNAME))
			return MetaDomain.getGlobalDomain();

		if (path.isUp())
			return parent.searchForDomain(path.pop());

		return (path.size() == 1) && path.matchFirst(name())
				? this
				: parent.searchForDomain(path);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#searchForField(com.slytechs.jnet.protocol.packet.meta.MetaPath)
	 */
	@Override
	public Optional<MetaField> searchForField(MetaPath path) {

		assert getClass() != Global.class && parent != null;

		if (path.match(GLOBAL_DOMAINNAME))
			return MetaDomain.getGlobalDomain().searchForField(path.pop());

		if (path.isUp())
			return parent.searchForDomain(path.pop()).searchForField(path.pop());

		return parent
				.searchForDomain(path)
				.searchForField(path.last());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String flags = null;

		return ""
				+ getClass().getSimpleName()
				+ " ["
				+ "name=" + name
				+ (flags != null ? " flags=" + flags : "")
				+ "]";
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaDomain#searchFor(java.lang.Object,
	 *      Class)
	 */
	@Override
	public <K, V> Optional<V> searchFor(K key, Class<V> valueType) {
		return Optional.empty();
	}
}
