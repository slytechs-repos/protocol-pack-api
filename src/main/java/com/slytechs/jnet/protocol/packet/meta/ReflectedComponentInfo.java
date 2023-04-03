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

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
abstract class ReflectedComponentInfo implements MetaInfo {

	private final MetaInfo metaInfo;
	private final DisplaysInfo displays;

	/**
	 * 
	 */
	public ReflectedComponentInfo(MetaInfo metaInfo, DisplaysInfo displays) {
		this.metaInfo = metaInfo;
		this.displays = displays;
	}

	@Override
	public String name() {
		return metaInfo.name();
	}

	@Override
	public String abbr() {
		return metaInfo.abbr();
	}

	@Override
	public String note() {
		return metaInfo.note();
	}

	@Override
	public int ordinal() {
		return metaInfo.ordinal();
	}

	protected final boolean isStatic(Member type) {
		return (type.getModifiers() & Modifier.STATIC) != 0;
	}

	protected final Throwable unwindCauseException(Throwable e) {
		while (e.getCause() != null)
			e = e.getCause();

		return e;
	}
	
	/**
	 * @return
	 */
	protected abstract DisplaysInfo getDisplaysInfo();

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ""
				+ getClass().getSimpleName()
				+ " ["
				+ "name=" + name()
				+ ", abbr=" + abbr()
				+ ", note=" + note()
				+ "]";
	}

}
