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
import java.util.Iterator;
import java.util.List;

/**
 * The Class ArrayImpl.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class ArrayImpl implements JsonArray {

	/** The list. */
	private final List<JsonValue> list ;
	
	/**
	 * Instantiates a new array impl.
	 */
	public ArrayImpl() {
		this.list = new ArrayList<>();
	}
	
	/**
	 * Instantiates a new array impl.
	 *
	 * @param list the list
	 */
	public ArrayImpl(List<JsonValue> list) {
		this.list = list;
	}

	/**
	 * Adds the.
	 *
	 * @param value the value
	 */
	public void add(JsonValue value) {
		list.add(value);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return list.toString();
	}

	/**
	 * Gets the value type.
	 *
	 * @return the value type
	 * @see com.slytechs.protocol.runtime.internal.json.JsonValue#getValueType()
	 */
	@Override
	public ValueType getValueType() {
		return ValueType.ARRAY;
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<JsonValue> iterator() {
		return list.iterator();
	}

	/**
	 * Gets the boolean.
	 *
	 * @param index the index
	 * @return the boolean
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int index) {
		return get(index, JsonBooleanImpl.class).isTrue();
	}

	/**
	 * Gets the boolean.
	 *
	 * @param index        the index
	 * @param defaultValue the default value
	 * @return the boolean
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#getBoolean(int,
	 *      boolean)
	 */
	@Override
	public boolean getBoolean(int index, boolean defaultValue) {
		JsonBooleanImpl v = getNullable(index, JsonBooleanImpl.class);
		if (v == null)
			return defaultValue;

		return v.isTrue();
	}

	/**
	 * Gets the int.
	 *
	 * @param index the index
	 * @return the int
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#getInt(int)
	 */
	@Override
	public int getInt(int index) {
		return get(index, JsonNumber.class).intValue();
	}

	/**
	 * Gets the int.
	 *
	 * @param index        the index
	 * @param defaultValue the default value
	 * @return the int
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#getInt(int, int)
	 */
	@Override
	public int getInt(int index, int defaultValue) {
		JsonNumber v = getNullable(index, JsonNumber.class);
		if (v == null)
			return defaultValue;

		return v.intValue();
	}

	/**
	 * Gets the string.
	 *
	 * @param index the index
	 * @return the string
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#getString(int)
	 */
	@Override
	public String getString(int index) {
		return get(index, JsonString.class).getString();
	}

	/**
	 * Gets the string.
	 *
	 * @param index        the index
	 * @param defaultValue the default value
	 * @return the string
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#getString(int,
	 *      java.lang.String)
	 */
	@Override
	public String getString(int index, String defaultValue) {
		JsonString v = getNullable(index, JsonString.class);
		if (v == null)
			return defaultValue;

		return v.getString();
	}

	/**
	 * Gets the json array.
	 *
	 * @param index the index
	 * @return the json array
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#getJsonArray(int)
	 */
	@Override
	public JsonArray getJsonArray(int index) {
		return get(index, JsonArray.class);
	}

	/**
	 * Gets the json number.
	 *
	 * @param index the index
	 * @return the json number
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#getJsonNumber(int)
	 */
	@Override
	public JsonNumber getJsonNumber(int index) {
		return get(index, JsonNumber.class);
	}

	/**
	 * Gets the json object.
	 *
	 * @param index the index
	 * @return the json object
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#getJsonObject(int)
	 */
	@Override
	public JsonObject getJsonObject(int index) {
		return get(index, JsonObject.class);
	}

	/**
	 * Gets the json string.
	 *
	 * @param index the index
	 * @return the json string
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#getJsonString(int)
	 */
	@Override
	public JsonString getJsonString(int index) {
		return get(index, JsonString.class);
	}

	/**
	 * Gets the.
	 *
	 * @param <T>   the generic type
	 * @param index the index
	 * @param type  the type
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	private <T extends JsonValue> T get(int index, Class<T> type) {
		return (T) list.get(index);
	}

	/**
	 * Gets the nullable.
	 *
	 * @param <T>   the generic type
	 * @param index the index
	 * @param type  the type
	 * @return the nullable
	 */
	@SuppressWarnings("unchecked")
	private <T extends JsonValue> T getNullable(int index, Class<T> type) {
		if (index < 0 || index >= list.size())
			return null;

		return (T) list.get(index);
	}

	/**
	 * Checks if is null.
	 *
	 * @param index the index
	 * @return true, if is null
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#isNull(int)
	 */
	@Override
	public boolean isNull(int index) {
		return get(index, JsonNullImpl.class).isNull();
	}

	/**
	 * Size.
	 *
	 * @return the int
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#size()
	 */
	@Override
	public int size() {
		return list.size();
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * Clear.
	 *
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#clear()
	 */
	@Override
	public void clear() {
		list.clear();
	}

	/**
	 * Gets the values as.
	 *
	 * @param <T>   the generic type
	 * @param clazz the clazz
	 * @return the values as
	 * @see com.slytechs.protocol.runtime.internal.json.JsonArray#getValuesAs(java.lang.Class)
	 */
	@SuppressWarnings({ "unchecked",
			"rawtypes" })
	@Override
	public <T extends JsonValue> List<T> getValuesAs(Class<T> clazz) {
		List l = this.list;

		return l;
	}

}
