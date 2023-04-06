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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
class ArrayImpl implements JsonArray {

	private final List<JsonValue> list ;
	
	public ArrayImpl() {
		this.list = new ArrayList<>();
	}
	
	public ArrayImpl(List<JsonValue> list) {
		this.list = list;
	}

	/**
	 * @param value
	 */
	public void add(JsonValue value) {
		list.add(value);
	}

	@Override
	public String toString() {
		return list.toString();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonValue#getValueType()
	 */
	@Override
	public ValueType getValueType() {
		return ValueType.ARRAY;
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<JsonValue> iterator() {
		return list.iterator();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int index) {
		return get(index, JsonBooleanImpl.class).isTrue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#getBoolean(int, boolean)
	 */
	@Override
	public boolean getBoolean(int index, boolean defaultValue) {
		JsonBooleanImpl v = getNullable(index, JsonBooleanImpl.class);
		if (v == null)
			return defaultValue;

		return v.isTrue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#getInt(int)
	 */
	@Override
	public int getInt(int index) {
		return get(index, JsonNumber.class).intValue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#getInt(int, int)
	 */
	@Override
	public int getInt(int index, int defaultValue) {
		JsonNumber v = getNullable(index, JsonNumber.class);
		if (v == null)
			return defaultValue;

		return v.intValue();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#getString(int)
	 */
	@Override
	public String getString(int index) {
		return get(index, JsonString.class).getString();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#getString(int,
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
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#getJsonArray(int)
	 */
	@Override
	public JsonArray getJsonArray(int index) {
		return get(index, JsonArray.class);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#getJsonNumber(int)
	 */
	@Override
	public JsonNumber getJsonNumber(int index) {
		return get(index, JsonNumber.class);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#getJsonObject(int)
	 */
	@Override
	public JsonObject getJsonObject(int index) {
		return get(index, JsonObject.class);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#getJsonString(int)
	 */
	@Override
	public JsonString getJsonString(int index) {
		return get(index, JsonString.class);
	}

	@SuppressWarnings("unchecked")
	private <T extends JsonValue> T get(int index, Class<T> type) {
		return (T) list.get(index);
	}

	@SuppressWarnings("unchecked")
	private <T extends JsonValue> T getNullable(int index, Class<T> type) {
		if (index < 0 || index >= list.size())
			return null;

		return (T) list.get(index);
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#isNull(int)
	 */
	@Override
	public boolean isNull(int index) {
		return get(index, JsonNullImpl.class).isNull();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#size()
	 */
	@Override
	public int size() {
		return list.size();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#clear()
	 */
	@Override
	public void clear() {
		list.clear();
	}

	/**
	 * @see com.slytechs.jnet.runtime.internal.json.JsonArray#getValuesAs(java.lang.Class)
	 */
	@SuppressWarnings({ "unchecked",
			"rawtypes" })
	@Override
	public <T extends JsonValue> List<T> getValuesAs(Class<T> clazz) {
		List l = this.list;

		return l;
	}

}
