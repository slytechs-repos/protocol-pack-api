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
package com.slytechs.jnet.runtime.util.json;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public interface JsonObject extends JsonStructure {

	void clear();

	Set<Entry<String, JsonValue>> entrySet();

	JsonValue get(String name);

	boolean getBoolean(String name);

	boolean getBoolean(String name, boolean defaultValue);

	int getInt(String name);

	int getInt(String name, int defaultValue);

	JsonArray getJsonArray(String name);

	JsonNumber getJsonNumber(String name);

	JsonObject getJsonObject(String name);

	JsonString getJsonString(String name);

	String getString(String name);

	String getString(String name, String defaultValue);

	boolean isEmpty();

	boolean isNull(String name);

	boolean isPresent(String name);

	Set<String> keySet();

	int size();

	Collection<JsonValue> values();
}
