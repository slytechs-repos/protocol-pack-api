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
import java.time.InstantSource;

/**
 * The Interface TimestampSource.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface TimestampSource extends InstantSource {

	public interface AssignableTimestampSource extends TimestampSource {
		void timestamp(long newTimestamp);
	}

	/**
	 * System.
	 *
	 * @return the timestamp source
	 */
	public static TimestampSource system() {
		return new TimestampSource() {
			Clock clock = Clock.systemUTC();

			@Override
			public long timestamp() {
				return clock.millis();
			}

			@Override
			public Instant instant() {
				return clock.instant();
			}
		};
	}

	public static AssignableTimestampSource assignable() {
		return new AssignableTimestampSource() {

			long ts;

			@Override
			public long timestamp() {
				return ts;
			}

			@Override
			public Instant instant() {
				return Instant.ofEpochMilli(ts);
			}

			@Override
			public void timestamp(long newTimestamp) {
				ts = newTimestamp;
			}

		};
	}

	/**
	 * Timestamp.
	 *
	 * @return the long
	 */
	long timestamp();

	/**
	 * Nano time.
	 *
	 * @return the long
	 */
	default long nanoTime() {
		return System.nanoTime();
	}

	/**
	 * Milli time.
	 *
	 * @return the long
	 */
	default long milliTime() {
		return System.currentTimeMillis();
	}

	/**
	 * Unit.
	 *
	 * @return the timestamp unit
	 */
	default TimestampUnit unit() {
		return TimestampUnit.EPOCH_MILLI;
	}
}
