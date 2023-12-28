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

/**
 * The Class UnitUtils.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class UnitUtils {

	/**
	 * The Interface ConvertableUnit.
	 *
	 * @author Sly Technologies
	 * @author repos@slytechs.com
	 * @param <T> the generic type
	 */
	interface ConvertableUnit<T extends Enum<T>> extends Unit {

		/**
		 * Convert.
		 *
		 * @param source the source
		 * @param unit   the unit
		 * @return the long
		 */
		long convert(long source, T unit);

		/**
		 * Convertf.
		 *
		 * @param source the source
		 * @return the double
		 */
		double convertf(double source);

		/**
		 * Convertf.
		 *
		 * @param source     the source
		 * @param sourceUnit the source unit
		 * @return the double
		 */
		double convertf(double source, T sourceUnit);

	}

	/**
	 * Instantiates a new unit utils.
	 */
	private UnitUtils() {
	}

	/**
	 * Nearest.
	 *
	 * @param <T>   the generic type
	 * @param value the value
	 * @param type  the type
	 * @param base  the base
	 * @return the t
	 */
	static <T extends Enum<T> & ConvertableUnit<T>> T nearest(long value, Class<T> type, T base) {
		T[] values = type.getEnumConstants();

		for (int i = values.length - 1; i >= 0; i--) {
			T u = values[i];

			if (u.convert(value, base) > 0) {
				return u;
			}
		}

		return base;
	}

	/**
	 * Format.
	 *
	 * @param <T>   the generic type
	 * @param fmt   the fmt
	 * @param value the value
	 * @param type  the type
	 * @param base  the base
	 * @return the string
	 */
	static <T extends Enum<T> & ConvertableUnit<T>> String format(String fmt,
			long value,
			Class<T> type,
			T base) {
		T unit = nearest(value, type, base);
		return String.format(fmt, unit.convertf(value), unit.getSymbol());
	}

	static <U extends Unit> U[] values(Class<U> cl) {
		if (!(Enum.class.isAssignableFrom(cl)))
			throw new IllegalArgumentException("only enum based units are supported [%s]"
					.formatted(cl));

		U[] values = cl.getEnumConstants();

		return values;
	}

	static <U extends Unit> U parseUnits(String valueAndUnits, Class<U> cl) {
		U[] values = values(cl);
		String str = valueAndUnits.toLowerCase().strip();

		for (U u : values) {
			String[] symbols = u.getSymbols();
			for (String sym : symbols) {
				String regex = "[^\\s-_@]+[\\s-_@]*\\(?" + sym + "\\)?$";
				if (str.matches(regex))
					return u;
			}
		}

		return null;
	}

	static String stripUnits(String valueAndUnits, Class<? extends Unit> cl) {
		Unit[] values = values(cl);
		String str = valueAndUnits.toLowerCase();

		for (Unit u : values) {
			String[] symbols = u.getSymbols();
			for (String sym : symbols) {
				String regex = "([^\\s-_@]+)[\\s-_@]*\\(?" + sym + "\\)?$";
				if (str.matches(regex))
					return str.replaceFirst(regex, "$1");
			}
		}

		return valueAndUnits;
	}
}
