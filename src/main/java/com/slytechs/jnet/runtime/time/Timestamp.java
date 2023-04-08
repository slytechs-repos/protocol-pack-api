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
package com.slytechs.jnet.runtime.time;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.slytechs.jnet.runtime.util.Detail;
import com.slytechs.jnet.runtime.util.StringBuildable;

/**
 * The Class Timestamp.
 */
public class Timestamp extends Date implements TimestampPrecisionInfo, StringBuildable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7477717973875482558L;

	/** The Constant TS_NANOS_PER_SECOND. */
	static final long TS_NANOS_PER_SECOND = 1000_000_000l;

	/** The Constant TS_TENS_NANOS_PER_SECOND. */
	static final long TS_TENS_NANOS_PER_SECOND = 100_000_000l;

	/** The Constant TS_MICROS_PER_SECOND. */
	static final long TS_MICROS_PER_SECOND = 1000_000l;

	/** The Constant TS_MILLIS_PER_SECOND. */
	static final long TS_MILLIS_PER_SECOND = 1000l;

	/** The Constant TS_NANOS_IN_MILLIS. */
	static final long TS_NANOS_IN_MILLIS = 1000_000l;

	/** The Constant TS_NANOS_IN_MICROS. */
	static final long TS_NANOS_IN_MICROS = 1000l;

	/** The Constant TS_NANOS_IN_SECOND. */
	static final long TS_NANOS_IN_SECOND = 1000_000_000l;

	/** The Constant TS_LSB_MASK. */
	static final long TS_LSB_MASK = 0xFFFFFFFFl;

	/** The Constant TS_MSB_SHIFT. */
	static final int TS_MSB_SHIFT = 32;

	/**
	 * Encode pcap micro.
	 *
	 * @param seconds     the seconds
	 * @param microAdjust the micro adjust
	 * @return the long
	 */
	public static long encodePcapMicro(long seconds, long microAdjust) {
		TimestampUnit unit = TimestampUnit.EPOCH_MICRO;
		return unit.ofSecond(seconds, microAdjust);
	}

	/**
	 * Encode pcap nano.
	 *
	 * @param seconds    the seconds
	 * @param nanoAdjust the nano adjust
	 * @return the long
	 */
	public static long encodePcapNano(long seconds, long nanoAdjust) {
		TimestampUnit unit = TimestampUnit.EPOCH_NANO;
		return unit.ofSecond(seconds, nanoAdjust);
	}

	/**
	 * Format decimal int.
	 *
	 * @param val    the val
	 * @param buf    the buf
	 * @param offset the offset
	 * @param len    the len
	 */
	private static void formatDecimalInt(int val, char[] buf, int offset, int len) {
		int charPos = offset + len;
		do {
			buf[--charPos] = (char) ('0' + (val % 10));
			val /= 10;
		} while (charPos > offset);
	}

	/**
	 * Of pcap micro.
	 *
	 * @param seconds     the seconds
	 * @param microAdjust the micro adjust
	 * @return the timestamp
	 */
	public static Timestamp ofPcapMicro(long seconds, long microAdjust) {
		TimestampUnit unit = TimestampUnit.EPOCH_MICRO;
		return new Timestamp(unit.ofSecond(seconds, microAdjust), unit);
	}

	/**
	 * Of pcap nano.
	 *
	 * @param seconds    the seconds
	 * @param nanoAdjust the nano adjust
	 * @return the timestamp
	 */
	public static Timestamp ofPcapNano(long seconds, long nanoAdjust) {
		TimestampUnit unit = TimestampUnit.EPOCH_NANO;
		return new Timestamp(unit.ofSecond(seconds, nanoAdjust), unit);
	}

	/** The nano adjustment. */
	private final int nanoAdjustment;

	/** The unit. */
	private final TimestampUnit unit;

	/**
	 * Instantiates a new timestamp.
	 *
	 * @param timestampInMillis the timestamp in millis
	 */
	public Timestamp(long timestampInMillis) {
		this(timestampInMillis, TimestampUnit.EPOCH_MILLI);
	}

	/**
	 * Instantiates a new timestamp.
	 *
	 * @param ts   the ts
	 * @param unit the unit
	 */
	public Timestamp(long ts, TimestampUnit unit) {
		super(unit.toEpochMilli(ts));
		this.unit = unit;
		nanoAdjustment = (int) unit.toNanoAdjustment(ts);
	}

	/**
	 * Gets the nanos.
	 *
	 * @return the nanos
	 */
	public int getNanos() {
		return nanoAdjustment;
	}

	/**
	 * Gets the unit.
	 *
	 * @return the unit
	 */
	public TimestampUnit getUnit() {
		return unit;
	}

	/**
	 * Precision time unit.
	 *
	 * @return the time unit
	 * @see com.slytechs.jnet.runtime.time.TimestampPrecisionInfo#precisionTimeUnit()
	 */
	@Override
	public TimeUnit precisionTimeUnit() {
		return unit.precisionTimeUnit();
	}

	/**
	 * Precision.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.runtime.time.TimestampPrecisionInfo#precision()
	 */
	@Override
	public int precision() {
		return unit.precision();
	}

	/**
	 * To instant.
	 *
	 * @return the instant
	 * @see java.util.Date#toInstant()
	 */
	@Override
	public Instant toInstant() {
		return Instant.ofEpochSecond(super.getTime() / TS_MILLIS_PER_SECOND, nanoAdjustment);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.util.Date#toString()
	 */
	@Override
	public String toString() {
		return buildString(new StringBuilder(), Detail.LOW).toString();
	}

	/**
	 * Builds the string.
	 *
	 * @param b      the b
	 * @param detail the detail
	 * @return the string builder
	 * @see com.slytechs.jnet.runtime.util.StringBuildable#buildString(java.lang.StringBuilder,
	 *      Detail)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public StringBuilder buildString(StringBuilder b, Detail detail) {

		int year = super.getYear() + 1900;
		int month = super.getMonth() + 1;
		int day = super.getDate();
		int hour = super.getHours();
		int minute = super.getMinutes();
		int second = super.getSeconds();

		int trailingZeros = 0;
		int tmpNanos = nanoAdjustment;
		if (tmpNanos == 0) {
			trailingZeros = 8;
		} else {
			while (tmpNanos % 10 == 0) {
				tmpNanos /= 10;
				trailingZeros++;
			}
		}

		// 8058429: To comply with current JCK tests, we need to deal with year
		// being any number between 0 and 292278995
		int count = 10000;
		int yearSize = 4;
		do {
			if (year < count) {
				break;
			}
			yearSize++;
			count *= 10;
		} while (count < 1000000000);

		char[] buf = new char[25 + yearSize];
		formatDecimalInt(year, buf, 0, yearSize);
		buf[yearSize] = '-';
		formatDecimalInt(month, buf, yearSize + 1, 2);
		buf[yearSize + 3] = '-';
		formatDecimalInt(day, buf, yearSize + 4, 2);
		buf[yearSize + 6] = ' ';
		formatDecimalInt(hour, buf, yearSize + 7, 2);
		buf[yearSize + 9] = ':';
		formatDecimalInt(minute, buf, yearSize + 10, 2);
		buf[yearSize + 12] = ':';
		formatDecimalInt(second, buf, yearSize + 13, 2);
		buf[yearSize + 15] = '.';
		formatDecimalInt(tmpNanos, buf, yearSize + 16, 9 - trailingZeros);

		while (trailingZeros > 0)
			buf[yearSize + 16 + 9 - trailingZeros--] = '0';

		b.append(buf);

		return b;
	}

	/**
	 * Format timestamp.
	 *
	 * @param ts the ts
	 * @return the string
	 */
	public static String formatTimestamp(Object ts) {
		TimestampUnit unit = guessTimestampUnit((Long) ts);
		if (unit == null)
			return "0x016X (unrecognized timestamp unit)".formatted(ts);

		return new Timestamp((Long) ts, unit).toString();
	}

	/**
	 * Format timestamp pcap micro.
	 *
	 * @param tsInPcapMicro the ts in pcap micro
	 * @return the string
	 */
	public static String formatTimestampPcapMicro(Object tsInPcapMicro) {
		return new Timestamp((Long) tsInPcapMicro, TimestampUnit.PCAP_MICRO).toString();
	}

	/**
	 * Format timestamp in epoch milli.
	 *
	 * @param tsInEpochMilli the ts in epoch milli
	 * @return the string
	 */
	public static String formatTimestampInEpochMilli(Object tsInEpochMilli) {
		return new Timestamp((Long) tsInEpochMilli, TimestampUnit.EPOCH_MILLI).toString();
	}

	/** The Constant NINETEEN_NINETY. */
	private static final long NINETEEN_NINETY = Instant.parse("1990-01-01T00:00:00Z")
			.toEpochMilli();

	/** The Constant ONE_YEAR_AHEAD. */
	private static final long ONE_YEAR_AHEAD = Instant.now()
			.plus(364, ChronoUnit.DAYS)
			.toEpochMilli();

	/** The last timestamp unit guess. */
	private static TimestampUnit lastTimestampUnitGuess;

	/**
	 * Format timestamp unit.
	 *
	 * @param timestamp the timestamp
	 * @return the string
	 */
	public static String formatTimestampUnit(Object timestamp) {
		return Optional.ofNullable(guessTimestampUnit((Long) timestamp))
				.map(String::valueOf)
				.orElse("unknown");
	}

	/**
	 * Guess timestamp unit.
	 *
	 * @param timestamp the timestamp
	 * @return the timestamp unit
	 */
	public static synchronized TimestampUnit guessTimestampUnit(long timestamp) {

		/* Check if we have a cached unit value from previous check */
		if (lastTimestampUnitGuess != null) {
			long guessInMillis = TimestampUnit.EPOCH_MILLI
					.convert(timestamp, lastTimestampUnitGuess);

			if ((guessInMillis > NINETEEN_NINETY) && (guessInMillis < ONE_YEAR_AHEAD))
				return lastTimestampUnitGuess;

			else
				lastTimestampUnitGuess = null;
		}

		for (TimestampUnit u : TimestampUnit.values()) {
			long guessInMillis = TimestampUnit.EPOCH_MILLI
					.convert(timestamp, u);

			if ((guessInMillis > NINETEEN_NINETY) && (guessInMillis < ONE_YEAR_AHEAD)) {
				lastTimestampUnitGuess = u;

				return u;
			}

		}

		return null;
	}
}
