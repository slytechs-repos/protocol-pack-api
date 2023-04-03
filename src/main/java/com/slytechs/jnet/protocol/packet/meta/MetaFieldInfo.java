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

import com.slytechs.jnet.protocol.packet.meta.MetaValue.ValueResolver;
import com.slytechs.jnet.runtime.util.Detail;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class MetaFieldInfo implements MetaInfo {

	private final ReflectedMemberInfo reflectedMetaInfo;
	private final DisplaysInfo displaysInfo;

	public MetaFieldInfo(ReflectedMemberInfo metaInfo) {
		this.displaysInfo = metaInfo.getDisplaysInfo();
		this.reflectedMetaInfo = metaInfo;
	}

	/**
	 * @return
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaInfo#abbr()
	 */
	@Override
	public String abbr() {
		return reflectedMetaInfo.abbr();
	}

	/**
	 * @return
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaInfo#name()
	 */
	@Override
	public String name() {
		return reflectedMetaInfo.name();
	}

	/**
	 * @return
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaInfo#note()
	 */
	@Override
	public String note() {
		return reflectedMetaInfo.note();
	}

	/**
	 * @return
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaInfo#ordinal()
	 */
	@Override
	public int ordinal() {
		return reflectedMetaInfo.ordinal();
	}

	public <T> T getValue(Object target, Object... args) {
		return reflectedMetaInfo.getValue(target, args);
	}

	public void setValue(Object target, Object... args) {
		reflectedMetaInfo.setValue(target, args);
	}

	public Class<?> getValueType() {
		return reflectedMetaInfo.getValueType();
	}

	public ValueResolver[] getValueResolvers() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param detail
	 * @return
	 */
	public DisplayInfo getDisplayInfo(Detail detail) {
		return displaysInfo.select(detail);
	}
}
