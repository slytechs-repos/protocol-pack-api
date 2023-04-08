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

/**
 * The Class JsonBooleanImpl.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class JsonBooleanImpl implements JsonValue {

	/** The value. */
	private final String value;
	
	/** The type. */
	private final ValueType type;

	/**
	 * Instantiates a new json boolean impl.
	 *
	 * @param value the value
	 */
	public JsonBooleanImpl(String value) {
		this.value = value;

		this.type = switch (value) {

		case "true" -> ValueType.TRUE;
		case "false" -> ValueType.FALSE;

		default -> throw new IllegalStateException("invalid value for json constant " + value);
		};
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return value;
	}

	/**
	 * Checks if is true.
	 *
	 * @return true, if is true
	 */
	public boolean isTrue() {
		return type == ValueType.TRUE;
	}

	/**
	 * Checks if is false.
	 *
	 * @return true, if is false
	 */
	public boolean isFalse() {
		return type == ValueType.FALSE;
	}

	/**
	 * Gets the value type.
	 *
	 * @return the value type
	 * @see com.slytechs.protocol.runtime.internal.json.JsonValue#getValueType()
	 */
	@Override
	public ValueType getValueType() {
		return type;
	}
}
