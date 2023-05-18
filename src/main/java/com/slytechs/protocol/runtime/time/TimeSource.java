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

import static com.slytechs.protocol.runtime.time.Timestamp.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface TimeSource extends AutoCloseable {

	interface Updatable extends TimeSource {
		default void initialize(long initTimestamp) {
		}

		@Override
		default Optional<Updatable> asUpdatable() {
			return Optional.of(this);
		}

		default void update(long newTimestamp) {
		}

	}

	TimeSource SYSTEM = TimeSource::milliEpochTime;

	static long milliEpochTime() {
		return System.currentTimeMillis();
	}

	static TimeSource ofRebased() {
		return new RebasedTimetSource(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	static TimeSource ofRebased(long rebasedTime, TimeUnit unit) {
		return new RebasedTimetSource(rebasedTime, unit);
	}

	static long nanoEpochTime() {
		return nanoEpochTime(milliEpochTime(), nanoTimeTicks());
	}

	static long nanoEpochTime(long epochMilli, long nanoAdjust) {
		return (epochMilli * TS_NANOS_IN_MILLIS) + (nanoAdjust % TS_NANOS_IN_MILLIS);
	}

	static long microEpochTime() {
		return microEpochTime(milliEpochTime(), nanoTimeTicks());
	}

	static long microEpochTime(long epochMilli, long nanoAdjust) {
		return (epochMilli * TS_MICROS_IN_MILLIS)
				+ ((nanoAdjust / TS_MICROS_IN_MILLIS) % TS_MICROS_IN_MILLIS);
	}

	static long nanoTimeTicks() {
		return System.nanoTime();
	}

	static TimeSource ofSystem() {
		return SYSTEM;
	}

	@Override
	default void close() {

	}

	default boolean isRealtime() {
		return true;
	}

	long timestamp();

	default TimeUnit timeUnit() {
		return TimeUnit.MILLISECONDS;
	}

	default Optional<Updatable> asUpdatable() {
		return Optional.empty();
	}
}
