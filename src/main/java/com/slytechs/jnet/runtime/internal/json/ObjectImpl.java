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
package com.slytechs.jnet.runtime.internal.json;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class ObjectImpl implements JsonObject {

	private final Map<String, JsonValue> map;

	public ObjectImpl() {
		this.map = new HashMap<>();
	}

	public ObjectImpl(Map<String, JsonValue> map) {
		this.map = map;
	}
	
	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<Entry<String, JsonValue>> entrySet() {
		return map.entrySet();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonObject#get(java.lang.String)
	 */
	@Override
	public JsonValue get(String name) {
		return map.get(name);
	}

	@SuppressWarnings("unchecked")
	private <T extends JsonValue> T get(String name, Class<T> type) {
		return (T) get(name);
	}

	@Override
	public boolean getBoolean(String name) {
		return get(name, JsonBooleanImpl.class).isTrue();
	}

	@Override
	public boolean getBoolean(String name, boolean defaultValue) {
		JsonBooleanImpl v = get(name, JsonBooleanImpl.class);
		if (v == null)
			return defaultValue;

		return v.isTrue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonObject#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String name) {
		return get(name, JsonNumber.class).intValue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonObject#getInt(java.lang.String,
	 *      int)
	 */
	@Override
	public int getInt(String name, int defaultValue) {
		JsonNumber v = get(name, JsonNumber.class);
		if (v == null)
			return defaultValue;

		return v.intValue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonObject#getJsonArray(java.lang.String)
	 */
	@Override
	public JsonArray getJsonArray(String name) {
		return get(name, JsonArray.class);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonObject#getJsonNumber(java.lang.String)
	 */
	@Override
	public JsonNumber getJsonNumber(String name) {
		return get(name, JsonNumber.class);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonObject#getJsonObject(java.lang.String)
	 */
	@Override
	public JsonObject getJsonObject(String name) {
		return get(name, JsonObject.class);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonObject#getJsonString(java.lang.String)
	 */
	@Override
	public JsonString getJsonString(String name) {
		return get(name, JsonString.class);
	}

	@SuppressWarnings("unchecked")
	private <T extends JsonValue> T getNotNull(String name, Class<T> type) {
		T t = (T) get(name);
		if (t == null)
			throw new NullPointerException(name);

		return t;
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonObject#getString(java.lang.String)
	 */
	@Override
	public String getString(String name) {
		return get(name, JsonString.class).getString();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonObject#getString(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String getString(String name, String defaultValue) {
		JsonString v = get(name, JsonString.class);
		if (v == null)
			return defaultValue;

		return v.getString();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonValue#getValueType()
	 */
	@Override
	public ValueType getValueType() {
		return ValueType.OBJECT;
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonObject#isNull(java.lang.String)
	 */
	@Override
	public boolean isNull(String name) {
		return getNotNull(name, JsonNullImpl.class).isNull();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	/**
	 * @param name
	 * @param value
	 */
	public void put(String name, JsonValue value) {
		map.put(name, value);
	}

	@Override
	public int size() {
		return map.size();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return map.toString();
	}

	@Override
	public Collection<JsonValue> values() {
		return map.values();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonObject#isPresent(java.lang.String)
	 */
	@Override
	public boolean isPresent(String name) {
		return map.containsKey(name);
	}
}
