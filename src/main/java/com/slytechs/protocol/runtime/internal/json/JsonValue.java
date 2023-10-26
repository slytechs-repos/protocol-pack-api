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
 * The Interface JsonValue.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface JsonValue {

	/**
	 * The Enum ValueType.
	 */
	public enum ValueType {

		/** The array. */
		ARRAY,

		/** The false. */
		FALSE,

		/** The null. */
		NULL,

		/** The number. */
		NUMBER,

		/** The object. */
		OBJECT,

		/** The string. */
		STRING,

		/** The true. */
		TRUE
	}

	/** The null. */
	JsonValue NULL = new JsonNullImpl();

	/** The false. */
	JsonValue FALSE = new JsonBooleanImpl("false");

	/** The true. */
	JsonValue TRUE = new JsonBooleanImpl("true");

	/**
	 * Gets the value type.
	 *
	 * @return the value type
	 */
	ValueType getValueType();

	/**
	 * Checks if is array.
	 *
	 * @return true, if is array
	 */
	default boolean isArray() {
		return getValueType() == ValueType.ARRAY;
	}

	/**
	 * Checks if is false.
	 *
	 * @return true, if is false
	 */
	default boolean isFalse() {
		return getValueType() == ValueType.FALSE;
	}

	/**
	 * Checks if is number.
	 *
	 * @return true, if is number
	 */
	default boolean isNumber() {
		return getValueType() == ValueType.NUMBER;
	}

	/**
	 * Checks if is object.
	 *
	 * @return true, if is object
	 */
	default boolean isObject() {
		return getValueType() == ValueType.OBJECT;
	}

	/**
	 * Checks if is string.
	 *
	 * @return true, if is string
	 */
	default boolean isString() {
		return getValueType() == ValueType.STRING;
	}

	/**
	 * Checks if is true.
	 *
	 * @return true, if is true
	 */
	default boolean isTrue() {
		return getValueType() == ValueType.TRUE;
	}

}
