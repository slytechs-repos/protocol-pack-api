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

import java.time.Instant;

/**
 * The Class RebasedTimestampSource.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public class RebasedTimestampSource implements TimestampSource {

	/** The base. */
	private long base;
	
	/** The last. */
	private long last;
	
	/** The updated. */
	private long updated;
	
	/** The realtime. */
	private final boolean realtime;

	/**
	 * Instantiates a new rebased timestamp source.
	 *
	 * @param realtime  the realtime
	 * @param baseNanos the base nanos
	 */
	public RebasedTimestampSource(boolean realtime, long baseNanos) {
		this.realtime = realtime;
		this.base = baseNanos;
	}

	/**
	 * Instant.
	 *
	 * @return the instant
	 * @see java.time.InstantSource#instant()
	 */
	@Override
	public Instant instant() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Timestamp.
	 *
	 * @return the long
	 * @see com.slytechs.jnet.jnetruntime.time.TimestampSource#timestamp()
	 */
	@Override
	public long timestamp() {
		return updated;
	}

	/**
	 * @see com.slytechs.jnet.jnetruntime.time.TimestampSource#init(long)
	 */
	@Override
	public void init(long initialTimestamp) {
		this.last = initialTimestamp;
		this.updated = base;
	}

	/**
	 * @see com.slytechs.jnet.jnetruntime.time.TimestampSource#update(long)
	 */
	@Override
	public void update(long newTimestamp) throws InterruptedException {
		long delta = (newTimestamp - last);

		this.last = newTimestamp;
		this.updated = base + delta;

		if (realtime)
			timeUnit().sleep(delta);
	}

	/**
	 * @see com.slytechs.jnet.jnetruntime.time.TimestampSource#isRealtime()
	 */
	@Override
	public boolean isRealtime() {
		return realtime;
	}

}
