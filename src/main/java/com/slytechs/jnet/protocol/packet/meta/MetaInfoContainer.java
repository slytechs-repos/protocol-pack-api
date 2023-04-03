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

import java.lang.reflect.AnnotatedElement;

import com.slytechs.jnet.runtime.util.json.JsonObject;

public class MetaInfoContainer2 implements MetaInfo.Proxy {

	public interface MetaInfoPraser<T extends MetaInfoType> {
		T parse(AnnotatedElement element, String name, JsonObject jsonDefaults);
	}

	private final AnnotatedElement element;
	private final JsonObject jsonDefaults;
	private final String name;
	private final MetaInfo metaInfo;
	private final DisplaysInfo displaysInfo;
	private final ResolversInfo resolversInfo;

	public MetaInfoContainer(AnnotatedElement element, String name, JsonObject jsonDefaults) {
		this.element = element;
		this.name = name;
		this.jsonDefaults = jsonDefaults;
		this.metaInfo = MetaInfo.parse(element, name, jsonDefaults);
		this.displaysInfo = DisplaysInfo.parse(element, name, jsonDefaults);
		this.resolversInfo = ResolversInfo.parse(element, name, jsonDefaults);
	}

	@SuppressWarnings("unchecked")
	public <T extends MetaInfoType> T getMetaType(Class<T> type) {
		if (type == MetaInfoContainer.class)
			return (T) this;

		if (type == MetaInfo.class)
			return (T) this;

		if (type == DisplaysInfo.class)
			return (T) displaysInfo;

		if (type == ResolversInfo.class)
			return (T) resolversInfo;

		return null;
	}

	<T extends MetaInfoType> T getMetaInfo(Class<T> type, MetaInfoPraser<T> parser) {
		return parser.parse(element, name, jsonDefaults);
	}

	/**
	 * @see com.slytechs.jnet.protocol.packet.meta.MetaInfo.Proxy#getProxy()
	 */
	@Override
	public MetaInfo getProxy() {
		return this.metaInfo;
	}

}
