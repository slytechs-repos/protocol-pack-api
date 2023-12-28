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
package com.slytechs.jnet.jnetruntime.internal.json;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public class JsonObjectCache {

	private final Map<String, JsonObject> map;

	public JsonObjectCache() {
		this(new HashMap<>());
	}

	public JsonObjectCache(Map<String, JsonObject> mapUsedForCaching) {
		this.map = Objects.requireNonNull(mapUsedForCaching);
	}

	public synchronized JsonObject computeIfAbsent(String name, Supplier<InputStream> in) {
		if (map.containsKey(name))
			return map.get(name);

		InputStream ins = in.get();
		if (ins == null)
			return null;

		try (var reader = Json.createReader(ins)) {
			JsonObject obj = reader.readObject();
			map.put(name, obj);

			return obj;
		} catch (JsonException e) {
			throw new IllegalStateException("unexpected error while reading resource " + name, e);
		}
	}
}
