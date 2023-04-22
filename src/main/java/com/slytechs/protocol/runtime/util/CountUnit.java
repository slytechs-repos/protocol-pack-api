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

import com.slytechs.protocol.runtime.util.UnitUtils.ConvertableUnit;

/**
 * Unit definition for counting large numbers.
 */
public enum CountUnit implements ConvertableUnit<CountUnit> {

	/** The uni. */
	COUNT(1L),

	/** The kilo. */
	KILO(1_000L),

	/** The mega. */
	MEGA(1_000_000L),

	/** The giga. */
	GIGA(1_000_000_000L),

	/** The tera. */
	TERA(1_000_000_000_000L),

	/** The peta. */
	PETA(1_000_000_000_000_000L),

	;

	/** The base. */
	private final long base;

	/** The basef. */
	private final double basef;

	/** The symbol. */
	private final String symbol;

	/**
	 * Instantiates a new count unit.
	 *
	 * @param baseSpeed the base speed
	 */
	CountUnit(long baseSpeed) {
		this.base = baseSpeed;
		this.basef = baseSpeed;
		this.symbol = "" + name().charAt(0);
	}

	/**
	 * Convertf.
	 *
	 * @param value      the value
	 * @param sourceUnit the source unit
	 * @return the double
	 */
	@Override
	public double convertf(double value, CountUnit sourceUnit) {
		return sourceUnit.toUnif(value) / this.basef;
	}

	/**
	 * Convert.
	 *
	 * @param value      the value
	 * @param sourceUnit the source unit
	 * @return the long
	 */
	@Override
	public long convert(long value, CountUnit sourceUnit) {
		return sourceUnit.toCount(value) / this.base;
	}

	/**
	 * To uni.
	 *
	 * @param value the value
	 * @return the long
	 */
	public long toCount(long value) {
		return value * base;
	}

	/**
	 * To int uni.
	 *
	 * @param value the value
	 * @return the int
	 */
	public int toCountAsInt(long value) {
		return (int) toCount(value);
	}

	/**
	 * To unif.
	 *
	 * @param value the value
	 * @return the double
	 */
	private double toUnif(double value) {
		return value * basef;
	}

	/**
	 * To giga.
	 *
	 * @param value the value
	 * @return the long
	 */
	public long toGiga(long value) {
		return GIGA.convert(value, this);
	}

	/**
	 * To kilo.
	 *
	 * @param value the value
	 * @return the long
	 */
	public long toKilo(long value) {
		return KILO.convert(value, this);
	}

	/**
	 * To mega.
	 *
	 * @param value the value
	 * @return the long
	 */
	public long toMega(long value) {
		return MEGA.convert(value, this);
	}

	/**
	 * To tera.
	 *
	 * @param value the value
	 * @return the long
	 */
	public long toTera(long value) {
		return TERA.convert(value, this);
	}

	/**
	 * To peta.
	 *
	 * @param value the value
	 * @return the long
	 */
	public long toPeta(long value) {
		return PETA.convert(value, this);
	}

	/**
	 * Convertf.
	 *
	 * @param inUni the in uni
	 * @return the double
	 */
	@Override
	public double convertf(double inUni) {
		return convertf(inUni, COUNT);
	}

	/**
	 * Gets the symbol.
	 *
	 * @return the symbol
	 */
	@Override
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Nearest.
	 *
	 * @param inUni the in uni
	 * @return the count unit
	 */
	public static CountUnit nearest(long inUni) {
		return UnitUtils.nearest(inUni, CountUnit.class, COUNT);
	}

	/**
	 * Format count.
	 *
	 * @param fmt   the fmt
	 * @param inUni the in uni
	 * @return the string
	 */
	public static String formatCount(String fmt, long inUni) {
		return UnitUtils.format(fmt, inUni, CountUnit.class, COUNT);
	}

}