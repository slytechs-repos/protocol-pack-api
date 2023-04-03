/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.runtime.util;

import java.util.Optional;
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

	public static <E extends Enum<E> & IntSupplier> E valueOf(int value, Class<E> cl) {
		E[] constants = cl.getEnumConstants();
		for (E e : constants)
			if (e.getAsInt() == value)
				return e;

		return null;
	}

	/**
	 * Instantiates a new enum utils.
	 */
	private Enums() {
	}

}
