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
package com.slytechs.protocol.meta;

import java.io.InputStream;

import com.slytechs.protocol.runtime.NotFound;
import com.slytechs.protocol.runtime.internal.json.Json;
import com.slytechs.protocol.runtime.internal.json.JsonException;
import com.slytechs.protocol.runtime.internal.json.JsonObject;
import com.slytechs.protocol.runtime.internal.json.JsonReader;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public class MetaResourceReader {

	private final JsonObject jsonObj;

	/**
	 * @throws NotFound      resource not found
	 * @throws JsonException
	 */
	public MetaResourceReader(String resourceName) throws JsonException {
		InputStream in = ReflectedClass.class.getResourceAsStream("/" + resourceName);
		if (in == null) {
			this.jsonObj = null;
		} else {

			try (JsonReader reader = Json.createReader(in)) {
				JsonObject obj = reader.readObject();
				reader.close();

				this.jsonObj = process(obj);
			}
		}
	}

	protected JsonObject process(JsonObject sourceJsonObj) {
		return sourceJsonObj;
	}

	public JsonObject toJsonObj() {
		return this.jsonObj;
	}
}
