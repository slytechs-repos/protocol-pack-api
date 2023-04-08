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
import java.util.List;

/**
 * The Class JsonArrayBuilder.
 */
public final class JsonArrayBuilder {

	/** The list. */
	private final List<JsonValue> list = new ArrayList<>();

	/**
	 * Adds the.
	 *
	 * @param value the value
	 * @return the json array builder
	 */
	public JsonArrayBuilder add(String value) {
		list.add(new StringImpl(value));

		return this;
	}

	/**
	 * Adds the.
	 *
	 * @param value the value
	 * @return the json array builder
	 */
	public JsonArrayBuilder add(Number value) {
		list.add(new NumberImpl(value));

		return this;
	}

	/**
	 * Adds the.
	 *
	 * @param value the value
	 * @return the json array builder
	 */
	public JsonArrayBuilder add(JsonValue value) {
		list.add(value);

		return this;
	}

	/**
	 * Builds the.
	 *
	 * @return the json array
	 */
	public JsonArray build() {
		return new ArrayImpl(list);
	}
}