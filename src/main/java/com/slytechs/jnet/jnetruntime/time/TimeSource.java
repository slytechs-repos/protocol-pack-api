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
package com.slytechs.jnet.jnetruntime.time;

import static com.slytechs.jnet.jnetruntime.time.Timestamp.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * The Interface TimeSource.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface TimeSource extends AutoCloseable {

	/**
	 * The Interface Updatable.
	 */
	interface Updatable extends TimeSource {
		
		/**
		 * Initialize.
		 *
		 * @param initTimestamp the init timestamp
		 */
		default void initialize(long initTimestamp) {
		}

		/**
		 * @see com.slytechs.jnet.jnetruntime.time.TimeSource#asUpdatable()
		 */
		@Override
		default Optional<Updatable> asUpdatable() {
			return Optional.of(this);
		}

		/**
		 * Update.
		 *
		 * @param newTimestamp the new timestamp
		 */
		default void update(long newTimestamp) {
		}

	}

	/** The system. */
	TimeSource SYSTEM = TimeSource::milliEpochTime;

	/**
	 * Milli epoch time.
	 *
	 * @return the long
	 */
	static long milliEpochTime() {
		return System.currentTimeMillis();
	}

	/**
	 * Of rebased.
	 *
	 * @return the time source
	 */
	static TimeSource ofRebased() {
		return new RebasedTimetSource(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	/**
	 * Of rebased.
	 *
	 * @param rebasedTime the rebased time
	 * @param unit        the unit
	 * @return the time source
	 */
	static TimeSource ofRebased(long rebasedTime, TimeUnit unit) {
		return new RebasedTimetSource(rebasedTime, unit);
	}

	/**
	 * Nano epoch time.
	 *
	 * @return the long
	 */
	static long nanoEpochTime() {
		return nanoEpochTime(milliEpochTime(), nanoTimeTicks());
	}

	/**
	 * Nano epoch time.
	 *
	 * @param epochMilli the epoch milli
	 * @param nanoAdjust the nano adjust
	 * @return the long
	 */
	static long nanoEpochTime(long epochMilli, long nanoAdjust) {
		return (epochMilli * TS_NANOS_IN_MILLIS) + (nanoAdjust % TS_NANOS_IN_MILLIS);
	}

	/**
	 * Micro epoch time.
	 *
	 * @return the long
	 */
	static long microEpochTime() {
		return microEpochTime(milliEpochTime(), nanoTimeTicks());
	}

	/**
	 * Micro epoch time.
	 *
	 * @param epochMilli the epoch milli
	 * @param nanoAdjust the nano adjust
	 * @return the long
	 */
	static long microEpochTime(long epochMilli, long nanoAdjust) {
		return (epochMilli * TS_MICROS_IN_MILLIS)
				+ ((nanoAdjust / TS_MICROS_IN_MILLIS) % TS_MICROS_IN_MILLIS);
	}

	/**
	 * Nano time ticks.
	 *
	 * @return the long
	 */
	static long nanoTimeTicks() {
		return System.nanoTime();
	}

	/**
	 * Of system.
	 *
	 * @return the time source
	 */
	static TimeSource ofSystem() {
		return SYSTEM;
	}

	/**
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	default void close() {

	}

	/**
	 * Checks if is realtime.
	 *
	 * @return true, if is realtime
	 */
	default boolean isRealtime() {
		return true;
	}

	/**
	 * Timestamp.
	 *
	 * @return the long
	 */
	long timestamp();

	/**
	 * Time unit.
	 *
	 * @return the time unit
	 */
	default TimeUnit timeUnit() {
		return TimeUnit.MILLISECONDS;
	}

	/**
	 * As updatable.
	 *
	 * @return the optional
	 */
	default Optional<Updatable> asUpdatable() {
		return Optional.empty();
	}
}
