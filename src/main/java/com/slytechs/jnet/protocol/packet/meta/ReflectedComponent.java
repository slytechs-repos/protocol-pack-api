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

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import com.slytechs.jnet.runtime.internal.json.JsonValue.ValueType;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public abstract class ReflectedComponent implements Comparable<ReflectedComponent> {

	private final MetaInfo meta;

	public ReflectedComponent(MetaInfo meta) {
		this.meta = meta;
	}

	/**
	 * @return
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaInfo.Proxy#abbr()
	 */
	public String abbr() {
		return meta.abbr();
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ReflectedComponent o) {
		return ordinal() - o.ordinal();
	}

	/**
	 * @param <T>
	 * @param type
	 * @return
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaInfoContainer#getMetaType(java.lang.Class)
	 */
	public <T extends MetaInfoType> T getMetaType(Class<T> type) {
		return meta.getMetaType(type);
	}

	protected final boolean isStatic(Member type) {
		return (type.getModifiers() & Modifier.STATIC) != 0;
	}

	/**
	 * @return
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaInfo.Proxy#name()
	 */
	public String name() {
		return meta.name();
	}

	/**
	 * @return
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaInfo.Proxy#note()
	 */
	public String note() {
		return meta.note();
	}

	/**
	 * @return
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaInfo.Proxy#ordinal()
	 */
	public int ordinal() {
		return meta.ordinal();
	}


	protected final Throwable unwindCauseException(Throwable e) {
		while (e.getCause() != null)
			e = e.getCause();

		return e;
	}
}
