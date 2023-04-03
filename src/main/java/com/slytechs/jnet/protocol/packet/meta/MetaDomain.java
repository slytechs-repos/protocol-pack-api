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
import java.util.function.Function;

import com.slytechs.jnet.protocol.packet.meta.AbstractMetaContext.Global;

public interface MetaDomain {

	public static MapMetaContext getGlobalDomain() {
		return Global.GLOBAL_STATIC_CTX;
	}

	static <K, V> V global(K key) {
		return getGlobalDomain().get(key);
	}

	static <K, V> V global(K key, Function<K, V> func) {
		return getGlobalDomain().getOrCompute(key, func);
	}

	MetaContext searchForDomain(MetaPath path);

	Optional<MetaField> searchForField(MetaPath path);

	<K, V> Optional<V> searchFor(K key, Class<V> valueType);
	
	<K, V> Optional<V> searchFor(MetaPath domain, K key, Class<V> valueType);
}
