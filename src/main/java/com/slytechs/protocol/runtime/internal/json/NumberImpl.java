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
 * The Class NumberImpl.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class NumberImpl implements JsonNumber {

	/** The v. */
	private final Number v;

	/**
	 * Instantiates a new number impl.
	 *
	 * @param v the v
	 */
	public NumberImpl(Number v) {
		this.v = v;
	}

	/**
	 * Int value.
	 *
	 * @return the int
	 * @see com.slytechs.protocol.runtime.internal.json.JsonNumber#intValue()
	 */
	@Override
	public int intValue() {
		return v.intValue();
	}

	/**
	 * Long value.
	 *
	 * @return the long
	 * @see com.slytechs.protocol.runtime.internal.json.JsonNumber#longValue()
	 */
	@Override
	public long longValue() {
		return v.longValue();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return v.toString();
	}

	/**
	 * Gets the value type.
	 *
	 * @return the value type
	 * @see com.slytechs.protocol.runtime.internal.json.JsonValue#getValueType()
	 */
	@Override
	public ValueType getValueType() {
		return ValueType.NUMBER;
	}
}
