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

	default TimestampUnit unit() {
		return TimestampUnit.EPOCH_MILLI;
	}
}
