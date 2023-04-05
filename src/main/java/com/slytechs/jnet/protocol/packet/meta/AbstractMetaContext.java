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

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public abstract class AbstractMetaContext implements MetaDomain, MetaContext {
	private final MetaContext parent;
	private final String name;

	protected AbstractMetaContext(MetaContext parent, String name) {
		this.parent = Objects.requireNonNull(parent, "parent domain");
		this.name = name;

		if (name.equals(MetaPath.GLOBAL_DOMAINNAME))
			throw new IllegalArgumentException("reserved context name [%s]"
					.formatted(name));
	}

	protected AbstractMetaContext(String name) {

		this.name = name;
		this.parent = name.equals(MetaPath.GLOBAL_DOMAINNAME)
				? null
				: MetaDomain.getGlobalDomain();

		if ((getClass() != Global.class) && (name.equals(MetaPath.GLOBAL_DOMAINNAME)))
			throw new IllegalArgumentException("reserved context name [%s]"
					.formatted(name));
	}

	@Override
	public final String name() {
		return name;
	}

	@Override
	public final MetaDomain parent() {
		return parent;
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

}
