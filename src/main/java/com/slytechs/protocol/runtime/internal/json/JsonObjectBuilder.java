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

import java.util.HashMap;
import java.util.Map;

/**
 * The Class JsonObjectBuilder.
 */
public final class JsonObjectBuilder {

	/** The map. */
	private Map<String, JsonValue> map = new HashMap<>();

	/**
	 * Adds the.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the json object builder
	 */
	public JsonObjectBuilder add(String key, String value) {
		map.put(key, new StringImpl(value));

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

		return this;
	}

	/**
	 * Builds the.
	 *
	 * @return the json object
	 */
	public JsonObject build() {
		return new ObjectImpl(map);
	}
}