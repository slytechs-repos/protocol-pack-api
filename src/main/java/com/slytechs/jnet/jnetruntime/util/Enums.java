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
package com.slytechs.jnet.jnetruntime.util;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Various enum constant related utility classes.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 */
public final class Enums {

	/**
	 * Find enum.
	 *
	 * @param <T>      the generic type
	 * @param enumType the enum type
	 * @param name     the name
	 * @return the optional
	 */
	public static <T extends Enum<T>> Optional<T> findEnum(Class<T> enumType, String name) {
		for (T e : enumType.getEnumConstants()) {
			boolean isMatch = false
					|| e.name().equalsIgnoreCase(name)
					|| e.name().equals(toEnumName(name));

			if (isMatch)
				return Optional.of(e);
		}

		return Optional.empty();
	}

	/**
	 * Gets the enum.
	 *
	 * @param <T>      the generic type
	 * @param enumType the enum type
	 * @param name     the name
	 * @return the enum
	 */
	public static <T extends Enum<T>> T getEnum(Class<T> enumType, String name) {
		for (T e : enumType.getEnumConstants()) {
			boolean isMatch = false
					|| e.name().equalsIgnoreCase(name)
					|| e.name().equals(toEnumName(name));

			if (isMatch)
				return e;
		}

		return null;
	}

	/**
	 * Gets the enum or throw.
	 *
	 * @param <T>           the generic type
	 * @param <T_EXCEPTION> the generic type
	 * @param enumType      the enum type
	 * @param name          the name
	 * @param onNotFound    the on not found
	 * @return the enum or throw
	 * @throws T_EXCEPTION the t exception
	 */
	public static <T extends Enum<T>, T_EXCEPTION extends Throwable> T getEnumOrThrow(Class<T> enumType, String name,
			Function<String, T_EXCEPTION> onNotFound)
			throws T_EXCEPTION {
		T e = getEnum(enumType, name);
		if (e == null)
			throw onNotFound.apply(name);

		return e;
	}

	/**
	 * Gets the enum or throw.
	 *
	 * @param <T>           the generic type
	 * @param <T_EXCEPTION> the generic type
	 * @param enumType      the enum type
	 * @param name          the name
	 * @param onNotFound    the on not found
	 * @return the enum or throw
	 * @throws T_EXCEPTION the t exception
	 */
	public static <T extends Enum<T>, T_EXCEPTION extends Throwable> T getEnumOrThrow(Class<T> enumType, String name,
			Supplier<T_EXCEPTION> onNotFound)
			throws T_EXCEPTION {
		return getEnumOrThrow(enumType, name, n -> onNotFound.get());
	}

	/**
	 * Resolve.
	 *
	 * @param <E>   the element type
	 * @param value the value
	 * @param cl    the cl
	 * @return the string
	 */
	public static <E extends Enum<E> & IntSupplier> String resolve(Object value, Class<E> cl) {
		if (!(value instanceof Number n))
			return null;

		E e = valueOf(n.intValue(), cl);
		if (e == null)
			return null;

		return e.name();
	}

	/**
	 * To dot name.
	 *
	 * @param enumName the enum name
	 * @return the string
	 */
	public static String toDotName(String enumName) {
		return enumName
				.replace('_', '.')
				.replace('$', '_')
				.toLowerCase();
	}

	/**
	 * To enum name.
	 *
	 * @param javaName the java name
	 * @return the string
	 */
	public static String toEnumName(String javaName) {
		return javaName
				.replace('_', '$')
				.replace('.', '_')
				.toUpperCase();
	}

	/**
	 * Value of.
	 *
	 * @param <E>   the element type
	 * @param value the value
	 * @param cl    the cl
	 * @return the e
	 */
	public static <E extends Enum<E> & IntSupplier> E valueOf(int value, Class<E> cl) {
		E[] constants = cl.getEnumConstants();
		for (E e : constants)
			if (e.getAsInt() == value)
				return e;

		return null;
	}

	/**
	 * Sets the of.
	 *
	 * @param <E>   the element type
	 * @param value the value
	 * @param cl    the cl
	 * @return the sets the
	 */
	public static <E extends Enum<E> & IsIntFlag> Set<E> setOf(int value, Class<E> cl) {
		E[] constants = cl.getEnumConstants();

		var set = EnumSet.noneOf(cl);

		for (E e : constants)
			if (e.isSet(value))
				set.add(e);

		return set;
	}

	/**
	 * Sets the of.
	 *
	 * @param <E>   the element type
	 * @param flags the flags
	 * @param cl    the cl
	 * @return the sets the
	 */
	public static <E extends Enum<E> & IsIntFlag> Set<E> setOf(Object flags, Class<E> cl) {
		return setOf(((Number) flags).intValue(), cl);
	}

	/**
	 * Instantiates a new enum utils.
	 */
	private Enums() {
	}

}
