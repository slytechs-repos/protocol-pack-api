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

/**
 * The Interface Unit.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface Unit {

	/**
	 * Convert to base value for whatever that is for this unit and round off to
	 * "integer".
	 *
	 * @param value the value to convert to base of the unit
	 * @return the base unit converted to an 32-bit integer, which will wrap if the
	 *         base value is
	 * @throws IllegalArgumentException integer conversion overflow
	 */
	default int toBaseAsInt(long value) throws IllegalArgumentException {
		long longValue = toBase(value);
		if (longValue > Integer.MAX_VALUE || longValue < Integer.MIN_VALUE)
			throw new IllegalArgumentException("integer conversion overflow [%d]"
					.formatted(longValue));

		return (int) longValue;
	}

	/**
	 * Parses the units when given both a value with units. For example
	 * {@code "10MB"} when using {@code MemoryUnit} will ignore that "10" value and
	 * will convert "MB" into a {@code MemoryUnit.MEGA_BYTES}. Spaces are ignored
	 * when parsing units such as {@code "10MB"} and {@code "10 MB"} are considered
	 * the same.
	 * <p>
	 * Note, if the unit parser fails for whatever reason, it defaults to the
	 * current units as a fallback and does not throw an exception.
	 * </p>
	 *
	 * @param <U>           the generic type of the units
	 * @param valueAndUnits the value and units in the same string
	 * @param defaultUnits  the default units
	 * @return the u
	 */
	@SuppressWarnings("unchecked")
	default <U extends Unit> U parseUnits(String valueAndUnits, U defaultUnits) {
		Unit newUnits = UnitUtils.parseUnits(valueAndUnits, this.getClass());
		if (newUnits == null)
			return defaultUnits;

		return (U) newUnits;
	}

	/**
	 * Will strip the unit component of the value and return only the value. For
	 * example {@code 10MB} will strip "MB" units and only return a string of "10".
	 *
	 * @param valueAndUnits the value and units
	 * @return a string containing only the value and not the units
	 */
	default String stripUnits(String valueAndUnits) {
		return UnitUtils.stripUnits(valueAndUnits, this.getClass());
	}

	/**
	 * To base.
	 *
	 * @param value the value
	 * @return the long
	 */
	long toBase(long value);

	/**
	 * Gets the symbol.
	 *
	 * @return the symbol
	 */
	default String getSymbol() {
		return getSymbols().length == 0 ? "" : getSymbols()[0];
	}

	/**
	 * The unit name.
	 *
	 * @return the string
	 */
	String name();

	/**
	 * Gets the symbols.
	 *
	 * @return the name
	 */
	String[] getSymbols();
}
