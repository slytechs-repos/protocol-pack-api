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

import java.util.concurrent.TimeUnit;

/**
 * The Class RebasedTimetSource.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public class RebasedTimetSource implements TimeSource.Updatable {

	/** The unit. */
	private final TimeUnit unit;
	
	/** The base. */
	private final long base;
	
	/** The last. */
	private long last;
	
	/** The delta. */
	private long delta;

	/**
	 * Instantiates a new rebased timet source.
	 *
	 * @param base the base
	 * @param unit the unit
	 */
	public RebasedTimetSource(long base, TimeUnit unit) {
		this.base = base;
		this.unit = unit;
	}

	/**
	 * @see com.slytechs.jnet.jnetruntime.time.TimeSource.Updatable#initialize(long)
	 */
	@Override
	public final void initialize(long initTimestamp) {
		this.last = initTimestamp;
		this.delta = (base - initTimestamp);
	}

	/**
	 * @see com.slytechs.jnet.jnetruntime.time.TimeSource.Updatable#update(long)
	 */
	@Override
	public final void update(long newTimestamp) {
		if ((last == 0) && (newTimestamp != 0))
			initialize(newTimestamp);

		long diff = newTimestamp - last;
		this.last = (newTimestamp + this.delta);

		onUpdate(last, diff);
	}

	/**
	 * On update.
	 *
	 * @param timestamp the timestamp
	 * @param delta     the delta
	 */
	protected void onUpdate(long timestamp, long delta) {

	}

	/**
	 * Timestamp.
	 *
	 * @return the long
	 * @see com.slytechs.jnet.jnetruntime.time.TimeSource#timestamp()
	 */
	@Override
	public final long timestamp() {
		return last;
	}

	/**
	 * @see com.slytechs.jnet.jnetruntime.time.TimeSource#timeUnit()
	 */
	@Override
	public final TimeUnit timeUnit() {
		return unit;
	}

}
