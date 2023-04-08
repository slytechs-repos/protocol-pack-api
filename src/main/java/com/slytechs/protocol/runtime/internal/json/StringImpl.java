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
 * The Class StringImpl.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class StringImpl implements JsonString {

	/** The str. */
	private final String str;

	/**
	 * Instantiates a new string impl.
	 *
	 * @param str the str
	 */
	public StringImpl(String str) {
		this.str = str;
	}

	/**
	 * Gets the string.
	 *
	 * @return the string
	 * @see com.slytechs.protocol.runtime.internal.json.JsonString#getString()
	 */
	@Override
	public String getString() {
		return str;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\"%s\"".formatted(str);
	}

	/**
	 * Gets the value type.
	 *
	 * @return the value type
	 * @see com.slytechs.protocol.runtime.internal.json.JsonValue#getValueType()
	 */
	@Override
	public ValueType getValueType() {
		return ValueType.STRING;
	}
}
