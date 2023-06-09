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
package com.slytechs.protocol.runtime.util;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * System properties retrieval utility methods.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public final class SystemProperties {

	/**
	 * Boolean value. Values of 1 and 0 also translate to true and false.
	 *
	 * @param property     the property name
	 * @param defaultValue the default value
	 * @return true or false value read from the system property
	 */
	public static boolean boolValue(String property, boolean defaultValue) {
		return guarded(property, value -> {

			/* Special case to allow boolean '1' and '0' as being true/false */
			if ((value.equals("0") || value.equals("1")))
				return value.equals("0") ? false : true;

			return Boolean.parseBoolean(value);
		}, defaultValue);
	}

	/**
	 * Guarded.
	 *
	 * @param <T>          the generic type
	 * @param property     the property name
	 * @param parser       the parser
	 * @param defaultValue the default value
	 * @return the t
	 */
	private static <T> T guarded(String property, Function<String, T> parser, T defaultValue) {
		try {
			String value = System.getProperty(property);
			if (value == null)
				return defaultValue;

			return parser.apply(value);

		} catch (Throwable e) {
			return defaultValue;
		}
	}

	/**
	 * Int value.
	 *
	 * @param property     the property name
	 * @param defaultValue the default value
	 * @return the int
	 */
	public static double doubleValue(String property, double defaultValue) {
		return numberValue(property, Double::parseDouble, defaultValue, CountUnit.COUNT).doubleValue();
	}

	/**
	 * Double value.
	 *
	 * @param property     the property
	 * @param defaultValue the default value
	 * @param unit         the unit
	 * @return the double
	 */
	public static double doubleValue(String property, double defaultValue, Unit unit) {
		return numberValue(property, Double::parseDouble, defaultValue, unit).doubleValue();
	}

	/**
	 * Int value.
	 *
	 * @param property     the property name
	 * @param defaultValue the default value
	 * @return the int
	 */
	public static int intValue(String property, int defaultValue) {
		return numberValue(property, Integer::parseInt, defaultValue, CountUnit.COUNT).intValue();
	}

	/**
	 * Int value.
	 *
	 * @param property     the property name
	 * @param defaultValue the default value
	 * @param unit         the unit
	 * @return the int
	 */
	public static int intValue(String property, int defaultValue, Unit unit) {
		return numberValue(property, Integer::parseInt, defaultValue, unit).intValue();
	}

	/**
	 * Long value.
	 *
	 * @param property     the property name
	 * @param defaultValue the default value
	 * @return the long
	 */
	public static long longValue(String property, long defaultValue) {
		return numberValue(property, Long::parseLong, defaultValue, CountUnit.COUNT).longValue();
	}

	/**
	 * Long value.
	 *
	 * @param property     the property name
	 * @param defaultValue the default value
	 * @param unit         the unit
	 * @return the long
	 */
	public static long longValue(String property, long defaultValue, Unit unit) {
		return numberValue(property, Long::parseLong, defaultValue, unit).longValue();
	}

	/**
	 * Number value.
	 *
	 * @param <U>          the generic type
	 * @param property     the property name
	 * @param parser       the parser
	 * @param defaultValue the default value
	 * @param unit         the unit
	 * @return the number
	 */
	private static <U extends Unit> Number numberValue(
			String property,
			Function<String, Number> parser,
			Number defaultValue,
			U unit) {

		try {
			String value = System.getProperty(property);
			if (value == null)
				return unit.toBase(defaultValue.longValue());

			U newUnit = unit.parseUnits(value, unit);
			value = unit.stripUnits(value);
			Number number = parser.apply(value);

			return newUnit.toBase(number.longValue());

		} catch (Throwable e) {
			return unit.toBase(defaultValue.longValue());
		}
	}

	/**
	 * String value.
	 *
	 * @param property     the property name
	 * @param defaultValue the default value
	 * @return the string
	 */
	public static String stringValue(String property, String defaultValue) {
		return guarded(property, String::valueOf, defaultValue);
	}

	/**
	 * String value.
	 *
	 * @param property   the property
	 * @param onProperty the on property
	 * @return the string
	 */
	public static String stringValue(String property, BiConsumer<String, String> onProperty) {
		String value = System.getProperty(property);
		if (value != null)
			onProperty.accept(property, value);

		return value;
	}

	/**
	 * String value.
	 *
	 * @param property     the property
	 * @param defaultValue the default value
	 * @param onProperty   the on property
	 * @return the string
	 */
	public static String stringValue(String property, String defaultValue, BiConsumer<String, String> onProperty) {
		String value = System.getProperty(property);
		if (value != null)
			onProperty.accept(property, value);
		else
			onProperty.accept(property, defaultValue);

		return value;
	}

	/**
	 * Instantiates a new system properties.
	 */
	private SystemProperties() {
	}

}
