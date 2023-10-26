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

import java.util.List;
import java.util.stream.IntStream;

/**
 * The Interface JsonArray.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public interface JsonArray extends JsonStructure, Iterable<JsonValue> {

	/**
	 * Clear.
	 */
	void clear();

	/**
	 * Gets the boolean.
	 *
	 * @param index the index
	 * @return the boolean
	 */
	boolean getBoolean(int index);

	/**
	 * Gets the boolean.
	 *
	 * @param index        the index
	 * @param defaultValue the default value
	 * @return the boolean
	 */
	boolean getBoolean(int index, boolean defaultValue);

	/**
	 * Gets the int.
	 *
	 * @param index the index
	 * @return the int
	 */
	int getInt(int index);

	/**
	 * Gets the int.
	 *
	 * @param index        the index
	 * @param defaultValue the default value
	 * @return the int
	 */
	int getInt(int index, int defaultValue);

	/**
	 * Gets the json array.
	 *
	 * @param index the index
	 * @return the json array
	 */
	JsonArray getJsonArray(int index);

	/**
	 * Gets the json number.
	 *
	 * @param index the index
	 * @return the json number
	 */
	JsonNumber getJsonNumber(int index);

	/**
	 * Gets the json object.
	 *
	 * @param index the index
	 * @return the json object
	 */
	JsonObject getJsonObject(int index);

	/**
	 * Gets the json string.
	 *
	 * @param index the index
	 * @return the json string
	 */
	JsonString getJsonString(int index);

	/**
	 * Gets the value type.
	 *
	 * @param index the index
	 * @return the value type
	 */
	ValueType getValueType(int index);

	/**
	 * Checks if is string.
	 *
	 * @param index the index
	 * @return true, if is string
	 */
	default boolean isString(int index) {
		return getValueType(index) == ValueType.STRING;
	}

	/**
	 * Gets the string.
	 *
	 * @param index the index
	 * @return the string
	 */
	String getString(int index);

	/**
	 * Gets the string.
	 *
	 * @param index        the index
	 * @param defaultValue the default value
	 * @return the string
	 */
	String getString(int index, String defaultValue);

	/**
	 * Gets the values as.
	 *
	 * @param <T>   the generic type
	 * @param clazz the clazz
	 * @return the values as
	 */
	<T extends JsonValue> List<T> getValuesAs(Class<T> clazz);

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	boolean isEmpty();

	/**
	 * Checks if is null.
	 *
	 * @param index the index
	 * @return true, if is null
	 */
	boolean isNull(int index);

	/**
	 * Size.
	 *
	 * @return the int
	 */
	int size();

	/**
	 * To string array. Json array can contain any type of value object, and only
	 * string object types are filtered out and returned as array by this method.
	 *
	 * @return an array of Java string objects converted from {@code JsonString}
	 *         object types
	 */
	default String[] toStringArray() {
		return IntStream.range(0, size())
				.filter(this::isString)
				.mapToObj(this::getJsonString)
				.map(JsonString::getString)
				.toArray(String[]::new);

	}
}
