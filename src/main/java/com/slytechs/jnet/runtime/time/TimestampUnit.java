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
package com.slytechs.jnet.runtime.time;

import static com.slytechs.jnet.runtime.time.Timestamp.*;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

/**
 * The Enum TimestampUnit.
 */
public enum TimestampUnit implements TimestampPrecisionInfo {

	/**
	 * PCAP-ns format is based on a 1 ns unit with a 32-bit unit counter in MSBs of
	 * the time stamp descriptor field, and a 32-bit second counter in LSBs of the
	 * time stamp descriptor field, so the time resolution is 1 ns. The start time
	 * is January 1st 1970.
	 */
	PCAP_NANO(9, ts -> ((ts >> TS_MSB_SHIFT) & TS_LSB_MASK), ts -> (ts & TS_LSB_MASK), (msb,
			lsb) -> (msb << TS_MSB_SHIFT) | (lsb & TS_LSB_MASK)) {
		@Override
		public long toPcapNano(long ts) {
			return ts;
		}
	},

	/**
	 * PCAP-μs format is based on a 1000 ns (1 μs) unit with a 32-bit unit counter
	 * in MSBs of the time stamp descriptor field, and a 32-bit second counter in
	 * LSBs of the time stamp descriptor field. The start time is January 1st 1970.
	 */
	PCAP_MICRO(6, ts -> ((ts >> TS_MSB_SHIFT) & TS_LSB_MASK), ts -> (ts & TS_LSB_MASK) * 1000l, (msb,
			lsb) -> (msb << TS_MSB_SHIFT) | (lsb & TS_LSB_MASK)) {
		@Override
		public long toPcapMicro(long ts) {
			return ts;
		}
	},

	/**
	 * Native UNIX format is based on a 1 ns unit with a 64-bit unit counter. The
	 * start time is January 1st 1970.
	 */
	EPOCH_NANO(9, ts -> (ts / TS_NANOS_IN_SECOND), ts -> (ts % TS_NANOS_IN_SECOND), (msb, lsb) -> ((msb
			* TS_NANOS_IN_SECOND) + lsb)) {
		@Override
		public long toEpochNano(long ts) {
			return ts;
		}
	},

	/**
	 * Native UNIX format is based on a 10 ns unit with a 64-bit unit counter. The
	 * start time is January 1st 1970.
	 */
	EPOCH_10NANO(8, ts -> (ts / TS_TENS_NANOS_PER_SECOND), ts -> (ts % TS_TENS_NANOS_PER_SECOND) * 10l, (msb,
			lsb) -> ((msb * TS_TENS_NANOS_PER_SECOND) + lsb)) {
		@Override
		public long toEpoch10Nano(long ts) {
			return ts;
		}
	},

	/**
	 * Native UNIX format is based on a 1 us unit with a 64-bit unit counter. The
	 * start time is January 1st 1970.
	 */
	EPOCH_MICRO(6, ts -> (ts / TS_MICROS_PER_SECOND), ts -> (ts % TS_MICROS_PER_SECOND) * 1000l, (msb, lsb) -> ((msb
			* TS_MICROS_PER_SECOND) + lsb)) {
		@Override
		public long toEpochMicro(long ts) {
			return ts;
		}
	},

	/**
	 * Native UNIX format is based on a 1 ms unit with a 64-bit unit counter. The
	 * start time is January 1st 1970.
	 */
	EPOCH_MILLI(3, ts -> (ts / TS_MILLIS_PER_SECOND), ts -> (ts % TS_MILLIS_PER_SECOND) * 1000_000l, (msb, lsb) -> ((msb
			* TS_MILLIS_PER_SECOND) + lsb)) {
		@Override
		public long toEpochMilli(long ts) {
			return ts;
		}
	},;

	/** The encoder. */
	private final LongBinaryOperator encoder;

	/** The seconds. */
	private final LongUnaryOperator seconds;

	/** The nanos. */
	private final LongUnaryOperator nanos;

	/** The precision exponent. */
	private final int precisionExponent;

	/** The precision time unit. */
	private final TimeUnit precisionTimeUnit;

	/**
	 * Instantiates a new timestamp unit.
	 *
	 * @param precision the precision
	 * @param seconds   the seconds
	 * @param nanos     the nanos
	 * @param encoder   the encoder
	 */
	private TimestampUnit(int precision, LongUnaryOperator seconds, LongUnaryOperator nanos,
			LongBinaryOperator encoder) {
		this.encoder = encoder;
		this.precisionExponent = precision;
		this.seconds = seconds;
		this.nanos = nanos;

		this.precisionTimeUnit = switch (precisionExponent) {
		case 9 -> TimeUnit.NANOSECONDS;
		case 8 -> TimeUnit.NANOSECONDS;
		case 6 -> TimeUnit.MICROSECONDS;
		case 3 -> TimeUnit.MILLISECONDS;

		default -> throw new IllegalArgumentException("Unexpected precision: " + precisionExponent);
		};
	}

	/**
	 * Adds the nano.
	 *
	 * @param ts             the ts
	 * @param nanoAdjustment the nano adjustment
	 * @return the long
	 */
	long addNano(long ts, long nanoAdjustment) {
		return convertAndAdjust(ts, this, nanoAdjustment);
	}

	/**
	 * Converts 'ts' which is 'unit' into 'this' timestamp unit.
	 *
	 * @param ts   the timestamp to convert
	 * @param unit the units the 'ts' parameter is in
	 * @return the converted 'ts' into 'this' unit
	 */
	public long convert(long ts, TimestampUnit unit) {
		return switch (this) {
		case PCAP_NANO -> unit.toPcapNano(ts);
		case PCAP_MICRO -> unit.toPcapMicro(ts);
		case EPOCH_NANO -> unit.toEpochNano(ts);
		case EPOCH_10NANO -> unit.toEpoch10Nano(ts);
		case EPOCH_MICRO -> unit.toEpochMicro(ts);
		case EPOCH_MILLI -> unit.toEpochMilli(ts);
		};
	}

	/**
	 * Convert.
	 *
	 * @param ts             the ts
	 * @param unit           the unit
	 * @param nanoAdjustment the nano adjustment
	 * @return the long
	 */
	public long convertAndAdjust(long ts, TimestampUnit unit, long nanoAdjustment) {
		ts = unit.toEpochNano(ts);
		ts += nanoAdjustment;

		return convert(ts, EPOCH_NANO);
	}

	/**
	 * From seconds.
	 *
	 * @param seconds  the seconds
	 * @param fraction the fraction
	 * @return the long
	 */
	public long ofSecond(long seconds, long fraction) {
		return encoder.applyAsLong(seconds, fraction);
	}

	/**
	 * The precision of the time stamp or how many digits after the decimal will the
	 * time stamp provide. Note that resolution, if known, can differ from precision
	 * and provide less time stamp information that what the precision reports. That
	 * is, a millisecond precision may only have a 10 millisecond resolution, if
	 * using a standard system clock on some systems.
	 *
	 * @return The precision is returned as a {@link TimeUnit} constant and only the
	 *         constants which represent time units of fraction of a second are
	 *         supported.
	 */
	@Override
	public TimeUnit precisionTimeUnit() {
		return precisionTimeUnit;
	}

	/**
	 * The precision of the time stamp or how many digits after the decimal will the
	 * time stamp provide. Note that resolution, if known, can differ from precision
	 * and provide less time stamp information that what the precision reports. That
	 * is, a millisecond precision may only have a 10 millisecond resolution, if
	 * using a standard system clock on some systems.
	 *
	 * @return The precision is returned as the absolute exponent value (i.e. 10^-9
	 *         will return 9) or how many zeros after the decimal place.
	 */
	@Override
	public int precision() {
		return precisionExponent;
	}

	/**
	 * To epoch 10 nano.
	 *
	 * @param ts the ts
	 * @return the long
	 */
	public long toEpoch10Nano(long ts) {
		return (toEpochSecond(ts) * TS_TENS_NANOS_PER_SECOND) + (toNanoAdjustment(ts) / 10);
	}

	/**
	 * To epoch micro.
	 *
	 * @param ts the ts
	 * @return the long
	 */
	public long toEpochMicro(long ts) {
		return (toEpochSecond(ts) * TS_MICROS_PER_SECOND) + toMicroAdjustment(ts);
	}

	/**
	 * To epoch milli.
	 *
	 * @param ts the ts
	 * @return the long
	 */
	public long toEpochMilli(long ts) {
		return (toEpochSecond(ts) * TS_MILLIS_PER_SECOND) + toMilliAdjustment(ts);
	}

	/**
	 * To epoch nano.
	 *
	 * @param ts the ts
	 * @return the long
	 */
	public long toEpochNano(long ts) {
		return (toEpochSecond(ts) * TS_NANOS_PER_SECOND) + toNanoAdjustment(ts);
	}

	/**
	 * To epoch second.
	 *
	 * @param ts the ts
	 * @return the long
	 */
	public long toEpochSecond(long ts) {
		return seconds.applyAsLong(ts);
	}

	/**
	 * To instant.
	 *
	 * @param ts the ts
	 * @return the instant
	 */
	public Instant toInstant(long ts) {
		return Instant.ofEpochSecond(toEpochSecond(ts), toNanoAdjustment(ts));
	}

	/**
	 * To maximum precision adjustment or fraction of a second. Whatever the unit
	 * actually is, this method will return the maximum number of significant bits
	 * after the decimal point of the timestamp. For example, if PCAP_MICRO is used,
	 * the maximum value will be micro-seconds, but the same call on a PCAP_NANO
	 * will return nano-second value.
	 *
	 * @param ts the ts
	 * @return the long
	 */
	public long toMaxPrecisionAdjustment(long ts) {
		return switch (this.precisionTimeUnit()) {
		case MILLISECONDS -> toMilliAdjustment(ts);
		case MICROSECONDS -> toMicroAdjustment(ts);
		case NANOSECONDS -> toNanoAdjustment(ts);
		default -> throw new IllegalStateException("unsupported timestamp unit type " + this);
		};
	}

	/**
	 * To micro adjustment.
	 *
	 * @param ts the ts
	 * @return the long
	 */
	public long toMicroAdjustment(long ts) {
		return (toNanoAdjustment(ts) / TS_NANOS_IN_MICROS);
	}

	/**
	 * To milli adjustment.
	 *
	 * @param ts the ts
	 * @return the long
	 */
	public long toMilliAdjustment(long ts) {
		return (toNanoAdjustment(ts) / TS_NANOS_IN_MILLIS);
	}

	/**
	 * To nano adjustment.
	 *
	 * @param ts the ts
	 * @return the long
	 */
	public long toNanoAdjustment(long ts) {
		return nanos.applyAsLong(ts);
	}

	/**
	 * To pcap micro.
	 *
	 * @param ts the ts
	 * @return the long
	 */
	public long toPcapMicro(long ts) {
		return (toEpochSecond(ts) << TS_MSB_SHIFT) | (toMicroAdjustment(ts) & TS_LSB_MASK);
	}

	/**
	 * To pcap nano.
	 *
	 * @param ts the ts
	 * @return the long
	 */
	public long toPcapNano(long ts) {
		return (toEpochSecond(ts) << TS_MSB_SHIFT) | (toNanoAdjustment(ts) & TS_LSB_MASK);
	}

	/**
	 * To timestamp.
	 *
	 * @param ts the ts
	 * @return the timestamp
	 */
	public Timestamp toTimestamp(long ts) {
		return new Timestamp(ts, this);
	}

}
