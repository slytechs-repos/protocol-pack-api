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
package com.slytechs.protocol.runtime.internal.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class JsonObjectBuilder.
 */
public final class JsonObjectBuilder {

	public static JsonObjectBuilder wrapOrElseNewInstance(JsonObject source) {
		if (source == null)
			return new JsonObjectBuilder();

		if (!(source instanceof ObjectImpl jsObj)) {
			throw new IllegalStateException("can not wrap around non-standard json object implementation [%s]"
					.formatted(source.getClass()));
		}

		return new JsonObjectBuilder(jsObj.map, jsObj.list);
	}

	/** The map. */
	private final Map<String, JsonValue> map;
	private final List<String> list;

	public JsonObjectBuilder() {
		this.map = new HashMap<>();
		this.list = new ArrayList<>();
	}

	private JsonObjectBuilder(Map<String, JsonValue> map, List<String> list) {
		this.map = map;
		this.list = list;
	}

	/**
	 * Adds the.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the json object builder
	 */
	public JsonObjectBuilder add(String key, String value) {
		map.put(key, new StringImpl(value));
		list.add(key);

		return this;
	}

	/**
	 * Adds the.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the json object builder
	 */
	public JsonObjectBuilder add(String key, Number value) {
		map.put(key, new NumberImpl(value));
		list.add(key);

		return this;
	}

	/**
	 * Adds the.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the json object builder
	 */
	public JsonObjectBuilder add(String key, JsonValue value) {
		map.put(key, value);
		list.add(key);

		return this;
	}

	/**
	 * Gets the json object.
	 *
	 * @param name the name
	 * @return the json object
	 */
	public JsonObject getJsonObject(String name) {
		return get(name, JsonObject.class);
	}

	/**
	 * Gets the json array.
	 *
	 * @param name the name
	 * @return the json array
	 */
	public JsonArray getJsonArray(String name) {
		return get(name, JsonArray.class);
	}

	/**
	 * Gets the.
	 *
	 * @param <T>  the generic type
	 * @param name the name
	 * @param type the type
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	private <T extends JsonValue> T get(String name, Class<T> type) {
		return (T) map.get(name);
	}

	/**
	 * Builds the.
	 *
	 * @return the json object
	 */
	public JsonObject build() {
		return new ObjectImpl(map, list);
	}
}