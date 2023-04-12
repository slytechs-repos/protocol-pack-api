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
package com.slytechs.protocol.runtime.time;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

/**
 * The Class TimestampClock.
 */
public class TimestampClock extends Clock implements TimestampPrecisionInfo {

	/** The Constant clock. */
	private static final Clock clock = Clock.systemUTC();
	
	/** The Constant MEASURED_RESOLUTION. */
	private static final int MEASURED_RESOLUTION = measureResolution();

	/**
	 * Measure resolution.
	 *
	 * @return the int
	 */
	private static int measureResolution() {

		TimestampClock measurementClock = new TimestampClock(TimestampUnit.EPOCH_NANO);

		final int LOOP_COUNT = 100_000;

		int resolution = 0;
		for (resolution = 0; resolution < 9; resolution++) {

			measurementClock.resetEventCounter();

			int ticks = (int) Math.pow(10, resolution);
			for (int i = 0; i < LOOP_COUNT; i++)
				measurementClock.timestampTicks(ticks);

			if (!measurementClock.isClockResolutionTooLow())
				break;
		}

		return (9 - resolution);
	}

	/**
	 * Measured max resolution.
	 *
	 * @return the int
	 */
	public static final int measuredMaxResolution() {
		return MEASURED_RESOLUTION;
	}

	/** The unit. */
	private final TimestampUnit unit;

	/** The last timestamp recorded. */
	private long lastTimestampRecorded = 0;
	
	/** The event counter. */
	private long eventCounter = 0;

	/**
	 * Instantiates a new timestamp clock.
	 */
	public TimestampClock() {
		this(TimestampUnit.PCAP_MICRO);
	}

	/**
	 * Instantiates a new timestamp clock.
	 *
	 * @param unit the unit
	 */
	public TimestampClock(TimestampUnit unit) {
		this.unit = unit;
	}

	/**
	 * Gets the zone.
	 *
	 * @return the zone
	 * @see java.time.Clock#getZone()
	 */
	@Override
	public ZoneId getZone() { return clock.getZone(); }

	/**
	 * Instant.
	 *
	 * @return the instant
	 * @see java.time.Clock#instant()
	 */
	@Override
	public Instant instant() {
		return clock.instant();
	}

	/**
	 * Micro adjustment.
	 *
	 * @return the int
	 */
	public int microAdjustment() {
		return nanoAdjustment() / 1_000;
	}

	/**
	 * Millis.
	 *
	 * @return the long
	 * @see java.time.Clock#millis()
	 */
	@Override
	public long millis() {
		return clock.millis();
	}

	/**
	 * Nano adjustment.
	 *
	 * @return the int
	 */
	public int nanoAdjustment() {
		return (int) (System.nanoTime() % 1_000_000_000);
	}

	/**
	 * Precision time unit.
	 *
	 * @return the time unit
	 * @see com.slytechs.protocol.runtime.time.TimestampPrecisionInfo#precisionTimeUnit()
	 */
	@Override
	public TimeUnit precisionTimeUnit() {
		return unit.precisionTimeUnit();
	}

	/**
	 * As a positive exponent of the measured resolution of this clock. For example
	 * 6 means 1000_000th of a second or microsecond.
	 *
	 * @return the int
	 */
	public int resolution() {
		int precision = precision();

		return (MEASURED_RESOLUTION >= precision) ? precision : MEASURED_RESOLUTION;
	}

	/**
	 * Precision.
	 *
	 * @return the int
	 * @see com.slytechs.protocol.runtime.time.TimestampPrecisionInfo#precision()
	 */
	@Override
	public int precision() {
		return unit.precision();
	}

	/**
	 * Seconds.
	 *
	 * @return the int
	 */
	public int seconds() {
		return (int) (millis() / 1_000);
	}

	/**
	 * Timestamp.
	 *
	 * @return the long
	 */
	public long timestamp() {
		long epochMilliseconds = millis();
		long milliAdjustmentInNanos = (nanoAdjustment() % 1000_000);

		long ts = unit.convertAndAdjust(epochMilliseconds, TimestampUnit.EPOCH_MILLI, milliAdjustmentInNanos);

		if (ts == lastTimestampRecorded) {
			eventCounter++; // Not multi-thread safe!

			// Handle counter roll over and carry over the "clock resolution too low" state
			if (eventCounter == Long.MAX_VALUE)
				eventCounter = 1;
		}

		lastTimestampRecorded = ts;

		return ts;
	}

	/**
	 * Timestamp ticks.
	 *
	 * @param ticks the ticks
	 * @return the long
	 */
	private long timestampTicks(long ticks) {
		long ts = (millis() % 1000) * 1000_000;
//		long ts = (nanoAdjustment() % 1000_000_000);

		ts -= (ts % ticks);

		if (ts == lastTimestampRecorded) {
			eventCounter++; // Not multi-thread safe!

			// Handle counter roll over and carry over the "clock resolution too low" state
			if (eventCounter == Long.MAX_VALUE)
				eventCounter = 1;
		}

		lastTimestampRecorded = ts;

		return ts;
	}

	/**
	 * Reset event counter.
	 */
	public void resetEventCounter() {
		eventCounter = 0;
	}

	/**
	 * Event counter.
	 *
	 * @return the long
	 */
	public long eventCounter() {
		return eventCounter;
	}

	/**
	 * Checks if is clock resolution too low.
	 *
	 * @return true, if is clock resolution too low
	 */
	public boolean isClockResolutionTooLow() { return (eventCounter != 0); }

	/**
	 * With zone.
	 *
	 * @param zone the zone
	 * @return the clock
	 * @see java.time.Clock#withZone(java.time.ZoneId)
	 */
	@Override
	public Clock withZone(ZoneId zone) {
		return clock.withZone(zone);
	}
}
