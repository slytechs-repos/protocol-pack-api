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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The Class ObjectImpl.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class ObjectImpl implements JsonObject {

	/** The map. */
	final Map<String, JsonValue> map;
	final List<String> list;

	/**
	 * Instantiates a new object impl.
	 */
	public ObjectImpl() {
		this.map = new HashMap<>();
		this.list = new ArrayList<>();
	}

	/**
	 * Instantiates a new object impl.
	 *
	 * @param map the map
	 */
	public ObjectImpl(Map<String, JsonValue> map, List<String> list) {
		this.map = map;
		this.list = list;
	}

	/**
	 * Clear.
	 *
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#clear()
	 */
	@Override
	public void clear() {
		map.clear();
	}

	/**
	 * Entry set.
	 *
	 * @return the sets the
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#entrySet()
	 */
	@Override
	public Set<Entry<String, JsonValue>> entrySet() {
		return map.entrySet();
	}

	/**
	 * Gets the.
	 *
	 * @param name the name
	 * @return the json value
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#get(java.lang.String)
	 */
	@Override
	public JsonValue get(String name) {
		return map.get(name);
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
		return (T) get(name);
	}

	/**
	 * Gets the boolean.
	 *
	 * @param name the name
	 * @return the boolean
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(String name) {
		return get(name, JsonBooleanImpl.class).isTrue();
	}

	/**
	 * Gets the boolean.
	 *
	 * @param name         the name
	 * @param defaultValue the default value
	 * @return the boolean
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#getBoolean(java.lang.String,
	 *      boolean)
	 */
	@Override
	public boolean getBoolean(String name, boolean defaultValue) {
		JsonBooleanImpl v = get(name, JsonBooleanImpl.class);
		if (v == null)
			return defaultValue;

		return v.isTrue();
	}

	/**
	 * Gets the int.
	 *
	 * @param name the name
	 * @return the int
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String name) {
		return get(name, JsonNumber.class).intValue();
	}

	/**
	 * Gets the int.
	 *
	 * @param name         the name
	 * @param defaultValue the default value
	 * @return the int
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#getInt(java.lang.String,
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
	 * Gets the json array.
	 *
	 * @param name the name
	 * @return the json array
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#getJsonArray(java.lang.String)
	 */
	@Override
	public JsonArray getJsonArray(String name) {
		return get(name, JsonArray.class);
	}

	/**
	 * Gets the json number.
	 *
	 * @param name the name
	 * @return the json number
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#getJsonNumber(java.lang.String)
	 */
	@Override
	public JsonNumber getJsonNumber(String name) {
		return get(name, JsonNumber.class);
	}

	/**
	 * Gets the json object.
	 *
	 * @param name the name
	 * @return the json object
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#getJsonObject(java.lang.String)
	 */
	@Override
	public JsonObject getJsonObject(String name) {
		return get(name, JsonObject.class);
	}

	/**
	 * Gets the json string.
	 *
	 * @param name the name
	 * @return the json string
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#getJsonString(java.lang.String)
	 */
	@Override
	public JsonString getJsonString(String name) {
		return get(name, JsonString.class);
	}

	/**
	 * Gets the not null.
	 *
	 * @param <T>  the generic type
	 * @param name the name
	 * @param type the type
	 * @return the not null
	 */
	@SuppressWarnings("unchecked")
	private <T extends JsonValue> T getNotNull(String name, Class<T> type) {
		T t = (T) get(name);
		if (t == null)
			throw new NullPointerException(name);

		return t;
	}

	/**
	 * Gets the string.
	 *
	 * @param name the name
	 * @return the string
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#getString(java.lang.String)
	 */
	@Override
	public String getString(String name) {
		return get(name, JsonString.class).getString();
	}

	/**
	 * Gets the string.
	 *
	 * @param name         the name
	 * @param defaultValue the default value
	 * @return the string
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#getString(java.lang.String,
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
	 * Gets the value type.
	 *
	 * @return the value type
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonValue#getValueType()
	 */
	@Override
	public ValueType getValueType() {
		return ValueType.OBJECT;
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * Checks if is null.
	 *
	 * @param name the name
	 * @return true, if is null
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#isNull(java.lang.String)
	 */
	@Override
	public boolean isNull(String name) {
		return getNotNull(name, JsonNullImpl.class).isNull();
	}

	/**
	 * Key set.
	 *
	 * @return the sets the
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#keyOrderedList()
	 */
	@Override
	public List<String> keyOrderedList() {
		return list;
	}

	/**
	 * Put.
	 *
	 * @param name  the name
	 * @param value the value
	 */
	public void put(String name, JsonValue value) {
		map.put(name, value);
		list.add(name);
	}

	/**
	 * Size.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#size()
	 */
	@Override
	public int size() {
		return map.size();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return map.toString();
	}

	/**
	 * Values.
	 *
	 * @return the collection
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#values()
	 */
	@Override
	public Collection<JsonValue> values() {
		return map.values();
	}

	/**
	 * Checks if is present.
	 *
	 * @param name the name
	 * @return true, if is present
	 * @see com.slytechs.jnet.jnetruntime.internal.json.JsonObject#isPresent(java.lang.String)
	 */
	@Override
	public boolean isPresent(String name) {
		return map.containsKey(name);
	}
}
