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

import com.slytechs.protocol.runtime.internal.json.JsonException;
import com.slytechs.protocol.runtime.internal.json.JsonObject;
import com.slytechs.protocol.runtime.internal.json.JsonObjectBuilder;

/**
 * The Class MetaResourceShortformReader.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class MetaResourceShortformReader extends MetaResourceReader {

	/**
	 * Instantiates a new meta resource shortform reader.
	 *
	 * @param resourceName the resource name
	 * @throws JsonException the json exception
	 */
	public MetaResourceShortformReader(String resourceName) throws JsonException {
		super(resourceName);
	}

	/**
	 * Process.
	 *
	 * @param sourceJsonObj the source json obj
	 * @return the json object
	 * @see com.slytechs.protocol.meta.MetaResourceReader#process(com.slytechs.protocol.runtime.internal.json.JsonObject)
	 */
	@Override
	protected JsonObject process(JsonObject sourceJsonObj) {
		return super.process(sourceJsonObj);
	}

	/**
	 * Builds the top.
	 *
	 * @param sobj the sobj
	 * @return the json object
	 */
	private JsonObject buildTop(JsonObject sobj) {

		var b = new JsonObjectBuilder();

		for (String key : sobj.keySet()) {

			if (key.startsWith("display")) {
				b.add(key, sobj.getJsonObject(key));

			} else if (key.equals("meta")) {
				b.add(key, sobj.getJsonObject(key));

			} else {
				b.add("display", buildDisplay(sobj.getString(key)));

			}

		}

		return b.build();
	}

	/**
	 * Builds the display.
	 *
	 * @param value the value
	 * @return the json object
	 */
	private JsonObject buildDisplay(String value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Builds the meta.
	 *
	 * @param value the value
	 * @return the json object
	 */
	private JsonObject buildMeta(String value) {
		throw new UnsupportedOperationException();

	}
}
