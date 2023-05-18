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

import java.util.concurrent.TimeUnit;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public class RebasedTimetSource implements TimeSource.Updatable {

	private final TimeUnit unit;
	private final long base;
	private long last;
	private long delta;

	public RebasedTimetSource(long base, TimeUnit unit) {
		this.base = base;
		this.unit = unit;
	}

	@Override
	public final void initialize(long initTimestamp) {
		this.last = initTimestamp;
		this.delta = (base - initTimestamp);
	}

	@Override
	public final void update(long newTimestamp) {
		if ((last == 0) && (newTimestamp != 0))
			initialize(newTimestamp);

		long diff = newTimestamp - last;
		this.last = (newTimestamp + this.delta);

		onUpdate(last, diff);
	}

	protected void onUpdate(long timestamp, long delta) {

	}

	/**
	 * @see com.slytechs.protocol.runtime.time.TimeSource#timestamp()
	 */
	@Override
	public final long timestamp() {
		return last;
	}

	@Override
	public final TimeUnit timeUnit() {
		return unit;
	}

}
