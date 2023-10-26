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
package com.slytechs.protocol.runtime.internal.util;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 * A cache utility class backed by WeakHashMap to garbage collect entries that
 * haven't been used recently.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @param <K> the key type
 * @param <V> the value type
 */
public final class WeakNamedCache<K, V> {

	/** The cache. */
	private final Map<String, Map<K, V>> cache = new WeakHashMap<>();

	/**
	 * Instantiates a new weak named cache.
	 */
	public WeakNamedCache() {
	}

	/**
	 * Gets a map from cache if present and computes a new map if necessary. This
	 * method is thread safe.
	 *
	 * @param cacheName       the cache name
	 * @param mappingFunction the mapping function to compute a new map
	 * @return either cached or newly computed map
	 */
	public synchronized Map<K, V> computeIfAbsent(String cacheName, Function<String, Map<K, V>> mappingFunction) {
		Map<K, V> value = cache.computeIfAbsent(cacheName, mappingFunction::apply);

		return value;
	}
}
