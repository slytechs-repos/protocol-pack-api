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

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The Interface JsonObject.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public interface JsonObject extends JsonStructure {

	/**
	 * Clear.
	 */
	void clear();

	/**
	 * Entry set.
	 *
	 * @return the sets the
	 */
	Set<Entry<String, JsonValue>> entrySet();

	/**
	 * Gets the.
	 *
	 * @param name the name
	 * @return the json value
	 */
	JsonValue get(String name);

	/**
	 * Gets the boolean.
	 *
	 * @param name the name
	 * @return the boolean
	 */
	boolean getBoolean(String name);

	/**
	 * Gets the boolean.
	 *
	 * @param name         the name
	 * @param defaultValue the default value
	 * @return the boolean
	 */
	boolean getBoolean(String name, boolean defaultValue);

	/**
	 * Gets the int.
	 *
	 * @param name the name
	 * @return the int
	 */
	int getInt(String name);

	/**
	 * Gets the int.
	 *
	 * @param name         the name
	 * @param defaultValue the default value
	 * @return the int
	 */
	int getInt(String name, int defaultValue);

	/**
	 * Gets the json array.
	 *
	 * @param name the name
	 * @return the json array
	 */
	JsonArray getJsonArray(String name);

	/**
	 * Gets the json number.
	 *
	 * @param name the name
	 * @return the json number
	 */
	JsonNumber getJsonNumber(String name);

	/**
	 * Gets the json object.
	 *
	 * @param name the name
	 * @return the json object
	 */
	JsonObject getJsonObject(String name);

	/**
	 * Gets the json string.
	 *
	 * @param name the name
	 * @return the json string
	 */
	JsonString getJsonString(String name);

	/**
	 * Gets the string.
	 *
	 * @param name the name
	 * @return the string
	 */
	String getString(String name);

	/**
	 * Gets the string.
	 *
	 * @param name         the name
	 * @param defaultValue the default value
	 * @return the string
	 */
	String getString(String name, String defaultValue);

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	boolean isEmpty();

	/**
	 * Checks if is null.
	 *
	 * @param name the name
	 * @return true, if is null
	 */
	boolean isNull(String name);

	/**
	 * Checks if is present.
	 *
	 * @param name the name
	 * @return true, if is present
	 */
	boolean isPresent(String name);

	/**
	 * Key set.
	 *
	 * @return the sets the
	 */
	Set<String> keySet();

	/**
	 * Size.
	 *
	 * @return the int
	 */
	int size();

	/**
	 * Values.
	 *
	 * @return the collection
	 */
	Collection<JsonValue> values();
}
