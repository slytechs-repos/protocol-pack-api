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
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
	public static AssignableTimestampSource system() {
		return new AssignableTimestampSource() {
			Clock clock = Clock.systemUTC();

			@Override
			public long timestamp() {
				return clock.millis();
			}

			@Override
			public Instant instant() {
				return clock.instant();
			}

			@Override
			public void sleep(long duration, TimeUnit unit) throws InterruptedException {
				unit.sleep(duration);
			}

			@Override
			public void timestamp(long newTimestamp) {
			}
		};
	}

	public static AssignableTimestampSource assignable() {
		return new AssignableTimestampSource() {
			final Exchanger<Long> exch = new Exchanger<>();;

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

				try {
					exch.exchange(newTimestamp, 1, TimeUnit.NANOSECONDS);
				} catch (InterruptedException | TimeoutException e) {}

			}

			@Override
			public void sleep(long duration, TimeUnit unit) throws InterruptedException {
				long oldTime = ts;
				long newTime = 0;

				for (;;) {
					try {
						newTime = exch.exchange(null, duration, unit);
					} catch (TimeoutException e) {
						// Interrupt on system time, if packet time hasn't arrived yet.
					}

					if (newTime - oldTime >= unit.toMillis(duration))
						return;
				}
			}

			@Override
			public void close() {
				timestamp(Long.MAX_VALUE);
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

	void sleep(long duration, TimeUnit unit) throws InterruptedException;

	default void timer(long duration, TimeUnit unit, Runnable action) throws InterruptedException {
		sleep(duration, unit);

		action.run();
	}

	default void close() {
	}
}
