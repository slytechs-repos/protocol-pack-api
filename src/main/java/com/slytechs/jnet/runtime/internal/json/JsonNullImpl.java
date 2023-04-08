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

/**
 * The Class JsonNullImpl.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class JsonNullImpl implements JsonValue {

	/**
	 * Instantiates a new json null impl.
	 */
	public JsonNullImpl() {
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "null";
	}

	/**
	 * Checks if is null.
	 *
	 * @return true, if is null
	 */
	public boolean isNull() {
		return true;
	}

	/**
	 * Gets the value type.
	 *
	 * @return the value type
	 * @see com.slytechs.jnet.runtime.internal.json.JsonValue#getValueType()
	 */
	@Override
	public ValueType getValueType() {
		return ValueType.NULL;
	}
}
