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

import java.util.List;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public interface JsonArray extends JsonStructure, Iterable<JsonValue> {

	void clear();

	boolean getBoolean(int index);

	boolean getBoolean(int index, boolean defaultValue);

	int getInt(int index);

	int getInt(int index, int defaultValue);

	JsonArray getJsonArray(int index);

	JsonNumber getJsonNumber(int index);

	JsonObject getJsonObject(int index);

	JsonString getJsonString(int index);

	String getString(int index);

	String getString(int index, String defaultValue);

	<T extends JsonValue> List<T> getValuesAs(Class<T> clazz);

	boolean isEmpty();

	boolean isNull(int index);

	int size();
}
